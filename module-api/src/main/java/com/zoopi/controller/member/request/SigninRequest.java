package com.zoopi.controller.member.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SigninRequest {

	@NotBlank
	@Size(max = 30)
	@ApiModelProperty(value = "아이디", required = true, example = "zoopi123")
	private String username;

	@Size(max = 30)
	@NotBlank
	@ApiModelProperty(value = "비밀번호", required = true, example = "1234asdf!@#$")
	private String password;

}
