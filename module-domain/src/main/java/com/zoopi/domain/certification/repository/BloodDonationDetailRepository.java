package com.zoopi.domain.certification.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zoopi.domain.certification.entity.BloodDonationDetail;

public interface BloodDonationDetailRepository extends JpaRepository<BloodDonationDetail, Long>, BloodDonationDetailRepositoryQueryDsl {
}
