package com.zoopi.controller.member;

import static com.zoopi.controller.ResultCode.*;
import static com.zoopi.domain.member.dto.SigninResponse.SigninResult.*;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zoopi.controller.ResultCode;
import com.zoopi.controller.ResultResponse;
import com.zoopi.controller.member.request.AuthenticationCodeCheckRequest;
import com.zoopi.controller.member.request.SigninRequest;
import com.zoopi.controller.member.request.SignupRequest;
import com.zoopi.controller.member.response.ValidationResponse;
import com.zoopi.domain.authentication.dto.response.AuthenticationResponse;
import com.zoopi.domain.authentication.dto.response.AuthenticationResult;
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
		final boolean isValidated = memberService.validateUsername(username);
		final ResultCode resultCode;
		if (isValidated) {
			resultCode = USERNAME_AVAILABLE;
		} else {
			resultCode = USERNAME_DUPLICATE;
		}

		final ValidationResponse response = new ValidationResponse(username, isValidated);
		return ResponseEntity.ok(ResultResponse.of(resultCode, response));
	}

	@ApiOperation(value = "휴대폰 번호 유효성 검사")
	@ApiImplicitParam(name = "phone", value = "휴대폰 번호", required = true, example = "01012345678")
	@PostMapping("/phone/validate")
	public ResponseEntity<ResultResponse> validatePhone(
		@RequestParam @Pattern(regexp = "^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$") String phone) {
		final boolean isValidated = memberService.validatePhone(phone);
		final ResultCode resultCode;
		if (isValidated) {
			resultCode = PHONE_AVAILABLE;
		} else {
			resultCode = PHONE_DUPLICATE;
		}

		final ValidationResponse response = new ValidationResponse(phone, isValidated);
		return ResponseEntity.ok(ResultResponse.of(resultCode, response));
	}

	// TODO: 비밀번호 찾기 API와 독립적으로 문자 전송 밴 처리
	@ApiOperation(value = "휴대폰 본인 인증 문자 전송")
	@ApiImplicitParam(name = "phone", value = "휴대폰 번호", required = true, example = "01012345678")
	@PostMapping("/phone/send")
	public ResponseEntity<ResultResponse> sendAuthenticationCode(
		@RequestParam @Pattern(regexp = "^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$") String phone) {
		if (banService.isBannedPhone(phone)) {
			return ResponseEntity.ok(ResultResponse.of(PHONE_BANNED));
		}

		final String authenticationCode = AuthenticationCodeUtils.generateRandomAuthenticationCode(
			AUTHENTICATION_CODE_LENGTH);
		authenticationService.sendAuthenticationCode(phone, authenticationCode);
		final AuthenticationResponse response = authenticationService.createAuthentication(phone, authenticationCode);
		if (authenticationService.getCountOfAuthentication(phone) >= MAX_SEND_COUNT) {
			banService.banPhone(phone);
		}

		return ResponseEntity.ok(ResultResponse.of(SEND_AUTHENTICATION_CODE_SUCCESS, response));
	}

	@ApiOperation(value = "인증 코드 확인")
	@PostMapping("/code/check")
	public ResponseEntity<ResultResponse> checkAuthenticationCode(
		@Valid @RequestBody AuthenticationCodeCheckRequest request) {
		final AuthenticationResult result = authenticationService.checkAuthenticationCode(
			request.getAuthenticationCode(), request.getPhone(), request.getAuthenticationKey());

		final boolean isValidated;
		final ResultCode resultCode;
		if (result.equals(AuthenticationResult.EXPIRED)) {
			resultCode = AUTHENTICATION_CODE_EXPIRED;
			isValidated = false;
		} else if (result.equals(AuthenticationResult.MISMATCHED)) {
			resultCode = AUTHENTICATION_CODE_MISMATCHED;
			isValidated = false;
		} else {
			resultCode = AUTHENTICATION_CODE_MATCHED;
			isValidated = true;
		}

		final ValidationResponse response = new ValidationResponse(request.getPhone(), isValidated);
		return ResponseEntity.ok(ResultResponse.of(resultCode, response));
	}

	@ApiOperation(value = "만료된 인증 코드 제거")
	@DeleteMapping("/code")
	public ResponseEntity<ResultResponse> deleteExpiredAuthenticationCodes() {
		authenticationService.deleteExpiredAuthenticationCodes();

		return ResponseEntity.ok(ResultResponse.of(DELETE_ALL_EXPIRED_AUTHENTICATION_CODES));
	}

	@ApiOperation(value = "일반 회원 가입")
	@PostMapping("/signup")
	public ResponseEntity<ResultResponse> signup(@Valid @RequestBody SignupRequest request) {
		authenticationService.validatePassword(request.getPassword(), request.getPasswordCheck());
		if (!memberService.validateUsername(request.getUsername())) {
			final ValidationResponse response = new ValidationResponse(request.getUsername(), false);
			return ResponseEntity.ok(ResultResponse.of(USERNAME_DUPLICATE, response));
		}
		if (!memberService.validatePhone(request.getPhone())) {
			final ValidationResponse response = new ValidationResponse(request.getPhone(), false);
			return ResponseEntity.ok(ResultResponse.of(PHONE_DUPLICATE, response));
		}

		final AuthenticationResult result = authenticationService.validateAuthenticationKey(request.getPhone(),
			request.getAuthenticationKey());
		if (result.equals(AuthenticationResult.EXPIRED)) {
			final ValidationResponse response = new ValidationResponse(request.getPhone(), false);
			return ResponseEntity.ok(ResultResponse.of(AUTHENTICATION_KEY_EXPIRED, response));
		} else if (result.equals(AuthenticationResult.NOT_AUTHENTICATED)) {
			final ValidationResponse response = new ValidationResponse(request.getPhone(), false);
			return ResponseEntity.ok(ResultResponse.of(AUTHENTICATION_KEY_NOT_AUTHENTICATED, response));
		}

		memberService.createMember(request.getUsername(), request.getPhone(), "", request.getPassword(), "");

		return ResponseEntity.ok(ResultResponse.of(SIGN_UP_SUCCESS));
	}

	// TODO: RefreshToken -> Cookie 저장
	@ApiOperation(value = "일반 로그인")
	@PostMapping("/signin")
	public ResponseEntity<ResultResponse> signinByEmail(@Valid @RequestBody SigninRequest request) {
		final SigninResponse response = memberService.signin(request.getUsername(), request.getPassword());
		final ResultCode resultCode;
		if (response.getResult().equals(NONEXISTENT_USERNAME)) {
			resultCode = MEMBER_USERNAME_NONEXISTENT;
		} else if(response.getResult().equals(MISMATCHED_PASSWORD)) {
			resultCode = MEMBER_PASSWORD_MISMATCHED;
		} else {
			resultCode = SIGN_IN_SUCCESS;
		}

		return ResponseEntity.ok(ResultResponse.of(resultCode, response.getJwt()));
	}

}
