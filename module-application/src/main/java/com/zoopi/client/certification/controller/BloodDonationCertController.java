package com.zoopi.client.certification.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import com.zoopi.client.certification.api.BloodDonationCertApi;
import com.zoopi.ResultResponse;
import com.zoopi.client.certification.service.BloodDonationCertService;

@RestController
@RequiredArgsConstructor
public class BloodDonationCertController implements BloodDonationCertApi {

	private final BloodDonationCertService certService;

	@GetMapping("/{petId}")
	public ResponseEntity<ResultResponse> findCertification(Long petId) {
		return ResponseEntity.ok(ResultResponse.of(null));
	}

}
