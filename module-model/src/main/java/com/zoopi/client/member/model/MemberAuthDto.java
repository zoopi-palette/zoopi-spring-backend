package com.zoopi.client.member.model;

import static com.zoopi.ResultCode.*;
import static com.zoopi.util.Constants.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.zoopi.ResultCode;
import com.zoopi.ResultResponse;
import com.zoopi.domain.phoneauthentication.entity.PhoneAuthenticationResult;
import com.zoopi.domain.phoneauthentication.entity.PhoneAuthenticationType;

public class MemberAuthDto {

	@Getter
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class AuthCodeCheckRequest {

		@NotNull
		@ApiModelProperty(value = "인증 타입", required = true, example = "SIGN_UP")
		private PhoneAuthenticationType type;

		@NotBlank
		@Size(min = 36, max = 36)
		@ApiModelProperty(value = "인증 키(36자)", required = true, example = "2a6ec3d7-7147-4cdc-a6c5-fc5d937cfe76")
		private String authenticationKey;

		@NotNull
		@Pattern(regexp = "^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$")
		@ApiModelProperty(value = "휴대폰 번호(정규표현식: ^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$)", required = true, example = "01012345678")
		private String phone;

		@NotBlank
		@Size(min = 6, max = 6)
		@ApiModelProperty(value = "인증 코드(6자)", required = true, example = "012345")
		private String authenticationCode;

	}

	@Getter
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class FindPasswordRequest {

		@NotBlank
		@Size(max = 36)
		@ApiModelProperty(value = "인증 키(36자)", required = true, example = "2a6ec3d7-7147-4cdc-a6c5-fc5d937cfe76")
		private String authenticationKey;

		@NotNull
		@Pattern(regexp = "^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$")
		@ApiModelProperty(value = "휴대폰 번호(정규표현식: ^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$)", required = true, example = "01012345678")
		private String phone;

		@NotBlank
		@Size(max = 30)
		@ApiModelProperty(value = "아이디(최대 30자)", required = true, example = "zoopi123")
		private String username;

		@NotNull
		@Pattern(regexp = "^.*(?=^.{10,20}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[`~!@#$%^&*()+=]).*$")
		@ApiModelProperty(value = "비밀번호(정규표현식:^.*(?=^.{10,20}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[`~!@#$%^&*()+=]).*$)", required = true, example = "1234asdf!@#$")
		private String password;

		@NotNull
		@Pattern(regexp = "^.*(?=^.{10,20}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[`~!@#$%^&*()+=]).*$")
		@ApiModelProperty(value = "비밀번호 확인(정규표현식:^.*(?=^.{10,20}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[`~!@#$%^&*()+=]).*$)", required = true, example = "1234asdf!@#$")
		private String passwordCheck;

	}

	@Getter
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class FindUsernameRequest {

		@NotBlank
		@Size(max = 36)
		@ApiModelProperty(value = "인증 키(36자)", required = true, example = "2a6ec3d7-7147-4cdc-a6c5-fc5d937cfe76")
		private String authenticationKey;

		@NotNull
		@Pattern(regexp = "^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$")
		@ApiModelProperty(value = "휴대폰 번호(정규표현식:^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$)", required = true, example = "01012345678")
		private String phone;

	}

	@Getter
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class SendAuthCodeRequest {

		@NotNull
		@ApiModelProperty(value = "인증 타입", required = true, example = "SIGN_UP")
		private PhoneAuthenticationType type;

		@NotNull
		@Pattern(regexp = "^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$")
		@ApiModelProperty(value = "휴대폰 번호", required = true, example = "01012341234")
		private String phone;

	}

	@Getter
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class SigninRequest {

		@NotBlank
		@Size(max = 30)
		@ApiModelProperty(value = "아이디(최대 30자)", required = true, example = "zoopi123")
		private String username;

		@NotNull
		@Pattern(regexp = "^.*(?=^.{10,20}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[`~!@#$%^&*()+=]).*$")
		@ApiModelProperty(value = "비밀번호(정규표현식: ^.*(?=^.{10,20}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[`~!@#$%^&*()+=]).*$)", required = true, example = "1234asdf!@#$")
		private String password;

	}

