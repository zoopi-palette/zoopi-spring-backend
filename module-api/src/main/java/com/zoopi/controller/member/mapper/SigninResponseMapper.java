package com.zoopi.controller.member.mapper;

import static com.zoopi.controller.ResultCode.*;

import com.zoopi.controller.ResultResponse;
import com.zoopi.domain.member.dto.SigninResponse;

public class SigninResponseMapper {

	public static ResultResponse fromSigningIn(SigninResponse response) {
		switch (response.getResult()) {
			case NONEXISTENT_USERNAME:
				return ResultResponse.of(USERNAME_NONEXISTENT, response.getJwt());
			case MISMATCHED_PASSWORD:
				return ResultResponse.of(PASSWORD_MISMATCHED, response.getJwt());
			default:
				return ResultResponse.of(SIGN_UP_SUCCESS, response.getJwt());
		}
	}

}
