package com.zoopi.exception;

import com.zoopi.ErrorCode;

public class EntityAlreadyExistException extends BusinessException {

	public EntityAlreadyExistException(ErrorCode errorCode) {
		super(errorCode);
	}

}
