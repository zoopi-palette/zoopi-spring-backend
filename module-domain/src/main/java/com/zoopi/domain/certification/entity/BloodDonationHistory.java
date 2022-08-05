package com.zoopi.domain.certification.entity;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.zoopi.domain.BaseEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BloodDonationHistory extends BaseEntity {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "blood_donation_history_id")
	private Long id;

	private Long petId;
	private Long receiverPetId;

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "blood_donation_detail_id")
	private BloodDonationDetail detail;

	@Column(name = "image")
	private String imageUrl;

	private Long hospitalId;

	private LocalDate bloodDonationAt;

	@Enumerated(EnumType.STRING)
	private BloodDonationType type;
}
