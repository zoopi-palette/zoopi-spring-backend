package com.zoopi.filter;

import static com.zoopi.exception.response.ErrorCode.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.zoopi.exception.AuthenticationException;
import com.zoopi.exception.InvalidRequestHeaderException;
import com.zoopi.exception.response.ErrorResponse;
import com.zoopi.util.JwtUtils;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

	public static final String AUTHORIZATION_HEADER = "Authorization";
	public static final String BEARER_PREFIX = "Bearer "; // RFC 6750: JWT, OAuth 2.0 token are bearer tokens
	public static final int BEARER_PREFIX_LENGTH = 7;

	private final JwtUtils jwtUtils;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		try {
			final String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);
			final List<ErrorResponse.FieldError> errors = validateAuthorizationHeader(authorizationHeader);
			if (errors.isEmpty()) {
				final String jwt = authorizationHeader.substring(BEARER_PREFIX_LENGTH);
				final String username = jwtUtils.getUsername(jwt);

				if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
					final Authentication authenticationToken = jwtUtils.getAuthentication(jwt);
					SecurityContextHolder.getContext().setAuthentication(authenticationToken);
				}
			} else {
				request.setAttribute("exception", new AuthenticationException(errors));
			}
		} catch (InvalidRequestHeaderException | JwtException e) {
			request.setAttribute("exception", e);
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("exception", e);
		}

		filterChain.doFilter(request, response);
	}

	private List<ErrorResponse.FieldError> validateAuthorizationHeader(String authorizationHeader) {
		final List<ErrorResponse.FieldError> errors = new ArrayList<>();
		if (authorizationHeader == null) {
			errors.add(new ErrorResponse.FieldError("Authorization", "",
				AUTHORIZATION_HEADER_MISSING.getMessage()));
		} else if (!authorizationHeader.startsWith(BEARER_PREFIX)) {
			errors.add(new ErrorResponse.FieldError("Authorization", authorizationHeader,
				INVALID_BEARER_PREFIX.getMessage()));
		}
		return errors;
	}

}
