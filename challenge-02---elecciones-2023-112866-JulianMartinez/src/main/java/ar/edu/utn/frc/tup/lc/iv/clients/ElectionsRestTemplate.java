package ar.edu.utn.frc.tup.lc.iv.clients;

import ar.edu.utn.frc.tup.lc.iv.models.Cargo;
import ar.edu.utn.frc.tup.lc.iv.models.Distrito;
import ar.edu.utn.frc.tup.lc.iv.models.Resultado;
import ar.edu.utn.frc.tup.lc.iv.models.Seccion;
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
public class ElectionsRestTemplate {

    @Autowired
    private RestTemplate restTemplate;

    public List<Cargo> getCargosList(){
        ResponseEntity<List<Cargo>> responseEntity = restTemplate.exchange(
                "http://localhost:8080/cargos",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Cargo>>() {
                }
        );
        if(responseEntity.getStatusCode()== HttpStatus.OK){
            return responseEntity.getBody();
        } else {
            return Collections.emptyList();
        }
    }

    public List<Distrito> getDistritosList(){
        ResponseEntity<List<Distrito>> responseEntity = restTemplate.exchange(
                "http://localhost:8080/distritos",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Distrito>>() {
                }
        );
        if(responseEntity.getStatusCode()==HttpStatus.OK) {
            return responseEntity.getBody();
        } else {
            return Collections.emptyList();
        }
    }

    public List<Seccion> getSeccionsList() {
        ResponseEntity<List<Seccion>> responseEntity = restTemplate.exchange(
                "http://localhost:8080/secciones",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Seccion>>() {
                }
        );
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return responseEntity.getBody();
        } else {
            return Collections.emptyList();
        }
    }

    public ResponseEntity <Resultado[]> getResultados(Long seccionId){
        return restTemplate.getForEntity("http://localhost:8080/" + "resultados?seccionId=" + seccionId, Resultado[].class);
    }

}
