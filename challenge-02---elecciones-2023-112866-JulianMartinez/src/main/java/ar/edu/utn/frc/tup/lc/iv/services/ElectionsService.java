package ar.edu.utn.frc.tup.lc.iv.services;

import ar.edu.utn.frc.tup.lc.iv.dtos.common.CargosDto;
import ar.edu.utn.frc.tup.lc.iv.dtos.common.DistritoDto;
import ar.edu.utn.frc.tup.lc.iv.dtos.common.ResultadoElecciones;
import ar.edu.utn.frc.tup.lc.iv.dtos.common.SeccionDto;
import ar.edu.utn.frc.tup.lc.iv.models.Distrito;
import ar.edu.utn.frc.tup.lc.iv.models.Seccion;

import java.util.List;

public interface ElectionsService {

    List<DistritoDto> getDistritos(String nombre);

    CargosDto getCargos(Long id);

    List<SeccionDto> getSecciones(Long idDistrito, Long idSeccion);

    ResultadoElecciones getResultado(Long idDistrito,Long idSeccion);
}
