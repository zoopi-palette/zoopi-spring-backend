package com.zoopi.service.member;

import static com.zoopi.model.ErrorCode.*;
import static com.zoopi.model.client.member.SigninResponse.SigninResult.*;
import static com.zoopi.util.Constants.*;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.zoopi.model.client.member.SigninResponse;
import com.zoopi.domain.member.entity.Member;
import com.zoopi.domain.member.repository.MemberRepository;
import com.zoopi.model.client.member.JwtDto;
import com.zoopi.exception.EntityNotFoundException;
import com.zoopi.util.JwtUtils;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;
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

		return memberRepository.save(member);
	}

	public SigninResponse signin(String username, String password) {
		final Optional<Member> memberOptional = memberRepository.findByUsername(username);
		if (memberOptional.isEmpty()) {
			return new SigninResponse(new JwtDto(EMPTY, EMPTY), NONEXISTENT_USERNAME);
		}

		final Member member = memberOptional.get();
		final boolean isMatchedPassword = passwordEncoder.matches(password, member.getPassword());
		if (!isMatchedPassword) {
			return new SigninResponse(new JwtDto(EMPTY, EMPTY), MISMATCHED_PASSWORD);
		}

		final JwtDto jwt = generateJwt(member);
		return new SigninResponse(jwt, SUCCESS);
	}

	public String getUsernameByPhone(String phone) {
		final Member member = memberRepository.findByPhone(phone)
			.orElseThrow(() -> new EntityNotFoundException(MEMBER_NOT_FOUND));
		return member.getUsername();
	}

	@Transactional
	public boolean changePassword(String username, String password) {
		final Member member = memberRepository.findByUsername(username)
			.orElseThrow(() -> new EntityNotFoundException(MEMBER_NOT_FOUND));
		member.changePassword(passwordEncoder.encode(password));
		return true;
	}

	private JwtDto generateJwt(Member member) {
		final String accessToken = jwtUtils.generateAccessToken(member);
		final String refreshToken = jwtUtils.generateRefreshToken(member);
		return new JwtDto(accessToken, refreshToken);
	}

}
