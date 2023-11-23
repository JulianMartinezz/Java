package ar.edu.utn.frc.tup.lciii.controllers;

import ar.edu.utn.frc.tup.lciii.dtos.WeatherResponseDto;
import ar.edu.utn.frc.tup.lciii.models.Location;
import ar.edu.utn.frc.tup.lciii.models.User;
import ar.edu.utn.frc.tup.lciii.models.WeatherResponse;
import ar.edu.utn.frc.tup.lciii.services.WeatherService;
import ar.edu.utn.frc.tup.lciii.services.impl.UserServiceImpl;
import ar.edu.utn.frc.tup.lciii.services.impl.WeatherServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/weathers")
public class WeatherController {

    @Autowired
    private WeatherService _weatherService;

    @GetMapping("/location/{locationId}")
    public ResponseEntity<WeatherResponseDto> getWeatherForLocation(
            @PathVariable Long locationId,
            @RequestParam(name="datetime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime localDateTime,
            @RequestHeader("user_id") Long userId,
            @RequestHeader("client_secret") String clientSecret){
        return new ResponseEntity<>(_weatherService.getWeatherListForLocationAndDate(locationId,userId,clientSecret,localDateTime),HttpStatus.OK);
    }

    @GetMapping("/locations")
    public ResponseEntity<List<Location>> getLocations(
            @RequestHeader("id_client") Long idClient,
            @RequestHeader("id_secret") String idSecret) {
        return new ResponseEntity<>(_weatherService.getLocations(idClient,idSecret),HttpStatus.OK);
    }

}
