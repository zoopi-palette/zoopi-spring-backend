package com.zoopi.domain.member.entity;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.zoopi.domain.BaseEntity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// TODO
//  - 프로필 이미지

@Entity
@Getter
@Table(name = "members")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_id")
	private Long id;

	@Column(name = "username", unique = true)
	private String username;

	@Column(name = "password")
	private String password;

	@Column(name = "name")
	private String name;

	@Column(name = "email", unique = true)
	private String email;

	@Column(name = "phone", unique = true)
	private String phone;

	@Column(name = "authorities")
	private String authorities;

	@Builder
	public Member(String username, String password, String name, String phone, String email) {
		this.username = username;
		this.password = password;
		this.name = name;
		this.phone = phone;
		this.email = email;
		this.authorities = MemberAuthorityTypes.ROLE_USER.name();
	}

	public boolean addAuthority(MemberAuthorityTypes authority) {
		if (this.authorities.isEmpty()) {
			return false;
		}

		final Set<MemberAuthorityTypes> authorityTypes = Arrays.stream(this.authorities.split(","))
			.map(MemberAuthorityTypes::valueOf)
			.collect(Collectors.toSet());
		for (MemberAuthorityTypes authorityType : authorityTypes) {
			if (authorityType.equals(authority)) {
				return false;
			}
		}

		authorityTypes.add(authority);
		this.authorities = authorityTypes.stream()
			.map(MemberAuthorityTypes::name)
			.collect(Collectors.joining(","));
		return true;
	}

	public void changePassword(String password) {
		this.password = password;
	}

}
