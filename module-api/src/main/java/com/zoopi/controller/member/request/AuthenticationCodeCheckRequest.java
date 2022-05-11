package com.zoopi.controller.member.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthenticationCodeCheckRequest {

	@NotBlank
	@ApiModelProperty(value = "인증 키", required = true, example = "2a6ec3d7-7147-4cdc-a6c5-fc5d937cfe76")
	private String authenticationKey;

	@Pattern(regexp = "^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$")
	@ApiModelProperty(value = "휴대폰 번호", required = true, example = "01012345678")
	private String phone;

	@NotBlank
	@Length(min = 6, max = 6)
	@ApiModelProperty(value = "인증 코드", required = true, example = "012345")
	private String authenticationCode;

}
