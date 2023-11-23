package ar.edu.utn.frc.tup.lciii.services.impl;

import ar.edu.utn.frc.tup.lciii.dtos.UserRequestDto;
import ar.edu.utn.frc.tup.lciii.dtos.UserResponseDto;
import ar.edu.utn.frc.tup.lciii.models.User;
import ar.edu.utn.frc.tup.lciii.repositories.jpa.UserJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserJpaRepository userJpaRepository;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private UserServiceImpl userService;

    private UserRequestDto userRequestDto;
    private User user;
    private UserResponseDto userResponseDto;

    @BeforeEach
    public void setUp() {
        userRequestDto = new UserRequestDto("test@test.com", "Celsius");
        user = new User();
        user.setEmail(userRequestDto.getEmail());
        user.setTemperature_unit(userRequestDto.getTemperature_unit());
        userResponseDto = new UserResponseDto(1L, "secret");
    }

    @Test
    public void testSaveUserWhenUserIsSavedThenReturnUserResponseDto() {
        when(userJpaRepository.save(any(User.class))).thenReturn(user);
        when(modelMapper.map(any(User.class), eq(UserResponseDto.class))).thenReturn(userResponseDto);

        UserResponseDto result = userService.saveUser(userRequestDto);

        verify(userJpaRepository, times(1)).save(any(User.class));
        verify(modelMapper, times(1)).map(any(User.class), eq(UserResponseDto.class));
        assertEquals(userResponseDto, result);
    }

    @Test
    public void testSaveUserWhenUserJpaRepositoryThrowsExceptionThenReturnNull() {
        when(userJpaRepository.save(any(User.class))).thenThrow(new RuntimeException());

        UserResponseDto result = userService.saveUser(userRequestDto);

        verify(userJpaRepository, times(1)).save(any(User.class));
        verify(modelMapper, times(0)).map(any(User.class), eq(UserResponseDto.class));
        assertEquals(null, result);
    }
}