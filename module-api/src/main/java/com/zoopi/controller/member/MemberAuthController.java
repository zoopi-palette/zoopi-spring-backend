package com.zoopi.controller.member;

import static com.zoopi.controller.ResultCode.*;
import static com.zoopi.util.Constants.*;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zoopi.controller.ResultResponse;
import com.zoopi.controller.member.mapper.FindPasswordResponseMapper;
import com.zoopi.controller.member.mapper.FindUsernameResponseMapper;
import com.zoopi.controller.member.mapper.SigninResponseMapper;
import com.zoopi.controller.member.mapper.ValidationResponseMapper;
import com.zoopi.controller.member.request.AuthCodeCheckRequest;
import com.zoopi.controller.member.request.FindPasswordRequest;
import com.zoopi.controller.member.request.FindUsernameRequest;
import com.zoopi.controller.member.request.SendAuthCodeRequest;
import com.zoopi.controller.member.request.SigninRequest;
import com.zoopi.controller.member.request.SignupRequest;
import com.zoopi.domain.authentication.dto.response.AuthenticationResponse;
import com.zoopi.domain.authentication.dto.response.AuthenticationResult;
import com.zoopi.domain.authentication.entity.AuthenticationType;
import com.zoopi.domain.authentication.service.AuthenticationService;
import com.zoopi.domain.authentication.service.BanService;
import com.zoopi.domain.member.dto.SigninResponse;
import com.zoopi.domain.member.service.MemberService;
import com.zoopi.util.AuthenticationCodeUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@Api(tags = "회원 인증 API")
@Validated
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class MemberAuthController {

	private static final int AUTHENTICATION_CODE_LENGTH = 6;
	private static final int MAX_SEND_COUNT = 5;

	private final MemberService memberService;
	private final AuthenticationService authenticationService;
	private final BanService banService;

	// TODO: @APiResponse 추가

	@ApiOperation(value = "아이디 유효성 검사")
	@ApiImplicitParam(name = "username", value = "아이디", required = true, example = "zoopi")
	@PostMapping("/username/validate")
	public ResponseEntity<ResultResponse> validateUsername(@RequestParam @Size(max = 30) String username) {
		final boolean isAvailable = memberService.isAvailableUsername(username);
		final ResultResponse response = ValidationResponseMapper.fromValidatingUsername(username, isAvailable);
		return ResponseEntity.ok(response);
	}

	@ApiOperation(value = "휴대폰 번호 유효성 검사")
	@ApiImplicitParam(name = "phone", value = "휴대폰 번호", required = true, example = "01012345678")
	@PostMapping("/phone/validate")
	public ResponseEntity<ResultResponse> validatePhone(
		@RequestParam @Pattern(regexp = "^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$") String phone) {
		final boolean isAvailable = memberService.isAvailablePhone(phone);
		final ResultResponse response = ValidationResponseMapper.fromValidatingPhone(phone, isAvailable);
		return ResponseEntity.ok(response);
	}

	@ApiOperation(value = "휴대폰 본인 인증 문자 전송")
	@PostMapping("/phone/send")
	public ResponseEntity<ResultResponse> sendAuthenticationCode(@Valid @RequestBody SendAuthCodeRequest request) {
		authenticationService.deleteExpiredAuthenticationCodes();
		if (banService.isBanned(request.getPhone(), request.getType())) {
			return ResponseEntity.ok(ResultResponse.of(PHONE_BANNED));
		}

		final String authenticationCode = AuthenticationCodeUtils.generateRandomAuthenticationCode(
			AUTHENTICATION_CODE_LENGTH);
		authenticationService.sendAuthenticationCode(request.getPhone(), authenticationCode);

		final AuthenticationResponse response = authenticationService.createAuthentication(request.getPhone(),
			authenticationCode, request.getType());
		if (authenticationService.getCountOfAuthentication(request.getPhone(), request.getType()) >= MAX_SEND_COUNT) {
			banService.ban(request.getPhone(), request.getType());
		}
		return ResponseEntity.ok(ResultResponse.of(SEND_AUTHENTICATION_CODE_SUCCESS, response));
	}

	@ApiOperation(value = "인증 코드 확인")
	@PostMapping("/code/check")
	public ResponseEntity<ResultResponse> checkAuthenticationCode(@Valid @RequestBody AuthCodeCheckRequest request) {
		final AuthenticationResult result = authenticationService.checkAuthenticationCode(
			request.getAuthenticationCode(), request.getPhone(), request.getAuthenticationKey());
		final ResultResponse response = ValidationResponseMapper.fromCheckingAuthenticationCode(result,
			request.getPhone());
		return ResponseEntity.ok(response);
	}

	@ApiOperation(value = "일반 회원 가입")
	@PostMapping("/signup")
	public ResponseEntity<ResultResponse> signup(@Valid @RequestBody SignupRequest request) {
		final AuthenticationResult result = authenticationService.validateAuthenticationKey(request.getPhone(),
			request.getAuthenticationKey(), AuthenticationType.SIGN_UP);

		authenticationService.validatePassword(request.getPassword(), request.getPasswordCheck());
		if (!memberService.isAvailableUsername(request.getUsername())) {
			final ResultResponse response = ValidationResponseMapper.fromValidatingUsername(request.getUsername(),
				false);
			return ResponseEntity.ok(response);
		}
		if (!memberService.isAvailablePhone(request.getPhone())) {
			final ResultResponse response = ValidationResponseMapper.fromValidatingPhone(request.getPhone(), false);
			return ResponseEntity.ok(response);
		}

		final ResultResponse response = ValidationResponseMapper.fromCheckingAuthenticationCode(result,
			request.getPhone());
		if (response.getCode().equals(SIGN_UP_SUCCESS.getCode())) {
			memberService.createMember(request.getUsername(), request.getPhone(), EMPTY, request.getPassword(), EMPTY);
		}

		return ResponseEntity.ok(response);
	}

	// TODO: 운영서버 - RefreshToken -> Cookie 전달
	// 	Security filter 단으로 이동
	@ApiOperation(value = "일반 로그인")
	@PostMapping("/signin")
	public ResponseEntity<ResultResponse> signin(@Valid @RequestBody SigninRequest request) {
		final SigninResponse result = memberService.signin(request.getUsername(), request.getPassword());
		final ResultResponse response = SigninResponseMapper.fromSigningIn(result);
		return ResponseEntity.ok(response);
	}

	@ApiOperation(value = "아이디 찾기")
	@PostMapping("/find/username")
	public ResponseEntity<ResultResponse> findUsername(@Valid @RequestBody FindUsernameRequest request) {
		final AuthenticationResult result = authenticationService.validateAuthenticationKey(
			request.getPhone(), request.getAuthenticationKey(), AuthenticationType.FIND_ID);

		ResultResponse response = ValidationResponseMapper.fromCheckingAuthenticationCode(result, request.getPhone());
		if (response.getCode().equals(AUTHENTICATION_CODE_MATCHED.getCode())) {
			final String username = memberService.getUsernameByPhone(request.getPhone());
			response = FindUsernameResponseMapper.fromFindingUsername(username);
		}

		return ResponseEntity.ok(response);
	}

	@ApiOperation(value = "비밀번호 찾기")
	@PostMapping("/find/password")
	public ResponseEntity<ResultResponse> findPassword(@Valid @RequestBody FindPasswordRequest request) {
		final AuthenticationResult authResult = authenticationService.validateAuthenticationKey(
			request.getPhone(), request.getAuthenticationKey(), AuthenticationType.FIND_PW);

		ResultResponse response = ValidationResponseMapper.fromCheckingAuthenticationCode(authResult, request.getPhone());
		if (response.getCode().equals(AUTHENTICATION_CODE_MATCHED.getCode())) {
			final boolean isExistentUsername = !memberService.isAvailableUsername(request.getUsername());
			response = FindPasswordResponseMapper.fromCheckingUsername(isExistentUsername, request.getUsername());
			if (response.getCode().equals(USERNAME_EXISTENT.getCode())) {
				authenticationService.validatePassword(request.getPassword(), request.getPasswordCheck());
				memberService.changePassword(request.getUsername(), request.getPassword());
				response = ResultResponse.of(PASSWORD_CHANGE_SUCCESS);
			}
		}

		return ResponseEntity.ok(response);
	}

}
