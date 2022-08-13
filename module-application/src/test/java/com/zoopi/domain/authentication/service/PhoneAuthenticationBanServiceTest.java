package com.zoopi.domain.authentication.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.zoopi.domain.phoneauthentication.entity.PhoneAuthenticationBan;
import com.zoopi.domain.phoneauthentication.entity.PhoneAuthenticationType;
import com.zoopi.domain.phoneauthentication.repository.PhoneAuthenticationBanRepository;
import com.zoopi.client.service.phoneauthentication.PhoneAuthenticationBanService;

@ExtendWith(MockitoExtension.class)
class PhoneAuthenticationBanServiceTest {

	@Mock
	private PhoneAuthenticationBanRepository phoneAuthenticationBanRepository;

	@InjectMocks
	private PhoneAuthenticationBanService phoneAuthenticationBanService;

	@Test
	void isBannedPhone_NotBannedPhone_False() throws Exception {
		// given
		final String phone = "01012341234";
		final PhoneAuthenticationType type = PhoneAuthenticationType.FIND_ID;

		given(phoneAuthenticationBanRepository.findByPhoneAndType(any(String.class),
			any(PhoneAuthenticationType.class))).willReturn(Optional.empty());

		// when
		final boolean isBanned = phoneAuthenticationBanService.isBanned(phone, type);

		// then
		assertThat(isBanned).isFalse();
	}

	@Test
	void isBannedPhone_BannedPhone_True() throws Exception {
		// given
		final String phone = "01012341234";
		final PhoneAuthenticationType type = PhoneAuthenticationType.FIND_ID;
		final PhoneAuthenticationBan phoneAuthenticationBan = new PhoneAuthenticationBan(phone, type);
		ReflectionTestUtils.setField(phoneAuthenticationBan, "createdAt", LocalDateTime.now());

		given(phoneAuthenticationBanRepository.findByPhoneAndType(any(String.class),
			any(PhoneAuthenticationType.class))).willReturn(Optional.of(phoneAuthenticationBan));

		// when
		final boolean isBanned = phoneAuthenticationBanService.isBanned(phone, type);

		// then
		assertThat(isBanned).isTrue();
	}

	@Test
	void isBannedPhone_BannedPhoneButDifferentType_False() throws Exception {
		// given
		final String phone = "01012341234";
		final PhoneAuthenticationType type = PhoneAuthenticationType.FIND_ID;
		final PhoneAuthenticationBan phoneAuthenticationBan = new PhoneAuthenticationBan(phone, type);
		ReflectionTestUtils.setField(phoneAuthenticationBan, "createdAt", LocalDateTime.now());

		given(phoneAuthenticationBanRepository.findByPhoneAndType(phone, PhoneAuthenticationType.FIND_PW))
			.willReturn(Optional.empty());

		// when
		final boolean isBanned = phoneAuthenticationBanService.isBanned(phone, PhoneAuthenticationType.FIND_PW);

		// then
		assertThat(isBanned).isFalse();
	}

	@Test
	void isBannedPhone_JustFreedPhone_False() throws Exception {
		// given
		final String phone = "01012341234";
		final PhoneAuthenticationType type = PhoneAuthenticationType.FIND_ID;
		final PhoneAuthenticationBan phoneAuthenticationBan = new PhoneAuthenticationBan(phone, type);
		final LocalDateTime bannedAt = LocalDateTime.now().minusDays(1L);
		ReflectionTestUtils.setField(phoneAuthenticationBan, "createdAt", bannedAt);

		given(phoneAuthenticationBanRepository.findByPhoneAndType(any(String.class),
			any(PhoneAuthenticationType.class))).willReturn(Optional.of(phoneAuthenticationBan));

		// when
		final boolean isBanned = phoneAuthenticationBanService.isBanned(phone, type);

		// then
		assertThat(isBanned).isFalse();
	}

	@Test
	void banPhone_ValidArguments_Success() throws Exception {
		// given
		final String phone = "01012341234";
		final PhoneAuthenticationType type = PhoneAuthenticationType.FIND_ID;

		given(phoneAuthenticationBanRepository.save(any(PhoneAuthenticationBan.class))).willReturn(
			new PhoneAuthenticationBan(phone, type));

		// when
		final PhoneAuthenticationBan phoneAuthenticationBan = phoneAuthenticationBanService.ban(phone, type);

		// then
		assertThat(phoneAuthenticationBan.getPhone()).isEqualTo(phone);
	}

}