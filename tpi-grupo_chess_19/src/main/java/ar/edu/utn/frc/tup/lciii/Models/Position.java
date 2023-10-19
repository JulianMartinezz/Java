package ar.edu.utn.frc.tup.lciii.Models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Position {

    private int fila;
    private int columna;
    @Override
    public boolean equals(Object obj) {
        Position posicion = (Position) obj;
        if(this.fila == posicion.fila && this.columna == posicion.columna) return true;
        return false;
    }
}
