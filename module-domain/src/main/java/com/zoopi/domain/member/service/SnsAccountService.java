package com.zoopi.domain.member.service;

import com.zoopi.domain.member.entity.Member;
import com.zoopi.domain.member.entity.oauth2.SnsAccount;
import com.zoopi.domain.member.entity.oauth2.SnsAccountPrimaryKey;
import com.zoopi.domain.member.repository.oauth2.SnsAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SnsAccountService {

    private final SnsAccountRepository snsAccountRepository;

    public Optional<SnsAccount> get(SnsAccountPrimaryKey primaryKey) {
        return snsAccountRepository.findById(primaryKey);
    }

    @Transactional
    public SnsAccount connect(Member member, SnsAccountPrimaryKey primaryKey) {
        return snsAccountRepository.save(new SnsAccount(primaryKey, member));
    }

}
