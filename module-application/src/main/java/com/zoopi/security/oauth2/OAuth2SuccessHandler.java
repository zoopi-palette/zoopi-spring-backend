package com.zoopi.security.oauth2;

import static com.zoopi.ResultCode.*;

import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.zoopi.ResultResponse;
import com.zoopi.client.member.model.JwtDto;
import com.zoopi.util.JwtUtils;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

	private final JwtUtils jwtUtils;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
		Authentication authentication) {
		final OAuth2User user = (OAuth2User)authentication.getPrincipal();
		final JwtDto jwtDto = generateJwt(user);
		setResponse(response, jwtDto);
	}

	private JwtDto generateJwt(OAuth2User oAuth2User) {
		final String accessToken = jwtUtils.generateAccessToken(oAuth2User);
		final String refreshToken = jwtUtils.generateRefreshToken(oAuth2User);
		return new JwtDto(accessToken, refreshToken);
	}

	private void setResponse(HttpServletResponse response, JwtDto jwtDto) {
		final ObjectMapper objectMapper = new ObjectMapper();
		try (OutputStream os = response.getOutputStream()) {
			objectMapper.writeValue(os, ResultResponse.of(SIGN_IN_SUCCESS, jwtDto));
			os.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}

		response.setStatus(HttpStatus.OK.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
	}

}
