package com.zoopi.api.client.certification;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.annotations.Api;

import com.zoopi.model.ResultResponse;

@Api(tags = "헌혈 인증 API")
@RequestMapping("/api/cert")
public interface BloodDonationCertApi {

	@GetMapping("/{petId}")
	ResponseEntity<ResultResponse> findCertification(@PathVariable Long petId);

}
