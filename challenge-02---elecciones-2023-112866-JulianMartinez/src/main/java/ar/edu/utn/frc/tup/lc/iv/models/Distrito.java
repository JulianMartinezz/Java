package ar.edu.utn.frc.tup.lc.iv.models;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Distrito {


    private Long distritoId;
    private String distritoNombre;
}
