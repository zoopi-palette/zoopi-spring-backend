package com.zoopi.client.phoneauthentication.service.exception;

import com.zoopi.exception.BusinessException;
import com.zoopi.ErrorCode;

public class PasswordMismatchException extends BusinessException {

	public PasswordMismatchException() {
		super(ErrorCode.PASSWORD_INPUT_MISMATCHED);
	}

}
