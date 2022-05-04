package com.zoopi.domain.authentication.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.zoopi.domain.authentication.dto.response.AuthenticationResponse;
import com.zoopi.domain.authentication.dto.response.AuthenticationResult;
import com.zoopi.domain.authentication.entity.Authentication;
import com.zoopi.domain.authentication.repository.AuthenticationRepository;
import com.zoopi.infra.sms.SmsClient;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

	@Mock
	private SmsClient smsClient;

	@Mock
	private AuthenticationRepository authenticationRepository;

	@InjectMocks
	private AuthenticationService authenticationService;

	@Test
	void sendAuthenticationCode_phone_sendSmsSuccess() throws Exception {
		// given
		final String phone = "01012345678";
		doReturn(true).when(smsClient).sendSms(any(String.class), any(String.class));
		doReturn(Optional.empty()).when(authenticationRepository).findById(any(String.class));
		doReturn(mock(Authentication.class)).when(authenticationRepository).save(any(Authentication.class));

		// when
		final boolean isPresent = authenticationService.sendAuthenticationCode(phone).isPresent();

		// then
		assertThat(isPresent).isTrue();
	}

	@Test
	void sendAuthenticationCode_phone_sendSmsFail() throws Exception {
		// given
		final String phone = "01012345678";
		doReturn(false).when(smsClient).sendSms(any(String.class), any(String.class));

		// when
		final boolean isPresent = authenticationService.sendAuthenticationCode(phone).isPresent();

		// then
		assertThat(isPresent).isFalse();
	}

	@Test
	void checkAuthenticationCode_nonexistentKey_expired() throws Exception {
		// given
		final String authenticationKey = "2a6ec3d7-7147-4cdc-a6c5-fc5d937cfe76";
		final String authenticationCode = "123456";
		final String phone = "01012345678";
		doReturn(Optional.empty()).when(authenticationRepository).findById(any(String.class));

		// when
		final AuthenticationResult result = authenticationService.checkAuthenticationCode(
			authenticationCode, phone, authenticationKey);

		// then
		assertThat(result).isEqualTo(AuthenticationResult.EXPIRED);
	}

	@Test
	void checkAuthenticationCode_expiredCode_expired() throws Exception {
		// given
		final String authenticationKey = "2a6ec3d7-7147-4cdc-a6c5-fc5d937cfe76";
		final String authenticationCode = "123456";
		final String phone = "01012345678";
		final Authentication authentication = new Authentication(authenticationKey, authenticationCode, phone,
			LocalDateTime.now().minusMinutes(1));
		doReturn(Optional.of(authentication)).when(authenticationRepository).findById(any(String.class));

		// when
		final AuthenticationResult result = authenticationService.checkAuthenticationCode(
			authenticationCode, phone, authenticationKey);

		// then
		assertThat(result).isEqualTo(AuthenticationResult.EXPIRED);
	}

	@Test
	void checkAuthenticationCode_matchedCodeAndMyPhone_success() throws Exception {
		// given
		final String authenticationKey = "2a6ec3d7-7147-4cdc-a6c5-fc5d937cfe76";
		final String authenticationCode = "123456";
		final String phone = "01012345678";
		final Authentication authentication = new Authentication(authenticationKey, authenticationCode, phone,
			LocalDateTime.now().plusMinutes(5));
		doReturn(Optional.of(authentication)).when(authenticationRepository).findById(any(String.class));

		// when
		final AuthenticationResult result = authenticationService.checkAuthenticationCode(
			authenticationCode, phone, authenticationKey);

		// then
		assertThat(result).isEqualTo(AuthenticationResult.SUCCESS);
	}

	@Test
	void checkAuthenticationCode_mismatchedCode_mismatched() throws Exception {
		// given
		final String authenticationKey = "2a6ec3d7-7147-4cdc-a6c5-fc5d937cfe76";
		final String authenticationCode = "123456";
		final String wrongAuthenticationCode = "634262";
		final String phone = "01012345678";
		final Authentication authentication = new Authentication(authenticationKey, authenticationCode, phone,
			LocalDateTime.now().plusMinutes(5));
		doReturn(Optional.of(authentication)).when(authenticationRepository).findById(any(String.class));

		// when
		final AuthenticationResult result = authenticationService.checkAuthenticationCode(
			wrongAuthenticationCode, phone, authenticationKey);

		// then
		assertThat(result).isEqualTo(AuthenticationResult.MISMATCHED);
	}

	@Test
	void checkAuthenticationCode_matchedCodeAndNotMyPhone_mismatched() throws Exception {
		// given
		final String authenticationKey = "2a6ec3d7-7147-4cdc-a6c5-fc5d937cfe76";
		final String authenticationCode = "123456";
		final String phone = "01012345678";
		final String otherPhone = "01012341234";
		final Authentication authentication = new Authentication(authenticationKey, authenticationCode, otherPhone,
			LocalDateTime.now().plusMinutes(5));
		doReturn(Optional.of(authentication)).when(authenticationRepository).findById(any(String.class));

		// when
		final AuthenticationResult result = authenticationService.checkAuthenticationCode(
			authenticationCode, phone, authenticationKey);

		// then
		assertThat(result).isEqualTo(AuthenticationResult.MISMATCHED);
	}

	@Test
	void deleteExpiredAuthenticationCodes_success() throws Exception {
		// given
		doReturn(List.of(mock(Authentication.class), mock(Authentication.class)))
			.when(authenticationRepository).findAllByExpiredDateBefore(any(LocalDateTime.class));
		doNothing().when(authenticationRepository).deleteAllInBatch(any(List.class));

		// when
		authenticationService.deleteExpiredAuthenticationCodes();

		// then
	}
}