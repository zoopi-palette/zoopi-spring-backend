package com.zoopi.domain.member.service;

import static com.zoopi.exception.response.ErrorCode.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zoopi.domain.member.entity.Member;
import com.zoopi.domain.member.entity.MemberAuthority;
import com.zoopi.domain.member.repository.MemberAuthorityRepository;
import com.zoopi.domain.member.repository.MemberRepository;
import com.zoopi.exception.EntityNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberAuthorityService {

	private final MemberRepository memberRepository;
	private final MemberAuthorityRepository memberAuthorityRepository;

	public List<MemberAuthority> getMemberAuthorities(String email) {
		final Member member = memberRepository.findByUsername(email)
			.orElseThrow(() -> new EntityNotFoundException(MEMBER_NOT_FOUND));
		return memberAuthorityRepository.findAllByMember(member);
	}

	public List<MemberAuthority> getMemberAuthorities(Member member) {
		return memberAuthorityRepository.findAllByMember(member);
	}

}
