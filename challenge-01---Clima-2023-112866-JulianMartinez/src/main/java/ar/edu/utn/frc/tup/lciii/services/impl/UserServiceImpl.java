package ar.edu.utn.frc.tup.lciii.services.impl;

import ar.edu.utn.frc.tup.lciii.dtos.UserRequestDto;
import ar.edu.utn.frc.tup.lciii.dtos.UserResponseDto;
import ar.edu.utn.frc.tup.lciii.models.User;
import ar.edu.utn.frc.tup.lciii.repositories.jpa.UserJpaRepository;
import ar.edu.utn.frc.tup.lciii.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserJpaRepository userJpaRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public UserResponseDto saveUser(UserRequestDto userRequestDto) {
        UUID secret = UUID.randomUUID();
        User userCreated = new User();
        userCreated.setEmail(userRequestDto.getEmail());
        userCreated.setId_secret(secret.toString());
        userCreated.setTemperature_unit(userRequestDto.getTemperature_unit());
        User userSaved = userJpaRepository.save(userCreated);
        return modelMapper.map(userSaved, UserResponseDto.class);
    }

}

