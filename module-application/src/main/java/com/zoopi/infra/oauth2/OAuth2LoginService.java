package com.zoopi.infra.oauth2;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import com.zoopi.domain.member.entity.oauth2.SnsProvider;
import com.zoopi.infra.oauth2.naver.NaverLoginClient;
import com.zoopi.security.oauth2.OAuth2Attributes;

@Service
@RequiredArgsConstructor
public class OAuth2LoginService {

	private final NaverLoginClient naverLoginClient;

	public OAuth2Attributes getOAuth2Attributes(SnsProvider provider, String code, String status) {
		switch (provider) {
			case NAVER:
				final String accessToken = naverLoginClient.issueAccessToken(code, status).getAccessToken();
				final OAuth2Attributes oAuth2Attributes = naverLoginClient.getOAuth2Attributes(accessToken);
				naverLoginClient.deleteAccessToken(accessToken);
				return oAuth2Attributes;
			default:
				throw new IllegalArgumentException();
		}
	}

}
