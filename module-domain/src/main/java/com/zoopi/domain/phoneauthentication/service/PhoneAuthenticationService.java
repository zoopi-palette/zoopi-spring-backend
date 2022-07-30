package com.zoopi.domain.phoneauthentication.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zoopi.domain.phoneauthentication.dto.response.PhoneAuthenticationResponse;
import com.zoopi.domain.phoneauthentication.dto.response.PhoneAuthenticationResult;
import com.zoopi.domain.phoneauthentication.entity.PhoneAuthentication;
import com.zoopi.domain.phoneauthentication.entity.PhoneAuthenticationStatus;
import com.zoopi.domain.phoneauthentication.entity.PhoneAuthenticationType;
import com.zoopi.domain.phoneauthentication.exception.PasswordMismatchException;
import com.zoopi.domain.phoneauthentication.repository.PhoneAuthenticationRepository;
import com.zoopi.infra.sms.SmsClient;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PhoneAuthenticationService {

	private static final long AUTHENTICATION_CODE_VALID_MINUTES = 5L;
	private static final long AUTHENTICATION_KEY_VALID_MINUTES = 30L;

	private final PhoneAuthenticationRepository phoneAuthenticationRepository;
	private final SmsClient smsClient;

	public boolean sendAuthenticationCode(String phone, String authenticationCode) {
		final String content = "[zoopi] 인증번호 [" + authenticationCode + "]를 입력해 주세요.";
		return smsClient.sendSms(phone, content);
	}

	@Transactional
	public PhoneAuthenticationResponse createAuthentication(String phone, String authenticationCode,
		PhoneAuthenticationType type) {
		String uuid = UUID.randomUUID().toString();
		while (phoneAuthenticationRepository.findById(uuid).isPresent()) {
			uuid = UUID.randomUUID().toString();
		}

		phoneAuthenticationRepository.save(new PhoneAuthentication(uuid, authenticationCode, phone, type));
		final String expiredDate = LocalDateTime.now()
			.plusMinutes(AUTHENTICATION_CODE_VALID_MINUTES)
			.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		return new PhoneAuthenticationResponse(uuid, expiredDate);
	}

	public int getCountOfAuthentication(String phone, PhoneAuthenticationType type) {
		return phoneAuthenticationRepository.countByPhoneAndTypeAndCreatedAtAfter(phone, type,
			LocalDateTime.now().minusMinutes(AUTHENTICATION_CODE_VALID_MINUTES));
	}

	@Transactional
	public PhoneAuthenticationResult checkAuthenticationCode(String authenticationCode, String phone,
		String authenticationKey) {
		final LocalDateTime now = LocalDateTime.now();
		final Optional<PhoneAuthentication> authenticationOptional = phoneAuthenticationRepository.findById(authenticationKey);

		if (authenticationOptional.isEmpty()) {
			return PhoneAuthenticationResult.EXPIRED;
		}

		final PhoneAuthentication phoneAuthentication = authenticationOptional.get();
		final boolean isExpired = phoneAuthentication.getCreatedAt()
			.plusMinutes(AUTHENTICATION_CODE_VALID_MINUTES)
			.isBefore(now);
		final boolean isMatch = phoneAuthentication.getCode().equals(authenticationCode);
		final boolean isMine = phoneAuthentication.getPhone().equals(phone);

		if (isExpired) {
			phoneAuthenticationRepository.deleteById(authenticationKey);
			return PhoneAuthenticationResult.EXPIRED;
		} else if (isMatch && isMine) {
			phoneAuthentication.authenticate();
			return PhoneAuthenticationResult.AUTHENTICATED;
		} else {
			return PhoneAuthenticationResult.MISMATCHED;
		}
	}

	@Transactional
	public boolean deleteExpiredAuthenticationCodes() {
		final LocalDateTime nowBeforeFiveMinutes = LocalDateTime.now().minusMinutes(AUTHENTICATION_CODE_VALID_MINUTES);
		final List<PhoneAuthentication> phoneAuthentications = phoneAuthenticationRepository.findAllByCreatedAtBefore(
			nowBeforeFiveMinutes);
		phoneAuthenticationRepository.deleteAllInBatch(phoneAuthentications);
		return true;
	}

	public boolean validatePassword(String password, String passwordCheck) {
		if (!password.equals(passwordCheck)) {
			throw new PasswordMismatchException();
		}
		return true;
	}

	public PhoneAuthenticationResult validateAuthenticationKey(String phone, String authenticationKey,
		PhoneAuthenticationType type) {
		final LocalDateTime now = LocalDateTime.now();
		final Optional<PhoneAuthentication> authenticationOptional = phoneAuthenticationRepository.findByIdAndType(
			authenticationKey, type);

		if (authenticationOptional.isEmpty()) {
			return PhoneAuthenticationResult.EXPIRED;
		}

		final PhoneAuthentication phoneAuthentication = authenticationOptional.get();
		final boolean isExpired = phoneAuthentication.getCreatedAt()
			.plusMinutes(AUTHENTICATION_KEY_VALID_MINUTES)
			.isBefore(now);
		final boolean isMine = phoneAuthentication.getPhone().equals(phone);
		final boolean isAuthenticated = phoneAuthentication.getStatus().equals(PhoneAuthenticationStatus.AUTHENTICATED);

		if (isExpired) {
			phoneAuthenticationRepository.deleteById(authenticationKey);
			return PhoneAuthenticationResult.EXPIRED;
		} else if (isAuthenticated && isMine) {
			phoneAuthenticationRepository.deleteById(authenticationKey);
			return PhoneAuthenticationResult.AUTHENTICATED;
		} else {
			return PhoneAuthenticationResult.NOT_AUTHENTICATED;
		}
	}

}
