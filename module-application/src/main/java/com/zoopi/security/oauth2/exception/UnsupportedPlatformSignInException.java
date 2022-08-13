package com.zoopi.security.oauth2.exception;

import com.zoopi.security.exception.CustomAuthenticationException;
import com.zoopi.model.ErrorCode;

public class UnsupportedPlatformSignInException extends CustomAuthenticationException {

	public UnsupportedPlatformSignInException() {
		super(ErrorCode.UNSUPPORTED_PLATFORM_SIGN_IN);
	}

}
