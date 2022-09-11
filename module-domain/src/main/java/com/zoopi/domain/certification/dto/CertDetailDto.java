package com.zoopi.domain.certification.dto;

import java.time.LocalDateTime;
import java.util.Optional;

import lombok.Getter;

import com.zoopi.domain.certification.entity.BloodDonationDetail;
import com.zoopi.domain.chat.entity.ChatMessage;
import com.zoopi.domain.pet.entity.Pet;

@Getter
public class CertDetailDto {

	private final Long bloodDonationDetailId;
	private final Pet receiverPet;
	private String thanksMessage = null;
	private LocalDateTime thanksMessageAt = null;

	public CertDetailDto(BloodDonationDetail detail, ChatMessage thanksMessage) {
		this.bloodDonationDetailId = detail.getId();
		this.receiverPet = detail.getReceiverPet();

		Optional.ofNullable(thanksMessage).ifPresent(thanks -> {
			this.thanksMessage = thanks.getMessage();
			this.thanksMessageAt = thanks.getCreatedAt();
		});
	}

}
