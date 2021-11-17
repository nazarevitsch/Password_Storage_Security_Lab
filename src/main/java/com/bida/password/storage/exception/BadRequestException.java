package com.bida.password.storage.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BadRequestException extends RuntimeException {

    private final String message;

    public String getErrorCode() {
        return "bad_request";
    }
}
