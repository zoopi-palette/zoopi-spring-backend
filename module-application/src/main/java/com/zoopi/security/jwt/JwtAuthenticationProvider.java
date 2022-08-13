package com.zoopi.security.jwt;

import static com.zoopi.model.ErrorCode.*;
import static com.zoopi.util.Constants.*;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;

import com.zoopi.security.jwt.exception.JwtAuthenticationException;
import com.zoopi.model.ErrorResponse.FieldError;
import com.zoopi.util.JwtUtils;

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
			throw new JwtAuthenticationException(FieldError.of(AUTHORIZATION_HEADER, jwt, JWT_MALFORMED));
		} catch (UnsupportedJwtException e) {
			throw new JwtAuthenticationException(FieldError.of(AUTHORIZATION_HEADER, jwt, JWT_UNSUPPORTED));
		} catch (SignatureException e) {
			throw new JwtAuthenticationException(FieldError.of(AUTHORIZATION_HEADER, jwt, JWT_SIGNATURE_INVALID));
		} catch (ExpiredJwtException e) {
			throw new JwtAuthenticationException(FieldError.of(AUTHORIZATION_HEADER, jwt, JWT_EXPIRED));
		} catch (JwtException e) {
			throw new JwtAuthenticationException(FieldError.of(AUTHORIZATION_HEADER, jwt, JWT_INVALID));
		}
	}

	@Override
	public boolean supports(Class<?> aClass) {
		return JwtAuthenticationToken.class.isAssignableFrom(aClass);
	}

}
