package com.zoopi.domain.authentication.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

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

import com.zoopi.domain.phoneauthentication.entity.PhoneAuthentication;
import com.zoopi.domain.phoneauthentication.entity.PhoneAuthenticationResult;
import com.zoopi.domain.phoneauthentication.entity.PhoneAuthenticationStatus;
import com.zoopi.domain.phoneauthentication.entity.PhoneAuthenticationType;
import com.zoopi.domain.phoneauthentication.repository.PhoneAuthenticationRepository;
import com.zoopi.infra.sms.SmsClient;
import com.zoopi.client.model.pheonauthentication.PhoneAuthenticationResponse;
import com.zoopi.client.service.phoneauthentication.PhoneAuthenticationService;
import com.zoopi.client.service.phoneauthentication.exception.PasswordMismatchException;
import com.zoopi.util.AuthenticationCodeUtils;

@ExtendWith(MockitoExtension.class)
class PhoneAuthenticationServiceTest {

	@Mock
	private SmsClient smsClient;

	@Mock
	private PhoneAuthenticationRepository phoneAuthenticationRepository;

	@InjectMocks
	private PhoneAuthenticationService phoneAuthenticationService;

	@Test
	void sendAuthenticationCode_ValidArguments_SendSms() throws Exception {
		// given
		final String phone = "01012345678";
		final String authenticationCode = AuthenticationCodeUtils.generateRandomAuthenticationCode(6);

		given(smsClient.sendSms(any(String.class), any(String.class))).willReturn(true);

		// when
		final boolean result = phoneAuthenticationService.sendAuthenticationCode(phone, authenticationCode);

		// then
		assertThat(result).isTrue();
	}

	@Test
	void createAuthentication_ValidArguments_Success() throws Exception {
		// given
		final String phone = "01012345678";
		final String authenticationCode = AuthenticationCodeUtils.generateRandomAuthenticationCode(6);
		final PhoneAuthenticationType type = PhoneAuthenticationType.FIND_ID;
		
		final PhoneAuthentication phoneAuthentication = PhoneAuthentication.of(authenticationCode, phone, type);
		ReflectionTestUtils.setField(phoneAuthentication, "createdAt", LocalDateTime.now());

		given(phoneAuthenticationRepository.save(any(PhoneAuthentication.class))).willReturn(phoneAuthentication);

		// when
		final PhoneAuthenticationResponse phoneAuthenticationResponse = phoneAuthenticationService.createAuthentication(
			phone, authenticationCode, type);

		// then
		assertThat(phoneAuthenticationResponse).isNotNull();
	}

	@Test
	void getCountOfAuthentication_3AuthenticationsWithin5Minutes_3() throws Exception {
		// given
		final String phone = "01012345678";
		final PhoneAuthenticationType type = PhoneAuthenticationType.FIND_ID;

		given(phoneAuthenticationRepository.countByPhoneAndTypeAndCreatedAtAfter(any(String.class),
			any(PhoneAuthenticationType.class), any(LocalDateTime.class))).willReturn(3);

		// when
		final int count = phoneAuthenticationService.getCountOfAuthentication(phone, type);

		// then
		assertThat(count).isEqualTo(3);
	}

	@Test
	void checkAuthenticationCode_NonexistentKey_Expired() throws Exception {
		// given
		final String authenticationKey = "2a6ec3d7-7147-4cdc-a6c5-fc5d937cfe76";
		final String authenticationCode = "123456";
		final String phone = "01012345678";
		final PhoneAuthenticationType type = PhoneAuthenticationType.FIND_ID;

		given(phoneAuthenticationRepository.findByIdAndTypeAndPhoneAndStatusNot(authenticationKey, type, phone,
			PhoneAuthenticationStatus.EXPIRED)).willReturn(Optional.empty());

		// when
		final PhoneAuthenticationResult result = phoneAuthenticationService.checkAuthenticationCode(
			authenticationKey, authenticationCode, phone, type);

		// then
		assertThat(result).isEqualTo(PhoneAuthenticationResult.EXPIRED);
	}

