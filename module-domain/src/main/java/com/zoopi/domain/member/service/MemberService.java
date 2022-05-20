package com.zoopi.domain.member.service;

import static com.zoopi.domain.member.dto.SigninResponse.SigninResult.*;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zoopi.domain.authentication.dto.JwtDto;
import com.zoopi.domain.member.dto.SigninResponse;
import com.zoopi.domain.member.entity.JoinType;
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

	public boolean validateEmail(String email) {
		return memberRepository.findByUsername(email).isEmpty();
	}

	public boolean validatePhone(String phone) {
		return memberRepository.findByPhone(phone).isEmpty();
	}

	@Transactional
	public Member createMember(String email, String phone, String name, String password, JoinType joinType) {
		final Member member = Member.builder()
			.username(email)
			.password(passwordEncoder.encode(password))
			.name(name)
			.phone(phone)
			.joinType(joinType)
			.build();
		memberRepository.save(member);

		final MemberAuthority memberAuthority = MemberAuthority.builder()
			.member(member)
			.type(AuthorityType.ROLE_USER)
			.build();
		memberAuthorityRepository.save(memberAuthority);

		return member;
	}

	public SigninResponse signin(String email, String password) {
		final Optional<Member> memberOptional = memberRepository.findByUsername(email);
		if (memberOptional.isEmpty()) {
			return new SigninResponse(new JwtDto("", ""), NONEXISTENT_USERNAME);
		}
		final Member member = memberOptional.get();

		final boolean isMatchedPassword = passwordEncoder.matches(password, member.getPassword());
		if (!isMatchedPassword) {
			return new SigninResponse(new JwtDto("", ""), MISMATCHED_PASSWORD);
		}

		final List<MemberAuthority> authorities = memberAuthorityRepository.findAllByMember(member);
		final String accessToken = jwtUtils.generateJwt(member, authorities, JwtUtils.JwtType.ACCESS_TOKEN);
		final String refreshToken = jwtUtils.generateJwt(member, authorities, JwtUtils.JwtType.REFRESH_TOKEN);

		final JwtDto jwt = new JwtDto(accessToken, refreshToken);
		return new SigninResponse(jwt, SUCCESS);
	}

}
