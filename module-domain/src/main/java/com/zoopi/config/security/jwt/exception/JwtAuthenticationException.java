package com.zoopi.config.security.jwt.exception;

import java.util.List;

import com.zoopi.config.security.exception.CustomAuthenticationException;
import com.zoopi.exception.response.ErrorCode;
import com.zoopi.exception.response.ErrorResponse;

import lombok.Getter;

@Getter
public class JwtAuthenticationException extends CustomAuthenticationException {


	public JwtAuthenticationException(List<ErrorResponse.FieldError> errors) {
		super(ErrorCode.AUTHENTICATION_FAILURE, errors);
	}

}
