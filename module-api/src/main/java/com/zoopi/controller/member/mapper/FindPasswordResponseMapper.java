package com.zoopi.controller.member.mapper;

import static com.zoopi.controller.ResultCode.*;

import com.zoopi.controller.ResultResponse;
import com.zoopi.controller.member.mapper.ValidationResponseMapper.ValidationResponse;

public class FindPasswordResponseMapper {

	public static ResultResponse fromCheckingUsername(boolean isExistentUsername, String username) {
		if (isExistentUsername) {
			return ResultResponse.of(USERNAME_EXISTENT);
		} else {
			final ValidationResponse response = ValidationResponse.of(username, false);
			return ResultResponse.of(USERNAME_NONEXISTENT, response);
		}
	}

}
