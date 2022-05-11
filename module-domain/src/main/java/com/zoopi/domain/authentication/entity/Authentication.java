package com.zoopi.domain.authentication.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "authentications")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Authentication {

	@Id
	@Column(name = "authentication_id")
	private String id;

	@Column(name = "authentication_code")
	private String code;

	@Column(name = "authentication_phone")
	private String phone;

	@CreatedDate
	@Column(name = "authentication_create_date")
	private LocalDateTime createdDate;

	@Enumerated(EnumType.STRING)
	@Column(name = "authentication_status")
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
