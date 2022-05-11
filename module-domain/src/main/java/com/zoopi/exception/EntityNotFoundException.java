package com.zoopi.exception;

import com.zoopi.exception.response.ErrorCode;

public class EntityNotFoundException extends BusinessException {

	public EntityNotFoundException(ErrorCode errorCode) {
		super(errorCode);
	}

}
