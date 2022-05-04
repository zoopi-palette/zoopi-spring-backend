package com.zoopi.util;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class AuthenticationCodeUtilsTest {

	@Test
	void generateRandomAuthenticationCode_length_success() throws Exception {
	    // given
		final int length = 6;

	    // when
		final String code = AuthenticationCodeUtils.generateRandomAuthenticationCode(6);

		// then
		assertThat(code).hasSize(6);
		System.out.println("code = " + code);
	}
}