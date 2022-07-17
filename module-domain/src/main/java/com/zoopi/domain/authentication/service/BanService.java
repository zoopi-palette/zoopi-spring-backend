package com.zoopi.domain.authentication.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zoopi.domain.authentication.entity.AuthenticationType;
import com.zoopi.domain.authentication.entity.Ban;
import com.zoopi.domain.authentication.repository.BanRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BanService {

	private final BanRepository banRepository;

	private final long BAN_DAY = 1L;

	@Transactional
	public boolean isBanned(String phone, AuthenticationType type) {
		final LocalDateTime now = LocalDateTime.now();
		final Optional<Ban> banOptional = banRepository.findByPhoneAndType(phone, type);

		if (banOptional.isEmpty()) {
			return false;
		} else {
			final Ban ban = banOptional.get();
			if (now.isAfter(ban.getCreatedAt().plusDays(BAN_DAY))) {
				banRepository.delete(ban);
				return false;
			} else {
				return true;
			}
		}
	}

	@Transactional
	public Ban ban(String phone, AuthenticationType type) {
		return banRepository.save(new Ban(phone, type));
	}

}
