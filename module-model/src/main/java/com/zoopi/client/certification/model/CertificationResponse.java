package com.zoopi.client.certification.model;

import java.time.LocalDate;
import java.util.Map.Entry;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import com.zoopi.domain.certification.dto.CertDetailDto;
import com.zoopi.domain.certification.entity.BloodDonationHistory;
import com.zoopi.domain.certification.entity.BloodDonationType;
import com.zoopi.domain.hospital.entity.Hospital;

@Getter
@AllArgsConstructor
@Builder
public class CertificationResponse {

	private Long bloodDonationHistoryId;
	private BloodDonationType type;
	private String imageUrl;
	private Hospital hospital;
	private LocalDate bloodDonationAt;

	@JsonInclude(JsonInclude.Include.NON_NULL)
	private BloodDonationDetailResponse detail;

	public static CertificationResponse of(Entry<BloodDonationHistory, CertDetailDto> entry) {
		final BloodDonationHistory history = entry.getKey();
		final CertDetailDto detailDto = entry.getValue();

		final BloodDonationDetailResponse detail = Optional.ofNullable(detailDto)
			.map(BloodDonationDetailResponse::of).orElse(null);

		return CertificationResponse.builder()
			.bloodDonationHistoryId(history.getId())
			.type(history.getType())
			.imageUrl(history.getImageUrl())
			.bloodDonationAt(history.getBloodDonationAt())
			.detail(detail)
			.build();
	}

}
