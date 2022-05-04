package com.zoopi.domain.authentication.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zoopi.domain.authentication.entity.Authentication;

public interface AuthenticationRepository extends JpaRepository<Authentication, String> {

	List<Authentication> findAllByExpiredDateBefore(LocalDateTime now);
}
