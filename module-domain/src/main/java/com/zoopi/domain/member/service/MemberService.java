package com.zoopi.domain.member.service;

import static com.zoopi.domain.member.dto.SigninResponse.SigninResult.*;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zoopi.domain.authentication.dto.JwtDto;
import com.zoopi.domain.member.dto.SigninResponse;
import com.zoopi.domain.member.entity.Member;
import com.zoopi.domain.member.entity.AuthorityType;
import com.zoopi.domain.member.entity.MemberAuthority;
import com.zoopi.domain.member.repository.MemberRepository;
import com.zoopi.domain.member.repository.MemberAuthorityRepository;
import com.zoopi.util.JwtUtils;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;
	private final MemberAuthorityRepository memberAuthorityRepository;
	private final JwtUtils jwtUtils;

	public boolean isAvailableUsername(String username) {
		return memberRepository.findByUsername(username).isEmpty();
	}

	public boolean isAvailablePhone(String phone) {
		return memberRepository.findByPhone(phone).isEmpty();
	}

	@Transactional
	public Member createMember(String username, String phone, String name, String password, String email) {
		final Member member = Member.builder()
			.username(username)
			.password(passwordEncoder.encode(password))
			.name(name)
			.phone(phone)
			.email(email)
			.build();

		memberRepository.save(member);
		addUserAuthority(member);

		return member;
	}

	private void addUserAuthority(Member member) {
		final MemberAuthority memberAuthority = MemberAuthority.builder()
			.member(member)
			.type(AuthorityType.ROLE_USER)
			.build();
		memberAuthorityRepository.save(memberAuthority);
	}

	public SigninResponse signin(String username, String password) {
		final Optional<Member> memberOptional = memberRepository.findByUsername(username);
		if (memberOptional.isEmpty()) {
			return new SigninResponse(new JwtDto("", ""), NONEXISTENT_USERNAME);
		}
		final Member member = memberOptional.get();

		final boolean isMatchedPassword = passwordEncoder.matches(password, member.getPassword());
		if (!isMatchedPassword) {
			return new SigninResponse(new JwtDto("", ""), MISMATCHED_PASSWORD);
		}

		final List<MemberAuthority> authorities = memberAuthorityRepository.findAllByMember(member);
		final JwtDto jwt = generateJwt(member, authorities);

		return new SigninResponse(jwt, SUCCESS);
	}

	private JwtDto generateJwt(Member member, List<MemberAuthority> authorities) {
		final String accessToken = jwtUtils.generateJwt(member, authorities, JwtUtils.JwtType.ACCESS_TOKEN);
		final String refreshToken = jwtUtils.generateJwt(member, authorities, JwtUtils.JwtType.REFRESH_TOKEN);

		return new JwtDto(accessToken, refreshToken);
	}

}
