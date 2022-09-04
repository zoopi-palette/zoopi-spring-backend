package com.zoopi.client.member.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.zoopi.domain.member.entity.oauth2.SnsProvider;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SigninAppRequest {

	@NotNull
	@ApiModelProperty(value = "OAuth2 플랫폼 제공자", required = true)
	private SnsProvider provider;

	@NotBlank
	@Size(max = 50)
	@ApiModelProperty(value = "인증 코드", required = true)
	private String code;

	@NotBlank
	@Size(max = 50)
	@ApiModelProperty(value = "csrf 방지 상태 토큰", required = true)
	private String status;

}
