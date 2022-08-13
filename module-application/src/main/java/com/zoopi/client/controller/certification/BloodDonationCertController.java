package com.zoopi.client.controller.certification;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import com.zoopi.client.api.certification.BloodDonationCertApi;
import com.zoopi.ResultResponse;
import com.zoopi.client.service.certification.BloodDonationCertService;

@RestController
@RequiredArgsConstructor
public class BloodDonationCertController implements BloodDonationCertApi {

	private final BloodDonationCertService certService;

	@GetMapping("/{petId}")
	public ResponseEntity<ResultResponse> findCertification(Long petId) {
		return ResponseEntity.ok(ResultResponse.of(null));
	}

}
