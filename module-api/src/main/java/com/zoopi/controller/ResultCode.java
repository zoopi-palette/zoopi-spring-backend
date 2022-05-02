package com.zoopi.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCode {

	// Member
	EMAIL_AVAILABLE(200, "R-M001", "사용 가능한 이메일입니다."),
	EMAIL_DUPLICATE(200, "R-M002", "이미 사용 중인 이메일입니다."),
	;

	private final int status;
	private final String code;
	private final String message;
}
