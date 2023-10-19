package ar.edu.utn.frc.tup.lciii.Models;

import lombok.Data;

@Data
public class Board {
    private Piece[][] tablero;
    private final Integer FILAS = 8;
    private final Integer COLUMNAS = 8;


    public Board(){
        this.tablero =  new Piece[FILAS][COLUMNAS];
    }
}
