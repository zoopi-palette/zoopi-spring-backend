package com.zoopi.controller.member.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FindPasswordRequest {

	@NotBlank
	@Size(max = 50)
	@ApiModelProperty(value = "인증 키", required = true, example = "2a6ec3d7-7147-4cdc-a6c5-fc5d937cfe76")
	private String authenticationKey;

	@NotNull
	@Pattern(regexp = "^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$")
	@ApiModelProperty(value = "휴대폰 번호", required = true, example = "01012345678")
	private String phone;

	@NotBlank
	@Size(max = 30)
	@ApiModelProperty(value = "아이디", required = true, example = "zoopi123")
	private String username;

	@NotNull
	@Pattern(regexp = "^.*(?=^.{10,20}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[`~!@#$%^&*()+=]).*$")
	@ApiModelProperty(value = "비밀번호", required = true, example = "1234asdf!@#$")
	private String password;

	@NotNull
	@Pattern(regexp = "^.*(?=^.{10,20}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[`~!@#$%^&*()+=]).*$")
	@ApiModelProperty(value = "비밀번호 확인", required = true, example = "1234asdf!@#$")
	private String passwordCheck;

}
