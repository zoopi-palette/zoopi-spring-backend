package com.zoopi.domain.member.repository.oauth2;

import com.zoopi.domain.member.entity.oauth2.SnsAccountPrimaryKey;
import org.springframework.data.jpa.repository.JpaRepository;

import com.zoopi.domain.member.entity.oauth2.SnsAccount;

public interface SnsAccountRepository extends JpaRepository<SnsAccount, SnsAccountPrimaryKey> {

}
