package com.zoopi.controller.certification;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;

import com.zoopi.controller.ResultResponse;
import com.zoopi.domain.certification.service.BloodDonationCertService;

@Api(tags = "헌혈 인증 API")
@RestController
@RequestMapping("/api/cert")
@RequiredArgsConstructor
public class BloodDonationCertController {

	private final BloodDonationCertService certService;

	@GetMapping("/{petId}")
	public ResponseEntity<ResultResponse> findCertification(
			@PathVariable Long petId
	) {
		return ResponseEntity.ok(ResultResponse.of(null));
	}
}
