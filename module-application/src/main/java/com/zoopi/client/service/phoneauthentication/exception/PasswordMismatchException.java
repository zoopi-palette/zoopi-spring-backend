package com.zoopi.client.service.phoneauthentication.exception;

import com.zoopi.exception.BusinessException;
import com.zoopi.ErrorCode;

public class PasswordMismatchException extends BusinessException {

	public PasswordMismatchException() {
		super(ErrorCode.PASSWORD_INPUT_MISMATCHED);
	}

}
