package com.zoopi.domain.member.service;

import static com.zoopi.domain.member.util.MemberUtils.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.zoopi.domain.member.entity.AuthorityType;
import com.zoopi.domain.member.entity.JoinType;
import com.zoopi.domain.member.entity.Member;
import com.zoopi.domain.member.entity.MemberAuthority;
import com.zoopi.domain.member.repository.MemberAuthorityRepository;
import com.zoopi.domain.member.repository.MemberRepository;
import com.zoopi.exception.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
class MemberAuthorityServiceTest {

	@Mock
	private MemberRepository memberRepository;

	@Mock
	private MemberAuthorityRepository memberAuthorityRepository;

	@InjectMocks
	private MemberAuthorityService memberAuthorityService;

	@Test
	void getMemberAuthorities_ValidEmail_Success() throws Exception {
		// given
		final Member member = getMember(JoinType.EMAIL);
		final List<MemberAuthority> authorities = getAuthorities(member, AuthorityType.ROLE_USER);

		doReturn(Optional.of(member)).when(memberRepository).findByUsername(member.getUsername());
		doReturn(authorities).when(memberAuthorityRepository).findAllByMember(member);

		// when
		final List<MemberAuthority> memberAuthorities = memberAuthorityService.getMemberAuthorities(
			member.getUsername());

		// then
		assertThat(memberAuthorities).isEqualTo(authorities);
	}

	@Test
	void getMemberAuthorities_InvalidEmail_ThrownException() throws Exception {
		// given
		final String username = "zoopi@gmail.com";

		doReturn(Optional.empty()).when(memberRepository).findByUsername(username);

		// when
		final Executable executable = () -> memberAuthorityService.getMemberAuthorities(username);

		// then
		assertThrows(EntityNotFoundException.class, executable);
	}

	@Test
	void getMemberAuthorities_ValidMember_Success() throws Exception {
		// given
		final Member member = getMember(JoinType.EMAIL);
		final List<MemberAuthority> authorities = getAuthorities(member, AuthorityType.ROLE_USER);

		doReturn(authorities).when(memberAuthorityRepository).findAllByMember(member);

		// when
		final List<MemberAuthority> memberAuthorities = memberAuthorityService.getMemberAuthorities(member);

		// then
		assertThat(memberAuthorities).isEqualTo(authorities);
	}

}