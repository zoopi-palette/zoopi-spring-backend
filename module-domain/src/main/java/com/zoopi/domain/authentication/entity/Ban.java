package com.zoopi.domain.authentication.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
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

	public Ban(String phone) {
		this.phone = phone;
	}

}
