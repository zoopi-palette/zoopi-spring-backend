package com.zoopi.domain.authentication.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zoopi.domain.authentication.entity.Authentication;
import com.zoopi.domain.authentication.entity.AuthenticationStatus;
import com.zoopi.domain.authentication.entity.AuthenticationType;

public interface AuthenticationRepository extends JpaRepository<Authentication, String> {

	List<Authentication> findAllByStatusAndCreatedAtAfter(AuthenticationStatus status, LocalDateTime date);

	int countByPhoneAndTypeAndCreatedAtAfter(String phone, AuthenticationType type, LocalDateTime date);

	Optional<Authentication> findByIdAndType(String authenticationKey, AuthenticationType type);

}
