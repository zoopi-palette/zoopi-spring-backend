package com.zoopi.exception;

import java.util.List;

import com.zoopi.exception.response.ErrorCode;
import com.zoopi.exception.response.ErrorResponse;

public class InvalidArgumentException extends BusinessException {

	public InvalidArgumentException(List<ErrorResponse.FieldError> errors) {
		super(ErrorCode.INPUT_VALUE_INVALID, errors);
	}
}
