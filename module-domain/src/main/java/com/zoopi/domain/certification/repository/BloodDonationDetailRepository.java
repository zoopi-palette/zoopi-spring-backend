package com.zoopi.domain.certification.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

import com.zoopi.domain.certification.entity.BloodDonationDetail;
import com.zoopi.domain.certification.entity.QBloodDonationDetail;
import com.zoopi.domain.certification.entity.QBloodDonationHistory;
import com.zoopi.domain.chat.entity.QChatMessage;
import com.zoopi.domain.chat.entity.QChatRoom;
import com.zoopi.domain.pet.entity.QPet;

@Repository
@RequiredArgsConstructor
public class BloodDonationDetailRepository {

	private final JPAQueryFactory jpaQueryFactory;
	private final QBloodDonationHistory bloodDonationHistory = QBloodDonationHistory.bloodDonationHistory;
	private final QBloodDonationDetail bloodDonationDetail = QBloodDonationDetail.bloodDonationDetail;
	private final QPet pet = QPet.pet;
	private final QChatRoom chatRoom = QChatRoom.chatRoom;
	private final QChatMessage chatMessage = QChatMessage.chatMessage;

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
