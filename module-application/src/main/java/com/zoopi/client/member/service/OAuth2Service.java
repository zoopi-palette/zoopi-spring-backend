package com.zoopi.client.member.service;

import static com.zoopi.util.Constants.*;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.zoopi.domain.member.entity.Member;
import com.zoopi.domain.member.entity.oauth2.SnsAccount;
import com.zoopi.domain.member.entity.oauth2.SnsAccountPrimaryKey;
import com.zoopi.domain.member.entity.oauth2.SnsProvider;
import com.zoopi.security.oauth2.OAuth2Attributes;

@Service
@RequiredArgsConstructor
public class OAuth2Service {

	private final static String OAUTH2_USERNAME_PREFIX = "user";

	private final MemberService memberService;
	private final SnsAccountService snsAccountService;

	@Transactional
	public Member getMember(OAuth2Attributes oAuth2Attributes) {
		final SnsProvider provider = SnsProvider.valueOf(oAuth2Attributes.getProvider());
		final String userId = oAuth2Attributes.getAttributeKey();
		final SnsAccountPrimaryKey primaryKey = new SnsAccountPrimaryKey(provider, userId);
		return snsAccountService.getWithMember(primaryKey)
			.map(SnsAccount::getMember)
			.orElseGet(() -> signup(primaryKey, oAuth2Attributes));
	}

	private Member signup(SnsAccountPrimaryKey primaryKey, OAuth2Attributes oAuth2Attributes) {
		final String username = OAUTH2_USERNAME_PREFIX + DELIMITER + System.currentTimeMillis();
		final String password = UUID.randomUUID().toString();
		final String phone = oAuth2Attributes.getPhone();
		final String email = oAuth2Attributes.getEmail();
		final Member member = memberService.signup(username, phone, EMPTY, password, email);
		snsAccountService.connect(member, primaryKey);

		return member;
	}

}
