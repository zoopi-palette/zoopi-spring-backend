package com.zoopi.controller.member.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SignupRequest {

	@NotBlank
	@Size(max = 30)
	private String username;

	@NotNull
	@Pattern(regexp = "^.*(?=^.{10,20}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[`~!@#$%^&*()+=]).*$")
	private String password;

	@NotNull
	@Pattern(regexp = "^.*(?=^.{10,20}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[`~!@#$%^&*()+=]).*$")
	private String passwordCheck;

	@NotNull
	@Pattern(regexp = "^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$")
	private String phone;

	@NotBlank
	@Size(max = 36)
	private String authenticationKey;

}
