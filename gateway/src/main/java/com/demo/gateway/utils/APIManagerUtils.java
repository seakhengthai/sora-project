package com.demo.gateway.utils;

import com.demo.gateway.config.properties.AppProperties;
import com.demo.gateway.exception.AppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.UUID;

@Service
@Slf4j
public class APIManagerUtils {

    @Autowired
    private AppProperties appProperties;

    public String genApiKey() {
        return UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }

    public String genClientId() {
        return UUID.randomUUID()
                .toString()
                .replace("-", "")
                .toUpperCase();
    }

    public String getAPIKey() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public String genSecret() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public String encryptedSecret(String clientId,String clientSecret) {
        try {
            SecretKey secretKey = SecurityUtils
                    .generateEncryptionString(clientSecret
                            , appProperties.getSecurity().getSalt());
            return SecurityUtils.convertSecretKeyToString(secretKey);
        } catch (NoSuchAlgorithmException e) {
            log.error("NoSuchAlgorithmException", e);
        } catch (InvalidKeySpecException e) {
            log.error("InvalidKeySpecException", e);
        }
        throw new AppException();
    }

    public String compareEncryption(String clientId,String inputString) {
        try {
            SecretKey secretKey = SecurityUtils.generateEncryptionString(inputString
                    , appProperties.getSecurity().getSalt());
            return SecurityUtils.convertSecretKeyToString(secretKey);
        } catch (NoSuchAlgorithmException e) {
            log.error("NoSuchAlgorithmException", e);
        } catch (InvalidKeySpecException e) {
            log.error("InvalidKeySpecException", e);
        }
        throw new AppException();
    }


    public String encryptedSecretV2(String clientId, String clientSecret) {
        try {
            return SecurityUtils.encrypt(clientId, appProperties.getSecurity().getSalt(), clientSecret);
        } catch (Exception e) {
            log.error("Error", e);
        }
        throw new AppException();
    }
    public String compareEncryptionV2(
            String clientId, String secret) {
        return this.encryptedSecretV2(clientId, secret);
    }

}
