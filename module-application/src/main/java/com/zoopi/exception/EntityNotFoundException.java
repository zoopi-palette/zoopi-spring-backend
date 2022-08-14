package com.zoopi.exception;

import com.zoopi.ErrorCode;

public class EntityNotFoundException extends BusinessException {

	public EntityNotFoundException(ErrorCode errorCode) {
		super(errorCode);
	}

}
