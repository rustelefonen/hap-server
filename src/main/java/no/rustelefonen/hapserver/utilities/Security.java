package no.rustelefonen.hapserver.utilities;

import org.apache.commons.codec.digest.DigestUtils;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Created by Simen Fonnes on 13.03.2016.
 */
public class Security {

    public static String generateSalt(){
        SecureRandom random = new SecureRandom();
        int bitsPerChar = 5;
        int twoPowerOfBits = 32; // 2^5
        int n = 26;
        return new BigInteger(n * bitsPerChar, random).toString(twoPowerOfBits);
    }

    /**
    * Generates an sha256Hex hash, combining the provided clearText and salt.
    * */
    public static String generateHash(String clearText, String salt) {
        return DigestUtils.sha256Hex(clearText + salt);
    }
}
