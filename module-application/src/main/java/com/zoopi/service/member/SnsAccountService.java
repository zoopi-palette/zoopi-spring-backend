package com.zoopi.service.member;

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

    public Optional<SnsAccount> getWithMember(SnsAccountPrimaryKey primaryKey) {
        return snsAccountRepository.findWithMemberById(primaryKey);
    }

    // TODO: 이미 연결된 소셜계정이 있는 경우 -> 예외 처리 + 테스트 코드 작성
    //  서로 다른 소설 계정을 여러 개 연동하는 케이스 -> 테스트 코드 작성
    @Transactional
    public SnsAccount connect(Member member, SnsAccountPrimaryKey primaryKey) {
        return snsAccountRepository.save(new SnsAccount(primaryKey, member));
    }

}
