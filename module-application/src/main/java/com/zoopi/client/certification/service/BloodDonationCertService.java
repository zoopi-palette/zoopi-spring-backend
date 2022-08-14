package com.zoopi.client.certification.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import com.zoopi.domain.certification.repository.BloodDonationCertRepository;

@Service
@RequiredArgsConstructor
public class BloodDonationCertService {

	private final BloodDonationCertRepository certRepository;
}
