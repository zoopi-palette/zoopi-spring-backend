package com.zoopi.infra.oauth2.naver;

import static com.zoopi.util.Constants.*;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.http.MediaType.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.zoopi.infra.oauth2.naver.dto.TokenRequest;
import com.zoopi.infra.oauth2.naver.dto.TokenResponse;
import com.zoopi.infra.oauth2.naver.dto.UserInfoResponse;
import com.zoopi.security.oauth2.OAuth2Attributes;
import com.zoopi.util.JwtUtils;

@Slf4j
@Component
@RequiredArgsConstructor
public class NaverLoginClient {

	private final RestTemplate restTemplate;

	@Value("${spring.security.oauth2.client.provider.naver.user-info-uri}")
	private String USER_INFO_URI;
	@Value("${spring.security.oauth2.client.provider.naver.token-uri}")
	private String TOKEN_URI;
	@Value("${spring.security.oauth2.client.registration.naver.client-id}")
	private String CLIENT_ID;
	@Value("${spring.security.oauth2.client.registration.naver.client-secret}")
	private String CLIENT_SECRET;

	public OAuth2Attributes getOAuth2Attributes(String accessToken) {
		return getUserInfoResponse(accessToken).toOAuth2Attributes();
	}

	public TokenResponse issueAccessToken(String code, String status) {
		final TokenRequest tokenRequest = TokenRequest.newIssueRequest(CLIENT_ID, CLIENT_SECRET, code, status);
		return getTokenResponse(tokenRequest);
	}

	public TokenResponse deleteAccessToken(String accessToken) {
		final TokenRequest tokenRequest = TokenRequest.newDeleteRequest(CLIENT_ID, CLIENT_SECRET, accessToken);
		final TokenResponse tokenResponse = getTokenResponse(tokenRequest);
		if (tokenResponse.isSuccessToDeleteToken()) {
			log.info("Access Token was deleted From Naver. Access Token={}", tokenResponse.getAccessToken());
		} else {
			log.warn("Access Token wasn't deleted From Naver. Access Token={}, Error Code={}, Error Message={}",
				tokenResponse.getAccessToken(), tokenResponse.getError(), tokenResponse.getErrorDescription());
		}
		return tokenResponse;
	}

	private UserInfoResponse getUserInfoResponse(String accessToken) {
		final HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set(CONTENT_TYPE_HEADER, APPLICATION_FORM_URLENCODED_VALUE);
		httpHeaders.set(AUTHORIZATION_HEADER, JwtUtils.TOKEN_TYPE + SPACE + accessToken);
		final HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(null, httpHeaders);
		return restTemplate.exchange(USER_INFO_URI, GET, requestEntity, UserInfoResponse.class).getBody();
	}

	private TokenResponse getTokenResponse(TokenRequest tokenRequest) {
		final RequestEntity<TokenRequest> requestEntity = RequestEntity
			.post(TOKEN_URI)
			.contentType(APPLICATION_JSON)
			.body(tokenRequest);
		return restTemplate.exchange(requestEntity, TokenResponse.class).getBody();
	}

}
