package ar.edu.utn.frc.tup.lciii.templates;

import ar.edu.utn.frc.tup.lciii.models.Barrio;
import ar.edu.utn.frc.tup.lciii.models.Equipo;
import ar.edu.utn.frc.tup.lciii.models.Resultado;
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
public class SurveyResultsRestTemplate {

    @Autowired
    private RestTemplate restTemplate;

    public List<Resultado> getResultadosList(){
        ResponseEntity<List<Resultado>> responseEntity = restTemplate.exchange(
                "http://localhost:8081/resultados",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Resultado>>() {
                }
        );
        if(responseEntity.getStatusCode()== HttpStatus.OK){
            return responseEntity.getBody();
        } else {
            return Collections.emptyList();
        }
    }

    public List<Equipo> getEquiposList(){
        ResponseEntity<List<Equipo>> responseEntity = restTemplate.exchange(
                "http://localhost:8081/equipos",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Equipo>>() {
                }
        );
        if(responseEntity.getStatusCode()==HttpStatus.OK){
            return responseEntity.getBody();
        } else {
            return Collections.emptyList();
        }
    }

    public List<Barrio> getBarriosList(){
        ResponseEntity<List<Barrio>> responseEntity = restTemplate.exchange(
                "http://localhost:8081/barrios",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Barrio>>() {
                }
        );
        if(responseEntity.getStatusCode()==HttpStatus.OK){
            return responseEntity.getBody();
        } else {
            return Collections.emptyList();
        }
    }
}
