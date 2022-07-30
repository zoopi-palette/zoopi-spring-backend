package com.zoopi.domain.phoneauthentication.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zoopi.domain.phoneauthentication.entity.PhoneAuthenticationType;
import com.zoopi.domain.phoneauthentication.entity.PhoneAuthenticationBan;

public interface PhoneAuthenticationBanRepository extends JpaRepository<PhoneAuthenticationBan, Long> {

	Optional<PhoneAuthenticationBan> findByPhoneAndType(String phone, PhoneAuthenticationType type);

}
