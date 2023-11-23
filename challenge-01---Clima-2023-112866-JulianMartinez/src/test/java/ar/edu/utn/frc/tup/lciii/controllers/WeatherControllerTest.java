package ar.edu.utn.frc.tup.lciii.controllers;

import ar.edu.utn.frc.tup.lciii.dtos.WeatherResponseDto;
import ar.edu.utn.frc.tup.lciii.models.Location;
import ar.edu.utn.frc.tup.lciii.services.WeatherService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WeatherController.class)
public class WeatherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WeatherService weatherService;

    @Test
    public void testGetWeatherForLocationWhenParametersAreValidThenReturnWeatherResponseDto() throws Exception {
        WeatherResponseDto mockResponse = new WeatherResponseDto();
        when(weatherService.getWeatherListForLocationAndDate(1L, 1L, "secret", LocalDateTime.now())).thenReturn(mockResponse);

        mockMvc.perform(get("/weathers/location/1")
                        .param("datetime", LocalDateTime.now().toString())
                        .header("user_id", 1L)
                        .header("client_secret", "secret"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetWeatherForLocationWhenLocationIdIsMissingThenReturnBadRequest() throws Exception {
        mockMvc.perform(get("/weathers/location/")
                        .param("datetime", LocalDateTime.now().toString())
                        .header("user_id", 1L)
                        .header("client_secret", "secret"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetWeatherForLocationWhenLocalDateTimeIsMissingThenReturnBadRequest() throws Exception {
        mockMvc.perform(get("/weathers/location/1")
                        .header("user_id", 1L)
                        .header("client_secret", "secret"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetWeatherForLocationWhenUserIdIsMissingThenReturnBadRequest() throws Exception {
        mockMvc.perform(get("/weathers/location/1")
                        .param("datetime", LocalDateTime.now().toString())
                        .header("client_secret", "secret"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetWeatherForLocationWhenClientSecretIsMissingThenReturnBadRequest() throws Exception {
        mockMvc.perform(get("/weathers/location/1")
                        .param("datetime", LocalDateTime.now().toString())
                        .header("user_id", 1L))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetLocationsWhenParametersAreValidThenReturnOkStatusAndListOfLocations() throws Exception {
        Location mockLocation = new Location(1L, "Test Location", "12.34", "56.78");
        when(weatherService.getLocations(1L, "secret")).thenReturn(Collections.singletonList(mockLocation));

        mockMvc.perform(get("/weathers/locations")
                        .header("id_client", 1L)
                        .header("id_secret", "secret"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetLocationsWhenIdClientIsMissingThenReturnBadRequest() throws Exception {
        mockMvc.perform(get("/weathers/locations")
                        .header("id_secret", "secret"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetLocationsWhenIdSecretIsMissingThenReturnBadRequest() throws Exception {
        mockMvc.perform(get("/weathers/locations")
                        .header("id_client", 1L))
                .andExpect(status().isBadRequest());
    }
}