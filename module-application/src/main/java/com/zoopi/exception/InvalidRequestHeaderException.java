package com.zoopi.exception;

import java.util.List;

import com.zoopi.ErrorCode;
import com.zoopi.ErrorResponse;

public class InvalidRequestHeaderException extends BusinessException {

	public InvalidRequestHeaderException(List<ErrorResponse.FieldError> errors) {
		super(ErrorCode.REQUEST_HEADER_MISSING, errors);
	}

}
