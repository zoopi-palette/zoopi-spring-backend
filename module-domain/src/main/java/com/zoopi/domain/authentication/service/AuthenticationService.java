package com.zoopi.domain.authentication.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zoopi.domain.authentication.dto.response.AuthenticationResponse;
import com.zoopi.domain.authentication.dto.response.AuthenticationResult;
import com.zoopi.domain.authentication.entity.Authentication;
import com.zoopi.domain.authentication.repository.AuthenticationRepository;
import com.zoopi.infra.sms.SmsClient;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthenticationService {

	private final AuthenticationRepository authenticationRepository;
	private final SmsClient smsClient;

	private final long AUTHENTICATION_CODE_VALID_TIME = 5L;

	public boolean sendAuthenticationCode(String phone, String authenticationCode) {
		final String content = "[zoopi] 인증번호 [" + authenticationCode + "]를 입력해 주세요.";
		return smsClient.sendSms(phone, content);
	}

	@Transactional
	public AuthenticationResponse createAuthentication(String phone, String authenticationCode) {
		String uuid = UUID.randomUUID().toString();
		while (authenticationRepository.findById(uuid).isPresent()) {
			uuid = UUID.randomUUID().toString();
		}

		final LocalDateTime expiredDate = LocalDateTime.now().plusMinutes(AUTHENTICATION_CODE_VALID_TIME);
		final Authentication authentication = new Authentication(uuid, authenticationCode, phone, expiredDate);
		authenticationRepository.save(authentication);

		return new AuthenticationResponse(uuid, expiredDate);
	}

	public int getCountOfAuthentication(String phone) {
		return authenticationRepository.countByPhoneAndExpiredDateAfter(phone,
			LocalDateTime.now().minusMinutes(AUTHENTICATION_CODE_VALID_TIME));
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
		final boolean isExpired = authentication.getExpiredDate().isBefore(now);
		final boolean isMatch = authentication.getCode().equals(authenticationCode);
		final boolean isMine = authentication.getPhone().equals(phone);

		if (isExpired) {
			authenticationRepository.deleteById(authenticationKey);
			return AuthenticationResult.EXPIRED;
		} else if (isMatch && isMine) {
			authenticationRepository.deleteById(authenticationKey);
			return AuthenticationResult.SUCCESS;
		} else {
			return AuthenticationResult.MISMATCHED;
		}
	}

	@Transactional
	public void deleteExpiredAuthenticationCodes() {
		final LocalDateTime now = LocalDateTime.now();
		final List<Authentication> authentications = authenticationRepository.findAllByExpiredDateBefore(now);
		authenticationRepository.deleteAllInBatch(authentications);
	}

}
