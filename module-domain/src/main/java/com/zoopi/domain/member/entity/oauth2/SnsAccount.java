package com.zoopi.domain.member.entity.oauth2;

import javax.persistence.*;

import com.zoopi.domain.BaseEntity;
import com.zoopi.domain.member.entity.Member;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "sns_acounts")
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SnsAccount extends BaseEntity {

	@EmbeddedId
	private SnsAccountPrimaryKey id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

}
