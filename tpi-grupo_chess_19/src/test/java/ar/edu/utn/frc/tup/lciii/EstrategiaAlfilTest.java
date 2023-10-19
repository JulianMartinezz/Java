package ar.edu.utn.frc.tup.lciii;

import ar.edu.utn.frc.tup.lciii.Models.*;
import ar.edu.utn.frc.tup.lciii.Services.EstrategiaAlfil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EstrategiaAlfilTest {
    private Board tablero;
    private EstrategiaAlfil _estrategia;
    private Piece _pieza;
    @BeforeEach
    public void setUp(){
         tablero = new Board();
         _estrategia = new EstrategiaAlfil();
         _pieza = new Piece(PieceType.ALFIL, PieceColor.BLANCA,0,2,"");
         tablero.getTablero()[0][2] = _pieza;
    }
    @Test
    @DisplayName("Alfil con tablero vacio")
    public void movimientosPosiblesTest(){
        List<Position> movimientos =_estrategia.obtenerMovimientosPosibles(_pieza,tablero.getTablero());
        assertEquals(7,movimientos.size());
    }
    @Test
    @DisplayName("Alfil con tablero con piezas")
    public void movimientosPosiblesTest2(){
        Piece reinaNegra = new Piece(PieceType.REINA,PieceColor.NEGRA,3,5,"");
        Piece caballoBlanco = new Piece(PieceType.CABALLO,PieceColor.BLANCA,2,0,"");
        tablero.getTablero()[reinaNegra.getPosition().getFila()][reinaNegra.getPosition().getColumna()]=reinaNegra;
        tablero.getTablero()[caballoBlanco.getPosition().getFila()][caballoBlanco.getPosition().getColumna()]=caballoBlanco;
        List<Position> movimientos =_estrategia.obtenerMovimientosPosibles(_pieza,tablero.getTablero());
        assertEquals(4,movimientos.size());
    }
    @Test
    @DisplayName("Probando el jaque del alfil sin rey")
    public void jaqueAlReyTest(){
        boolean jaque = _estrategia.jaqueAlRey(_pieza,tablero.getTablero());
        assertFalse(jaque);
    }
    @Test
    @DisplayName("Probando el jaque del alfil con rey")
    public void jaqueAlReyTest2(){
        tablero.getTablero()[5][7] = new Piece(PieceType.REY,PieceColor.NEGRA,5,7,"");
        boolean jaque = _estrategia.jaqueAlRey(_pieza,tablero.getTablero());
        assertTrue(jaque);
    }
    @Test
    @DisplayName("Zonas atacadas")
    public void zonasAtacadasTest(){
        Piece reinaNegra = new Piece(PieceType.REINA,PieceColor.NEGRA,3,5,"");
        Piece caballoBlanco = new Piece(PieceType.CABALLO,PieceColor.BLANCA,2,0,"");
        tablero.getTablero()[reinaNegra.getPosition().getFila()][reinaNegra.getPosition().getColumna()]=reinaNegra;
        tablero.getTablero()[caballoBlanco.getPosition().getFila()][caballoBlanco.getPosition().getColumna()]=caballoBlanco;
        List<Position> zonasAtacadas = _estrategia.zonasAtacadas(_pieza,tablero.getTablero());
        assertEquals(5,zonasAtacadas.size());
    }
}

