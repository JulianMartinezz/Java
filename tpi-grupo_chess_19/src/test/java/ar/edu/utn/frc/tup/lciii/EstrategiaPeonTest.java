package ar.edu.utn.frc.tup.lciii;

import ar.edu.utn.frc.tup.lciii.Models.*;
import ar.edu.utn.frc.tup.lciii.Services.EstrategiaPeon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EstrategiaPeonTest {
    private Board tablero;
    private EstrategiaPeon _estrategia;
    private Piece _pieza;
    @BeforeEach
    public void setUp(){
        tablero = new Board();
        _estrategia = new EstrategiaPeon();
        _pieza = new Piece(PieceType.TORRE, PieceColor.BLANCA,1,4,"");
        tablero.getTablero()[1][4] = _pieza;
    }
    @Test
    @DisplayName("Mov posibles con tablero vacio")
    public void movPosiblesTest(){
        List<Position> movDisponibles = _estrategia.obtenerMovimientosPosibles(_pieza,tablero.getTablero());
        assertEquals(2,movDisponibles.size());
    }
    @Test
    @DisplayName("Mov posibles con tablero con piezas")
    public void movPosiblesTest2(){
        tablero.getTablero()[2][5]=new Piece(PieceType.CABALLO,PieceColor.NEGRA,2,5,"");
        tablero.getTablero()[2][3]=new Piece(PieceType.ALFIL,PieceColor.NEGRA,2,3,"");
        List<Position> movDisponibles = _estrategia.obtenerMovimientosPosibles(_pieza,tablero.getTablero());
        assertEquals(4,movDisponibles.size());
    }
    @Test
    public void jaqueAlReyTest(){
        Piece reyNegro = new Piece(PieceType.REY,PieceColor.NEGRA,7,5,"");
        tablero.getTablero()[reyNegro.getPosition().getFila()][reyNegro.getPosition().getColumna()] = reyNegro;
        tablero.getTablero()[1][4] = null;
        tablero.getTablero()[6][4] = _pieza;
        _pieza.setPosition(new Position(6,4));
        boolean jaque = _estrategia.jaqueAlRey(_pieza,tablero.getTablero());
        assertTrue(jaque);
    }
    @Test
    public void zonasAtacadasTest(){
        tablero.getTablero()[2][5]=new Piece(PieceType.CABALLO,PieceColor.BLANCA,2,5,"");
        List<Position> movDisponibles = _estrategia.obtenerMovimientosPosibles(_pieza,tablero.getTablero());
        assertEquals(2,movDisponibles.size());
    }
}
