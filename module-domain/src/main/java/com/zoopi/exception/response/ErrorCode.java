package com.zoopi.exception.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

	// Global
	INTERNAL_SERVER_ERROR(500, "E-G001", "내부 서버 오류입니다."),
	INPUT_VALUE_INVALID(400, "E-G002", "입력 값이 유효하지 않습니다."),
	INPUT_TYPE_INVALID(400, "E-G003", "입력 타입이 유효하지 않습니다."),
	METHOD_NOT_ALLOWED(405, "E-G004", "허용되지 않은 HTTP method 입니다."),
	HTTP_MESSAGE_NOT_READABLE(400, "E-G005", "HTTP Request Body 형식이 올바르지 않습니다."),
	REQUEST_PARAMETER_MISSING(400, "E-G006", "요청 파라미터는 필수입니다."),
	REQUEST_HEADER_MISSING(400, "E-G007", "요청 헤더는 필수입니다."),

	// Authorization
	AUTHORIZATION_HEADER_MISSING(400, "E-A001", "인증 헤더는 필수입니다."),
	BEARER_PREFIX_MISSING(400, "E-A002", "인증 헤더에 Bearer 접두사는 필수입니다."),
	AUTHENTICATION_FAILURE(401, "E-A003", "인증에 실패하였습니다."),
	INSUFFICIENT_AUTHORITY(403, "E-A004", "접근 권한이 부족합니다."),

	// OAuth2
	UNSUPPORTED_PLATFORM_SIGN_IN(400, "E-O001", "지원하지 않는 플랫폼 로그인입니다."),

	// Jwt
	JWT_INVALID(400, "E-J001", "유효하지 않은 토큰입니다."),
	JWT_EXPIRED(400, "E-J002", "만료된 토큰입니다."),
	JWT_MALFORMED(400, "E-J003", "구조가 올바르지 않은 토큰입니다."),
	JWT_UNSUPPORTED(400, "E-J004", "지원하지 않는 형식의 토큰입니다."),
	JWT_SIGNATURE_INVALID(400, "E-J005", "서명이 올바르지 않은 토큰입니다."),

	// Member
	MEMBER_NOT_FOUND(400, "E-M001", "존재하지 않는 회원입니다"),
	PASSWORD_INPUT_MISMATCHED(400, "E-M002", "비밀번호와 비밀번호 확인이 일치하지 않습니다."),

	// BloodPost
	BLOODPOST_NOT_FOUND(400, "E-BP001", "존재하지 않는 헌혈 요청글입니다."),

	;

	private final int status;
	private final String code;
	private final String message;

}
