package com.zoopi.exception;

import com.zoopi.model.ErrorCode;

public class EntityAlreadyExistException extends BusinessException {

	public EntityAlreadyExistException(ErrorCode errorCode) {
		super(errorCode);
	}

}
