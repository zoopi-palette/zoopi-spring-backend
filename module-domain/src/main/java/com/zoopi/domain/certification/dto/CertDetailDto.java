package com.zoopi.domain.certification.dto;

import java.time.LocalDateTime;

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

	public CertDetailDto(BloodDonationDetail detail) {
		this.bloodDonationDetailId = detail.getId();
		this.receiverPet = detail.getReceiverPet();
	}

	public void setThanksMessage(ChatMessage thanksMessage) {
		this.thanksMessage = thanksMessage.getMessage();
		this.thanksMessageAt = thanksMessage.getCreatedAt();
	}
}
