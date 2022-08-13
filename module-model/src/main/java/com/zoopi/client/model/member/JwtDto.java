package com.zoopi.client.model.member;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JwtDto {

	private String accessToken;
	private String refreshToken;

}
