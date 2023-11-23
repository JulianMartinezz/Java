package ar.edu.utn.frc.tup.lciii.dtos;

import ar.edu.utn.frc.tup.lciii.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Service
public class WeatherTemplate {

    @Autowired
    private RestTemplate restTemplate;

    public List<Location> getLocationList(){
        ResponseEntity<List<Location>> responseEntity = restTemplate.exchange(
                "https://my-json-server.typicode.com/LCIV-2023/fake-weather/location",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Location>>() {

                }
        );
        if(responseEntity.getStatusCode()== HttpStatus.OK){
            return responseEntity.getBody();
        } else {
            return Collections.emptyList();
        }
    }

    public List<Wind> getWindList(){
        ResponseEntity<List<Wind>> responseEntity = restTemplate.exchange(
                "https://my-json-server.typicode.com/LCIV-2023/fake-weather/wind",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Wind>>() {
                }
        );
        if(responseEntity.getStatusCode()==HttpStatus.OK){
            return responseEntity.getBody();
        } else {
            return Collections.emptyList();
        }
    }

    public List<Temperature> getTemperatureList(){
        ResponseEntity<List<Temperature>> responseEntity= restTemplate.exchange(
                "https://my-json-server.typicode.com/LCIV-2023/fake-weather/temperature",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Temperature>>() {}
        );
        if(responseEntity.getStatusCode()==HttpStatus.OK){
            return responseEntity.getBody();
        }else {
            return Collections.emptyList();
        }
    }

    public List<AirQuality> getAirQualityList(){
        ResponseEntity<List<AirQuality>> responseEntity = restTemplate.exchange(
                "https://my-json-server.typicode.com/LCIV-2023/fake-weather/air_quality",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<AirQuality>>() {}
        );
        if(responseEntity.getStatusCode()==HttpStatus.OK){
            return responseEntity.getBody();
        } else {
            return Collections.emptyList();
        }
    }

    public List<Cloudiness> getCloudinessList(){
        ResponseEntity<List<Cloudiness>> responseEntity = restTemplate.exchange(
                "https://my-json-server.typicode.com/LCIV-2023/fake-weather/cloudiness",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Cloudiness>>() {}
        );
        if(responseEntity.getStatusCode()==HttpStatus.OK){
            return responseEntity.getBody();
        } else {
            return Collections.emptyList();
        }
    }
}
