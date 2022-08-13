package com.zoopi.model.client.member;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JwtDto {

	private String accessToken;
	private String refreshToken;

}
