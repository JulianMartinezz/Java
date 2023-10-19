package ar.edu.utn.frc.tup.lciii.Services;


import ar.edu.utn.frc.tup.lciii.Models.Piece;
import ar.edu.utn.frc.tup.lciii.Models.PieceType;
import ar.edu.utn.frc.tup.lciii.Models.Position;

public  class TraductorNotacion {
    private static TraductorNotacion  instancia;
    private static String[] letras =  {"A","B","C","D","E","F","G","H"};
    private TraductorNotacion(){
    }
    public static TraductorNotacion obtenerInstancia(){
        if(instancia == null){
            instancia = new TraductorNotacion();
        }
        return instancia;
    }
    public  String convertirNotacion(Piece piezaAtacante, Position posDestino, Piece[][] tablero){
        boolean come = false;
        if(tablero[posDestino.getFila()][posDestino.getColumna()] != null &&
                !tablero[posDestino.getFila()][posDestino.getColumna()].getPieceType().equals(PieceType.REY)){
            come = true;
        }
        StringBuilder resultado = new StringBuilder();
        resultado.append(letraAsignada(piezaAtacante));
        if(piezaAtacante.getPieceType().equals(PieceType.PEON) && come) resultado.append(letras[piezaAtacante.getPosition().getColumna()]);
       /* if(hayAmbiguedad(piezaAtacante,posDestino,tablero)){
            resultado.append(letras[piezaAtacante.getPosition().getColumna()]);
        }*/
        if(come) resultado.append("x");

        resultado.append(traductorCoordenada(posDestino));
        /*if(tablero[posDestino.getFila()][posDestino.getColumna()] != null  &&
                tablero[posDestino.getFila()][posDestino.getColumna()].getPieceType().equals(PieceType.REY)){
            resultado.append("+");
        }*/
        return resultado.toString();
    }
    private String letraAsignada(Piece pieza){
        String letraASignada = "";
        switch (pieza.getPieceType()){
            case PEON:break;
            case TORRE:letraASignada = "R";break;
            case CABALLO:letraASignada = "N";break;
            case ALFIL:letraASignada = "B";break;
            case REINA:letraASignada = "Q";break;
            case REY:letraASignada = "K";break;
        }
        return letraASignada;
    }
    private String traductorCoordenada(Position posDestino){
        String destino = "";
        destino = letras[posDestino.getColumna()];
        destino += posDestino.getFila();
        return destino;
    }
    private boolean hayAmbiguedad(Piece piezaAtacante,Position p,Piece[][] tablero){
        return false;
    }
}
