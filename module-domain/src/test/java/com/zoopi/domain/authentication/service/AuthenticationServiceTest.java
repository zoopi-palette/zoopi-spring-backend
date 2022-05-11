package com.zoopi.domain.authentication.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.zoopi.domain.authentication.dto.response.AuthenticationResponse;
import com.zoopi.domain.authentication.dto.response.AuthenticationResult;
import com.zoopi.domain.authentication.entity.Authentication;
import com.zoopi.domain.authentication.entity.AuthenticationStatus;
import com.zoopi.domain.authentication.exception.PasswordMismatchException;
import com.zoopi.domain.authentication.repository.AuthenticationRepository;
import com.zoopi.infra.sms.SmsClient;
import com.zoopi.util.AuthenticationCodeUtils;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

	@Mock
	private SmsClient smsClient;

	@Mock
	private AuthenticationRepository authenticationRepository;

	@InjectMocks
	private AuthenticationService authenticationService;

	@Test
	void sendAuthenticationCode_Phone_SendSmsSuccess() throws Exception {
		// given
		final String phone = "01012345678";
		final String authenticationCode = AuthenticationCodeUtils.generateRandomAuthenticationCode(6);
		doReturn(true).when(smsClient).sendSms(any(String.class), any(String.class));

		// when
		final boolean result = authenticationService.sendAuthenticationCode(phone, authenticationCode);

		// then
		assertThat(result).isTrue();
	}

	@Test
	void createAuthentication_CodeAndPhone_Success() throws Exception {
		// given
		final String phone = "01012345678";
		final String authenticationCode = AuthenticationCodeUtils.generateRandomAuthenticationCode(6);
		final Authentication authentication = new Authentication(UUID.randomUUID().toString(), authenticationCode,
			phone);
		ReflectionTestUtils.setField(authentication, "createdDate", LocalDateTime.now());
		doReturn(Optional.empty()).when(authenticationRepository).findById(any(String.class));
		doReturn(authentication).when(authenticationRepository).save(any(Authentication.class));

		// when
		final AuthenticationResponse authenticationResponse = authenticationService.createAuthentication(phone,
			authenticationCode);

		// then
		assertThat(authenticationResponse).isNotNull();
	}

	@Test
	void getCountOfAuthentication_3AuthenticationsWithin5Minutes_3() throws Exception {
		// given
		final String phone = "01012345678";
		doReturn(3).when(authenticationRepository)
			.countByPhoneAndCreatedDateAfter(any(String.class), any(LocalDateTime.class));

		// when
		final int count = authenticationService.getCountOfAuthentication(phone);

		// then
		assertThat(count).isEqualTo(3);
	}

	@Test
	void checkAuthenticationCode_NonexistentKey_Expired() throws Exception {
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
	void checkAuthenticationCode_ExpiredCode_Expired() throws Exception {
		// given
		final String authenticationKey = "2a6ec3d7-7147-4cdc-a6c5-fc5d937cfe76";
		final String authenticationCode = "123456";
		final String phone = "01012345678";
		final Authentication authentication = new Authentication(authenticationKey, authenticationCode, phone);
		ReflectionTestUtils.setField(authentication, "createdDate", LocalDateTime.now().minusMinutes(5L));
		doReturn(Optional.of(authentication)).when(authenticationRepository).findById(any(String.class));

		// when
		final AuthenticationResult result = authenticationService.checkAuthenticationCode(
			authenticationCode, phone, authenticationKey);

		// then
		assertThat(result).isEqualTo(AuthenticationResult.EXPIRED);
	}

	@Test
	void checkAuthenticationCode_MatchedCodeAndMyPhone_Success() throws Exception {
		// given
		final String authenticationKey = "2a6ec3d7-7147-4cdc-a6c5-fc5d937cfe76";
		final String authenticationCode = "123456";
		final String phone = "01012345678";
		final Authentication authentication = new Authentication(authenticationKey, authenticationCode, phone);
		ReflectionTestUtils.setField(authentication, "createdDate", LocalDateTime.now());
		doReturn(Optional.of(authentication)).when(authenticationRepository).findById(any(String.class));

		// when
		final AuthenticationResult result = authenticationService.checkAuthenticationCode(
			authenticationCode, phone, authenticationKey);

		// then
		assertThat(result).isEqualTo(AuthenticationResult.AUTHENTICATED);
	}

	@Test
	void checkAuthenticationCode_MismatchedCode_Mismatched() throws Exception {
		// given
		final String authenticationKey = "2a6ec3d7-7147-4cdc-a6c5-fc5d937cfe76";
		final String authenticationCode = "123456";
		final String wrongAuthenticationCode = "634262";
		final String phone = "01012345678";
		final Authentication authentication = new Authentication(authenticationKey, authenticationCode, phone);
		ReflectionTestUtils.setField(authentication, "createdDate", LocalDateTime.now());
		doReturn(Optional.of(authentication)).when(authenticationRepository).findById(any(String.class));

		// when
		final AuthenticationResult result = authenticationService.checkAuthenticationCode(
			wrongAuthenticationCode, phone, authenticationKey);

		// then
		assertThat(result).isEqualTo(AuthenticationResult.MISMATCHED);
	}

	@Test
	void checkAuthenticationCode_MatchedCodeAndNotMyPhone_Mismatched() throws Exception {
		// given
		final String authenticationKey = "2a6ec3d7-7147-4cdc-a6c5-fc5d937cfe76";
		final String authenticationCode = "123456";
		final String phone = "01012345678";
		final String otherPhone = "01012341234";
		final Authentication authentication = new Authentication(authenticationKey, authenticationCode, otherPhone);
		ReflectionTestUtils.setField(authentication, "createdDate", LocalDateTime.now());
		doReturn(Optional.of(authentication)).when(authenticationRepository).findById(any(String.class));

		// when
		final AuthenticationResult result = authenticationService.checkAuthenticationCode(
			authenticationCode, phone, authenticationKey);

		// then
		assertThat(result).isEqualTo(AuthenticationResult.MISMATCHED);
	}

	@Test
	void deleteExpiredAuthenticationCodes_Success() throws Exception {
		// given
		doReturn(new ArrayList<>())
			.when(authenticationRepository)
			.findAllByStatusAndCreatedDateAfter(any(AuthenticationStatus.class), any(LocalDateTime.class));

		// when
		final boolean result = authenticationService.deleteExpiredAuthenticationCodes();

		// then
		assertThat(result).isTrue();
	}

	@Test
	void validatePassword_MatchedPassword_True() throws Exception {
		// given
		final String password = "qlalfqjsgh1!";
		final String passwordCheck = "qlalfqjsgh1!";

		// when
		final boolean result = authenticationService.validatePassword(password, passwordCheck);

		// then
		assertThat(result).isTrue();
	}

	@Test
	void validatePassword_MismatchedPassword_ExceptionThrown() throws Exception {
		// given
		final String password = "qlalfqjsgh1!";
		final String passwordCheck = "qlalfqjsgh1@";

		// when
		final Executable executable = () -> authenticationService.validatePassword(password, passwordCheck);

		// then
		assertThrows(PasswordMismatchException.class, executable);
	}

	@Test
	void validateAuthenticationKey_NonExistentKey_Expired() throws Exception {
		// given
		final String phone = "01012341234";
		final String authenticationKey = UUID.randomUUID().toString();
		doReturn(Optional.empty()).when(authenticationRepository).findById(any(String.class));

		// when
		final AuthenticationResult result = authenticationService.validateAuthenticationKey(phone, authenticationKey);

		// then
		assertThat(result).isEqualTo(AuthenticationResult.EXPIRED);
	}

	@Test
	void validateAuthenticationKey_ExpiredKey_Expired() throws Exception {
		// given
		final String phone = "01012341234";
		final String authenticationKey = UUID.randomUUID().toString();
		final String authenticationCode = "123456";
		final Authentication authentication = new Authentication(authenticationKey, authenticationCode, phone);
		ReflectionTestUtils.setField(authentication, "createdDate", LocalDateTime.now().minusMinutes(60L));
		doReturn(Optional.of(authentication)).when(authenticationRepository).findById(any(String.class));

		// when
		final AuthenticationResult result = authenticationService.validateAuthenticationKey(phone, authenticationKey);

		// then
		assertThat(result).isEqualTo(AuthenticationResult.EXPIRED);
	}

	@Test
	void validateAuthenticationKey_AuthenticatedKeyAndMyPhone_Authenticated() throws Exception {
		// given
		final String phone = "01012341234";
		final String authenticationKey = UUID.randomUUID().toString();
		final String authenticationCode = "123456";
		final Authentication authentication = new Authentication(authenticationKey, authenticationCode, phone);
		authentication.authenticate();
		ReflectionTestUtils.setField(authentication, "createdDate", LocalDateTime.now().minusMinutes(10L));
		doReturn(Optional.of(authentication)).when(authenticationRepository).findById(any(String.class));

		// when
		final AuthenticationResult result = authenticationService.validateAuthenticationKey(phone, authenticationKey);

		// then
		assertThat(result).isEqualTo(AuthenticationResult.AUTHENTICATED);
	}

	@Test
	void validateAuthenticationKey_NotAuthenticatedKey_NotAuthenticated() throws Exception {
		// given
		final String phone = "01012341234";
		final String authenticationKey = UUID.randomUUID().toString();
		final String authenticationCode = "123456";
		final Authentication authentication = new Authentication(authenticationKey, authenticationCode, phone);
		ReflectionTestUtils.setField(authentication, "createdDate", LocalDateTime.now().minusMinutes(10L));
		doReturn(Optional.of(authentication)).when(authenticationRepository).findById(any(String.class));

		// when
		final AuthenticationResult result = authenticationService.validateAuthenticationKey(phone, authenticationKey);

		// then
		assertThat(result).isEqualTo(AuthenticationResult.NOT_AUTHENTICATED);
	}

	@Test
	void validateAuthenticationKey_NotMyPhone_NotAuthenticated() throws Exception {
		// given
		final String phone = "01012341234";
		final String otherPhone = "01034341212";
		final String authenticationKey = UUID.randomUUID().toString();
		final String authenticationCode = "123456";
		final Authentication authentication = new Authentication(authenticationKey, authenticationCode, otherPhone);
		authentication.authenticate();
		ReflectionTestUtils.setField(authentication, "createdDate", LocalDateTime.now().minusMinutes(10L));
		doReturn(Optional.of(authentication)).when(authenticationRepository).findById(any(String.class));

		// when
		final AuthenticationResult result = authenticationService.validateAuthenticationKey(phone, authenticationKey);

		// then
		assertThat(result).isEqualTo(AuthenticationResult.NOT_AUTHENTICATED);
	}

}