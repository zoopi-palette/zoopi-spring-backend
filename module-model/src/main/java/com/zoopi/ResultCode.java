package com.zoopi;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCode {

	// Member
	USERNAME_AVAILABLE(200, "R-M001", "사용 가능한 아이디입니다."),
	USERNAME_EXISTENT(200, "R-M002", "존재하는 아이디입니다."),
	PHONE_AVAILABLE(200, "R-M003", "사용 가능한 휴대폰 번호입니다."),
	PHONE_EXISTENT(200, "R-M004", "존재하는 휴대폰 번호입니다."),
	SIGN_UP_SUCCESS(200, "R-M005", "회원 가입에 성공하였습니다."),
	SIGN_IN_SUCCESS(200, "R-M006", "로그인에 성공하였습니다."),
	PASSWORD_MISMATCHED(200, "R-M007", "회원 비밀번호가 일치하지 않습니다."),
	USERNAME_NONEXISTENT(200, "R-M008", "회원 아이디가 올바르지 않습니다."),
	FIND_USERNAME_SUCCESS(200, "R-M009", "회원 아이디 찾기에 성공하였습니다."),
	PASSWORD_CHANGE_SUCCESS(200, "R-M010", "회원 비밀번호 변경에 성공하였습니다."),

	// Authentication
	AUTHENTICATION_CODE_EXPIRED(200, "R-A001", "만료된 인증 코드입니다."),
	AUTHENTICATION_CODE_MISMATCHED(200, "R-A002", "인증 코드가 일치하지 않습니다."),
	AUTHENTICATION_CODE_MATCHED(200, "R-A003", "인증 코드가 일치합니다."),
	DELETE_ALL_EXPIRED_AUTHENTICATION_CODES(200, "R-A004", "만료된 인증 코드를 모두 제거하였습니다."),
	SEND_AUTHENTICATION_CODE_SUCCESS(200, "R-A005", "인증 코드 전송에 성공하였습니다."),
	AUTHENTICATION_KEY_NOT_AUTHENTICATED(200, "R-A006", "인증 확인이 되지 않은 인증 키입니다."),
	AUTHENTICATION_KEY_EXPIRED(200, "R-A007", "만료된 인증 키입니다."),

	// Ban
	PHONE_BANNED(200, "R-B001", "인증번호 전송이 제한된 휴대폰 번호입니다. 24시간 후 재시도해 주세요."),
	;

	private final int status;
	private final String code;
	private final String message;

}
