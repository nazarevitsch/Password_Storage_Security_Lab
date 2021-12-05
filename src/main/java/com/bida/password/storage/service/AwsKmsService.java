package com.bida.password.storage.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.encryptionsdk.AwsCrypto;
import com.amazonaws.encryptionsdk.CommitmentPolicy;
import com.amazonaws.encryptionsdk.CryptoResult;
import com.amazonaws.encryptionsdk.kms.KmsMasterKey;
import com.amazonaws.encryptionsdk.kms.KmsMasterKeyProvider;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.Map;

@Service
public class AwsKmsService {
    private final String KEY_ARN = "arn:aws:kms:us-east-2:125481175000:key/1ac8e609-7827-42ce-9077-bc15af8e7358";
    private final AwsCrypto crypto;
    private final KmsMasterKeyProvider keyProvider;
    public AwsKmsService () {
        var credentials = new BasicAWSCredentials("", "");
        crypto = AwsCrypto
                .builder()
                .withCommitmentPolicy(CommitmentPolicy.RequireEncryptRequireDecrypt)
                .build();
        keyProvider = KmsMasterKeyProvider
                .builder()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .buildStrict(KEY_ARN);
    }

    /**
     *
     * @param plainText {String}
     * @return cipherText {String}
     */
    public String encrypt(String plainText) {
        final Map<String, String> encryptionContext = Collections.singletonMap("KeyAppContext", "KeyAppContextValue");
        final CryptoResult<byte[], KmsMasterKey> encryptResult = crypto.encryptData(keyProvider, plainText.getBytes(StandardCharsets.UTF_8), encryptionContext);
        return new String(encryptResult.getResult());
    }

    public byte[] encrypt(byte[] plainText) {
        final Map<String, String> encryptionContext = Collections.singletonMap("KeyAppContext", "KeyAppContextValue");
        final CryptoResult<byte[], KmsMasterKey> encryptResult = crypto.encryptData(keyProvider, plainText, encryptionContext);
        return encryptResult.getResult();
    }

    public String decrypt(String cipheredText) {
        final CryptoResult<byte[], KmsMasterKey> decryptResult = crypto.decryptData(keyProvider, cipheredText.getBytes(StandardCharsets.UTF_8));
        return new String(decryptResult.getResult());
    }

    public byte[] decrypt(byte[] cipheredText) {
        final CryptoResult<byte[], KmsMasterKey> decryptResult = crypto.decryptData(keyProvider, cipheredText);
        return decryptResult.getResult();
    }
}
