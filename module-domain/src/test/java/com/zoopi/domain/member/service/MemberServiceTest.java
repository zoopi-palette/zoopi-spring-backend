package com.zoopi.domain.member.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.zoopi.domain.member.entity.Member;
import com.zoopi.domain.member.repository.MemberRepository;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

	@Mock
	private MemberRepository memberRepository;

	@InjectMocks
	private MemberService memberService;

	@Test
	void validateEmail_UniqueEmail_True() throws Exception {
	    // given
		final String email = "unique@zoopi.com";
		doReturn(Optional.empty()).when(memberRepository).findByEmail(email);

	    // when
		final boolean isValidated = memberService.validateEmail(email);

		// then
		assertThat(isValidated).isTrue();
	}

	@Test
	void validateEmail_DuplicateEmail_False() throws Exception {
	    // given
		final String email = "duplicate@zoopi.com";
		doReturn(Optional.of(mock(Member.class))).when(memberRepository).findByEmail(email);

	    // when
		final boolean isValidated = memberService.validateEmail(email);

		// then
		assertThat(isValidated).isFalse();
	}

	@Test
	void validatePhone_UniquePhone_True() throws Exception {
	    // given
		final String phone = "01012345678";
		doReturn(Optional.empty()).when(memberRepository).findByPhone(phone);

	    // when
		final boolean isValidated = memberService.validatePhone(phone);

		// then
		assertThat(isValidated).isTrue();
	}

	@Test
	void validatePhone_DuplicatePhone_False() throws Exception {
	    // given
		final String phone = "01012345678";
		doReturn(Optional.of(mock(Member.class))).when(memberRepository).findByPhone(phone);

	    // when
		final boolean isValidated = memberService.validatePhone(phone);

		// then
		assertThat(isValidated).isFalse();
	}
}