package com.bida.password.storage.service;

import com.bida.password.storage.domain.Message;
import com.bida.password.storage.domain.MyUserDetails;
import com.bida.password.storage.domain.User;
import com.bida.password.storage.domain.dto.RegistrationUserDTO;
import com.bida.password.storage.exception.BadRequestException;
import com.bida.password.storage.exception.NotFoundException;
import com.bida.password.storage.repository.UserRepository;
import com.bida.password.storage.validation.EmailValidator;
import com.bida.password.storage.validation.PasswordValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EmailValidator emailValidator;
    @Autowired
    private PasswordValidator passwordValidator;


    public void registration(RegistrationUserDTO userDTO) {
        emailValidator.validateEmail(userDTO.getEmail());
        passwordValidator.validatePassword(userDTO.getPassword());

        if (userRepository.findUserByEmail(userDTO.getEmail()) != null) {
            throw new BadRequestException("User with email: " + userDTO.getEmail() + " is already existed.");
        }

    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return new MyUserDetails(findUserByEmail(s));
    }

    public User findUserByEmail(String email){
        return Optional.of(userRepository.findUserByEmail(email))
                .orElseThrow(() -> new NotFoundException("User with email: " + email + " wasn't found."));
    }
}
