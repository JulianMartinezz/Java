package ar.edu.utn.frc.tup.lciii.Data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Param {
    private Object valor;
    private int indice;
    private String tipo;
}
