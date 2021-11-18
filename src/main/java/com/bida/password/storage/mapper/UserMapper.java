package com.bida.password.storage.mapper;

import com.bida.password.storage.domain.User;
import com.bida.password.storage.domain.dto.UserRegistrationDTO;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final ModelMapper modelMapper;

    public UserMapper() {
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    public User dtoToEntity(UserRegistrationDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }
}
