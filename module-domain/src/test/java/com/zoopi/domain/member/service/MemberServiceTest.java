package com.zoopi.domain.member.service;

import static com.zoopi.domain.member.util.MemberUtils.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.zoopi.domain.member.dto.SigninResponse;
import com.zoopi.domain.member.entity.AuthorityType;
import com.zoopi.domain.member.entity.JoinType;
import com.zoopi.domain.member.entity.Member;
import com.zoopi.domain.member.entity.MemberAuthority;
import com.zoopi.domain.member.repository.MemberAuthorityRepository;
import com.zoopi.domain.member.repository.MemberRepository;
import com.zoopi.util.JwtUtils;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

	@Mock
	private MemberRepository memberRepository;

	@Mock
	private MemberAuthorityRepository memberAuthorityRepository;

	@Mock
	private JwtUtils jwtUtils;

	@Mock
	private PasswordEncoder passwordEncoder;

	@InjectMocks
	private MemberService memberService;

	@Test
	void validateEmail_UniqueEmail_True() throws Exception {
		// given
		final String username = getMember(JoinType.EMAIL).getUsername();

		given(memberRepository.findByUsername(username)).willReturn(Optional.empty());

		// when
		final boolean isValidated = memberService.validateEmail(username);

		// then
		assertThat(isValidated).isTrue();
	}

	@Test
	void validateEmail_DuplicateEmail_False() throws Exception {
		// given
		final String username = getMember(JoinType.EMAIL).getUsername();

		given(memberRepository.findByUsername(username)).willReturn(Optional.of(mock(Member.class)));

		// when
		final boolean isValidated = memberService.validateEmail(username);

		// then
		assertThat(isValidated).isFalse();
	}

	@Test
	void validatePhone_UniquePhone_True() throws Exception {
		// given
		final String phone = getMember(JoinType.EMAIL).getPhone();

		given(memberRepository.findByPhone(phone)).willReturn(Optional.empty());

		// when
		final boolean isValidated = memberService.validatePhone(phone);

		// then
		assertThat(isValidated).isTrue();
	}

	@Test
	void validatePhone_DuplicatePhone_False() throws Exception {
		// given
		final String phone = getMember(JoinType.EMAIL).getPhone();

		given(memberRepository.findByPhone(phone)).willReturn(Optional.of(mock(Member.class)));

		// when
		final boolean isValidated = memberService.validatePhone(phone);

		// then
		assertThat(isValidated).isFalse();
	}

	@Test
	void createMember_ValidArguments_SuccessToSave() throws Exception {
		// given
		final Member member = getMember(JoinType.EMAIL);

		given(memberRepository.save(any(Member.class))).willReturn(member);

		// when
		final Member savedMember = memberService.createMember(member.getUsername(), member.getPhone(), member.getName(),
			member.getPassword(), JoinType.EMAIL);

		// then
		assertThat(member.getPhone()).isEqualTo(savedMember.getPhone());
	}

	@Test
	void signin_ValidEmailAndMatchedPassword_Success() throws Exception {
		// given
		final Member member = getMember(JoinType.EMAIL);
		final List<MemberAuthority> authorities = getAuthorities(member, AuthorityType.ROLE_USER);
		final String accessToken = "accessToken";
		final String refreshToken = "refreshToken";

		given(memberRepository.findByUsername(any(String.class))).willReturn(Optional.of(member));
		given(passwordEncoder.matches(any(String.class), any(String.class))).willReturn(true);
		given(memberAuthorityRepository.findAllByMember(any(Member.class))).willReturn(authorities);
		given(jwtUtils.generateJwt(member, authorities, JwtUtils.JwtType.ACCESS_TOKEN)).willReturn(accessToken);
		given(jwtUtils.generateJwt(member, authorities, JwtUtils.JwtType.REFRESH_TOKEN)).willReturn(refreshToken);

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
		final Member member = getMember(JoinType.EMAIL);
		final String wrongPassword = "qlalfqjsgh2@";

		given(memberRepository.findByUsername(any(String.class))).willReturn(Optional.of(member));
		given(passwordEncoder.matches(wrongPassword, member.getPassword())).willReturn(false);

		// when
		final SigninResponse response = memberService.signin(member.getUsername(), wrongPassword);

		// then
		assertThat(response.getResult()).isEqualTo(SigninResponse.SigninResult.MISMATCHED_PASSWORD);
		assertThat(response.getJwt().getAccessToken()).isEqualTo("");
		assertThat(response.getJwt().getRefreshToken()).isEqualTo("");
	}

	@Test
	void signin_InvalidEmail_Fail() throws Exception {
		// given
		final String wrongUsername = "nop@gmail.com";
		final String wrongPassword = "qlalfqjsgh2@";

		given(memberRepository.findByUsername(any(String.class))).willReturn(Optional.empty());

		// when
		final SigninResponse response = memberService.signin(wrongUsername, wrongPassword);

		// then
		assertThat(response.getResult()).isEqualTo(SigninResponse.SigninResult.NONEXISTENT_USERNAME);
		assertThat(response.getJwt().getAccessToken()).isEqualTo("");
		assertThat(response.getJwt().getRefreshToken()).isEqualTo("");
	}

}