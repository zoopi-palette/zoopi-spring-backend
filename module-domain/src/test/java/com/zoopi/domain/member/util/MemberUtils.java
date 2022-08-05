package com.zoopi.domain.member.util;

import java.util.UUID;

import com.zoopi.domain.member.entity.Member;
import com.zoopi.domain.member.entity.oauth2.SnsAccount;
import com.zoopi.domain.member.entity.oauth2.SnsAccountPrimaryKey;
import com.zoopi.domain.member.entity.oauth2.SnsProvider;

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

	public static SnsAccount newSnsAccount(SnsProvider provider) {
		return SnsAccount.builder()
			.id(getPrimaryKey(provider))
			.member(newMember())
			.build();
	}

	public static SnsAccountPrimaryKey getPrimaryKey(SnsProvider provider) {
		return SnsAccountPrimaryKey.builder()
			.provider(provider)
			.id(UUID.randomUUID().toString())
			.build();
	}

}
