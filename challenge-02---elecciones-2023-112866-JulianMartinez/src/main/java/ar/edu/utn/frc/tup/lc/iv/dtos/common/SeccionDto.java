package ar.edu.utn.frc.tup.lc.iv.dtos.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeccionDto {

    @JsonProperty("id")
    private Long seccionId;
    @JsonProperty("nombre")
    private String seccionNombre;
    @JsonIgnore
    private Long distritoId;
}
