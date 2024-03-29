package com.zoopi.security.matcher;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class SkipPathRequestMatcher implements RequestMatcher {

	private final OrRequestMatcher matcher;

	public SkipPathRequestMatcher(List<String> skipPaths) {
		final List<RequestMatcher> requestMatchers = skipPaths.stream()
			.map(AntPathRequestMatcher::new)
			.collect(Collectors.toList());
		this.matcher = new OrRequestMatcher(requestMatchers);
	}

	@Override
	public boolean matches(HttpServletRequest request) {
		return !matcher.matches(request);
	}

}
