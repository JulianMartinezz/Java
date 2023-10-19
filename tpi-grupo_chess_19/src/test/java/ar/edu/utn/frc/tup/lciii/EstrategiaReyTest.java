package ar.edu.utn.frc.tup.lciii;

import ar.edu.utn.frc.tup.lciii.Models.*;
import ar.edu.utn.frc.tup.lciii.Services.EstrategiaRey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class EstrategiaReyTest {
    private Board tablero;
    private EstrategiaRey _estrategia;
    private Piece _pieza;
    @BeforeEach
    public void setUp(){
        tablero = new Board();
        _estrategia = new EstrategiaRey();
        _pieza = new Piece(PieceType.REY, PieceColor.BLANCA,0,4,"");
        tablero.getTablero()[0][4] = _pieza;
    }
    @Test
    @DisplayName("Movimientos disponibles con tablero vacio")
    public void movimientosDisponiblesTest(){
        List<Position> mov = _estrategia.obtenerMovimientosPosibles(_pieza, tablero.getTablero());
        assertEquals(5,mov.size());
    }
    @Test
    @DisplayName("Movimientos disponibles con tablero y piezas")
    public void movimientosDisponiblesTest2(){
        tablero.getTablero()[1][3]=new Piece(PieceType.PEON,PieceColor.BLANCA,1,3,"");
        tablero.getTablero()[1][4]=new Piece(PieceType.PEON,PieceColor.BLANCA,1,4,"");
        tablero.getTablero()[1][5]=new Piece(PieceType.PEON,PieceColor.BLANCA,1,5,"");
        List<Position> mov = _estrategia.obtenerMovimientosPosibles(_pieza, tablero.getTablero());
        assertEquals(2,mov.size());
    }
    @Test
    public void jaqueAlReyTest(){
        boolean jaque = _estrategia.jaqueAlRey(_pieza, tablero.getTablero());
        assertFalse(jaque);
    }
    @Test
    @DisplayName("Movimientos disponibles con tablero y piezas")
    public void zonasAtacadasTest(){
        tablero.getTablero()[1][3]=new Piece(PieceType.PEON,PieceColor.BLANCA,1,3,"");
        tablero.getTablero()[1][4]=new Piece(PieceType.PEON,PieceColor.BLANCA,1,4,"");
        tablero.getTablero()[1][5]=new Piece(PieceType.PEON,PieceColor.BLANCA,1,5,"");
        List<Position> zonas = _estrategia.zonasAtacadas(_pieza, tablero.getTablero());
        assertEquals(5,zonas.size());
    }

}
