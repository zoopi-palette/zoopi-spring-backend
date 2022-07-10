package com.zoopi.controller.member.request;

import javax.validation.constraints.NotBlank;
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
public class SigninRequest {

	@NotBlank
	@Size(max = 30)
	private String username;

	@Size(max = 30)
	@NotBlank
	private String password;

}
