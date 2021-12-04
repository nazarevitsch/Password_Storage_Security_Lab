package com.bida.password.storage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

@Service
public class DataEncryptionService {
    private final AesCgmCipher aesCgmCipher;
    private final AwsKmsService awsKmsService;
    public DataEncryptionService(@Autowired AesCgmCipher aesCgmCipher, @Autowired AwsKmsService awsKmsService) {
        this.aesCgmCipher = aesCgmCipher;
        this.awsKmsService = awsKmsService;
    }

    public EncryptionResult encrypt(String input, String password) {
        var iv = aesCgmCipher.generateIv(16);
        var salt = new byte[128];
        new SecureRandom().nextBytes(salt);
        try {
            var key = aesCgmCipher.getKeyFromPassword(password, new String(salt));
            var cipheredText = aesCgmCipher.encrypt(input, key, iv);
            var keyB64 = Base64.getEncoder().encodeToString(key.getEncoded());
            return new EncryptionResult(cipheredText + "::" + new String(iv.getIV()), awsKmsService.encrypt(keyB64));
        } catch (NoSuchPaddingException | InvalidKeySpecException |
                IllegalBlockSizeException | BadPaddingException |
                InvalidKeyException | InvalidAlgorithmParameterException |
                NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static class EncryptionResult {
        public String cipheredText;
        public String key;
        public EncryptionResult (String cipheredText, String key) {
            this.cipheredText = cipheredText;
            this.key = key;
        }
    }
}


