package ar.edu.utn.frc.tup.lc.iv.services.impl;

import ar.edu.utn.frc.tup.lc.iv.clients.ElectionsRestTemplate;
import ar.edu.utn.frc.tup.lc.iv.dtos.common.*;
import ar.edu.utn.frc.tup.lc.iv.models.Cargo;
import ar.edu.utn.frc.tup.lc.iv.models.Distrito;
import ar.edu.utn.frc.tup.lc.iv.models.Resultado;
import ar.edu.utn.frc.tup.lc.iv.models.Seccion;
import ar.edu.utn.frc.tup.lc.iv.services.ElectionsService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class ElectionsServiceImpl implements ElectionsService {

    @Autowired
    private ElectionsRestTemplate restTemplate;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<DistritoDto> getDistritos(String nombre) {
        List<DistritoDto> distritos = new ArrayList<>();
        if(nombre==null){
            return getDistritosList(restTemplate.getDistritosList());
        } else {
            List<Distrito> allDistritos = restTemplate.getDistritosList();
            Distrito distrito = getDistrito(allDistritos,nombre);
            distritos.add(modelMapper.map(distrito,DistritoDto.class));
            return distritos;
        }
    }

    private List<DistritoDto> getDistritosList(List<Distrito> distritosList) {
        List<DistritoDto> distritoDtos = new ArrayList<>();
        for(Distrito distrito:distritosList){
            distritoDtos.add(modelMapper.map(distrito,DistritoDto.class));
        }
        return distritoDtos;
    }

    @Override
    public CargosDto getCargos(Long id) {
        List<Distrito> distritos = restTemplate.getDistritosList();
        List<Cargo> cargos = restTemplate.getCargosList();
        CargosDto cargosDto= new CargosDto();
        DistritoDto distrito = modelMapper.map(findDistrito(id,distritos),DistritoDto.class);
        cargosDto.setDistrito(distrito);
        cargosDto.setCargos(findCargos(cargos,id));
        return cargosDto;
    }

    @Override
        public List<SeccionDto> getSecciones(Long idDistrito,Long idSeccion) {
        List<SeccionDto> seccions;
        List<SeccionDto> allSecciones = getSeccionesById(idSeccion,restTemplate.getSeccionsList());
        if(idDistrito==null){
            return allSecciones;
        } else {
            seccions = getSeccion(allSecciones,idDistrito);
            return seccions;
        }
    }

    @Override
    public ResultadoElecciones getResultado(Long idDistrito, Long idSeccion) {
        Resultado[] listResultado = restTemplate.getResultados(idSeccion).getBody();
        assert listResultado != null;

        Map<String, ResultadoDto> resultadoDTOMap = new LinkedHashMap<>();
        Long votosTotales = 0L;

        for (Resultado resultado : listResultado) {
           if(!resultado.votosTipo().equals("COMANDO")) {
               votosTotales += resultado.votosCantidad();
               String nombre = obtenerNombre(resultado);
               // si la clave existe, se actualiza el valor
               resultadoDTOMap.computeIfPresent(nombre, (key, value) -> {
                   value.setVotos((int) (value.getVotos() + resultado.votosCantidad()));
                   return value;
               });
               // si la clave no existe, se agrega
               resultadoDTOMap.putIfAbsent(nombre, ResultadoDto.builder()
                       .nombre(nombre)
                       .votos(Math.toIntExact(resultado.votosCantidad()))
                       .build());
           }
        }
        // se crea una list de resultado dto y se le agregar los valores del mapa
        List<ResultadoDto> resultadoDTOList = new ArrayList<>(resultadoDTOMap.values());
        // se ordena la lista por votos de mayor a menor
        resultadoDTOList.sort(Comparator.comparingInt(ResultadoDto::getVotos).reversed());

        asignarOrden(resultadoDTOList);
        calcularPorcentajes(resultadoDTOList, votosTotales);

        ResultadoElecciones responseDtoList = new ResultadoElecciones();
        responseDtoList.setResultados(resultadoDTOList);
        responseDtoList.setDistrito(listResultado[0].distritoNombre());
        responseDtoList.setSeccion(listResultado[0].seccionNombre());

        return responseDtoList;
    }

    private void calcularPorcentajes(List<ResultadoDto> resultadoDTOList, Long votosTotales) {
        BigDecimal votosTotalesBigDecimal = BigDecimal.valueOf(votosTotales == 0 ? 1 : votosTotales);

        for (ResultadoDto resultado : resultadoDTOList) {
            BigDecimal porcentaje = BigDecimal.valueOf(resultado.getVotos())
                    .divide(votosTotalesBigDecimal, 4, RoundingMode.HALF_UP);
            resultado.setPorcentaje(porcentaje);
        }
    }

    private void asignarOrden(List<ResultadoDto> resultadoDTOList) {
        for (int i = 0; i < resultadoDTOList.size(); i++) {
            resultadoDTOList.get(i).setOrden((int) (i + 1));
        }
    }

    private String obtenerNombre(Resultado resultado) {
        return resultado.agrupacionNombre().isEmpty() ?
                resultado.votosTipo().isEmpty() ? "Sin Nombre" : resultado.votosTipo() :
                resultado.agrupacionNombre();
    }


    private List<SeccionDto> getSeccionesById(Long idSeccion,List<Seccion> seccions) {
        List<SeccionDto> seccionList = new ArrayList<>();
        if(idSeccion!=null) {
            for (Seccion seccion:seccions){
                if(seccion.getSeccionId()==idSeccion){
                    seccionList.add(modelMapper.map(seccion,SeccionDto.class));
                }
            }
        } else {
            for(Seccion seccion:seccions) {
                seccionList.add(modelMapper.map(seccion,SeccionDto.class));
            }
        }
        return seccionList;
    }

    private List<SeccionDto> getSeccion(List<SeccionDto> allSecciones, Long id) {
        List<SeccionDto> seccions = new ArrayList<>();
        for (SeccionDto seccion:allSecciones) {
            if(seccion.getDistritoId()==id){
                seccions.add(seccion);
            }
        }
        return seccions;
    }

    private List<CargoDto> findCargos(List<Cargo> cargos, Long id) {
        List<CargoDto> cargoList = new ArrayList<>();
        for (Cargo cargo:cargos){
            if(Objects.equals(cargo.getDistritoId(), id)){
                cargoList.add(modelMapper.map(cargo, CargoDto.class));
            }
        }
        return cargoList;
    }

    private Distrito findDistrito(Long id,List<Distrito> distritos) {
        return distritos.stream()
                .filter(distrito -> Objects.equals(distrito.getDistritoId(), id))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("No se encontró ningún distrito con el ID: " + id));
    }


    private Distrito getDistrito(List<Distrito> allDistritos, String nombre) {
       return allDistritos.stream()
                .filter(distrito -> distrito.getDistritoNombre()==nombre)
                .findFirst()
                .orElse(null);
    }

}
