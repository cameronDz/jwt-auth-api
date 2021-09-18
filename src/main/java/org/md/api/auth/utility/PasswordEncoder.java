package org.md.api.auth.utility;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class PasswordEncoder {

	private static final String CHAR_SET = "UTF-8";
	private static final String HASH_ALGORITHM = "SHA-256";
	private static final int ITERATION_COUNT = 5;
	
	public static String encode(String password, String saltKey) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String encodedPassword = null;
        byte[] salt = base64ToByte(saltKey);

        MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
        digest.reset();
        digest.update(salt);

        byte[] btPass = digest.digest(password.getBytes(CHAR_SET));
        for (int i = 0; i < ITERATION_COUNT; i++) {
            digest.reset();
            btPass = digest.digest(btPass);
        }

        encodedPassword = byteToBase64(btPass);
        return encodedPassword;
	}
	
    private static byte[] base64ToByte(String str) {
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] returnbyteArray = decoder.decode(str);
        return returnbyteArray;
    }

    private static String byteToBase64(byte[] bt) {
        Base64.Encoder encoder = Base64.getEncoder();
        String returnString = encoder.encodeToString(bt);
        return returnString;
    }
}
