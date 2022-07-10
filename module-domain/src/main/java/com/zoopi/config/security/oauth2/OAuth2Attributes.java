package com.zoopi.config.security.oauth2;

import java.util.HashMap;
import java.util.Map;

import com.zoopi.config.security.oauth2.exception.UnsupportedPlatformSignInException;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class OAuth2Attributes {

	public static final String NAVER = "naver";

	private Map<String, Object> attributes;
	private String provider;
	private String attributeKey;
	private String phone;
	private String email;

	public static OAuth2Attributes of(String provider, String attributeKey, Map<String, Object> attributes) {
		if (provider.equals(NAVER)) {
			return ofNaver(provider, attributeKey, attributes);
		} else {
			throw new UnsupportedPlatformSignInException();
		}
	}

	private static OAuth2Attributes ofNaver(String provider, String attributeKey, Map<String, Object> attributes) {
		final Map<String, Object> response = (Map<String, Object>)attributes.get("response");

		final String phone = response.containsKey("mobile") ? (String)response.get("mobile") : "";
		final String email = response.containsKey("email") ? (String)response.get("email") : "";
		return OAuth2Attributes.builder()
			.provider(provider)
			.phone(phone)
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
		map.put("phone", phone);
		map.put("email", email);

		return map;
	}

}
