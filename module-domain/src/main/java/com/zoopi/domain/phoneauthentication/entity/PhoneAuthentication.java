package com.zoopi.domain.phoneauthentication.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import com.zoopi.domain.BaseEntity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "phone_authentications")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PhoneAuthentication extends BaseEntity {

	@Id
	@Column(name = "phone_authentication_id")
	private String id;

	@Column(name = "code")
	private String code;

	@Column(name = "phone")
	private String phone;

	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private PhoneAuthenticationStatus status;

	@Enumerated(EnumType.STRING)
	@Column(name = "type")
	private PhoneAuthenticationType type;

	public PhoneAuthentication(String id, String code, String phone, PhoneAuthenticationType type) {
		this.id = id;
		this.code = code;
		this.phone = phone;
		this.status = PhoneAuthenticationStatus.NOT_AUTHENTICATED;
		this.type = type;
	}

	public void authenticate() {
		this.status = PhoneAuthenticationStatus.AUTHENTICATED;
	}

}
