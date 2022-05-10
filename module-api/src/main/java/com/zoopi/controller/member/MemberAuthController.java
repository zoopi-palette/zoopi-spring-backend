package com.zoopi.controller.member;

import static com.zoopi.controller.ResultCode.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
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
import com.zoopi.controller.member.response.ValidationResponse;
import com.zoopi.domain.authentication.dto.response.AuthenticationResponse;
import com.zoopi.domain.authentication.dto.response.AuthenticationResult;
import com.zoopi.domain.authentication.service.AuthenticationService;
import com.zoopi.domain.authentication.service.BanService;
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

	private final MemberService memberService;
	private final AuthenticationService authenticationService;
	private final BanService banService;

	private final int AUTHENTICATION_CODE_LENGTH = 6;
	private final int MAX_SEND_COUNT = 5;

	@ApiOperation(value = "이메일 유효성 검사")
	@ApiImplicitParam(name = "email", value = "이메일", required = true, example = "zoopi@gmail.com")
	@PostMapping("/email/validate")
	public ResponseEntity<ResultResponse> validateEmail(@RequestParam @Length(max = 30) @Email String email) {
		final boolean isValidated = memberService.validateEmail(email);
		final ResultCode resultCode;
		if (isValidated) {
			resultCode = EMAIL_AVAILABLE;
		} else {
			resultCode = EMAIL_DUPLICATE;
		}

		final ValidationResponse response = new ValidationResponse(email, isValidated);
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

	// TODO: 회원가입 API에 휴대폰, 이메일 중복 체크 로직 추가
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

}
