package com.zoopi.infra.oauth2.naver.dto;

import static com.zoopi.domain.member.entity.oauth2.SnsProvider.*;
import static com.zoopi.infra.oauth2.naver.dto.TokenRequest.GrantTypes.*;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Reference: <a href="https://developers.naver.com/docs/login/api/api.md">NAVER Developers</a>
 */
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class TokenRequest {

	private String grantType;
	private String clientId;
	private String clientSecret;
	private String code;
	private String state;
	private String refreshToken;
	private String accessToken;
	private String serviceProvider;

	public static TokenRequest newIssueRequest(String clientId, String clientSecret, String code, String state) {
		return TokenRequest.builder()
			.grantType(ISSUE.getType())
			.clientId(clientId)
			.clientSecret(clientSecret)
			.code(code)
			.state(state)
			.build();
	}

	public static TokenRequest newRefreshRequest(String clientId, String clientSecret, String refreshToken) {
		return TokenRequest.builder()
			.grantType(REFRESH.getType())
			.clientId(clientId)
			.clientSecret(clientSecret)
			.refreshToken(refreshToken)
			.build();
	}

	public static TokenRequest newDeleteRequest(String clientId, String clientSecret, String accessToken) {
		return TokenRequest.builder()
			.grantType(DELETE.getType())
			.clientId(clientId)
			.clientSecret(clientSecret)
			.accessToken(accessToken)
			.serviceProvider(NAVER.getProvider().toUpperCase())
			.build();
	}

	@Getter
	@AllArgsConstructor
	public enum GrantTypes {

		ISSUE("authorization_code"),
		REFRESH("refresh_token"),
		DELETE("delete");

		private String type;

	}

}
