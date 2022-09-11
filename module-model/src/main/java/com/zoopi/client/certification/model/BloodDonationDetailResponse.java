package com.zoopi.client.certification.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import com.zoopi.domain.certification.dto.CertDetailDto;

@Getter
@AllArgsConstructor
@Builder
public class BloodDonationDetailResponse {

	private Long bloodDonationDetailId;
	private Long receiverPetId;
	private String receiverPetName;
	private String thanksMessage;

	public static BloodDonationDetailResponse of(CertDetailDto dto) {
		return BloodDonationDetailResponse.builder()
			.bloodDonationDetailId(dto.getBloodDonationDetailId())
			.receiverPetId(dto.getReceiverPet().getId())
			.receiverPetName(dto.getReceiverPet().getName())
			.thanksMessage(dto.getThanksMessage())
			.build();
	}
}
