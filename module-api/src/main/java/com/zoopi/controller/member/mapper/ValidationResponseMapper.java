package com.zoopi.controller.member.mapper;

import static com.zoopi.controller.ResultCode.*;

import com.zoopi.controller.ResultCode;
import com.zoopi.controller.ResultResponse;
import com.zoopi.controller.member.response.ValidationResponse;
import com.zoopi.domain.authentication.dto.response.AuthenticationResult;

public class ValidationResponseMapper {

	public static ResultResponse fromValidatingUsername(String username, boolean isValidated) {
		final ResultCode resultCode = isValidated ? USERNAME_AVAILABLE : USERNAME_DUPLICATE;
		final ValidationResponse response = ValidationResponse.of(username, isValidated);
		return ResultResponse.of(resultCode, response);
	}

	public static ResultResponse fromValidatingPhone(String phone, boolean isValidated) {
		final ResultCode resultCode = isValidated ? PHONE_AVAILABLE : PHONE_DUPLICATE;
		final ValidationResponse response = ValidationResponse.of(phone, isValidated);
		return ResultResponse.of(resultCode, response);
	}

	public static ResultResponse fromCheckingAuthenticationCode(AuthenticationResult result, String phone) {
		switch (result) {
			case EXPIRED:
				return ResultResponse.of(AUTHENTICATION_CODE_EXPIRED, ValidationResponse.of(phone, false));
			case MISMATCHED:
				return ResultResponse.of(AUTHENTICATION_CODE_MISMATCHED, ValidationResponse.of(phone, false));
			default:
				return ResultResponse.of(AUTHENTICATION_CODE_MATCHED, ValidationResponse.of(phone, true));
		}
	}

	public static ResultResponse fromValidatingAuthenticationKey(AuthenticationResult result, String phone) {
		final ValidationResponse response = ValidationResponse.of(phone, false);
		switch (result) {
			case EXPIRED:
				return ResultResponse.of(AUTHENTICATION_KEY_EXPIRED, response);
			case NOT_AUTHENTICATED:
				return ResultResponse.of(AUTHENTICATION_KEY_NOT_AUTHENTICATED, response);
			default:
				return ResultResponse.of(SIGN_UP_SUCCESS);
		}
	}

}
