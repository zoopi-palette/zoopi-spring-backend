package com.zoopi.config.security.oauth2.exception;

import com.zoopi.config.security.exception.CustomAuthenticationException;
import com.zoopi.exception.response.ErrorCode;

public class UnsupportedPlatformSignInException extends CustomAuthenticationException {

	public UnsupportedPlatformSignInException() {
		super(ErrorCode.UNSUPPORTED_PLATFORM_SIGN_IN);
	}

}
