package com.zoopi.domain.phoneauthentication.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zoopi.domain.phoneauthentication.entity.PhoneAuthentication;
import com.zoopi.domain.phoneauthentication.entity.PhoneAuthenticationType;

public interface PhoneAuthenticationRepository extends JpaRepository<PhoneAuthentication, String> {

	List<PhoneAuthentication> findAllByCreatedAtBefore(LocalDateTime date);

	int countByPhoneAndTypeAndCreatedAtAfter(String phone, PhoneAuthenticationType type, LocalDateTime date);

	Optional<PhoneAuthentication> findByIdAndType(String authenticationKey, PhoneAuthenticationType type);

}
