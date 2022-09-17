package com.zoopi.client.certification.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import com.zoopi.client.certification.model.CertificationResponse;
import com.zoopi.domain.certification.entity.BloodDonationHistory;
import com.zoopi.domain.certification.service.CertificationService;

@Service
@RequiredArgsConstructor
public class BloodDonationCertService {

	private final CertificationService certificationService;

	public List<CertificationResponse> retrieveCertification(Long petId) {
		List<BloodDonationHistory> histories = certificationService.findAllHistoryBy(petId);
		return certificationService.mapHistoryAndDetail(histories)
			.entrySet().stream()
			.map(CertificationResponse::of)
			.collect(Collectors.toList());
	}

}
