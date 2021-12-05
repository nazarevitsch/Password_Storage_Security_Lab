package com.bida.password.storage.domain.dto;

import lombok.Data;

@Data
public class UserRegistrationDTO {
    private String email;
    private String password;
    private String credit_card;
}
