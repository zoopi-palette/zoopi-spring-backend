package com.zoopi.client.member.controller;

import static com.zoopi.ResultCode.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import com.zoopi.ResultResponse;
import com.zoopi.client.member.api.MemberOAuth2Api;
import com.zoopi.client.member.model.JwtDto;
import com.zoopi.client.member.model.SigninAppRequest;
import com.zoopi.client.member.service.MemberService;
import com.zoopi.client.member.service.OAuth2Service;
import com.zoopi.domain.member.entity.Member;
import com.zoopi.infra.oauth2.OAuth2LoginService;
import com.zoopi.security.oauth2.OAuth2Attributes;

@RestController
@RequiredArgsConstructor
public class MemberOAuth2Controller implements MemberOAuth2Api {

	private final OAuth2LoginService oAuth2LoginService;
	private final OAuth2Service oAuth2Service;
	private final MemberService memberService;

	@Override
	public void authorize(String provider) {}

	@Override
	public void callback(String provider, String code, String status) {}

	@Override
	public ResponseEntity<ResultResponse> signinApp(SigninAppRequest request) {
		final OAuth2Attributes oAuth2Attributes = oAuth2LoginService.getOAuth2Attributes(request.getProvider(),
			request.getCode(), request.getStatus());
		final Member member = oAuth2Service.getMember(oAuth2Attributes);
		final JwtDto jwtDto = memberService.generateJwt(member);
		return ResponseEntity.ok(ResultResponse.of(SIGN_IN_SUCCESS, jwtDto));
	}

}
