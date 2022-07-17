package com.zoopi.domain.authentication.entity;

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
@Table(name = "bans")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Ban extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ban_id")
	private Long id;

	@Column(name = "phone")
	private String phone;

	@Enumerated(EnumType.STRING)
	@Column(name = "type")
	private AuthenticationType type;

	public Ban(String phone, AuthenticationType type) {
		this.phone = phone;
		this.type = type;
	}

}
