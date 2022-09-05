package com.zoopi.infra.oauth2.naver.dto;

import static com.zoopi.domain.member.entity.oauth2.SnsProvider.*;
import static com.zoopi.util.Constants.*;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.zoopi.security.oauth2.OAuth2Attributes;

/**
 * Reference: <a href="https://developers.naver.com/docs/login/profile/profile.md">NAVER Developers</a>
 */
@Getter
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserInfoResponse {

	private String id;
	private String nickname;
	private String name;
	private String email;
	private String gender;
	private String age;
	private String birthday;
	private String profileImage;
	private String birthyear;
	private String mobile;

	public OAuth2Attributes toOAuth2Attributes() {
		return OAuth2Attributes.builder()
			.attributeKey(this.id)
			.email(this.email)
			.phone(this.mobile.replaceAll(DASH, EMPTY))
			.provider(NAVER.getProvider())
			.build();
	}
	
}
