package com.zoopi.exception.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

	// Global
	INTERNAL_SERVER_ERROR(500, "G001", "내부 서버 오류입니다."),
	INPUT_VALUE_INVALID(400, "G002", "입력 값이 유효하지 않습니다."),
	INPUT_TYPE_INVALID(400, "G003", "입력 타입이 유효하지 않습니다."),
	METHOD_NOT_ALLOWED(405, "G004", "허용되지 않은 HTTP method 입니다."),
	HTTP_MESSAGE_NOT_READABLE(400, "G005", "HTTP Request Body 형식이 올바르지 않습니다."),
	REQUEST_PARAMETER_MISSING(400, "G006", "요청 파라미터는 필수입니다."),

	;

	private final int status;
	private final String code;
	private final String message;
}
