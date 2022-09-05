package com.zoopi.infra.oauth2.naver.dto;

import static com.zoopi.infra.oauth2.naver.dto.TokenResponse.ResultTypes.*;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Reference: <a href="https://developers.naver.com/docs/login/api/api.md">NAVER Developers</a>
 */
@Getter
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TokenResponse {

	private String accessToken;
	private String refreshToken;
	private String tokenType;
	private String result;
	private Integer expiresIn;
	private String error;
	private String errorDescription;

	public boolean isSuccessToDeleteToken() {
		return this.result.equals(SUCCESS.getType());
	}

	@Getter
	@AllArgsConstructor
	public enum ResultTypes {

		SUCCESS("success");

		private String type;

	}

}
