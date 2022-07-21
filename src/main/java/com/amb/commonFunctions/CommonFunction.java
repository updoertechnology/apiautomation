package com.amb.commonFunctions;

import java.security.SecureRandom;

public class CommonFunction {

	/**
	 * This method will return Random number as per user desirable length.
	 * @param numberLength
	 * @return
	 */
	public static int getRandomNumber(int numberLength) {
		StringBuilder sb = new StringBuilder(numberLength);
		final String strg = "0123456789";
		SecureRandom rndm = new SecureRandom();
		for (int i = 0; i < numberLength; i++)
			sb.append(strg.charAt(rndm.nextInt(strg.length())));
		return Integer.parseInt(sb.toString());
	}
}
