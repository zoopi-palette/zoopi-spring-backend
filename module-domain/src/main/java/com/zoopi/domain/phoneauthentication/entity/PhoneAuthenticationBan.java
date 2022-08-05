package com.zoopi.domain.phoneauthentication.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.zoopi.domain.BaseEntity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "phone_authentication_bans")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PhoneAuthenticationBan extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "phone_authentication_ban_id")
	private Long id;

	@Column(name = "phone")
	private String phone;

	@Enumerated(EnumType.STRING)
	@Column(name = "type")
	private PhoneAuthenticationType type;

	public PhoneAuthenticationBan(String phone, PhoneAuthenticationType type) {
		this.phone = phone;
		this.type = type;
	}

}
