package com.zoopi.domain.certification.repository;

import java.util.List;

import com.zoopi.domain.certification.entity.BloodDonationDetail;

public interface BloodDonationDetailRepositoryQueryDsl {
	List<BloodDonationDetail> findByHistoryIdsIn(List<Long> ids);
}
