package com.zoopi.domain.phoneauthentication.entity;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PhoneAuthenticationConstants {

	public static final long PHONE_AUTHENTICATION_CODE_VALID_MINUTES = 5L;
	public static final long PHONE_AUTHENTICATION_KEY_VALID_MINUTES = 30L;
	public static final String PHONE_AUTHENTICATION_CODE_PHRASE = "[zoopi] 인증번호 [%s]를 입력해 주세요.";
	public static final int PHONE_AUTHENTICATION_CODE_LENGTH = 6;
	public static final int PHONE_AUTHENTICATION_SEND_MAX_COUNT = 5;

}
