package com.zoopi.security.oauth2;

import static com.zoopi.util.Constants.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import com.zoopi.domain.member.entity.Member;
import com.zoopi.domain.member.entity.oauth2.SnsProvider;
import com.zoopi.security.oauth2.exception.UnsupportedPlatformSignInException;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class OAuth2Attributes {

	public final static String PRIMARY_KEY = "pk";
	public final static String NAME_ATTRIBUTE_KEY = "nameAttributeKey";
	public final static String PROVIDER_KEY = "provider";
	public final static String PHONE_KEY = "phone";
	public final static String EMAIL_KEY = "email";

	private Map<String, Object> attributes;
	private String provider;
	private String attributeKey;
	private String phone;
	private String email;

	public static OAuth2Attributes of(String provider, Map<String, Object> attributes) {
		try {
			return of(SnsProvider.valueOf(provider), attributes);
		} catch (Exception e) {
			throw new UnsupportedPlatformSignInException();
		}
	}

	private static OAuth2Attributes of(SnsProvider snsProvider, Map<String, Object> attributes) {
		final Map<String, Object> response = (Map<String, Object>)attributes.get(snsProvider.getResponse());
		return OAuth2Attributes.builder()
			.provider(snsProvider.getProvider())
			.phone(response.containsKey(snsProvider.getPhone()) ?
				response.get(snsProvider.getPhone()).toString().replaceAll(DASH, EMPTY) : EMPTY)
			.email(response.containsKey(snsProvider.getEmail()) ? (String)response.get(snsProvider.getEmail()) : EMPTY)
			.attributes(response)
			.attributeKey(response.get(snsProvider.getId()).toString())
			.build();
	}

	public OAuth2User toOAuth2User(Member member) {
		final List<SimpleGrantedAuthority> authorities = Arrays.stream(member.getAuthorities().split(COMMA))
			.map(SimpleGrantedAuthority::new)
			.collect(Collectors.toList());

		final Map<String, Object> attributes = toMap();
		attributes.put(PRIMARY_KEY, member.getId());

		return new DefaultOAuth2User(authorities, attributes, NAME_ATTRIBUTE_KEY);
	}

	private Map<String, Object> toMap() {
		final Map<String, Object> map = new HashMap<>();
		map.put(NAME_ATTRIBUTE_KEY, this.attributeKey);
		map.put(PROVIDER_KEY, this.provider);
		map.put(PHONE_KEY, this.phone);
		map.put(EMAIL_KEY, this.email);
		return map;
	}

}