	@Test
	void checkAuthenticationCode_ExpiredCode_Expired() throws Exception {
		// given
		final String authenticationKey = "2a6ec3d7-7147-4cdc-a6c5-fc5d937cfe76";
		final String authenticationCode = "123456";
		final String phone = "01012345678";
		final PhoneAuthenticationType type = PhoneAuthenticationType.FIND_ID;
		
		final PhoneAuthentication phoneAuthentication = PhoneAuthentication.of(authenticationCode, phone, type);
		final LocalDateTime expiredAt = LocalDateTime.now().minusMinutes(5L);
		ReflectionTestUtils.setField(phoneAuthentication, "createdAt", expiredAt);

		given(phoneAuthenticationRepository.findByIdAndTypeAndPhoneAndStatusNot(authenticationKey, type, phone,
			PhoneAuthenticationStatus.EXPIRED)).willReturn(Optional.of(phoneAuthentication));

		// when
		final PhoneAuthenticationResult result = phoneAuthenticationService.checkAuthenticationCode(
			authenticationKey, authenticationCode, phone, type);

		// then
		assertThat(result).isEqualTo(PhoneAuthenticationResult.EXPIRED);
	}

	@Test
	void checkAuthenticationCode_ValidArguments_Success() throws Exception {
		// given
		final String authenticationKey = "2a6ec3d7-7147-4cdc-a6c5-fc5d937cfe76";
		final String authenticationCode = "123456";
		final String phone = "01012345678";
		final PhoneAuthenticationType type = PhoneAuthenticationType.FIND_ID;
		
		final PhoneAuthentication phoneAuthentication = PhoneAuthentication.of(authenticationCode, phone, type);
		ReflectionTestUtils.setField(phoneAuthentication, "createdAt", LocalDateTime.now());

		given(phoneAuthenticationRepository.findByIdAndTypeAndPhoneAndStatusNot(authenticationKey, type, phone,
			PhoneAuthenticationStatus.EXPIRED)).willReturn(Optional.of(phoneAuthentication));

		// when
		final PhoneAuthenticationResult result = phoneAuthenticationService.checkAuthenticationCode(
			authenticationKey, authenticationCode, phone, type);

		// then
		assertThat(result).isEqualTo(PhoneAuthenticationResult.AUTHENTICATED);
	}

	@Test
	void checkAuthenticationCode_MismatchedCode_Mismatched() throws Exception {
		// given
		final String authenticationKey = "2a6ec3d7-7147-4cdc-a6c5-fc5d937cfe76";
		final String authenticationCode = "123456";
		final String wrongAuthenticationCode = "634262";
		final String phone = "01012345678";
		final PhoneAuthenticationType type = PhoneAuthenticationType.FIND_ID;
		
		final PhoneAuthentication phoneAuthentication = PhoneAuthentication.of(authenticationCode, phone, type);
		ReflectionTestUtils.setField(phoneAuthentication, "createdAt", LocalDateTime.now());

		given(phoneAuthenticationRepository.findByIdAndTypeAndPhoneAndStatusNot(authenticationKey, type, phone,
			PhoneAuthenticationStatus.EXPIRED)).willReturn(Optional.of(phoneAuthentication));

		// when
		final PhoneAuthenticationResult result = phoneAuthenticationService.checkAuthenticationCode(
			authenticationKey, wrongAuthenticationCode, phone, type);

		// then
		assertThat(result).isEqualTo(PhoneAuthenticationResult.MISMATCHED);
	}

	@Test
	void deleteExpiredAuthenticationCodes_Success() throws Exception {
		// given
		given(phoneAuthenticationRepository.findAllByCreatedAtBeforeOrStatus(any(LocalDateTime.class),
			any(PhoneAuthenticationStatus.class))).willReturn(new ArrayList<>());

		// when
		final boolean result = phoneAuthenticationService.deleteExpiredAuthenticationCodes();

		// then
		assertThat(result).isTrue();
	}

	@Test
	void validatePassword_MatchedPassword_True() throws Exception {
		// given
		final String password = "qlalfqjsgh1!";
		final String passwordCheck = "qlalfqjsgh1!";

		// when
		final boolean result = phoneAuthenticationService.validatePassword(password, passwordCheck);

		// then
		assertThat(result).isTrue();
	}

	@Test
	void validatePassword_MismatchedPassword_ExceptionThrown() throws Exception {
		// given
		final String password = "qlalfqjsgh1!";
		final String passwordCheck = "qlalfqjsgh1@";

		// when
		final Executable executable = () -> phoneAuthenticationService.validatePassword(password, passwordCheck);

		// then
		assertThrows(PasswordMismatchException.class, executable);
	}

