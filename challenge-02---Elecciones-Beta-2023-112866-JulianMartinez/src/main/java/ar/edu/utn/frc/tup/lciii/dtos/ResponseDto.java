package ar.edu.utn.frc.tup.lciii.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDto {
    @JsonIgnore
    private Long id;
    @JsonIgnore
    private Long idBarrio;
    @JsonFormat
    private String nombre;
    @JsonFormat
    private int cantidadDeHinchas;
    @JsonFormat
    private double porcentajeSobreTotalDeHinchas;
}
