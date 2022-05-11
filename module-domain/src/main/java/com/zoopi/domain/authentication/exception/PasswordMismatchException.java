package com.zoopi.domain.authentication.exception;

import com.zoopi.exception.BusinessException;
import com.zoopi.exception.response.ErrorCode;

public class PasswordMismatchException extends BusinessException {

	public PasswordMismatchException() {
		super(ErrorCode.PASSWORD_MISMATCHED);
	}

}
