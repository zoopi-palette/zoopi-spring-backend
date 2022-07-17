package com.zoopi.config.security.oauth2;

import static com.zoopi.util.Constants.*;

import java.util.HashMap;
import java.util.Map;

import com.zoopi.config.security.oauth2.exception.UnsupportedPlatformSignInException;
import com.zoopi.util.Constants;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class OAuth2Attributes {

	public static final String NAVER = "naver";

	private static final String NAVER_RESPONSE = "response";
	private static final String PROVIDER = "provider";
	private static final String OAUTH2_ID = "id";
	private static final String OAUTH2_KEY = "id";
	private static final String PHONE = "phone";
	private static final String MOBILE = "mobile";
	private static final String EMAIL = "email";

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
		final Map<String, Object> response = (Map<String, Object>)attributes.get(NAVER_RESPONSE);

		final String phone = response.containsKey(MOBILE) ? (String)response.get(MOBILE) : EMPTY;
		final String email = response.containsKey(EMAIL) ? (String)response.get(EMAIL) : EMPTY;
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
		map.put(PROVIDER, provider);
		map.put(OAUTH2_ID, attributeKey);
		map.put(OAUTH2_KEY, attributeKey);
		map.put(PHONE, phone);
		map.put(EMAIL, email);

		return map;
	}

}
