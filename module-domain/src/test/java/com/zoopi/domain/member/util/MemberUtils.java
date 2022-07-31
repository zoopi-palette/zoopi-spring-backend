package com.zoopi.domain.member.util;

import com.zoopi.domain.member.entity.Member;

public class MemberUtils {

	public static Member newMember() {
		return Member.builder()
			.username("zoopi")
			.password("qlalfqjsgh1!")
			.name("홍길동")
			.email("zoopi@gmail.com")
			.phone("01012341234")
			.build();
	}

}
