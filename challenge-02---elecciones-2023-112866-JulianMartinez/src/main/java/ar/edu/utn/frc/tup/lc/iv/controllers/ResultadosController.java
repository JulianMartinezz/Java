package ar.edu.utn.frc.tup.lc.iv.controllers;

import ar.edu.utn.frc.tup.lc.iv.dtos.common.ResultadoElecciones;
import ar.edu.utn.frc.tup.lc.iv.services.ElectionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ResultadosController {
    @Autowired
    private ElectionsService electionsService;

    @GetMapping("/resultados")
    public ResponseEntity<ResultadoElecciones> getResultados(@RequestParam Long distrito_id,@RequestParam Long seccion_id){
        try {
            return new ResponseEntity<>(electionsService.getResultado(distrito_id,seccion_id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
