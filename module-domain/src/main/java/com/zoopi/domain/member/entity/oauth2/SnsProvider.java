package com.zoopi.domain.member.entity.oauth2;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SnsProvider {

    NAVER("NAVER", "response", "id", "mobile", "email");

    private final String provider;
    private final String response;
    private final String id;
    private final String phone;
    private final String email;

}
