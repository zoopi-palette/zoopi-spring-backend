package com.zoopi.domain.authentication.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthenticationResponse {

	private String authenticationKey;

	private String expiredDate;

}
