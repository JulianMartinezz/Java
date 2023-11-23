package ar.edu.utn.frc.tup.lciii.controllers;

import ar.edu.utn.frc.tup.lciii.dtos.UserRequestDto;
import ar.edu.utn.frc.tup.lciii.dtos.UserResponseDto;
import ar.edu.utn.frc.tup.lciii.services.UserService;
import ar.edu.utn.frc.tup.lciii.services.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@RestController
@RequestMapping("/Users")
public class UserController {

    @Autowired
    private UserService _userService;

    @PostMapping("")
    public ResponseEntity<UserResponseDto> PostUser(@RequestBody UserRequestDto userRequestDto){
        UserResponseDto userCreated = _userService.saveUser(userRequestDto);
        if(Objects.isNull(userCreated)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The request has an error");
        } else {
            return ResponseEntity.ok(userCreated);
        }
    }
}
