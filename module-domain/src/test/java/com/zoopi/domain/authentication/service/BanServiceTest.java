package com.zoopi.domain.authentication.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.zoopi.domain.authentication.entity.Ban;
import com.zoopi.domain.authentication.repository.BanRepository;

@ExtendWith(MockitoExtension.class)
class BanServiceTest {

	@Mock
	private BanRepository banRepository;

	@InjectMocks
	private BanService banService;

	@Test
	void isBannedPhone_NotBannedPhone_False() throws Exception {
	    // given
		final String phone = "01012341234";

		given(banRepository.findByPhone(any(String.class))).willReturn(Optional.empty());

	    // when
		final boolean isBanned = banService.isBannedPhone(phone);

		// then
		assertThat(isBanned).isFalse();
	}

	@Test
	void isBannedPhone_BannedPhone_True() throws Exception {
	    // given
		final String phone = "01012341234";
		final Ban ban = new Ban(phone, LocalDateTime.now());

		given(banRepository.findByPhone(any(String.class))).willReturn(Optional.of(ban));

	    // when
		final boolean isBanned = banService.isBannedPhone(phone);

		// then
		assertThat(isBanned).isTrue();
	}

	@Test
	void isBannedPhone_JustFreedPhone_False() throws Exception {
	    // given
		final String phone = "01012341234";
		final Ban ban = new Ban(phone, LocalDateTime.now().minusDays(2L));

		given(banRepository.findByPhone(any(String.class))).willReturn(Optional.of(ban));

	    // when
		final boolean isBanned = banService.isBannedPhone(phone);

		// then
		assertThat(isBanned).isFalse();
	}

	@Test
	void banPhone_Phone_Ban() throws Exception {
	    // given
		final String phone = "01012341234";

		given(banRepository.save(any(Ban.class))).willReturn(new Ban(phone, LocalDateTime.now()));

	    // when
		final Ban ban = banService.banPhone(phone);

		// then
		assertThat(ban.getPhone()).isEqualTo(phone);
	}

}