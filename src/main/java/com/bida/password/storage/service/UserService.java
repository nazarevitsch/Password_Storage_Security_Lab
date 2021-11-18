package com.bida.password.storage.service;

import com.bida.password.storage.domain.MyUserDetails;
import com.bida.password.storage.domain.Token;
import com.bida.password.storage.domain.User;
import com.bida.password.storage.domain.dto.UserLoginDTO;
import com.bida.password.storage.domain.dto.UserRegistrationDTO;
import com.bida.password.storage.exception.BadRequestException;
import com.bida.password.storage.exception.NotFoundException;
import com.bida.password.storage.mapper.UserMapper;
import com.bida.password.storage.repository.UserRepository;
import com.bida.password.storage.validation.EmailValidator;
import com.bida.password.storage.validation.PasswordValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JWTUtilService jwtUtilService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public void registration(UserRegistrationDTO userDTO) {
        emailValidator.validateEmail(userDTO.getEmail());
        passwordValidator.validatePassword(userDTO.getPassword());

        if (userRepository.findUserByEmail(userDTO.getEmail()) != null) {
            throw new BadRequestException("User with email: " + userDTO.getEmail() + " is already existed.");
        }
        User user = userMapper.dtoToEntity(userDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public Token login(UserLoginDTO userDTO) {
        UserDetails userDetails = loadUserByUsername(userDTO.getEmail());
        return new Token("Bearer " + jwtUtilService.generateTOKEN(userDetails));
    }

    public String loginGenerateRefreshToken(UserLoginDTO userLogin){
        UserDetails userDetails = loadUserByUsername(userLogin.getEmail());
        return jwtUtilService.generateRefreshToken(userDetails);
    }

    public void authenticate(UserLoginDTO userLogin) throws BadCredentialsException  {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLogin.getEmail(), userLogin.getPassword()));
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
