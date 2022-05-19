package com.zoopi.domain.member.repository.oauth2;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zoopi.domain.member.entity.oauth2.NaverAccount;

public interface NaverAccountRepository extends JpaRepository<NaverAccount, Long> {

}
