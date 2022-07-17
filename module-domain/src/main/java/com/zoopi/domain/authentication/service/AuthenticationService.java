package com.zoopi.domain.authentication.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zoopi.domain.authentication.dto.response.AuthenticationResponse;
import com.zoopi.domain.authentication.dto.response.AuthenticationResult;
import com.zoopi.domain.authentication.entity.Authentication;
import com.zoopi.domain.authentication.entity.AuthenticationStatus;
import com.zoopi.domain.authentication.entity.AuthenticationType;
import com.zoopi.domain.authentication.exception.PasswordMismatchException;
import com.zoopi.domain.authentication.repository.AuthenticationRepository;
import com.zoopi.infra.sms.SmsClient;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthenticationService {

	private static final long AUTHENTICATION_CODE_VALID_MINUTES = 5L;
	private static final long AUTHENTICATION_KEY_VALID_MINUTES = 30L;

	private final AuthenticationRepository authenticationRepository;
	private final SmsClient smsClient;

	public boolean sendAuthenticationCode(String phone, String authenticationCode) {
		final String content = "[zoopi] 인증번호 [" + authenticationCode + "]를 입력해 주세요.";
		return smsClient.sendSms(phone, content);
	}

	@Transactional
	public AuthenticationResponse createAuthentication(String phone, String authenticationCode,
		AuthenticationType type) {
		String uuid = UUID.randomUUID().toString();
		while (authenticationRepository.findById(uuid).isPresent()) {
			uuid = UUID.randomUUID().toString();
		}

		authenticationRepository.save(new Authentication(uuid, authenticationCode, phone, type));
		final String expiredDate = LocalDateTime.now()
			.plusMinutes(AUTHENTICATION_CODE_VALID_MINUTES)
			.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		return new AuthenticationResponse(uuid, expiredDate);
	}

	public int getCountOfAuthentication(String phone, AuthenticationType type) {
		return authenticationRepository.countByPhoneAndTypeAndCreatedAtAfter(phone, type,
			LocalDateTime.now().minusMinutes(AUTHENTICATION_CODE_VALID_MINUTES));
	}

	@Transactional
	public AuthenticationResult checkAuthenticationCode(String authenticationCode, String phone,
		String authenticationKey) {
		final LocalDateTime now = LocalDateTime.now();
		final Optional<Authentication> authenticationOptional = authenticationRepository.findById(authenticationKey);

		if (authenticationOptional.isEmpty()) {
			return AuthenticationResult.EXPIRED;
		}

		final Authentication authentication = authenticationOptional.get();
		final boolean isExpired = authentication.getCreatedAt()
			.plusMinutes(AUTHENTICATION_CODE_VALID_MINUTES)
			.isBefore(now);
		final boolean isMatch = authentication.getCode().equals(authenticationCode);
		final boolean isMine = authentication.getPhone().equals(phone);

		if (isExpired) {
			authenticationRepository.deleteById(authenticationKey);
			return AuthenticationResult.EXPIRED;
		} else if (isMatch && isMine) {
			authentication.authenticate();
			return AuthenticationResult.AUTHENTICATED;
		} else {
			return AuthenticationResult.MISMATCHED;
		}
	}

	@Transactional
	public boolean deleteExpiredAuthenticationCodes() {
		final LocalDateTime nowBeforeFiveMinutes = LocalDateTime.now().minusMinutes(AUTHENTICATION_CODE_VALID_MINUTES);
		final List<Authentication> authentications = authenticationRepository.findAllByCreatedAtBefore(
			nowBeforeFiveMinutes);
		authenticationRepository.deleteAllInBatch(authentications);
		return true;
	}

	public boolean validatePassword(String password, String passwordCheck) {
		if (!password.equals(passwordCheck)) {
			throw new PasswordMismatchException();
		}
		return true;
	}

	public AuthenticationResult validateAuthenticationKey(String phone, String authenticationKey,
		AuthenticationType type) {
		final LocalDateTime now = LocalDateTime.now();
		final Optional<Authentication> authenticationOptional = authenticationRepository.findByIdAndType(
			authenticationKey, type);

		if (authenticationOptional.isEmpty()) {
			return AuthenticationResult.EXPIRED;
		}

		final Authentication authentication = authenticationOptional.get();
		final boolean isExpired = authentication.getCreatedAt()
			.plusMinutes(AUTHENTICATION_KEY_VALID_MINUTES)
			.isBefore(now);
		final boolean isMine = authentication.getPhone().equals(phone);
		final boolean isAuthenticated = authentication.getStatus().equals(AuthenticationStatus.AUTHENTICATED);

		if (isExpired) {
			authenticationRepository.deleteById(authenticationKey);
			return AuthenticationResult.EXPIRED;
		} else if (isAuthenticated && isMine) {
			authenticationRepository.deleteById(authenticationKey);
			return AuthenticationResult.AUTHENTICATED;
		} else {
			return AuthenticationResult.NOT_AUTHENTICATED;
		}
	}

}
