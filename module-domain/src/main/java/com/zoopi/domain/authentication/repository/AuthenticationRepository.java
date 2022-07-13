package com.zoopi.domain.authentication.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zoopi.domain.authentication.entity.Authentication;
import com.zoopi.domain.authentication.entity.AuthenticationStatus;

public interface AuthenticationRepository extends JpaRepository<Authentication, String> {

	List<Authentication> findAllByStatusAndCreatedAtAfter(AuthenticationStatus status, LocalDateTime date);

	int countByPhoneAndCreatedAtAfter(String phone, LocalDateTime date);

}
