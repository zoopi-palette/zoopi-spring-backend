package com.zoopi.domain.phoneauthentication.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.zoopi.domain.phoneauthentication.dto.response.PhoneAuthenticationResponse;
import com.zoopi.domain.phoneauthentication.dto.response.PhoneAuthenticationResult;
import com.zoopi.domain.phoneauthentication.entity.PhoneAuthentication;
import com.zoopi.domain.phoneauthentication.entity.PhoneAuthenticationStatus;
import com.zoopi.domain.phoneauthentication.entity.PhoneAuthenticationType;
import com.zoopi.domain.phoneauthentication.exception.PasswordMismatchException;
import com.zoopi.domain.phoneauthentication.repository.PhoneAuthenticationRepository;
import com.zoopi.infra.sms.SmsClient;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PhoneAuthenticationService {

	public static final long PHONE_AUTHENTICATION_CODE_VALID_MINUTES = 5L;
	public static final long PHONE_AUTHENTICATION_KEY_VALID_MINUTES = 30L;
	private static final String PHONE_AUTHENTICATION_CODE_PHRASE = "[zoopi] 인증번호 [%s]를 입력해 주세요.";

	private final PhoneAuthenticationRepository phoneAuthenticationRepository;
	private final SmsClient smsClient;

	/**
	 * Reference: <a href="https://docs.oracle.com/javase/7/docs/api/java/util/Formatter.html#syntax">Formatter</a>
	 */
	public boolean sendAuthenticationCode(String phone, String authenticationCode) {
		final String content = String.format(PHONE_AUTHENTICATION_CODE_PHRASE, authenticationCode);
		return smsClient.sendSms(phone, content);
	}

	@Transactional
	public PhoneAuthenticationResponse createAuthentication(String phone, String authenticationCode,
		PhoneAuthenticationType type) {
		final PhoneAuthentication phoneAuthentication = PhoneAuthentication.of(authenticationCode, phone, type);
		return PhoneAuthenticationResponse.of(phoneAuthenticationRepository.save(phoneAuthentication));
	}

	public int getCountOfAuthentication(String phone, PhoneAuthenticationType type) {
		return phoneAuthenticationRepository.countByPhoneAndTypeAndCreatedAtAfter(phone, type,
			LocalDateTime.now().minusMinutes(PHONE_AUTHENTICATION_CODE_VALID_MINUTES));
	}

	@Transactional
	public PhoneAuthenticationResult checkAuthenticationCode(String key, String code, String phone,
		PhoneAuthenticationType type) {
		final Optional<PhoneAuthentication> authenticationOptional = phoneAuthenticationRepository
			.findByIdAndTypeAndPhoneAndStatusNot(key, type, phone, PhoneAuthenticationStatus.EXPIRED);
		if (authenticationOptional.isEmpty()) {
			return PhoneAuthenticationResult.EXPIRED;
		}

		final PhoneAuthentication phoneAuthentication = authenticationOptional.get();
		return phoneAuthentication.checkCode(code);
	}

	public PhoneAuthenticationResult validateAuthentication(String key, String phone, PhoneAuthenticationType type) {
		final Optional<PhoneAuthentication> authenticationOptional = phoneAuthenticationRepository
			.findByIdAndTypeAndPhoneAndStatusNot(key, type, phone, PhoneAuthenticationStatus.EXPIRED);
		if (authenticationOptional.isEmpty()) {
			return PhoneAuthenticationResult.EXPIRED;
		}

		final PhoneAuthentication phoneAuthentication = authenticationOptional.get();
		return phoneAuthentication.validate();
	}

	@Transactional
	public boolean deleteExpiredAuthenticationCodes() {
		final LocalDateTime nowBeforeFiveMinutes = LocalDateTime.now()
			.minusMinutes(PHONE_AUTHENTICATION_CODE_VALID_MINUTES);
		final List<PhoneAuthentication> phoneAuthentications = phoneAuthenticationRepository
			.findAllByCreatedAtBeforeOrStatus(nowBeforeFiveMinutes, PhoneAuthenticationStatus.EXPIRED);
		phoneAuthenticationRepository.deleteAllInBatch(phoneAuthentications);
		return true;
	}

	public boolean validatePassword(String password, String passwordCheck) {
		if (!password.equals(passwordCheck)) {
			throw new PasswordMismatchException();
		}
		return true;
	}

}
