package com.zoopi.controller.member.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ValidationResponse {

	private String value;
	private boolean isValidated;

	public static ValidationResponse of(String username, boolean isValidated) {
		return new ValidationResponse(username, isValidated);
	}

}
