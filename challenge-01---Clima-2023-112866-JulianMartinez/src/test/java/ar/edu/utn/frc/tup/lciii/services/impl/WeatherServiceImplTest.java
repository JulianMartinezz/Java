package ar.edu.utn.frc.tup.lciii.services.impl;

import ar.edu.utn.frc.tup.lciii.dtos.WeatherResponseDto;
import ar.edu.utn.frc.tup.lciii.models.Location;
import ar.edu.utn.frc.tup.lciii.models.User;
import ar.edu.utn.frc.tup.lciii.repositories.jpa.UserJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WeatherServiceImplTest {

    @Mock
    private UserJpaRepository userJpaRepository;

    @InjectMocks
    private WeatherServiceImpl weatherService;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId_client(1L);
        user.setId_secret("secret");
    }

    @Test
    public void testGetLocationsWhenUserIsVerifiedThenReturnLocations() {
        // Arrange
        when(userJpaRepository.getReferenceById(user.getId_client())).thenReturn(user);

        // Act
        List<Location> locations = weatherService.getLocations(user.getId_client(), user.getId_secret());

        // Assert
        assertThat(locations).isNotNull();
        assertThat(locations).isEqualTo(Collections.emptyList());
    }

    @Test
    public void testGetLocationsWhenUserIsNotVerifiedThenReturnNull() {
        // Arrange
        when(userJpaRepository.getReferenceById(user.getId_client())).thenReturn(user);

        // Act
        List<Location> locations = weatherService.getLocations(user.getId_client(), "wrong_secret");

        // Assert
        assertThat(locations).isNull();
    }

    @Test
    public void testGetWeatherListForLocationAndDateWhenUserIsVerifiedThenReturnWeatherResponseDto() {
        // Arrange
        when(userJpaRepository.getReferenceById(user.getId_client())).thenReturn(user);

        // Act
        WeatherResponseDto weatherResponseDto = weatherService.getWeatherListForLocationAndDate(1L, user.getId_client(), user.getId_secret(), LocalDateTime.now());

        // Assert
        assertThat(weatherResponseDto).isNotNull();
    }


}