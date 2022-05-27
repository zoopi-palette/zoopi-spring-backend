package com.zoopi.config.security.jwt;

import java.util.Collection;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class JwtAuthenticationToken extends UsernamePasswordAuthenticationToken {

	private JwtAuthenticationToken(Object principal, Object credentials) {
		super(principal, credentials);
	}

	private JwtAuthenticationToken(Object principal, Object credentials,
		Collection<? extends GrantedAuthority> authorities) {
		super(principal, credentials, authorities);
	}

	public static JwtAuthenticationToken of(String jwt) {
		return new JwtAuthenticationToken(jwt, jwt);
	}

	public static JwtAuthenticationToken of(Object principal, Object credentials,
		Collection<? extends GrantedAuthority> authorities) {
		return new JwtAuthenticationToken(principal, credentials, authorities);
	}

}
