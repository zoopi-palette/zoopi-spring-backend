package com.zoopi.exception;

import java.util.List;

import com.zoopi.exception.response.ErrorCode;
import com.zoopi.exception.response.ErrorResponse;

public class AuthenticationException extends BusinessException {

	public AuthenticationException(List<ErrorResponse.FieldError> errors) {
		super(ErrorCode.AUTHENTICATION_FAILURE, errors);
	}

}
