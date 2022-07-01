package com.zoopi.config.security.oauth2;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import com.zoopi.config.security.oauth2.exception.UnsupportedPlatformSignInException;
import com.zoopi.domain.member.entity.JoinType;
import com.zoopi.domain.member.entity.MemberAuthority;
import com.zoopi.domain.member.entity.Member;
import com.zoopi.domain.member.entity.oauth2.NaverAccount;
import com.zoopi.domain.member.repository.oauth2.NaverAccountRepository;
import com.zoopi.domain.member.service.MemberAuthorityService;
import com.zoopi.domain.member.service.MemberService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

	private final MemberService memberService;
	private final MemberAuthorityService memberAuthorityService;
	private final NaverAccountRepository naverAccountRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
		final OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();
		final OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

		final String registrationId = userRequest.getClientRegistration().getRegistrationId();
		final String userNameAttributeName = userRequest.getClientRegistration()
			.getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

		final OAuth2Attributes oAuth2Attributes = OAuth2Attributes.of(registrationId, userNameAttributeName,
			oAuth2User.getAttributes());
		final Long id = Long.valueOf(userNameAttributeName);

		final boolean isFirstLogin;
		if (registrationId.equals(OAuth2Attributes.NAVER)) {
			isFirstLogin = naverAccountRepository.findById(id).isEmpty();
		} else {
			throw new UnsupportedPlatformSignInException();
		}

		final List<SimpleGrantedAuthority> authorities;
		final String email = oAuth2Attributes.getEmail();
		final String phone = oAuth2Attributes.getPhone();
		if (isFirstLogin) {
			final Member member = signup(registrationId, id, email, phone);
			authorities = memberAuthorityService.getMemberAuthorities(member).stream()
				.map(MemberAuthority::getType)
				.map(Enum::name)
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());
		} else {
			authorities = memberAuthorityService.getMemberAuthorities(email).stream()
				.map(MemberAuthority::getType)
				.map(Enum::name)
				.map(SimpleGrantedAuthority::new)
				.collect(Collectors.toList());
		}

		return new DefaultOAuth2User(authorities, oAuth2Attributes.convertToMap(), oAuth2Attributes.getAttributeKey());
	}

	private Member signup(String provider, Long id, String email, String phone) {
		final Member member;
		final String password = passwordEncoder.encode(UUID.randomUUID().toString());
		if (provider.equals(OAuth2Attributes.NAVER)) {
			member = memberService.createMember(email, phone, "", password, JoinType.NAVER);
			naverAccountRepository.save(new NaverAccount(id, member));
		} else {
			throw new UnsupportedPlatformSignInException();
		}

		return member;
	}

}
