package com.zoopi.domain.authentication.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "authentications")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Authentication {

	@Id
	@Column(name = "authentication_id")
	private String id;

	@Column(name = "authentication_code")
	private String code;

	@Column(name = "authentication_phone")
	private String phone;

	@Column(name = "authentication_expire_date")
	private LocalDateTime expiredDate;
}
