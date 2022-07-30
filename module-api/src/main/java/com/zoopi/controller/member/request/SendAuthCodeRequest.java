package com.zoopi.controller.member.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.zoopi.domain.phoneauthentication.entity.PhoneAuthenticationType;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SendAuthCodeRequest {

	@NotNull
	@ApiModelProperty(value = "인증 타입", required = true, example = "SIGN_UP")
	private PhoneAuthenticationType type;

	@NotNull
	@Pattern(regexp = "^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$")
	@ApiModelProperty(value = "휴대폰 번호", required = true, example = "01012341234")
	private String phone;

}
