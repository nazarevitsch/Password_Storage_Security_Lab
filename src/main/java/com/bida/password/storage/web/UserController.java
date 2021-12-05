package com.bida.password.storage.web;

import com.bida.password.storage.domain.Message;
import com.bida.password.storage.domain.UserInfo;
import com.bida.password.storage.domain.dto.UserLoginDTO;
import com.bida.password.storage.domain.dto.UserRegistrationDTO;
import com.bida.password.storage.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/registration")
    public ResponseEntity<Message> registration(@RequestBody UserRegistrationDTO userDTO){
        userService.registration(userDTO);
        return new ResponseEntity<>(new Message("User was successfully registered."), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDTO userDTO, HttpServletResponse response) {
        try {
            userService.authenticate(userDTO);
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(new Message("There isn't user with such password or email!"), HttpStatus.UNAUTHORIZED);
        }
        Cookie cookie = new Cookie("refreshToken", userService.loginGenerateRefreshToken(userDTO));
        cookie.setPath("/");
        response.addCookie(cookie);
        return new ResponseEntity<>(userService.login(userDTO), HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<UserInfo> me () {
        var user = userService.findUserByEmail("sash3a2@gmail.com"); // HARDCODE
        var creditCard = userService.decryptValueByDek(user.getCredit_card(), user.getDek());
        return new ResponseEntity<>(new UserInfo(creditCard, user.getEmail()), HttpStatus.OK);
    }

}
