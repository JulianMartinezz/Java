package ar.edu.utn.frc.tup.lciii.services.impl;

import ar.edu.utn.frc.tup.lciii.dtos.*;
import ar.edu.utn.frc.tup.lciii.models.*;
import ar.edu.utn.frc.tup.lciii.repositories.jpa.UserJpaRepository;
import ar.edu.utn.frc.tup.lciii.services.WeatherService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class WeatherServiceImpl implements WeatherService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private WeatherTemplate _weatherTemplate;

    @Autowired
    private UserJpaRepository _userJpaRepository;

    @Override
    public WeatherResponseDto getWeatherListForLocationAndDate(Long locationId, Long id, String secret,LocalDateTime date) {
        if(!verifyUser(id,secret)) return createErrorResponse("User not found");
        List<Location> locations=_weatherTemplate.getLocationList();
        List<Wind> winds=_weatherTemplate.getWindList();
        List<Temperature> temperatures=_weatherTemplate.getTemperatureList();
        List<AirQuality> airQualities=_weatherTemplate.getAirQualityList();
        List<Cloudiness> cloudinesses=_weatherTemplate.getCloudinessList();
        Location location = locations.stream()
                .filter(loc-> Objects.equals(loc.getId(), locationId))
                .findFirst()
                .orElse(null);
        if(location==null) return createErrorResponse("Location not found for ID: "+locationId);

        WeatherResponseDto responseDto = new WeatherResponseDto();
        responseDto.setLocation(modelMapper.map(location, LocationDto.class));

        Temperature temperature = findTemperatureByLocationIdAndDate(temperatures,locationId,date);
        if(temperature!=null) responseDto.setTemperature(createTemperature(temperature));

        Wind wind=findWindByLocationIdAndDate(winds,locationId,date);
        if(wind!=null) responseDto.setWind(formatWind(wind));

        AirQuality airQuality = findAirQualityByLocationIdAndDate(airQualities,locationId,date);
        if(airQuality!=null) responseDto.setAirQuality(createAirQualityDescription(airQuality));

        Cloudiness cloudiness = findCloudinessByLocationIdAndDate(cloudinesses,locationId,date);
        if(cloudiness!=null) responseDto.setCloudiness(createCloudinessDescription(cloudiness));

        return responseDto;
    }

    @Override
    public List<Location> getLocations(Long id,String secret) {
        if (verifyUser(id,secret)) {
            return _weatherTemplate.getLocationList();
        }
        return null;
    }

    private Boolean verifyUser(Long id,String secret){
        if(_userJpaRepository.getReferenceById(id).getId_secret().equals(secret)){
            return true;
        } else return false;
    }

    private Cloudiness findCloudinessByLocationIdAndDate(List<Cloudiness> cloudinesses, long locationId,LocalDateTime dateTime) {
        return cloudinesses.stream()
                .filter(cloudiness -> cloudiness.getLocation_id()==locationId&&cloudiness.getCreated_at()==dateTime)
                .findFirst()
                .orElse(null);

    }

    private AirQuality findAirQualityByLocationIdAndDate(List<AirQuality> airQualities, long locationId,LocalDateTime dateTime) {
        return airQualities.stream()
                .filter(airQuality -> airQuality.getLocation_id()==locationId&&airQuality.getCreated_at()==dateTime)
                .findFirst()
                .orElse(null);
    }

    private String formatWind(Wind wind) {
        return String.format("%.2f Km/h from %s",(float) wind.getSpeed(),formatWindDirection(wind.getDirection()));
    }

    private String formatWindDirection(int degrees) {
        if (degrees >= 315 || degrees < 45) {
            return "N"; // North
        } else if (degrees >= 45 && degrees < 135) {
            return "E"; // East
        } else if (degrees >= 135 && degrees < 225) {
            return "S"; // South
        } else if (degrees >= 225 && degrees < 315) {
            return "W"; // West
        } else {
            return "Invalid"; // Degrees out of range
        }
    }

    private Wind findWindByLocationIdAndDate(List<Wind> winds, long locationId,LocalDateTime dateTime) {
        return winds.stream()
                .filter(wind -> wind.getLocation_id()==locationId&&wind.getCreated_at()==dateTime)
                .findFirst()
                .orElse(null);
    }

    private Temperature findTemperatureByLocationIdAndDate(List<Temperature> temperatures, long locationId,LocalDateTime localDateTime) {
    return temperatures.stream()
            .filter(temperature -> temperature.getLocation_id()==locationId && temperature.getCreated_at()==localDateTime)
            .findFirst()
            .orElse(null);
    }

    private WeatherResponseDto createErrorResponse(String errorMessage) {
    WeatherResponseDto errorResponseDto = new WeatherResponseDto();
    errorResponseDto.setError(errorMessage);
    return errorResponseDto;
    }

    private AirQualityDto createAirQualityDescription(AirQuality airQuality){
        Integer index= airQuality.getQuality();
        AirQualityDto airQualityDto= new AirQualityDto();
        if (index >= 0 && index <= 50) {
            airQualityDto.setIndex(index);
            airQualityDto.setDescription("Good");
            return airQualityDto;
        } else if (index >= 51 && index <= 100) {
            airQualityDto.setIndex(index);
            airQualityDto.setDescription("Moderate");
            return airQualityDto;
        } else if (index >= 101 && index <= 150) {
            airQualityDto.setIndex(index);
            airQualityDto.setDescription("Unhealthy for Sensitive Groups");
            return airQualityDto;
        } else if (index >= 151 && index <= 200) {
            airQualityDto.setIndex(index);
            airQualityDto.setDescription("Unhealthy");
            return airQualityDto;
        } else if (index >= 201 && index <= 300) {
            airQualityDto.setIndex(index);
            airQualityDto.setDescription("Very Unhealthy");
            return airQualityDto;
        } else if (index >= 301 && index <= 500) {
            airQualityDto.setIndex(index);
            airQualityDto.setDescription("Hazardous");
            return airQualityDto;
        } else {
            airQualityDto.setIndex(index);
            airQualityDto.setDescription("Unknown");
            return airQualityDto;
        }

    }

    private CloudinessDto createCloudinessDescription(Cloudiness cloudiness){
        Integer index = cloudiness.getCloudiness();
        CloudinessDto cloudinessDto = new CloudinessDto();

        if (index >= 0 && index <= 1) {
            cloudinessDto.setIndex(index);
            cloudinessDto.setDescription("Clear sky");
            return cloudinessDto;
        } else if (index >= 2 && index <= 3) {
            cloudinessDto.setIndex(index);
            cloudinessDto.setDescription("Few clouds");
            return cloudinessDto;
        }else if (index >= 4 && index <= 6) {
            cloudinessDto.setIndex(index);
            cloudinessDto.setDescription("Sky half cloudy");
            return cloudinessDto;
        } else if (index >= 7 && index <= 8) {
            cloudinessDto.setIndex(index);
            cloudinessDto.setDescription("Sky completely cloudy");
            return cloudinessDto;
        }
        return cloudinessDto;
    }
    private TemperatureDto createTemperature(Temperature temperature){
        TemperatureDto createTemperatureDto = new TemperatureDto();
        if(temperature.getUnit().equalsIgnoreCase("f")) {
            createTemperatureDto.setValue(celsiusToFahrenheit(temperature.getTemperature()));
            createTemperatureDto.setUnit(temperature.getUnit());
            return createTemperatureDto;
        }
        createTemperatureDto.setUnit(temperature.getUnit());
        createTemperatureDto.setValue(temperature.getTemperature());
        return createTemperatureDto;
    }

    private Double celsiusToFahrenheit(Double celsius) {
        Double fahrenheit = (celsius * 9/5) + 32;
        return fahrenheit;
    }
}
