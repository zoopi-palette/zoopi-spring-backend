package com.zoopi.domain.pet.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.zoopi.domain.BaseEntity;

@Entity
@Getter
@Table(name = "pets")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Pet extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "pet_id")
	private Long id;

	@Column(name = "type")
	private PetTypes type;

	@Column(name = "kind")
	private String kind;

	@Column(name = "name")
	private String name;

	@Column(name = "month_of_birth")
	private Integer monthOfBirth;

	@Column(name = "blood_type")
	private BloodTypes bloodType;

	@Column(name = "gender")
	private Gender gender;

	@Column(name = "register_number")
	private String registerNumber;

	@Column(name = "neuter_flag")
	private Boolean neuterFlag;

	@Column(name = "weight")
	private Integer weight;

	/**
	 * yyyy-MM-dd | none
	 */
	@Column(name = "blood_donation_history")
	private String bloodDonationHistory;

	// TODO: 재검토 필요
	@Column(name = "image_url")
	private String imageUrl;

	@Builder
	public Pet(String name, PetTypes type, String kind, Integer monthOfBirth, BloodTypes bloodType, Gender gender,
		String registerNumber, Boolean neuterFlag, Integer weight, String bloodDonationHistory) {
		this.name = name;
		this.type = type;
		this.kind = kind;
		this.monthOfBirth = monthOfBirth;
		this.bloodType = bloodType;
		this.gender = gender;
		this.registerNumber = registerNumber;
		this.neuterFlag = neuterFlag;
		this.weight = weight;
		this.bloodDonationHistory = bloodDonationHistory;
	}

}
