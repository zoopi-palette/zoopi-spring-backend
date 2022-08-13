package com.zoopi.domain.member.service;

import static com.zoopi.ErrorCode.*;
import static com.zoopi.util.Constants.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.zoopi.domain.member.entity.Member;
import com.zoopi.domain.member.repository.MemberRepository;
import com.zoopi.domain.member.util.MemberUtils;
import com.zoopi.exception.EntityNotFoundException;
import com.zoopi.client.model.member.SigninResponse;
import com.zoopi.client.service.member.MemberService;
import com.zoopi.util.JwtUtils;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

	@Mock
	private MemberRepository memberRepository;

	@Mock
	private JwtUtils jwtUtils;

	@Mock
	private PasswordEncoder passwordEncoder;

	@InjectMocks
	private MemberService memberService;

	@Test
	void isAvailableUsername_UniqueUsername_True() throws Exception {
		// given
		final String username = MemberUtils.newMember().getUsername();

		given(memberRepository.findByUsername(username)).willReturn(Optional.empty());

		// when
		final boolean isValidated = memberService.isAvailableUsername(username);

		// then
		assertThat(isValidated).isTrue();
	}

	@Test
	void isAvailableUsername_DuplicateUsername_False() throws Exception {
		// given
		final String username = MemberUtils.newMember().getUsername();

		given(memberRepository.findByUsername(username)).willReturn(Optional.of(mock(Member.class)));

		// when
		final boolean isValidated = memberService.isAvailableUsername(username);

		// then
		assertThat(isValidated).isFalse();
	}

	@Test
	void isAvailablePhone_UniquePhone_True() throws Exception {
		// given
		final String phone = MemberUtils.newMember().getPhone();

		given(memberRepository.findByPhone(phone)).willReturn(Optional.empty());

		// when
		final boolean isValidated = memberService.isAvailablePhone(phone);

		// then
		assertThat(isValidated).isTrue();
	}

	@Test
	void isAvailablePhone_DuplicatePhone_False() throws Exception {
		// given
		final String phone = MemberUtils.newMember().getPhone();

		given(memberRepository.findByPhone(phone)).willReturn(Optional.of(mock(Member.class)));

		// when
		final boolean isValidated = memberService.isAvailablePhone(phone);

		// then
		assertThat(isValidated).isFalse();
	}

	@Test
	void createMember_ValidArguments_SuccessToSave() throws Exception {
		// given
		final Member member = MemberUtils.newMember();

		given(memberRepository.save(any(Member.class))).willReturn(member);

		// when
		final Member savedMember = memberService.createMember(member.getUsername(), member.getPhone(), member.getName(),
			member.getPassword(), member.getEmail());

		// then
		assertThat(member.getPhone()).isEqualTo(savedMember.getPhone());
	}

	@Test
	void signin_ValidUsernameAndMatchedPassword_Success() throws Exception {
		// given
		final Member member = MemberUtils.newMember();
		final String accessToken = "accessToken";
		final String refreshToken = "refreshToken";

		given(memberRepository.findByUsername(any(String.class))).willReturn(Optional.of(member));
		given(passwordEncoder.matches(any(String.class), any(String.class))).willReturn(true);
		given(jwtUtils.generateAccessToken(member)).willReturn(accessToken);
		given(jwtUtils.generateRefreshToken(member)).willReturn(refreshToken);

		// when
		final SigninResponse response = memberService.signin(member.getUsername(), member.getPassword());

		// then
		assertThat(response.getResult()).isEqualTo(SigninResponse.SigninResult.SUCCESS);
		assertThat(response.getJwt().getAccessToken()).isEqualTo(accessToken);
		assertThat(response.getJwt().getRefreshToken()).isEqualTo(refreshToken);
	}

	@Test
	void signin_MismatchedPassword_Fail() throws Exception {
		// given
		final Member member = MemberUtils.newMember();
		final String wrongPassword = "qlalfqjsgh2@";

		given(memberRepository.findByUsername(any(String.class))).willReturn(Optional.of(member));
		given(passwordEncoder.matches(wrongPassword, member.getPassword())).willReturn(false);

		// when
		final SigninResponse response = memberService.signin(member.getUsername(), wrongPassword);

		// then
		assertThat(response.getResult()).isEqualTo(SigninResponse.SigninResult.MISMATCHED_PASSWORD);
		assertThat(response.getJwt().getAccessToken()).isEqualTo(EMPTY);
		assertThat(response.getJwt().getRefreshToken()).isEqualTo(EMPTY);
	}

	@Test
	void signin_InvalidUsername_Fail() throws Exception {
		// given
		final String wrongUsername = "nop";
		final String wrongPassword = "qlalfqjsgh2@";

		given(memberRepository.findByUsername(any(String.class))).willReturn(Optional.empty());

		// when
		final SigninResponse response = memberService.signin(wrongUsername, wrongPassword);

		// then
		assertThat(response.getResult()).isEqualTo(SigninResponse.SigninResult.NONEXISTENT_USERNAME);
		assertThat(response.getJwt().getAccessToken()).isEqualTo(EMPTY);
		assertThat(response.getJwt().getRefreshToken()).isEqualTo(EMPTY);
	}

	@Test
	void getUsernameByPhone_ExistentPhone_Username() throws Exception {
	    // given
		final Member member = MemberUtils.newMember();

		given(memberRepository.findByPhone(any(String.class))).willReturn(Optional.of(member));

		// when
		final String username = memberService.getUsernameByPhone(member.getPhone());

		// then
		assertThat(username).isEqualTo(member.getUsername());
	}

	@Test
	void getUsernameByPhone_NonExistentPhone_ExceptionThrown() throws Exception {
	    // given
		final Member member = MemberUtils.newMember();

		given(memberRepository.findByPhone(any(String.class))).willThrow(new EntityNotFoundException(MEMBER_NOT_FOUND));

		// when
		final Executable executable = () -> memberService.getUsernameByPhone(member.getPhone());

		// then
		assertThrows(EntityNotFoundException.class, executable);
	}

	@Test
	void changePassword_ExistentUsername_Success() throws Exception {
	    // given
		final Member member = MemberUtils.newMember();
		final String encodedPassword = "encodedPassword";

		given(memberRepository.findByUsername(any(String.class))).willReturn(Optional.of(member));
		given(passwordEncoder.encode(any(String.class))).willReturn(encodedPassword);

	    // when
		final boolean result = memberService.changePassword(member.getUsername(), member.getPassword());

		// then
		assertThat(result).isTrue();
		assertThat(member.getPassword()).isEqualTo(encodedPassword);
	}

	@Test
	void changePassword_NonExistentUsername_ExceptionThrown() throws Exception {
		// given
		final Member member = MemberUtils.newMember();

		given(memberRepository.findByUsername(any(String.class))).willThrow(new EntityNotFoundException(MEMBER_NOT_FOUND));

		// when
		final Executable executable = () -> memberService.changePassword(member.getUsername(), member.getPassword());

		// then
		assertThrows(EntityNotFoundException.class, executable);
	}

}