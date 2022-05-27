package com.zoopi.config.security.jwt;

import static com.zoopi.exception.response.ErrorCode.*;
import static com.zoopi.util.Constants.*;

import java.util.List;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.zoopi.config.security.jwt.exception.JwtAuthenticationException;
import com.zoopi.exception.response.ErrorResponse.FieldError;
import com.zoopi.util.JwtUtils;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

	private final JwtUtils jwtUtils;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		final String jwt = (String)authentication.getPrincipal();
		try {
			return jwtUtils.getAuthentication(jwt);
		} catch (MalformedJwtException e) {
			final List<FieldError> errors = FieldError.of(AUTHORIZATION_HEADER, jwt, JWT_MALFORMED);
			throw new JwtAuthenticationException(errors);
		} catch (UnsupportedJwtException e) {
			final List<FieldError> errors = FieldError.of(AUTHORIZATION_HEADER, jwt, JWT_UNSUPPORTED);
			throw new JwtAuthenticationException(errors);
		} catch (SignatureException e) {
			final List<FieldError> errors = FieldError.of(AUTHORIZATION_HEADER, jwt, JWT_SIGNATURE_INVALID);
			throw new JwtAuthenticationException(errors);
		} catch (ExpiredJwtException e) {
			final List<FieldError> errors = FieldError.of(AUTHORIZATION_HEADER, jwt, JWT_EXPIRED);
			throw new JwtAuthenticationException(errors);
		} catch (JwtException e) {
			final List<FieldError> errors = FieldError.of(AUTHORIZATION_HEADER, jwt, JWT_INVALID);
			throw new JwtAuthenticationException(errors);
		}
	}

	@Override
	public boolean supports(Class<?> aClass) {
		return JwtAuthenticationToken.class.isAssignableFrom(aClass);
	}

}
