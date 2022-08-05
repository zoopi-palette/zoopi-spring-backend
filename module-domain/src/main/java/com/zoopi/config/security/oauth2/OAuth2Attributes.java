package com.zoopi.config.security.oauth2;

import static com.zoopi.util.Constants.*;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import com.zoopi.config.security.oauth2.exception.UnsupportedPlatformSignInException;
import com.zoopi.domain.member.entity.oauth2.SnsProvider;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class OAuth2Attributes {

	private Map<String, Object> attributes;
	private String provider;
	private String attributeKey;
	private String phone;
	private String email;

	public static OAuth2Attributes of(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
		try {
			final String registrationId = userRequest.getClientRegistration().getRegistrationId();
			return of(SnsProvider.valueOf(registrationId), userRequest, oAuth2User.getAttributes());
		} catch (Exception e) {
			throw new UnsupportedPlatformSignInException();
		}
	}

	private static OAuth2Attributes of(SnsProvider snsProvider, OAuth2UserRequest userRequest, Map<String, Object> attributes) {
		final Map<String, Object> response = (Map<String, Object>)attributes.get(snsProvider.getResponse());
		final String userNameAttributeName = userRequest.getClientRegistration()
			.getProviderDetails()
			.getUserInfoEndpoint()
			.getUserNameAttributeName();
		return OAuth2Attributes.builder()
			.provider(snsProvider.getProvider())
			.phone(response.containsKey(snsProvider.getPhone()) ? (String)response.get(snsProvider.getPhone()) : EMPTY)
			.email(response.containsKey(snsProvider.getEmail()) ? (String)response.get(snsProvider.getEmail()) : EMPTY)
			.attributes(response)
			.attributeKey(userNameAttributeName)
			.build();
	}

	public Map<String, Object> convertToMap() {
		final Map<String, Object> map = new HashMap<>();
		map.put("provider", this.provider);
		map.put("id", this.attributeKey);
		map.put("key", this.attributeKey);
		map.put("phone", this.phone);
		map.put("email", this.email);
		return map;
	}

}
