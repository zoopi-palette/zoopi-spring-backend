package com.zoopi.domain.phoneauthentication.exception;

import com.zoopi.exception.BusinessException;
import com.zoopi.exception.response.ErrorCode;

public class PasswordMismatchException extends BusinessException {

	public PasswordMismatchException() {
		super(ErrorCode.PASSWORD_INPUT_MISMATCHED);
	}

}
