package com.bida.password.storage.service;

import org.springframework.stereotype.Service;

@Service
public class AwsKmsService {
    public String encrypt(String plainText) {
        return plainText;
    }

    public String decrypt(String cipheredText) {
        return cipheredText;
    }
}
