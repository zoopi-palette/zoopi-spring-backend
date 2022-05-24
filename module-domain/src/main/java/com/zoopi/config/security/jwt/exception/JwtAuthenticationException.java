package com.zoopi.config.security.jwt.exception;

import java.util.List;

import org.springframework.security.core.AuthenticationException;

import com.zoopi.exception.response.ErrorCode;
import com.zoopi.exception.response.ErrorResponse;

import lombok.Getter;

@Getter
public class JwtAuthenticationException extends AuthenticationException {

	private final List<ErrorResponse.FieldError> errors;

	public JwtAuthenticationException(List<ErrorResponse.FieldError> errors) {
		super(ErrorCode.AUTHENTICATION_FAILURE.getMessage());
		this.errors = errors;
	}

}
