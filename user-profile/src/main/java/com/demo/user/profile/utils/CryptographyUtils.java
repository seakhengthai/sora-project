package com.demo.user.profile.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Random;

public class CryptographyUtils {
    private static final Logger log = LoggerFactory.getLogger(CryptographyUtils.class);
    private static final int RANDOM_SIZE = 16;
    private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public CryptographyUtils() {
    }

    public static String encodeBase64(byte[] input) {
        return Base64.getEncoder().encodeToString(input);
    }

    public static byte[] decodeBase64(String input) {
        return Base64.getDecoder().decode(input);
    }

    public static String randomStr() {
        int n = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".length();
        StringBuilder result = new StringBuilder();
        Random r = new Random();

        for(int i = 0; i < 16; ++i) {
            result.append("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".charAt(r.nextInt(n)));
        }

        return result.toString();
    }

    public static byte[] salt(String randomText) {
        return decodeBase64(randomText);
    }

    public static byte[] hashPBKDF2(String password, byte[] salt) {
        try {
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 256);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] hash = factory.generateSecret(spec).getEncoded();
            return hash;
        } catch (Exception var5) {
            log.error("hashPBKDF2", var5);
            return null;
        }
    }

    public static String generatePwd(String rawPassword, byte[] salt) {
        return encodeBase64(hashPBKDF2(rawPassword, salt));
    }

}
