package com.bida.password.storage.validation;

import com.bida.password.storage.exception.BadRequestException;
import org.springframework.stereotype.Component;

@Component
public class PasswordValidator {

    private static final String KEYBOARD_PATTERN_NUMBERS = "01234567890";
    private static final String KEYBOARD_PATTERN_LETTERS = "qwertyuiopasdfghjklzxcvbnm";
    private static final int MIN_UPPER_CASE_LETTERS_AMOUNT = 2;
    private static final int MIN_LOWER_CASE_LETTERS_AMOUNT = 2;
    private static final int MIN_PASSWORD_LENGTH = 10;
    private static final int MAX_PASSWORD_LENGTH = 30;

    public void validatePassword(String password) {
        if (password.length() < MIN_PASSWORD_LENGTH) {
            throw new BadRequestException("Password is too small, min: " + MIN_PASSWORD_LENGTH);
        }
        if (password.length() > MAX_PASSWORD_LENGTH) {
            throw new BadRequestException("Password is too big, max: " + MAX_PASSWORD_LENGTH);
        }

        char[] passwordArray = password.toCharArray();
        int upperLetterCount = 0;
        int lowerLettersCount = 0;
        for (int i = 0; i < passwordArray.length; i++) {
            if (Character.isUpperCase(passwordArray[i])) {
                upperLetterCount++;
            } else {
                lowerLettersCount++;
            }
        }
        if (upperLetterCount < MIN_UPPER_CASE_LETTERS_AMOUNT) {
            throw new BadRequestException("Password has to contain at last " + MIN_UPPER_CASE_LETTERS_AMOUNT + " letters in upper case.");
        }
        if (lowerLettersCount < MIN_LOWER_CASE_LETTERS_AMOUNT) {
            throw new BadRequestException("Password has to contain at last " + MIN_LOWER_CASE_LETTERS_AMOUNT + " letters in lower case.");
        }



        for (int i = 0; i < KEYBOARD_PATTERN_LETTERS.length() - 3; i++) {
            if (password.contains(KEYBOARD_PATTERN_LETTERS.substring(i, i + 4))) {
                throw new BadRequestException("Password can't contain keyboard sequence more then 3.");
            }
        }
        for (int i = 0; i < KEYBOARD_PATTERN_NUMBERS.length() - 3; i++) {
            if (password.contains(KEYBOARD_PATTERN_NUMBERS.substring(i, i + 4))) {
                throw new BadRequestException("Password can't contain keyboard sequence more then 3.");
            }
        }
    }
}
