package com.zoopi.domain.member.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zoopi.domain.member.entity.Member;
import com.zoopi.domain.member.entity.MemberAuthority;

public interface MemberAuthorityRepository extends JpaRepository<MemberAuthority, Long> {

	List<MemberAuthority> findAllByMember(Member member);

}
