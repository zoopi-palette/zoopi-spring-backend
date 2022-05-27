package com.zoopi.config.security.handler;

import static com.zoopi.exception.response.ErrorCode.*;

import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zoopi.config.security.jwt.exception.JwtAuthenticationException;
import com.zoopi.exception.response.ErrorResponse;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException authException) {
		final JwtAuthenticationException exception = (JwtAuthenticationException)authException;
		final int status = AUTHENTICATION_FAILURE.getStatus();
		final String code = AUTHENTICATION_FAILURE.getCode();
		final String message = AUTHENTICATION_FAILURE.getMessage();
		final List<ErrorResponse.FieldError> errors = exception.getErrors();
		final ErrorResponse errorResponse = ErrorResponse.of(status, code, message, errors);

		final ObjectMapper objectMapper = new ObjectMapper();
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
