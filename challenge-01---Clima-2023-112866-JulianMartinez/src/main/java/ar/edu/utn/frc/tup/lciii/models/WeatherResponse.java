package ar.edu.utn.frc.tup.lciii.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeatherResponse {
    private Location location;
    private Temperature temperature;
    private String wind;
    private AirQuality airQuality;
    private Cloudiness cloudiness;

    public void setError(String errorMessage) {
    }
}
