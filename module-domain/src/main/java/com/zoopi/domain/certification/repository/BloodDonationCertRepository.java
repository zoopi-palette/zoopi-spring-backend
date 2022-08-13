package com.zoopi.domain.certification.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zoopi.domain.certification.entity.BloodDonationHistory;

@Repository
public interface BloodDonationCertRepository extends JpaRepository<BloodDonationHistory, Long> {
}
