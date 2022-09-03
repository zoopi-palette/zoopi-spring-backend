package com.zoopi.domain.certification.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.zoopi.domain.BaseEntity;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class BloodDonationDetail extends BaseEntity {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "blood_dontation_detail_id")
	private Long id;

	private Long chatRoomId;

	private String message;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "blood_donation_history_id", referencedColumnName = "blood_donation_history_id")
	private BloodDonationHistory history;

}
