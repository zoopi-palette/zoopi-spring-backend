package com.zoopi.domain.authentication.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "bans")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Ban {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ban_id")
	private Long id;

	@Column(name = "ban_phone")
	private String phone;

	@Column(name = "ban_date")
	private LocalDateTime bannedDate;

	public Ban(String phone, LocalDateTime bannedDate) {
		this.phone = phone;
		this.bannedDate = bannedDate;
	}

}
