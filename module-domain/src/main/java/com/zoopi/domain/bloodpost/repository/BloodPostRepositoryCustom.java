package com.zoopi.domain.bloodpost.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zoopi.domain.bloodpost.entity.BloodPost;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;

import static com.zoopi.domain.bloodpost.entity.QBloodPost.bloodPost;
import static org.springframework.util.StringUtils.hasText;

@Repository
@RequiredArgsConstructor
public class BloodPostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public List<BloodPost> searchOrderByPullDateDesc(BloodPost.Status status, String title) {
        return queryFactory.selectFrom(bloodPost)
                .where(
                        eqStatus(status),
                        containsTitle(title)
                )
                .orderBy(bloodPost.pullDate.desc())
                .fetch();
    }

    private BooleanExpression eqStatus(BloodPost.Status status) {
        return !Objects.isNull(status) ? bloodPost.status.eq(status) : null;
    }

    private BooleanExpression containsTitle(String title) {
        return hasText(title) ? bloodPost.title.contains(title) : null;
    }

}
