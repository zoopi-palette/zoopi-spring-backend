package com.zoopi.domain.member.repository.oauth2;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zoopi.domain.member.entity.oauth2.KakaoAccount;

public interface KakaoAccountRepository extends JpaRepository<KakaoAccount, Long> {

}
