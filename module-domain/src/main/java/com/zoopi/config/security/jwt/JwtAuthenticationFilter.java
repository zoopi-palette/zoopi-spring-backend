package com.zoopi.config.security.jwt;

import static com.zoopi.util.Constants.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import com.zoopi.config.security.jwt.exception.JwtAuthenticationException;
import com.zoopi.exception.InvalidRequestHeaderException;
import com.zoopi.util.JwtUtils;

public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	private final JwtUtils jwtUtils;
	private final Set<String> ignoreEndpoints = new HashSet<>();

	public JwtAuthenticationFilter(JwtUtils jwtUtils, String[] ignoreEndpoints1, String[] ignoreEndpoints2) {
		super(ALL_PATTERN);
		this.jwtUtils = jwtUtils;
		this.ignoreEndpoints.addAll(Arrays.asList(ignoreEndpoints1));
		this.ignoreEndpoints.addAll(Arrays.asList(ignoreEndpoints2));
	}

	@Override
	protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
		final String endpoint = request.getRequestURI();
		return !ignoreEndpoints.contains(endpoint);
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
		super.successfulAuthentication(request, response, chain, authResult);
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException failed) throws IOException, ServletException {
		SecurityContextHolder.clearContext();
		super.unsuccessfulAuthentication(request, response, failed);
	}

}
