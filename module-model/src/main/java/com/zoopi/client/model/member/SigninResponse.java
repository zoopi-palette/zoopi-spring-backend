package com.zoopi.client.model.member;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SigninResponse {

	private JwtDto jwt;
	private SigninResult result;

	public enum SigninResult {

		SUCCESS, NONEXISTENT_USERNAME, MISMATCHED_PASSWORD

	}

}
