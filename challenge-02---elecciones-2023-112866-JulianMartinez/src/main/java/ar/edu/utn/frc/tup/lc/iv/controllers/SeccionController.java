package ar.edu.utn.frc.tup.lc.iv.controllers;

import ar.edu.utn.frc.tup.lc.iv.dtos.common.SeccionDto;
import ar.edu.utn.frc.tup.lc.iv.models.Seccion;
import ar.edu.utn.frc.tup.lc.iv.services.ElectionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SeccionController {

    @Autowired
    private ElectionsService electionsService;

    @GetMapping("/secciones")
    public ResponseEntity<List<SeccionDto>> getSecciones(@RequestParam Long distrito_id, @RequestParam (required = false) Long seccion_id) {
        try {
            return new ResponseEntity<>(electionsService.getSecciones(distrito_id,seccion_id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
