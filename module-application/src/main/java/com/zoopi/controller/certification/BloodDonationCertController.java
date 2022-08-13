package com.zoopi.controller.certification;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import com.zoopi.api.client.certification.BloodDonationCertApi;
import com.zoopi.model.ResultResponse;
import com.zoopi.service.certification.BloodDonationCertService;

@RestController
@RequiredArgsConstructor
public class BloodDonationCertController implements BloodDonationCertApi {

	private final BloodDonationCertService certService;

	@GetMapping("/{petId}")
	public ResponseEntity<ResultResponse> findCertification(Long petId) {
		return ResponseEntity.ok(ResultResponse.of(null));
	}

}
