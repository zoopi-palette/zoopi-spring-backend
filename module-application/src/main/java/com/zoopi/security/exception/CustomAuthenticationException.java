package com.zoopi.security.exception;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.AuthenticationException;

import com.zoopi.model.ErrorCode;
import com.zoopi.model.ErrorResponse;

import lombok.Getter;

@Getter
public abstract class CustomAuthenticationException extends AuthenticationException {

	private final ErrorCode errorCode;
	private final List<ErrorResponse.FieldError> errors;

	public CustomAuthenticationException(ErrorCode errorCode, List<ErrorResponse.FieldError> errors) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
		this.errors = errors;
	}

	public CustomAuthenticationException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
		this.errors = new ArrayList<>();
	}

}
