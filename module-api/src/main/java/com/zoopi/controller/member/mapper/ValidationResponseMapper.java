package com.zoopi.controller.member.mapper;

import static com.zoopi.controller.ResultCode.*;

import com.zoopi.controller.ResultCode;
import com.zoopi.controller.ResultResponse;
import com.zoopi.domain.authentication.dto.response.AuthenticationResult;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class ValidationResponseMapper {

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

	public static ResultResponse fromCheckingAuthenticationCode(AuthenticationResult result, String value) {
		switch (result) {
			case EXPIRED:
				return ResultResponse.of(AUTHENTICATION_CODE_EXPIRED, ValidationResponse.of(value, false));
			case MISMATCHED:
				return ResultResponse.of(AUTHENTICATION_CODE_MISMATCHED, ValidationResponse.of(value, false));
			default:
				return ResultResponse.of(AUTHENTICATION_CODE_MATCHED, ValidationResponse.of(value, true));
		}
	}

	@Getter
	@AllArgsConstructor(access = AccessLevel.PROTECTED)
	public static class ValidationResponse {

		private String value;
		private boolean isValidated;

		public static ValidationResponse of(String value, boolean isValidated) {
			return new ValidationResponse(value, isValidated);
		}

	}

}
