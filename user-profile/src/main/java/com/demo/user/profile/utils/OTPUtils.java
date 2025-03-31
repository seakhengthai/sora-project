package com.demo.user.profile.utils;

public class OTPUtils {

    public static String decrypt(String encryptedValue, String privateKey) {
        return RSAUtils.decryptByKeyAsText(encryptedValue, privateKey);
    }
}
