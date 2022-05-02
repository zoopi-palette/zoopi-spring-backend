package com.zoopi.controller.member;

import static com.zoopi.controller.ResultCode.*;

import javax.validation.constraints.Email;

import org.hibernate.validator.constraints.Length;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zoopi.controller.ResultCode;
import com.zoopi.controller.ResultResponse;
import com.zoopi.controller.member.response.EmailValidationResponse;
import com.zoopi.domain.member.service.MemberService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@Api(tags = "회원 인증 API")
@Validated
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;

	@ApiOperation(value = "이메일 중복 체크")
	@PostMapping("/email/validate")
	public ResponseEntity<ResultResponse> validateEmail(@RequestParam @Length(max = 30) @Email String email) {
		final boolean isValidated = memberService.validateEmail(email);
		final ResultCode resultCode;
		if (isValidated) {
			resultCode = EMAIL_AVAILABLE;
		} else {
			resultCode = EMAIL_DUPLICATE;
		}

		final EmailValidationResponse response = EmailValidationResponse.builder()
			.email(email)
			.isValidated(isValidated)
			.build();
		return ResponseEntity.ok(ResultResponse.of(resultCode, response));
	}
}
