package com.zoopi.domain.certification.repository;

import com.zoopi.domain.certification.entity.BloodDonationHistory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BloodDonationCertRepository extends JpaRepository<BloodDonationHistory, Long> {
}
