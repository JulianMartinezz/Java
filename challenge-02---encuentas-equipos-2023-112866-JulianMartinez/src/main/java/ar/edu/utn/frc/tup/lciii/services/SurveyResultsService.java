package ar.edu.utn.frc.tup.lciii.services;

import ar.edu.utn.frc.tup.lciii.dtos.ResponseDto;
import ar.edu.utn.frc.tup.lciii.dtos.common.ResponseNeighborhoodsDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SurveyResultsService {

    List<ResponseDto> getResponseDtoList();

    List<ResponseNeighborhoodsDto> getResponseNeighborhoodsDtoList();
}
