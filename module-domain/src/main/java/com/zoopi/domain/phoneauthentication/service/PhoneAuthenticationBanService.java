package com.zoopi.domain.phoneauthentication.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zoopi.domain.phoneauthentication.entity.PhoneAuthenticationType;
import com.zoopi.domain.phoneauthentication.entity.PhoneAuthenticationBan;
import com.zoopi.domain.phoneauthentication.repository.PhoneAuthenticationBanRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PhoneAuthenticationBanService {

	private final PhoneAuthenticationBanRepository phoneAuthenticationBanRepository;

	private final long BAN_DAY = 1L;

	@Transactional
	public boolean isBanned(String phone, PhoneAuthenticationType type) {
		final LocalDateTime now = LocalDateTime.now();
		final Optional<PhoneAuthenticationBan> banOptional = phoneAuthenticationBanRepository.findByPhoneAndType(phone, type);

		if (banOptional.isEmpty()) {
			return false;
		} else {
			final PhoneAuthenticationBan phoneAuthenticationBan = banOptional.get();
			if (now.isAfter(phoneAuthenticationBan.getCreatedAt().plusDays(BAN_DAY))) {
				phoneAuthenticationBanRepository.delete(phoneAuthenticationBan);
				return false;
			} else {
				return true;
			}
		}
	}

	@Transactional
	public PhoneAuthenticationBan ban(String phone, PhoneAuthenticationType type) {
		return phoneAuthenticationBanRepository.save(new PhoneAuthenticationBan(phone, type));
	}

}
