package com.bida.password.storage.web;

import com.bida.password.storage.domain.Message;
import com.bida.password.storage.domain.dto.RegistrationUserDTO;
import com.bida.password.storage.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/registration")
    public ResponseEntity<Message> registration(@RequestBody RegistrationUserDTO userDTO){
        userService.registration(userDTO);
        return new ResponseEntity<>(new Message("User was successfully registered."), HttpStatus.OK);
    }

}
