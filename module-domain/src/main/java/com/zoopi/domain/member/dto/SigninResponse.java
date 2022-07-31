package com.zoopi.domain.member.dto;

import com.zoopi.domain.phoneauthentication.dto.JwtDto;

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
