package com.zoopi.domain.authentication.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zoopi.domain.authentication.entity.Ban;

public interface BanRepository extends JpaRepository<Ban, Long> {

	Optional<Ban> findByPhone(String phone);

}
