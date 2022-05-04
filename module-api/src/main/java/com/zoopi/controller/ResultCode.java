package com.zoopi.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCode {

	// Member
	EMAIL_AVAILABLE(200, "R-M001", "사용 가능한 이메일입니다."),
	EMAIL_DUPLICATE(200, "R-M002", "이미 사용 중인 이메일입니다."),
	PHONE_AVAILABLE(200, "R-M003", "사용 가능한 휴대폰 번호입니다."),
	PHONE_DUPLICATE(200, "R-M004", "이미 사용 중인 휴대폰 번호입니다."),

	// Authentication
	AUTHENTICATION_CODE_EXPIRED(200, "R-A001", "만료된 인증 코드입니다."),
	AUTHENTICATION_CODE_MISMATCHED(200, "R-A002", "인증 코드가 일치하지 않습니다."),
	AUTHENTICATION_CODE_MATCHED(200, "R-A003", "인증 코드가 일치합니다."),
	DELETE_ALL_EXPIRED_AUTHENTICATION_CODES(200, "R-A004", "만료된 인증 코드를 모두 제거하였습니다."),


	// Sms
	SEND_SMS_SUCCESS(200, "R-S001", "메시지 전송에 성공하였습니다."),
	SEND_SMS_FAILURE(200, "R-S002", "메시지 전송에 실패하였습니다. 잠시 후 다시 시도해 주세요."),
	;

	private final int status;
	private final String code;
	private final String message;
}
