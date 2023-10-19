package ar.edu.utn.frc.tup.lciii;

import ar.edu.utn.frc.tup.lciii.Models.*;
import ar.edu.utn.frc.tup.lciii.Services.EstrategiaReina;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class EstrategiaReinaTest {
    private Board tablero;
    private EstrategiaReina _estrategia;
    private Piece _pieza;
    @BeforeEach
    public void setUp(){
        tablero = new Board();
        _estrategia = new EstrategiaReina();
        _pieza = new Piece(PieceType.REINA, PieceColor.BLANCA,3,4,"");
        tablero.getTablero()[3][4] = _pieza;
    }
    @Test
    @DisplayName("Movimientos disponibles con tablero vacio")
    public void movimientosDisponiblesTest(){
        List<Position> mov = _estrategia.obtenerMovimientosPosibles(_pieza, tablero.getTablero());
        assertEquals(27,mov.size());
    }
    @Test
    @DisplayName("Movimientos disponibles con tablero y piezas")
    public void movimientosDisponiblesTest2(){
        tablero.getTablero()[4][4] = new Piece(PieceType.PEON,PieceColor.NEGRA,4,4,"");
        tablero.getTablero()[3][6] = new Piece(PieceType.ALFIL,PieceColor.NEGRA,3,6,"");
        tablero.getTablero()[3][0] = new Piece(PieceType.TORRE,PieceColor.BLANCA,3,0,"");
        List<Position> mov = _estrategia.obtenerMovimientosPosibles(_pieza, tablero.getTablero());
        assertEquals(22,mov.size());
    }
    @Test
    @DisplayName("Probando jaque, rey tapado")
    public void jaqueAlReyTest(){
        Piece reinaNegra = new Piece(PieceType.REINA,PieceColor.NEGRA,6,3,"");
        tablero.getTablero()[reinaNegra.getPosition().getFila()][reinaNegra.getPosition().getColumna()]=reinaNegra;
        Piece reyNegro = new Piece(PieceType.REY,PieceColor.NEGRA,7,3,"");
        tablero.getTablero()[reyNegro.getPosition().getFila()][reyNegro.getPosition().getColumna()] = reyNegro;

        boolean jaque = _estrategia.jaqueAlRey(_pieza,tablero.getTablero());
        assertFalse(jaque);
    }
    @Test
    @DisplayName("Zonas atacadas")
    public void zonasAtacadasTest(){

        tablero.getTablero()[1][2]=new Piece(PieceType.PEON,PieceColor.BLANCA,1,2,"");
        tablero.getTablero()[1][3]=new Piece(PieceType.PEON,PieceColor.BLANCA,1,3,"");
        tablero.getTablero()[1][4]=new Piece(PieceType.PEON,PieceColor.BLANCA,1,4,"");
        tablero.getTablero()[3][4] = null;
        tablero.getTablero()[0][3] = _pieza;
        _pieza.setPosition(new Position(0,3));
        List<Position> zonasAtacadas = _estrategia.zonasAtacadas(_pieza,tablero.getTablero());
        assertEquals(10,zonasAtacadas.size());
    }
}
