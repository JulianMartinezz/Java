package ar.edu.utn.frc.tup.lciii.Models;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class Piece {
    private int idPieza;
    private PieceType pieceType;
    private PieceColor pieceColor;
    private  Position position;
    private PieceStatus status;
    private String valor;
    private List<Position> movDisponibles;
    private Position posicionAnterior;
    private List<Position> movRealizados;
    private boolean comioEnMovAnterior;
    private List<Piece> piezsComidas;
    private List<Position> zonaAtacada;

    public Piece(PieceType pieceType, PieceColor pieceColor, int fila,int columna, String valor) {
        this.idPieza = -1;
        this.pieceType = pieceType;
        this.pieceColor = pieceColor;
        this.position = new Position(fila,columna);
        this.status = PieceStatus.ENJUEGO;
        this.valor = valor;
        this.movDisponibles = new ArrayList<>();
        this.posicionAnterior = null;
        this.zonaAtacada = new ArrayList<>();
        this.movRealizados = new ArrayList<>();
        this.piezsComidas = new ArrayList<>();
        this.comioEnMovAnterior = false;
    }
}