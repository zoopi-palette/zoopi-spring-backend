package com.zoopi.domain.certification.service;

import com.zoopi.domain.certification.repository.BloodDonationCertRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BloodDonationCertService {

	private final BloodDonationCertRepository certRepository;
}
