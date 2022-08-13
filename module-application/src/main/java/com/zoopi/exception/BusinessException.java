package com.zoopi.exception;

import java.util.ArrayList;
import java.util.List;

import com.zoopi.ErrorCode;
import com.zoopi.ErrorResponse;

import lombok.Getter;

@Getter
public abstract class BusinessException extends RuntimeException {

	private final ErrorCode errorCode;
	private final List<ErrorResponse.FieldError> errors;

	public BusinessException(String message, ErrorCode errorCode) {
		super(message);
		this.errorCode = errorCode;
		this.errors = new ArrayList<>();
	}

	public BusinessException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
		this.errors = new ArrayList<>();
	}

	public BusinessException(ErrorCode errorCode, List<ErrorResponse.FieldError> errors) {
		super(errorCode.getMessage());
		this.errors = errors;
		this.errorCode = errorCode;
	}

}
