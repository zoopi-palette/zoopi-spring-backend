package com.zoopi.domain.member.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.zoopi.domain.member.entity.oauth2.SnsAccount;
import com.zoopi.domain.member.entity.oauth2.SnsAccountPrimaryKey;
import com.zoopi.domain.member.entity.oauth2.SnsProvider;
import com.zoopi.domain.member.repository.oauth2.SnsAccountRepository;
import com.zoopi.domain.member.util.MemberUtils;
import com.zoopi.client.member.service.SnsAccountService;

@ExtendWith(MockitoExtension.class)
class SnsAccountServiceTest {

	@Mock
	private SnsAccountRepository snsAccountRepository;

	@InjectMocks
	private SnsAccountService snsAccountService;

	@Test
	void getWithMember_ExistentPrimaryKey_Success() throws Exception {
		// given
		final SnsAccount snsAccount = MemberUtils.newSnsAccount(SnsProvider.NAVER);

		given(snsAccountRepository.findWithMemberById(any(SnsAccountPrimaryKey.class)))
			.willReturn(Optional.of(snsAccount));

		// when
		final Optional<SnsAccount> snsAccountOptional = snsAccountService.getWithMember(snsAccount.getId());

		// then
		assertThat(snsAccountOptional.isPresent()).isTrue();
		assertThat(snsAccountOptional.get()).isEqualTo(snsAccount);
		assertThat(snsAccountOptional.get().getMember()).isNotNull();
	}

	@Test
	void getWithMember_NonExistentPrimaryKey_OptionalEmpty() throws Exception {
		// given
		final SnsAccount snsAccount = MemberUtils.newSnsAccount(SnsProvider.NAVER);

		given(snsAccountRepository.findWithMemberById(any(SnsAccountPrimaryKey.class)))
			.willReturn(Optional.empty());

		// when
		final Optional<SnsAccount> snsAccountOptional = snsAccountService.getWithMember(snsAccount.getId());

		// then
		assertThat(snsAccountOptional.isPresent()).isFalse();
	}

	@Test
	void connect_ValidArguments_Success() throws Exception {
		// given
		final SnsAccount snsAccount = MemberUtils.newSnsAccount(SnsProvider.NAVER);

		given(snsAccountRepository.save(any(SnsAccount.class))).willReturn(snsAccount);

		// when
		final SnsAccount connectedAccount = snsAccountService.connect(snsAccount.getMember(), snsAccount.getId());

		// then
		assertThat(snsAccount).isEqualTo(connectedAccount);
	}

}