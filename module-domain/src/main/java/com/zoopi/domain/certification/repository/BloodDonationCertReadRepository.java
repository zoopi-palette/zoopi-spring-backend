package com.zoopi.domain.certification.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BloodDonationCertReadRepository {

	private final JPAQueryFactory queryFactory;

//	public List<AppointBloodDonationCertDto> findAppointBloodDonationCerts(Long petId) {
//		QBloodDonationHistory bloodDonationHistory = QBloodDonationHistory.bloodDonationHistory;
//		QBloodDonationDetail bloodDonationDetail = QBloodDonationDetail.bloodDonationDetail;
//	}
}
