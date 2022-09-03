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
import javax.persistence.ManyToOne;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.zoopi.domain.BaseEntity;
import com.zoopi.domain.hospital.entity.Hospital;
import com.zoopi.domain.pet.entity.Pet;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class BloodDonationHistory extends BaseEntity {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "blood_donation_history_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pet_id", referencedColumnName = "pet_id")
	private Pet pet;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "receiver_pet_id", referencedColumnName = "receiver_pet_id")
	private Pet receiverPet;

	private String imageUrl;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "hospital_id", referencedColumnName = "hospital_id")
	private Hospital hospital;

	private LocalDate bloodDonationAt;

	@Enumerated(EnumType.STRING)
	private BloodDonationType type;

}
