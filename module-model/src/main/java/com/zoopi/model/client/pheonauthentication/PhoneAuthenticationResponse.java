package com.zoopi.model.client.pheonauthentication;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import com.zoopi.domain.phoneauthentication.entity.PhoneAuthentication;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class PhoneAuthenticationResponse {

	private String authenticationKey;

	private String authenticationCodeExpiredDate;

	public static PhoneAuthenticationResponse of(PhoneAuthentication phoneAuthentication) {
		final String key = phoneAuthentication.getId();
		final String codeExpiredDateToString = phoneAuthentication.getCodeExpiredDateToString();
		return new PhoneAuthenticationResponse(key, codeExpiredDateToString);
	}

}
