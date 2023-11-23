package ar.edu.utn.frc.tup.lc.iv.dtos.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CargoDto {

    @JsonProperty("id")
    private Long cargoId;
    @JsonProperty("nombre")
    private String cargoNombre;
    @JsonIgnore
    private Long distritoId;
}
