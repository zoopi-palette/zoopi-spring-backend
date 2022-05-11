package com.zoopi.domain.member.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zoopi.domain.member.entity.JoinType;
import com.zoopi.domain.member.entity.Member;
import com.zoopi.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;

	public boolean validateEmail(String email) {
		return memberRepository.findByEmail(email).isEmpty();
	}

	public boolean validatePhone(String phone) {
		return memberRepository.findByPhone(phone).isEmpty();
	}

	@Transactional
	public Member createMember(String email, String phone, String name, String password) {
		return memberRepository.save(new Member(email, passwordEncoder.encode(password), name, phone, JoinType.EMAIL));
	}

}
