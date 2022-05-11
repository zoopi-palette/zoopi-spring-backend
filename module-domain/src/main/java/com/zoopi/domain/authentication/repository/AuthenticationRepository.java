package com.zoopi.domain.authentication.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zoopi.domain.authentication.entity.Authentication;
import com.zoopi.domain.authentication.entity.AuthenticationStatus;

public interface AuthenticationRepository extends JpaRepository<Authentication, String> {

	List<Authentication> findAllByStatusAndCreatedDateAfter(AuthenticationStatus status, LocalDateTime date);

	int countByPhoneAndCreatedDateAfter(String phone, LocalDateTime date);

}
