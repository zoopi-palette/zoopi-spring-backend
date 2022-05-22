package com.zoopi.domain.member.util;

import java.util.List;

import com.zoopi.domain.member.entity.AuthorityType;
import com.zoopi.domain.member.entity.JoinType;
import com.zoopi.domain.member.entity.Member;
import com.zoopi.domain.member.entity.MemberAuthority;

public class MemberUtils {

	public static Member getMember(JoinType type) {
		return Member.builder()
			.joinType(type)
			.phone("01012341234")
			.name("홍길동")
			.password("qlalfqjsgh1!")
			.username("zoopi@gmail.com")
			.build();
	}

	public static List<MemberAuthority> getAuthorities(Member member, AuthorityType type) {
		if (type.equals(AuthorityType.ROLE_USER)) {
			return List.of(new MemberAuthority(member, AuthorityType.ROLE_USER));
		} else if (type.equals(AuthorityType.ROLE_ADMIN)) {
			return List.of(new MemberAuthority(member, AuthorityType.ROLE_USER),
				new MemberAuthority(member, AuthorityType.ROLE_ADMIN));
		} else {
			return null;
		}
	}

}
