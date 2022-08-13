package com.zoopi.security.jwt.exception;

import java.util.List;

import com.zoopi.security.exception.CustomAuthenticationException;
import com.zoopi.model.ErrorCode;
import com.zoopi.model.ErrorResponse;

import lombok.Getter;

@Getter
public class JwtAuthenticationException extends CustomAuthenticationException {


	public JwtAuthenticationException(List<ErrorResponse.FieldError> errors) {
		super(ErrorCode.AUTHENTICATION_FAILURE, errors);
	}

}
