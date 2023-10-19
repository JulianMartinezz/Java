package ar.edu.utn.frc.tup.lciii;

import ar.edu.utn.frc.tup.lciii.Models.*;
import ar.edu.utn.frc.tup.lciii.Services.EstrategiaCaballo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EstrategiaCaballoTest {
    private Board tablero;
    private EstrategiaCaballo _estrategia;
    private Piece _pieza;
    @BeforeEach
    public void setUp(){
        tablero = new Board();
        _estrategia = new EstrategiaCaballo();
        _pieza = new Piece(PieceType.CABALLO, PieceColor.BLANCA,0,1,"");
        tablero.getTablero()[0][1] = _pieza;
    }
    @Test
    @DisplayName("Movimientos posibles tablero vacio")
    public void movDisponiblesTest(){
        List<Position> movDisponibles = _estrategia.obtenerMovimientosPosibles(_pieza,tablero.getTablero());
        assertEquals(3,movDisponibles.size());
    }
    @Test
    @DisplayName("Movimientos posibles tablero con piezas")
    public void movDisponiblesTest2(){
        tablero.getTablero()[2][0] = new Piece(PieceType.TORRE,PieceColor.BLANCA,2,0,"");
        tablero.getTablero()[2][2] = new Piece(PieceType.CABALLO,PieceColor.NEGRA,2,2,"");
        tablero.getTablero()[1][3] = new Piece(PieceType.PEON,PieceColor.BLANCA,1,3,"");
        List<Position> movDisponibles = _estrategia.obtenerMovimientosPosibles(_pieza,tablero.getTablero());
        assertEquals(1,movDisponibles.size());
    }
    @Test
    @DisplayName("jaque al rey")
    public void jaqueAlReyTest(){
        tablero.getTablero()[7][4] = new Piece(PieceType.REY,PieceColor.NEGRA,7,4,"");
        tablero.getTablero()[0][1] = null;
        tablero.getTablero()[5][3] = _pieza;
        _pieza.setPosition(new Position(5,3));
        boolean jaque = _estrategia.jaqueAlRey(_pieza,tablero.getTablero());
        assertTrue(jaque);
    }
    @Test
    @DisplayName("Zonas atacadas")
    public void zonasAtacadasTest(){
        tablero.getTablero()[2][0] = new Piece(PieceType.TORRE,PieceColor.BLANCA,2,0,"");
        tablero.getTablero()[2][2] = new Piece(PieceType.CABALLO,PieceColor.NEGRA,2,2,"");
        tablero.getTablero()[1][3] =         tablero.getTablero()[1][3] = new Piece(PieceType.PEON,PieceColor.BLANCA,1,3,"");
        List<Position> zonasAtacadas = _estrategia.zonasAtacadas(_pieza, tablero.getTablero());
        assertEquals(3,zonasAtacadas.size());
    }
}
