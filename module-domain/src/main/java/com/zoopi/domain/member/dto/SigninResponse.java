package com.zoopi.domain.member.dto;

import com.zoopi.domain.authentication.dto.JwtDto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SigninResponse {

	private JwtDto jwt;
	private SigninResult result;

	public enum SigninResult {

		SUCCESS, NONEXISTENT_USERNAME, MISMATCHED_PASSWORD

	}

}
