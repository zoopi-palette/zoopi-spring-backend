package com.zoopi.util;

import static com.zoopi.exception.response.ErrorCode.*;
import static com.zoopi.util.Constants.*;
import static com.zoopi.util.HttpServletRequestHeaderUtils.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import com.zoopi.config.security.jwt.JwtAuthenticationToken;
import com.zoopi.domain.member.entity.Member;
import com.zoopi.domain.member.entity.MemberAuthority;
import com.zoopi.exception.InvalidRequestHeaderException;
import com.zoopi.exception.response.ErrorResponse;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtils {

	private static final String CLAIM_AUTHORITIES = "authorities";
	private static final String CLAIM_JWT_TYPE = "jwt_type";
	private static final String HEADER_TYPE = "typ";
	private static final String TOKEN_NAME = "JWT";
	private static final String TOKEN_TYPE = "Bearer"; // RFC 6750: JWT, OAuth 2.0 token are bearer tokens
	private static final int TOKEN_PREFIX_LENGTH = 7;

	@Value("${jwt.access-validity}")
	private long ACCESS_TOKEN_VALIDITY;
	@Value("${jwt.refresh-validity}")
	private long REFRESH_TOKEN_VALIDITY;
	@Value("${jwt.secret}")
	private String SECRET_KEY;

	@PostConstruct
	protected void init() {
		SECRET_KEY = Base64.getEncoder().encodeToString(SECRET_KEY.getBytes());
	}

	public String extractJwt(String authorizationHeader) throws InvalidRequestHeaderException {
		validateAuthorizationHeader(authorizationHeader);

		return authorizationHeader.substring(TOKEN_PREFIX_LENGTH);
	}

	private void validateAuthorizationHeader(String authorizationHeader) throws InvalidRequestHeaderException {
		final List<ErrorResponse.FieldError> errors;
		if (authorizationHeader == null) {
			errors = ErrorResponse.FieldError.of(AUTHORIZATION_HEADER, EMPTY, AUTHORIZATION_HEADER_MISSING);
		} else if (!authorizationHeader.startsWith(TOKEN_TYPE + SPACE)) {
			errors = ErrorResponse.FieldError.of(AUTHORIZATION_HEADER, authorizationHeader, BEARER_PREFIX_MISSING);
		} else {
			errors = new ArrayList<>();
		}

		if (!errors.isEmpty()) {
			throw new InvalidRequestHeaderException(errors);
		}
	}

	public Authentication getAuthentication(String token) throws JwtException {
		final Claims claims = getAllClaims(token);
		final List<SimpleGrantedAuthority> authorities = Arrays.stream(
				claims.get(CLAIM_AUTHORITIES).toString().split(","))
			.map(SimpleGrantedAuthority::new)
			.collect(Collectors.toList());
		final User principal = new User(claims.getSubject(), EMPTY, authorities);

		return JwtAuthenticationToken.of(principal, token, authorities);
	}

	public String generateJwt(OAuth2User oAuth2User, JwtType type) {
		final Map<String, Object> claims = new HashMap<>();
		final String authorities = oAuth2User.getAuthorities().stream()
			.map(GrantedAuthority::getAuthority)
			.collect(Collectors.joining(","));
		claims.put(CLAIM_AUTHORITIES, authorities);
		claims.put(CLAIM_JWT_TYPE, type);

		final Map<String, Object> headers = new HashMap<>();
		headers.put(HEADER_TYPE, TOKEN_NAME);

		final Date now = new Date(System.currentTimeMillis());
		final Date validity = new Date(System.currentTimeMillis() +
			(type.equals(JwtType.ACCESS_TOKEN) ? ACCESS_TOKEN_VALIDITY : REFRESH_TOKEN_VALIDITY));
		final String email = (String)oAuth2User.getAttributes().get("email");

		return Jwts.builder()
			.setHeader(headers)
			.setClaims(claims)
			.setSubject(email)
			.setIssuedAt(now)
			.setExpiration(validity)
			.signWith(SignatureAlgorithm.HS512, SECRET_KEY)
			.compact();
	}

	public String generateJwt(Member member, List<MemberAuthority> memberAuthorities, JwtType type) {
		final Map<String, Object> claims = new HashMap<>();
		final String authorities = memberAuthorities.stream()
			.map(MemberAuthority::getType)
			.map(Enum::name)
			.collect(Collectors.joining(","));
		claims.put(CLAIM_AUTHORITIES, authorities);
		claims.put(CLAIM_JWT_TYPE, type);

		final Map<String, Object> headers = new HashMap<>();
		headers.put(HEADER_TYPE, TOKEN_NAME);

		final Date now = new Date(System.currentTimeMillis());
		final Date validity = new Date(System.currentTimeMillis() +
			(type.equals(JwtType.ACCESS_TOKEN) ? ACCESS_TOKEN_VALIDITY : REFRESH_TOKEN_VALIDITY));
		final String email = member.getUsername();

		return Jwts.builder()
			.setHeader(headers)
			.setClaims(claims)
			.setSubject(email)
			.setIssuedAt(now)
			.setExpiration(validity)
			.signWith(SignatureAlgorithm.HS512, SECRET_KEY)
			.compact();
	}

	public String getUsername(String token) throws JwtException {
		return getClaim(token, Claims::getSubject);
	}

	public <T> T getClaim(String token, Function<Claims, T> claimsResolver) throws JwtException {
		final Claims claims = getAllClaims(token);
		return claimsResolver.apply(claims);
	}

	private Claims getAllClaims(String token) throws JwtException {
		return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
	}

	public enum JwtType {

		ACCESS_TOKEN, REFRESH_TOKEN

	}

}
