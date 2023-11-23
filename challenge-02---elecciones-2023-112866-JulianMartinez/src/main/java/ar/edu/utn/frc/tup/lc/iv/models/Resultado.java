package ar.edu.utn.frc.tup.lc.iv.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public record Resultado(Long id,
                        Long distritoId, String distritoNombre, Long seccionId, String seccionNombre,
                        String agrupacionNombre, Integer votosCantidad , String votosTipo) {

}
