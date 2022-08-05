package com.zoopi.domain.phoneauthentication.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zoopi.domain.phoneauthentication.entity.PhoneAuthentication;
import com.zoopi.domain.phoneauthentication.entity.PhoneAuthenticationStatus;
import com.zoopi.domain.phoneauthentication.entity.PhoneAuthenticationType;

public interface PhoneAuthenticationRepository extends JpaRepository<PhoneAuthentication, String> {

	List<PhoneAuthentication> findAllByCreatedAtBeforeOrStatus(LocalDateTime date, PhoneAuthenticationStatus status);

	int countByPhoneAndTypeAndCreatedAtAfter(String phone, PhoneAuthenticationType type, LocalDateTime date);

	Optional<PhoneAuthentication> findByIdAndTypeAndPhoneAndStatusNot(String authenticationKey,
		PhoneAuthenticationType type, String phone, PhoneAuthenticationStatus status);

}