	@Test
	void validateAuthentication_NonExistentKey_Expired() throws Exception {
		// given
		final String phone = "01012341234";
		final String authenticationKey = UUID.randomUUID().toString();
		final PhoneAuthenticationType type = PhoneAuthenticationType.FIND_ID;

		given(phoneAuthenticationRepository.findByIdAndTypeAndPhoneAndStatusNot(authenticationKey, type, phone,
			PhoneAuthenticationStatus.EXPIRED)).willReturn(Optional.empty());

		// when
		final PhoneAuthenticationResult result = phoneAuthenticationService.validateAuthentication(authenticationKey,
			phone, type);

		// then
		assertThat(result).isEqualTo(PhoneAuthenticationResult.EXPIRED);
	}

	@Test
	void validateAuthentication_ExpiredKey_Expired() throws Exception {
		// given
		final String phone = "01012341234";
		final String authenticationKey = UUID.randomUUID().toString();
		final String authenticationCode = "123456";
		final PhoneAuthenticationType type = PhoneAuthenticationType.FIND_ID;
		
		final PhoneAuthentication phoneAuthentication = PhoneAuthentication.of(authenticationCode, phone, type);
		final LocalDateTime expiredAt = LocalDateTime.now().minusMinutes(60L);
		ReflectionTestUtils.setField(phoneAuthentication, "createdAt", expiredAt);

		given(phoneAuthenticationRepository.findByIdAndTypeAndPhoneAndStatusNot(authenticationKey, type, phone,
			PhoneAuthenticationStatus.EXPIRED)).willReturn(Optional.of(phoneAuthentication));

		// when
		final PhoneAuthenticationResult result = phoneAuthenticationService.validateAuthentication(authenticationKey,
			phone, type);

		// then
		assertThat(result).isEqualTo(PhoneAuthenticationResult.EXPIRED);
	}

	@Test
	void validateAuthentication_AuthenticatedAndValidKey_Authenticated() throws Exception {
		// given
		final String phone = "01012341234";
		final String authenticationKey = UUID.randomUUID().toString();
		final String authenticationCode = "123456";
		final PhoneAuthenticationType type = PhoneAuthenticationType.FIND_ID;
		
		final PhoneAuthentication phoneAuthentication = PhoneAuthentication.of(authenticationCode, phone, type);
		ReflectionTestUtils.setField(phoneAuthentication, "status", PhoneAuthenticationStatus.AUTHENTICATED);

		final LocalDateTime validAt = LocalDateTime.now().minusMinutes(10L);
		ReflectionTestUtils.setField(phoneAuthentication, "createdAt", validAt);

		given(phoneAuthenticationRepository.findByIdAndTypeAndPhoneAndStatusNot(authenticationKey, type, phone,
			PhoneAuthenticationStatus.EXPIRED)).willReturn(Optional.of(phoneAuthentication));

		// when
		final PhoneAuthenticationResult result = phoneAuthenticationService.validateAuthentication(authenticationKey,
			phone, type);

		// then
		assertThat(result).isEqualTo(PhoneAuthenticationResult.AUTHENTICATED);
	}

	@Test
	void validateAuthentication_NotAuthenticatedKey_NotAuthenticated() throws Exception {
		// given
		final String phone = "01012341234";
		final String authenticationKey = UUID.randomUUID().toString();
		final String authenticationCode = "123456";
		final PhoneAuthenticationType type = PhoneAuthenticationType.FIND_ID;
		
		final PhoneAuthentication phoneAuthentication = PhoneAuthentication.of(authenticationCode, phone, type);
		final LocalDateTime validAt = LocalDateTime.now().minusMinutes(10L);
		ReflectionTestUtils.setField(phoneAuthentication, "createdAt", validAt);

		given(phoneAuthenticationRepository.findByIdAndTypeAndPhoneAndStatusNot(authenticationKey, type, phone,
			PhoneAuthenticationStatus.EXPIRED)).willReturn(Optional.of(phoneAuthentication));

		// when
		final PhoneAuthenticationResult result = phoneAuthenticationService.validateAuthentication(authenticationKey,
			phone, type);

		// then
		assertThat(result).isEqualTo(PhoneAuthenticationResult.NOT_AUTHENTICATED);
	}

}