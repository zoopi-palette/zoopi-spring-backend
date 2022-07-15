package com.zoopi.config.security.oauth2;

import static com.zoopi.util.Constants.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.zoopi.domain.member.entity.oauth2.SnsAccount;
import com.zoopi.domain.member.entity.oauth2.SnsAccountPrimaryKey;
import com.zoopi.domain.member.entity.oauth2.SnsProvider;
import com.zoopi.domain.member.service.SnsAccountService;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import com.zoopi.domain.member.entity.Member;
import com.zoopi.domain.member.service.MemberService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

	private final static String USERNAME_PREFIX = "user_";
	public final static String PRIMARY_KEY = "primary_key";

	private final MemberService memberService;
	private final SnsAccountService snsAccountService;
	private final PasswordEncoder passwordEncoder;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) {
		final OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();
		final OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

		final String registrationId = userRequest.getClientRegistration().getRegistrationId();
		final String userNameAttributeName = userRequest.getClientRegistration()
			.getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

		final OAuth2Attributes oAuth2Attributes = OAuth2Attributes.of(registrationId, userNameAttributeName,
			oAuth2User.getAttributes());

		final SnsProvider provider = SnsProvider.valueOf(registrationId);
		final SnsAccountPrimaryKey primaryKey = new SnsAccountPrimaryKey(provider, userNameAttributeName);

		final Member member;
		final String email = oAuth2Attributes.getEmail();
		final String phone = oAuth2Attributes.getPhone();
		final Optional<SnsAccount> snsAccountOptional = snsAccountService.getWithMember(primaryKey);
		final boolean isFirstLogin = snsAccountOptional.isEmpty();
		if (isFirstLogin) {
			member = signup(primaryKey, email, phone);
		} else {
			member = snsAccountOptional.get().getMember();
		}

		final List<SimpleGrantedAuthority> authorities = Arrays.stream(member.getAuthorities().split(","))
			.map(SimpleGrantedAuthority::new)
			.collect(Collectors.toList());

		final Map<String, Object> attributes = oAuth2Attributes.convertToMap();
		attributes.put(PRIMARY_KEY, member.getId());
		return new DefaultOAuth2User(authorities, attributes, oAuth2Attributes.getAttributeKey());
	}

	private Member signup(SnsAccountPrimaryKey primaryKey, String email, String phone) {
		final String username = USERNAME_PREFIX + System.currentTimeMillis();
		final String password = passwordEncoder.encode(UUID.randomUUID().toString());
		final Member member = memberService.createMember(username, phone, EMPTY, password, email);
		snsAccountService.connect(member, primaryKey);

		return member;
	}

}
