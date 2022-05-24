package com.zoopi.util;

import javax.servlet.http.HttpServletRequest;

public class HttpServletRequestHeaderUtils {

	public static final String AUTHORIZATION_HEADER = "Authorization";

	public static String extractAuthorizationHeader(HttpServletRequest request) {
		return request.getHeader(AUTHORIZATION_HEADER);
	}

}
