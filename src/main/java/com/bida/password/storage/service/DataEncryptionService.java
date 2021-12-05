package com.bida.password.storage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
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
        var salt = new byte[128];
        var b64Encoder = Base64.getEncoder();
        new SecureRandom().nextBytes(salt);
        try {
            var iv = aesCgmCipher.generateIv(16);
            var key = aesCgmCipher.getKeyFromPassword(password, salt);
            var cipheredTextB64 = b64Encoder.encodeToString(aesCgmCipher.encrypt(input, key, iv));
            return new EncryptionResult(cipheredTextB64 + "::" + b64Encoder.encodeToString(iv.getIV()), b64Encoder.encodeToString(awsKmsService.encrypt(key.getEncoded())));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String decrypt(String cipherText, String dek) {
        var b64Decoder = Base64.getDecoder();
        var cipherTextSplit = cipherText.split("::");
        var iv = b64Decoder.decode(cipherTextSplit[1]);
        var key = awsKmsService.decrypt(b64Decoder.decode(dek));
        var ct = b64Decoder.decode(cipherTextSplit[0]);
        try {
            return aesCgmCipher.decrypt(ct, aesCgmCipher.generateKey(key), aesCgmCipher.generateIv(iv));
        } catch (Exception e) {
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


