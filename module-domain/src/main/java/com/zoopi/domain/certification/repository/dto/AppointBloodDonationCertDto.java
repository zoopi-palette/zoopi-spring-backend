package com.zoopi.domain.certification.repository.dto;

import java.time.LocalDate;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class AppointBloodDonationCertDto {
	private final Long bloodDonationHistoryId;

	private final String receiverPetName;
	private final String receiverPetImageUrl;
	private final String thanksMessage;

	private final String imageUrl;
	private final LocalDate bloodDonationAt;
	private final String hospitalName;

	@QueryProjection
	public AppointBloodDonationCertDto(
		Long bloodDonationHistoryId,

		String receiverPetName,
		String receiverPetImageUrl,
		String thanksMessage,

		String imageUrl,
		LocalDate bloodDonationAt,
		String hospitalName
	) {
		this.bloodDonationHistoryId = bloodDonationHistoryId;
		this.receiverPetName = receiverPetName;
		this.receiverPetImageUrl = receiverPetImageUrl;
		this.thanksMessage = thanksMessage;
		this.imageUrl = imageUrl;
		this.bloodDonationAt = bloodDonationAt;
		this.hospitalName = hospitalName;
	}
}
