package com.zoopi.config.security.jwt;

import static com.zoopi.util.Constants.*;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.zoopi.config.security.jwt.exception.JwtAuthenticationException;
import com.zoopi.exception.InvalidRequestHeaderException;
import com.zoopi.util.JwtUtils;

public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	private final JwtUtils jwtUtils;

	public JwtAuthenticationFilter(RequestMatcher matcher, JwtUtils jwtUtils) {
		super(matcher);
		this.jwtUtils = jwtUtils;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws
		AuthenticationException {
		final String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);
		final String jwt;
		try {
			jwt = jwtUtils.extractJwt(authorizationHeader);
		} catch (InvalidRequestHeaderException e) {
			throw new JwtAuthenticationException(e.getErrors());
		}
		final JwtAuthenticationToken authentication = JwtAuthenticationToken.of(jwt);

		return super.getAuthenticationManager().authenticate(authentication);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
		Authentication authResult) throws IOException, ServletException {
		SecurityContextHolder.getContext().setAuthentication(authResult);
		chain.doFilter(request, response);
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException failed) throws IOException, ServletException {
		SecurityContextHolder.clearContext();
		super.getFailureHandler().onAuthenticationFailure(request, response, failed);
	}

}
