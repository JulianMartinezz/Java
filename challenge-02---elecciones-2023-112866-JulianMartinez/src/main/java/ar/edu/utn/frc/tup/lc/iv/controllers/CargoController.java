package ar.edu.utn.frc.tup.lc.iv.controllers;

import ar.edu.utn.frc.tup.lc.iv.dtos.common.CargosDto;
import ar.edu.utn.frc.tup.lc.iv.services.ElectionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
public class CargoController {

    @Autowired
    private ElectionsService electionsService;

    @GetMapping("/cargos")
    public ResponseEntity<CargosDto> getCargos(@RequestParam Long distrito_id){
        try {
           return new ResponseEntity<>(electionsService.getCargos(distrito_id), HttpStatus.OK);
        } catch (Exception e) {
           return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
