package com.zoopi.util;

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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import com.zoopi.domain.member.entity.Member;
import com.zoopi.domain.member.entity.MemberAuthority;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtils {

	private static final String AUTHORITIES_KEY = "authorities";
	private static final String JWT_TYPE = "jwt_type";
	private static final String TYPE = "typ";
	private static final String JWT = "JWT";

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

	public Authentication getAuthentication(String token) {
		final Claims claims = getAllClaims(token);
		final List<SimpleGrantedAuthority> authorities = Arrays.stream(
				claims.get(AUTHORITIES_KEY).toString().split(","))
			.map(SimpleGrantedAuthority::new)
			.collect(Collectors.toList());
		final User principal = new User(claims.getSubject(), "", authorities);

		return new UsernamePasswordAuthenticationToken(principal, token, authorities);
	}

	public String generateJwt(OAuth2User oAuth2User, JwtType type) {
		final Map<String, Object> claims = new HashMap<>();
		final String authorities = oAuth2User.getAuthorities().stream()
			.map(GrantedAuthority::getAuthority)
			.collect(Collectors.joining(","));
		claims.put(AUTHORITIES_KEY, authorities);
		claims.put(JWT_TYPE, type);

		final Map<String, Object> headers = new HashMap<>();
		headers.put(TYPE, JWT);

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
		claims.put(AUTHORITIES_KEY, authorities);
		claims.put(JWT_TYPE, type);

		final Map<String, Object> headers = new HashMap<>();
		headers.put(TYPE, JWT);

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

	public String getUsername(String token) {
		return getClaim(token, Claims::getSubject);
	}

	public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaims(token);
		return claimsResolver.apply(claims);
	}

	private Claims getAllClaims(String token) {
		return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
	}

	public enum JwtType {

		ACCESS_TOKEN, REFRESH_TOKEN

	}

}
