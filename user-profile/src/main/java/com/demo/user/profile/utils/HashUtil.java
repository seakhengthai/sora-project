package com.demo.user.profile.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.zip.CRC32C;

public class HashUtil {
    public static final String ALGORITHM = "HmacSHA256";

    public HashUtil() {
    }

    public static String encodeHMAC(String data, String key) throws NoSuchAlgorithmException, InvalidKeyException {
        return encodeWithKey(data, key, "HmacSHA256");
    }

    public static String encodeWithKey(String data, String key, String algorithm) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac sha256HMAC = Mac.getInstance(algorithm);
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), algorithm);
        sha256HMAC.init(secretKey);
        return Hex.encodeHexString(sha256HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8)));
    }

    public static String encode(final String data) throws Exception {
        try {
            return new String(Base64.getEncoder().encode(MessageDigest.getInstance("SHA-256").digest(data.getBytes(StandardCharsets.UTF_8))));
        } catch (NoSuchAlgorithmException var2) {
            throw new Exception("Unable to encode!", var2);
        }
    }
}
