package com.pks.demo.util;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

public class RandomToken {
	private static final SecureRandom secureRandom = new SecureRandom();

	public String getRandomToken(String username) {

		byte[] randomBytes = new byte[24];
		secureRandom.nextBytes(randomBytes);

		LocalDateTime dateTime = LocalDateTime.now();
		String date = dateTime.toString();
		String token = (username+"@" + date + "@" + randomBytes.toString());

		Base64.Encoder encoder = Base64.getEncoder();

		return encoder.encodeToString(token.getBytes());
	}

}
