package com.zoopi.domain.member.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "member_authorities", uniqueConstraints = {
	@UniqueConstraint(columnNames = {"member_id", "member_authority_type"})
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberAuthority {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	@Enumerated(EnumType.STRING)
	@Column(name = "member_authority_type")
	private AuthorityType type;

	@Builder
	public MemberAuthority(Member member, AuthorityType type) {
		this.member = member;
		this.type = type;
	}

}
