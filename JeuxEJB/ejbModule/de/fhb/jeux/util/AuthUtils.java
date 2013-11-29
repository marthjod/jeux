package de.fhb.jeux.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.jboss.logging.Logger;

public class AuthUtils {

	protected static Logger logger = Logger.getLogger(AuthUtils.class);

	public static final String generateAuthToken(String str, String salt) {
		// should not become null (NPE)
		String authToken = "";
		// do not just hash the salt alone
		if (str != null && salt != null && salt.length() > 0
				&& str.length() > 0) {
			// sufficient?
			// let's hash at least twice
			authToken = DigestUtils.sha1Hex(DigestUtils.sha1Hex(".::" + str
					+ ":" + salt + "::."));
		}
		return authToken;
	}

	public static final boolean checkAuthTokens(String str, String salt,
			String authToken) {
		boolean checkOk = false;

		// avoid nullpointer
		if (str != null && authToken != null && str.length() > 0
				&& authToken.length() > 0) {

			// if the authtoken generated from the cookies
			// yields the same result as the authtoken the user passed,
			// we assume it's ok
			// logger.debug("Comparing " + authToken + " with "
			// + generateAuthToken(str, salt));
			if (generateAuthToken(str, salt).equals(authToken)) {
				checkOk = true;
			}

		}
		return checkOk;
	}

}
