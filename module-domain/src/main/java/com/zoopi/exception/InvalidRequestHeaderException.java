package com.zoopi.exception;

import java.util.List;

import com.zoopi.exception.response.ErrorCode;
import com.zoopi.exception.response.ErrorResponse;

public class InvalidRequestHeaderException extends BusinessException {

	public InvalidRequestHeaderException(List<ErrorResponse.FieldError> errors) {
		super(ErrorCode.REQUEST_HEADER_MISSING, errors);
	}

}
