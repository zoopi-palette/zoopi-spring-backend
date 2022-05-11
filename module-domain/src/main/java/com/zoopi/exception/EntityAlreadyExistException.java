package com.zoopi.exception;

import com.zoopi.exception.response.ErrorCode;

public class EntityAlreadyExistException extends BusinessException {

	public EntityAlreadyExistException(ErrorCode errorCode) {
		super(errorCode);
	}

}
