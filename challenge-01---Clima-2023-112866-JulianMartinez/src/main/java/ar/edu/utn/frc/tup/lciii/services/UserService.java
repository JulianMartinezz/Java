package ar.edu.utn.frc.tup.lciii.services;

import ar.edu.utn.frc.tup.lciii.dtos.UserRequestDto;
import ar.edu.utn.frc.tup.lciii.dtos.UserResponseDto;
import ar.edu.utn.frc.tup.lciii.models.User;
import org.springframework.stereotype.Service;

import java.util.Optional;


public interface UserService {

    UserResponseDto saveUser(UserRequestDto userRequestDto);



}
