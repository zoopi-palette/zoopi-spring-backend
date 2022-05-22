package com.zoopi.config.security;

import static com.zoopi.exception.response.ErrorCode.*;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zoopi.exception.InvalidRequestHeaderException;
import com.zoopi.exception.response.ErrorResponse;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException authException) {
		final Exception exception = (Exception)request.getAttribute("exception");
		final ObjectMapper objectMapper = new ObjectMapper();
		final ErrorResponse errorResponse;

		if (exception instanceof InvalidRequestHeaderException) {
			final List<ErrorResponse.FieldError> errors = ((InvalidRequestHeaderException)exception).getErrors();
			errorResponse = ErrorResponse.of(AUTHENTICATION_FAILURE, errors);
		} else {
			final int status = AUTHENTICATION_FAILURE.getStatus();
			final String code = AUTHENTICATION_FAILURE.getCode();
			final String message = exception != null ? exception.getMessage() : AUTHENTICATION_FAILURE.getMessage();
			errorResponse = ErrorResponse.of(status, code, message, new ArrayList<>());
		}

		try (OutputStream os = response.getOutputStream()) {
			objectMapper.writeValue(os, errorResponse);
			os.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}

		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
	}

}
