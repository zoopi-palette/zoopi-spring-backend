package com.zoopi.domain.certification.repository;

import static com.zoopi.domain.certification.entity.QBloodDonationDetail.*;
import static com.zoopi.domain.certification.entity.QBloodDonationHistory.*;
import static com.zoopi.domain.pet.entity.QPet.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

import com.zoopi.domain.certification.entity.BloodDonationDetail;

@Repository
@RequiredArgsConstructor
public class BloodDonationDetailRepositoryQueryDslImpl {

	private final JPAQueryFactory jpaQueryFactory;

	public List<BloodDonationDetail> findByHistoryIdsIn(List<Long> ids) {
		return jpaQueryFactory.selectFrom(bloodDonationDetail)
			.innerJoin(bloodDonationHistory).on(bloodDonationHistory.id.eq(bloodDonationDetail.history.id))
			.fetchJoin()
			.innerJoin(pet).on(pet.id.eq(bloodDonationDetail.receiverPet.id))
			.fetchJoin()
			.where(bloodDonationDetail.history.id.in(ids))
			.fetch();
	}

}
