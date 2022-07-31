package com.zoopi.domain.certification.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BloodDonationDetail {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "blood_dontation_detail_id")
	private Long id;

	private Long chatRoomId;

	private String message;

}
