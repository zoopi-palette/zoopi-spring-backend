package com.zoopi.domain.member.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// TODO
//  - 프로필 이미지

@Entity
@Getter
@Table(name = "members")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_id")
	private Long id;

	@Column(name = "member_username", unique = true)
	private String username;

	@Column(name = "member_password")
	private String password;

	@Column(name = "member_name")
	private String name;

	@Column(name = "member_phone", unique = true)
	private String phone;

	@CreatedDate
	@Column(name = "member_create_date")
	private LocalDateTime createdDate;

	@Enumerated(EnumType.STRING)
	@Column(name = "member_join_type")
	private JoinType joinType;

	@Builder
	public Member(String username, String password, String name, String phone, JoinType joinType) {
		this.username = username;
		this.password = password;
		this.name = name;
		this.phone = phone;
		this.joinType = joinType;
	}

}
