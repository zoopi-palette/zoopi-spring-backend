package com.zoopi.util;

import java.util.Random;

public class AuthenticationCodeUtils {

	public static String generateRandomAuthenticationCode(int length) {
		final Random rand = new Random();
		final StringBuilder numStr = new StringBuilder();

		for (int i = 0; i < length; i++) {
			final String randomDigit = Integer.toString(rand.nextInt(10));
			numStr.append(randomDigit);
		}

		return numStr.toString();
	}
}
