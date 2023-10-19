package ar.edu.utn.frc.tup.lciii;

import ar.edu.utn.frc.tup.lciii.Models.*;
import ar.edu.utn.frc.tup.lciii.Services.EstrategiaTorre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EstrategiaTorreTest {
    private Board tablero;
    private EstrategiaTorre _estrategia;
    private Piece _pieza;
    @BeforeEach
    public void setUp(){
        tablero = new Board();
        _estrategia = new EstrategiaTorre();
        _pieza = new Piece(PieceType.TORRE, PieceColor.BLANCA,0,0,"");
        tablero.getTablero()[0][0] = _pieza;
    }
    @Test
    @DisplayName("Mov posibles con tablero vacio")
    public void movPosiblesTest(){
        List<Position> movDisponibles = _estrategia.obtenerMovimientosPosibles(_pieza,tablero.getTablero());
        assertEquals(14,movDisponibles.size());
    }
    @Test
    @DisplayName("Mov posibles con tablero con piezas")
    public void movPosiblesTest2(){
        Piece reinaNegra = new Piece(PieceType.REINA,PieceColor.NEGRA,0,5,"");
        Piece caballoBlanco = new Piece(PieceType.CABALLO,PieceColor.BLANCA,2,0,"");
        tablero.getTablero()[reinaNegra.getPosition().getFila()][reinaNegra.getPosition().getColumna()]=reinaNegra;
        tablero.getTablero()[caballoBlanco.getPosition().getFila()][caballoBlanco.getPosition().getColumna()]=caballoBlanco;
        List<Position> movDisponibles = _estrategia.obtenerMovimientosPosibles(_pieza,tablero.getTablero());
        assertEquals(6,movDisponibles.size());
    }
    @Test
    @DisplayName("Probando jaque, rey tapado")
    public void jaqueAlReyTest(){
        Piece reinaNegra = new Piece(PieceType.REINA,PieceColor.NEGRA,6,4,"");
        tablero.getTablero()[reinaNegra.getPosition().getFila()][reinaNegra.getPosition().getColumna()]=reinaNegra;
        Piece reyNegro = new Piece(PieceType.REY,PieceColor.NEGRA,7,4,"");
        tablero.getTablero()[reyNegro.getPosition().getFila()][reyNegro.getPosition().getColumna()] = reyNegro;
        tablero.getTablero()[0][0] = null;
        tablero.getTablero()[0][4] = _pieza;
        _pieza.setPosition(new Position(0,4));
        boolean jaque = _estrategia.jaqueAlRey(_pieza,tablero.getTablero());
        assertFalse(jaque);
    }
    @Test
    @DisplayName("Probando el jaque, rey destapado")
    public void jaqueAlReyTest2(){
        Piece reyNegro = new Piece(PieceType.REY,PieceColor.NEGRA,7,4,"");
        tablero.getTablero()[reyNegro.getPosition().getFila()][reyNegro.getPosition().getColumna()] = reyNegro;
        tablero.getTablero()[0][0] = null;
        tablero.getTablero()[0][4] = _pieza;
        _pieza.setPosition(new Position(0,4));
        boolean jaque = _estrategia.jaqueAlRey(_pieza,tablero.getTablero());
        assertTrue(jaque);
    }
    @Test
    @DisplayName("Zonas atacadas")
    public void zonasAtacadasTest(){
        Piece reinaNegra = new Piece(PieceType.REINA,PieceColor.NEGRA,0,5,"");
        Piece caballoBlanco = new Piece(PieceType.CABALLO,PieceColor.BLANCA,2,0,"");
        tablero.getTablero()[reinaNegra.getPosition().getFila()][reinaNegra.getPosition().getColumna()]=reinaNegra;
        tablero.getTablero()[caballoBlanco.getPosition().getFila()][caballoBlanco.getPosition().getColumna()]=caballoBlanco;
        tablero.getTablero()[0][0] = null;
        tablero.getTablero()[0][4] = _pieza;
        _pieza.setPosition(new Position(0,4));
        List<Position> zonasAtacadas = _estrategia.zonasAtacadas(_pieza,tablero.getTablero());
        assertEquals(12,zonasAtacadas.size());
    }
}
