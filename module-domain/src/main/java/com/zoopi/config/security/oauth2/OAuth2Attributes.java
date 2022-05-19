package com.zoopi.config.security.oauth2;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class OAuth2Attributes {

	public static final String KAKAO = "kakao";
	public static final String NAVER = "naver";

	private Map<String, Object> attributes;
	private String provider;
	private String attributeKey;
	private String nickname;
	private String email;

	public static OAuth2Attributes of(String provider, String attributeKey, Map<String, Object> attributes) {
		if (provider.equals(KAKAO)) {
			return ofKakao(provider, attributeKey, attributes);
		} else {
			return ofNaver(provider, attributeKey, attributes);
		}
	}

	private static OAuth2Attributes ofKakao(String provider, String attributeKey, Map<String, Object> attributes) {
		final Map<String, Object> profile = (Map<String, Object>)attributes.get("profile");
		final Map<String, Object> account = (Map<String, Object>)attributes.get("kakao_acount");

		final String nickname = profile.containsKey("nickname") ? (String)profile.get("nickname") : "";
		final String email = account.containsKey("email") ? (String)account.get("email") : "";
		return OAuth2Attributes.builder()
			.provider(provider)
			.nickname(nickname)
			.email(email)
			.attributes(attributes)
			.attributeKey(attributeKey)
			.build();
	}

	private static OAuth2Attributes ofNaver(String provider, String attributeKey, Map<String, Object> attributes) {
		final Map<String, Object> response = (Map<String, Object>)attributes.get("response");

		final String nickname = response.containsKey("nickname") ? (String)response.get("nickname") : "";
		final String email = response.containsKey("email") ? (String)response.get("email") : "";
		return OAuth2Attributes.builder()
			.provider(provider)
			.nickname(nickname)
			.email(email)
			.attributes(response)
			.attributeKey(attributeKey)
			.build();
	}

	public Map<String, Object> convertToMap() {
		final Map<String, Object> map = new HashMap<>();
		map.put("provider", provider);
		map.put("id", attributeKey);
		map.put("key", attributeKey);
		map.put("nickname", nickname);
		map.put("email", email);

		return map;
	}

}
