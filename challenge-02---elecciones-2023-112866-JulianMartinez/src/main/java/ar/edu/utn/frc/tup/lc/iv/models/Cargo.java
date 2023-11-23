package ar.edu.utn.frc.tup.lc.iv.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cargo {

    private Long cargoId;
    private String cargoNombre;
    private Long distritoId;
}
