package com.bida.password.storage.web;

import com.bida.password.storage.domain.Message;
import com.bida.password.storage.domain.dto.RegistrationUserDTO;
import com.bida.password.storage.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<Message> registration(@RequestBody RegistrationUserDTO userDTO){
        return new ResponseEntity<>(userService.registration(userDTO), HttpStatus.OK);
    }

}