	@Getter
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	public static class SignupRequest {

		@NotBlank
		@Size(max = 30)
		@ApiModelProperty(value = "아이디(최대 30자)", required = true, example = "zoopi123")
		private String username;

		@NotNull
		@Pattern(regexp = "^.*(?=^.{10,20}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[`~!@#$%^&*()+=]).*$")
		@ApiModelProperty(value = "비밀번호(정규표현식: ^.*(?=^.{10,20}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[`~!@#$%^&*()+=]).*$)", required = true, example = "1234asdf!@#$")
		private String password;

		@NotNull
		@Pattern(regexp = "^.*(?=^.{10,20}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[`~!@#$%^&*()+=]).*$")
		@ApiModelProperty(value = "비밀번호 확인(정규표현식: ^.*(?=^.{10,20}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[`~!@#$%^&*()+=]).*$)", required = true, example = "1234asdf!@#$")
		private String passwordCheck;

		@NotNull
		@Pattern(regexp = "^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$")
		@ApiModelProperty(value = "휴대폰 번호(정규표현식:^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$)", required = true, example = "01012341234")
		private String phone;

		@NotBlank
		@Size(max = 36)
		@ApiModelProperty(value = "인증 키(36자)", required = true, example = "2a6ec3d7-7147-4cdc-a6c5-fc5d937cfe76")
		private String authenticationKey;

	}

	@Getter
	@AllArgsConstructor(access = AccessLevel.PROTECTED)
	public static class ValidationResponse {

		@ApiModelProperty(name = "value", value = "검증 대상(값)", example = "username | phone | ...")
		private String value;

		@JsonProperty(value = "validated")
		@ApiModelProperty(name = "validated", value = "검증 결과", example = "true | false")
		private boolean isValidated;

		public static ValidationResponse of(String value, boolean isValidated) {
			return new ValidationResponse(value, isValidated);
		}

		public static ResultResponse fromValidatingUsername(String username, boolean isAvailable) {
			final ResultCode resultCode = isAvailable ? USERNAME_AVAILABLE : USERNAME_EXISTENT;
			final ValidationResponse response = ValidationResponse.of(username, isAvailable);
			return ResultResponse.of(resultCode, response);
		}

		public static ResultResponse fromValidatingPhone(String phone, boolean isAvailable) {
			final ResultCode resultCode = isAvailable ? PHONE_AVAILABLE : PHONE_EXISTENT;
			final ValidationResponse response = ValidationResponse.of(phone, isAvailable);
			return ResultResponse.of(resultCode, response);
		}

		public static ResultResponse fromCheckingAuthenticationCode(PhoneAuthenticationResult result, String value) {
			switch (result) {
				case EXPIRED:
					return ResultResponse.of(AUTHENTICATION_CODE_EXPIRED, ValidationResponse.of(value, false));
				case MISMATCHED:
					return ResultResponse.of(AUTHENTICATION_CODE_MISMATCHED, ValidationResponse.of(value, false));
				default:
					return ResultResponse.of(AUTHENTICATION_CODE_MATCHED, ValidationResponse.of(value, true));
			}
		}

		public static ResultResponse fromCheckingUsername(boolean isExistentUsername, String username) {
			if (isExistentUsername) {
				return ResultResponse.of(USERNAME_EXISTENT);
			} else {
				final ValidationResponse response = ValidationResponse.of(username, false);
				return ResultResponse.of(USERNAME_NONEXISTENT, response);
			}
		}

	}

	@Getter
	@AllArgsConstructor(access = AccessLevel.PROTECTED)
	public static class UsernameResponse {

		private String username;

		public static UsernameResponse of(String username) {
			return new UsernameResponse(username);
		}

		public static ResultResponse fromFindingUsername(String username) {
			return ResultResponse.of(FIND_USERNAME_SUCCESS, UsernameResponse.of(username));
		}

	}

	public static class SigninResponseMapper {

		public static ResultResponse fromSigningIn(SigninResponse response) {
			switch (response.getResult()) {
				case NONEXISTENT_USERNAME:
					return ResultResponse.of(USERNAME_NONEXISTENT, EMPTY);
				case MISMATCHED_PASSWORD:
					return ResultResponse.of(PASSWORD_MISMATCHED, EMPTY);
				default:
					return ResultResponse.of(SIGN_IN_SUCCESS, response.getJwt());
			}
		}

	}

}
