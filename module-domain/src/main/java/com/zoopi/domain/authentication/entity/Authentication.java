package com.zoopi.domain.authentication.entity;

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
@Table(name = "authentications")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Authentication extends BaseEntity {

	@Id
	@Column(name = "authentication_id")
	private String id;

	@Column(name = "code")
	private String code;

	@Column(name = "phone")
	private String phone;

	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private AuthenticationStatus status;

	public Authentication(String id, String code, String phone) {
		this.id = id;
		this.code = code;
		this.phone = phone;
		this.status = AuthenticationStatus.NOT_AUTHENTICATED;
	}

	public void authenticate() {
		this.status = AuthenticationStatus.AUTHENTICATED;
	}

}
