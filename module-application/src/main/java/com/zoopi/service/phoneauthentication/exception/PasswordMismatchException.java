package com.zoopi.service.phoneauthentication.exception;

import com.zoopi.exception.BusinessException;
import com.zoopi.model.ErrorCode;

public class PasswordMismatchException extends BusinessException {

	public PasswordMismatchException() {
		super(ErrorCode.PASSWORD_INPUT_MISMATCHED);
	}

}
