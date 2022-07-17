package com.zoopi.controller.member.mapper;

import static com.zoopi.controller.ResultCode.*;

import com.zoopi.controller.ResultResponse;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class FindUsernameResponseMapper {

	public static ResultResponse fromFindingUsername(String username) {
		return ResultResponse.of(FIND_USERNAME_SUCCESS, UsernameResponse.of(username));
	}

	@Getter
	@AllArgsConstructor(access = AccessLevel.PROTECTED)
	public static class UsernameResponse {

		private String username;

		public static UsernameResponse of(String username) {
			return new UsernameResponse(username);
		}

	}

}
