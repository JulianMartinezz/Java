package ar.edu.utn.frc.tup.lciii.controllers;

import ar.edu.utn.frc.tup.lciii.dtos.ResponseDto;
import ar.edu.utn.frc.tup.lciii.dtos.common.ResponseNeighborhoodsDto;
import ar.edu.utn.frc.tup.lciii.services.SurveyResultsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/surveyResults")
public class SurveyController {

    @Autowired
    private SurveyResultsService surveyResultsService;

    @GetMapping("/all")
    public ResponseEntity<List<ResponseDto>> getSurveyResults(){
        return new ResponseEntity<>(surveyResultsService.getResponseDtoList(), HttpStatus.OK);
    }

    @GetMapping("/neighborhoods")
    public ResponseEntity<List<ResponseNeighborhoodsDto>> getSurveyNeighborhoodsResults(){
        return new ResponseEntity<>(surveyResultsService.getResponseNeighborhoodsDtoList(), HttpStatus.OK);
    }
}
