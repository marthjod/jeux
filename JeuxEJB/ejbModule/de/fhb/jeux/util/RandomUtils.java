package de.fhb.jeux.util;

import java.util.Random;

public class RandomUtils {

	// taken from
	// http://stackoverflow.com/questions/2863852/how-to-generate-a-random-string-in-java
	public static String randString(String characters, int length) {
		Random rng = new Random();
		char[] text = new char[length];
		for (int i = 0; i < length; i++) {
			text[i] = characters.charAt(rng.nextInt(characters.length()));
		}
		return new String(text);
	}

	public static String randEmailAddress(int lenName, int lenDomain, String tld) {
		StringBuffer sb = new StringBuffer();
		sb.append(randString("abcdefghijklmnopqrstuvwxyz0123456789_.", lenName));
		sb.append("@");
		sb.append(randString("abcdefghijklmnopqrstuvwxyz0123456789", lenDomain));
		sb.append(tld);

		return sb.toString();
	}

	public static String randAlpha(int length) {
		return randString(
				"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz", length);
	}

	public static String weakPassword() {
		return randString(
				"abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789",
				16);
	}

	public static int randInt(int min, int max) {
		Random rand = new Random();
		int range = max - min + 1;
		return rand.nextInt(range) + min;
	}
}
