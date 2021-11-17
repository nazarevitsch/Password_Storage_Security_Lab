package com.bida.password.storage.validation;

import com.bida.password.storage.exception.BadRequestException;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class EmailValidator {
    private static final Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");

    public void validateEmail(String email) {
        if(!emailPattern.matcher(email).matches()) {
            throw new BadRequestException("Email: " + email + " is not valid");
        }
    }
}
