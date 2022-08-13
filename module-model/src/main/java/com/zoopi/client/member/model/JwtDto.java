package com.zoopi.client.member.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class JwtDto {

	private String accessToken;
	private String refreshToken;

}
