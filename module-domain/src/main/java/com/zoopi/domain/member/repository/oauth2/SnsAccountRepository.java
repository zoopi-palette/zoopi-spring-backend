package com.zoopi.domain.member.repository.oauth2;

import java.util.Optional;

import com.zoopi.domain.member.entity.oauth2.SnsAccountPrimaryKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.zoopi.domain.member.entity.oauth2.SnsAccount;

public interface SnsAccountRepository extends JpaRepository<SnsAccount, SnsAccountPrimaryKey> {

	// TODO: fetch join 테스트 필요
	@Query("select sa from SnsAccount sa join fetch sa.member where sa.id = :id")
	Optional<SnsAccount> findWithMemberById(@Param("id") SnsAccountPrimaryKey id);

}
