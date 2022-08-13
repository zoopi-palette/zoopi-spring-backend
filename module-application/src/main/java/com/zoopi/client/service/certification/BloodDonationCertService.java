package com.zoopi.client.service.certification;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import com.zoopi.domain.certification.repository.BloodDonationCertRepository;

@Service
@RequiredArgsConstructor
public class BloodDonationCertService {

	private final BloodDonationCertRepository certRepository;
}
