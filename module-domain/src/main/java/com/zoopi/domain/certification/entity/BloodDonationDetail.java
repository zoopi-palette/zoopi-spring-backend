package com.zoopi.domain.certification.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.zoopi.domain.BaseEntity;
import com.zoopi.domain.pet.entity.Pet;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class BloodDonationDetail extends BaseEntity {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "blood_dontation_detail_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "receiver_pet_id", referencedColumnName = "pet_id")
	private Pet receiverPet;

	private Long chatRoomId;

	private String message;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "blood_donation_history_id", referencedColumnName = "blood_donation_history_id")
	private BloodDonationHistory history;

}
