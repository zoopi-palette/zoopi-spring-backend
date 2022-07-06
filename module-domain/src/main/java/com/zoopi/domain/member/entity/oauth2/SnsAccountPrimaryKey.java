package com.zoopi.domain.member.entity.oauth2;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SnsAccountPrimaryKey implements Serializable {

    private static final long serialVersionUID = 2868210232929931052L;

    @Enumerated(EnumType.STRING)
    @Column(name = "sns_provider")
    private SnsProvider provider;

    @Column(name = "sns_account_id")
    private String id;

    @Override
    public int hashCode() {
        return Objects.hash(provider, id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null || getClass() != obj.getClass()) {
            return false;
        } else {
            final SnsAccountPrimaryKey primaryKey = (SnsAccountPrimaryKey) obj;
            return Objects.equals(provider, primaryKey.getProvider()) && Objects.equals(id, primaryKey.getId());
        }
    }
}
