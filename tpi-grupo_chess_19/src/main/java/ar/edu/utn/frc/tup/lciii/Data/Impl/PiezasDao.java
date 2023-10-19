package ar.edu.utn.frc.tup.lciii.Data.Impl;


import ar.edu.utn.frc.tup.lciii.Data.DbConnection;
import ar.edu.utn.frc.tup.lciii.Data.Interfaces.IPiezasDao;
import ar.edu.utn.frc.tup.lciii.Data.Param;
import ar.edu.utn.frc.tup.lciii.Models.Piece;
import ar.edu.utn.frc.tup.lciii.Models.PieceColor;
import ar.edu.utn.frc.tup.lciii.Models.Position;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class PiezasDao implements IPiezasDao {
    private static final String BPAWN ="\u265F";
    private static final String NPAWN = "\u2659";
    private static final String BROOK = "\u265C";
    private static final String NROOK = "\u2656";

    private static final String BBISHOP = "\u265D";
    private static final String NBISHOP = "\u2657";

    private static final String BQUEEN = "\u265B";
    private static final String NQUEEN = "\u2655";

    private static final String BKING = "\u265A";
    private static final String NKING = "\u2654";

    private static final String BKNIGHT = "\u265E";
    private static final String NKNIGHT = "\u2658";
    @Override
    public List<Piece> obtenerTodas(int idPartida) {
        List<Piece> piezas = DbConnection.obtenerInstancia().obtenerPiezas(idPartida);
        piezas.forEach(p ->{
            asignarValor(p);
        });
        return piezas;
    }

    @Override
    public void moverPieza(int idPieza, Position p) {
        List<Param> parametros = new ArrayList<>();
        parametros.add(new Param(idPieza,1,"int"));
        parametros.add(new Param(p.getFila(),2,"int"));
        parametros.add(new Param(p.getColumna(),3,"int"));
        DbConnection.obtenerInstancia().ejecutarSpSinRes("{call dbo.movimientoPieza(?,?,?)}",parametros);
    }

    @Override
    public void eliminarPieza(int idPieza) {
        List<Param> parametros = new ArrayList<>();
        parametros.add(new Param(idPieza,1,"int"));
        DbConnection.obtenerInstancia().ejecutarSpSinRes("{call dbo.eliminarPieza(?)}",parametros);
    }

    @Override
    public Piece ultimaEnMover(int idPartida) {
        Piece p = DbConnection.obtenerInstancia().ultimaEnMover(idPartida);
        asignarValor(p);
        return p;
    }

    private void asignarValor(Piece p){
        switch (p.getPieceType()){
            case PEON:
                if (p.getPieceColor().equals(PieceColor.BLANCA)) {
                    p.setValor(BPAWN);
                } else {
                    p.setValor(NPAWN);
                }
                break;
            case TORRE:
                if (p.getPieceColor().equals(PieceColor.BLANCA)) {
                    p.setValor(BROOK);
                } else {
                    p.setValor(NROOK);
                }
                break;
            case CABALLO:
                if (p.getPieceColor().equals(PieceColor.BLANCA)) {
                    p.setValor(BKNIGHT);
                } else {
                    p.setValor(NKNIGHT);
                }
                break;
            case ALFIL:
                if (p.getPieceColor().equals(PieceColor.BLANCA)) {
                    p.setValor(BBISHOP);
                } else {
                    p.setValor(NBISHOP);
                }
                break;
            case REINA:
                if (p.getPieceColor().equals(PieceColor.BLANCA)) {
                    p.setValor(BQUEEN);
                } else {
                    p.setValor(NQUEEN);
                }
                break;
            case REY:
                if (p.getPieceColor().equals(PieceColor.BLANCA)) {
                    p.setValor(BKING);
                } else {
                    p.setValor(NKING);
                }
                break;
        }
    }
}
