package ar.edu.utn.frc.tup.lciii.services;

import ar.edu.utn.frc.tup.lciii.dtos.WeatherResponseDto;
import ar.edu.utn.frc.tup.lciii.models.Location;
import ar.edu.utn.frc.tup.lciii.models.WeatherResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


public interface WeatherService {
    WeatherResponseDto getWeatherListForLocationAndDate(Long locationId, Long id, String secret, LocalDateTime date);
    List<Location> getLocations(Long id,String secret);
}
