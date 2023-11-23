package ar.edu.utn.frc.tup.lciii.services.impl;

import ar.edu.utn.frc.tup.lciii.dtos.ResponseDto;
import ar.edu.utn.frc.tup.lciii.dtos.common.ResponseNeighborhoodsDto;
import ar.edu.utn.frc.tup.lciii.models.Barrio;
import ar.edu.utn.frc.tup.lciii.models.Equipo;
import ar.edu.utn.frc.tup.lciii.models.Resultado;
import ar.edu.utn.frc.tup.lciii.services.SurveyResultsService;
import ar.edu.utn.frc.tup.lciii.templates.SurveyResultsRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SurveyResultsServiceImpl implements SurveyResultsService {

    private Map<Long,ResponseDto> map= new HashMap<Long,ResponseDto>();

    private int total = 0;

    @Autowired
    private SurveyResultsRestTemplate surveyResultsRestTemplate;
    @Override
    public List<ResponseDto> getResponseDtoList() {
        map.clear();
        List<Resultado> resultadoList = surveyResultsRestTemplate.getResultadosList();
        List<ResponseDto> responseDtoList;
        for (Resultado resultado: resultadoList){
            if(map.containsKey(resultado.getEquipoId())){
                ResponseDto responseDto = map.get(resultado.getEquipoId());
                responseDto.setCantidadDeHinchas(responseDto.getCantidadDeHinchas()+resultado.getVotos());
                total += resultado.getVotos();
                map.replace(resultado.getId(),responseDto);
            } else {
                ResponseDto responseDto = new ResponseDto();
                responseDto.setCantidadDeHinchas(resultado.getVotos());
                total += resultado.getVotos();
                map.put(resultado.getEquipoId(), responseDto);
            }
        }
        estimateVotingPercentages();
        setTeamsNames();
        responseDtoList = loadListWithCollection(map.values());
        return responseDtoList;
    }

    @Override
    public List<ResponseNeighborhoodsDto> getResponseNeighborhoodsDtoList() {
        List<Resultado> resultadoList = surveyResultsRestTemplate.getResultadosList();
        List<ResponseNeighborhoodsDto> responseNeighborhoodsDtos = new ArrayList<>();
        List<Barrio> barrios = surveyResultsRestTemplate.getBarriosList();
        for (Barrio barrio: barrios){
            map.clear();
            total = 0;
            List<ResponseDto> responseDtoList;
            ResponseNeighborhoodsDto responseNeighborhoodsDto = new ResponseNeighborhoodsDto();
            responseNeighborhoodsDto.setId(barrio.getId());
            responseNeighborhoodsDto.setName(barrio.getNombre());
            for(Resultado resultado: resultadoList) {
                if(barrio.getId().equals(resultado.getBarrioId())){
                    if(map.containsKey(resultado.getEquipoId())) {
                        ResponseDto responseDto = map.get(resultado.getEquipoId());
                        responseDto.setIdBarrio(resultado.getBarrioId());
                        responseDto.setCantidadDeHinchas(responseDto.getCantidadDeHinchas() + resultado.getVotos());
                        total += resultado.getVotos();
                        map.replace(resultado.getId(), responseDto);
                    }
                    else {
                        ResponseDto responseDto = new ResponseDto();
                        responseDto.setIdBarrio(resultado.getBarrioId());
                        responseDto.setCantidadDeHinchas(resultado.getVotos());
                        total += resultado.getVotos();
                        map.put(resultado.getEquipoId(), responseDto);
                    }
                }

            }
            setTeamsNames();
            estimateVotingPercentages();
            responseDtoList=loadListWithCollection(map.values());
            responseNeighborhoodsDto.setResponseDtoList(responseDtoList);
            responseNeighborhoodsDtos.add(responseNeighborhoodsDto);
        }
        return responseNeighborhoodsDtos;
    }



    private List<ResponseDto> loadListWithCollection(Collection<ResponseDto> values) {
        List<ResponseDto> responseDtoList = new ArrayList<>();
        for(ResponseDto responseDto : values){
            responseDtoList.add(responseDto);
        }
        return responseDtoList;
    }


    private void setTeamsNames() {
        List<Equipo> equipos = surveyResultsRestTemplate.getEquiposList();
        for (Equipo equipo : equipos){
            ResponseDto responseDto = map.get(equipo.getId());
            if(responseDto!=null){
                responseDto.setNombre(equipo.getNombre());
                map.replace(equipo.getId(), responseDto);
            }
        }
    }

    private void estimateVotingPercentages() {
        for(ResponseDto response : map.values()){
            response.setPorcentajeSobreTotalDeHinchas(
                calculatingVotingPercentages(response.getCantidadDeHinchas())
            );
            map.replace(response.getId(),response);
        }
    }

    private double calculatingVotingPercentages(int votes){
        return ((double) votes /total)*100;
    }
}

