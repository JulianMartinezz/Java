package ar.edu.utn.frc.tup.lciii.Services;

import ar.edu.utn.frc.tup.lciii.Models.Piece;
import ar.edu.utn.frc.tup.lciii.Models.PieceColor;
import ar.edu.utn.frc.tup.lciii.Models.PieceType;
import ar.edu.utn.frc.tup.lciii.Models.Position;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class EstrategiaPieza {
    public abstract List<Position> obtenerMovimientosPosibles(Piece pieza, Piece[][] tablero);
    protected abstract void avanzar(Integer[] coords, DIRECCIONES direction);
    protected boolean puedeLlegar(Integer coordX,Integer coordY){
        boolean aux = false;
        if((coordX <= 7 && coordY <= 7) && (coordX >= 0 && coordY >= 0)) aux = true;
        return aux;
    }
    //Pasamos coordenadas de punto al cual nos queremos mover
    protected Piece hayPieza (Integer[] posicionActual, Piece[][] tablero) {
        if (tablero[posicionActual[0]][posicionActual[1]] == null) return null;
        return tablero[posicionActual[0]][posicionActual[1]];
    }

    //Implementaciones para jaque
    protected int reyAlinedo(Position piezaAtacante, Piece[][] tablero, PieceColor colorAtacante){
        Position reyContrario = encontrarReyContrario(colorAtacante,tablero);
        //Misma fila
        if(reyContrario.getFila() == piezaAtacante.getFila()){
            //Rey a la derecha
            if(reyContrario.getColumna() > piezaAtacante.getColumna()) return 7;
            else return 3;//El rey esta mas a la izquierda
        }
        //Misma columna
        if(reyContrario.getColumna() == piezaAtacante.getColumna()){

            //Rey esta mas arriba
            if(reyContrario.getFila() > piezaAtacante.getFila()) return 5;
            else return 1;
        }
        //Diagonales  IA a DA
        if((piezaAtacante.getFila() - reyContrario.getFila()) == (piezaAtacante.getColumna() - reyContrario.getColumna())){
            //Rey esta arriba a la derecha
            if((piezaAtacante.getFila() - reyContrario.getFila()) < 0) return 6;
            else return 2;
        }
        //Diagonales IA a DA
        if(reyContrario.getFila() == piezaAtacante.getColumna() && reyContrario.getColumna() == piezaAtacante.getFila()){
            //Rey arriba a la derecha
            if((reyContrario.getFila() - piezaAtacante.getFila()) > 0) return 4;
            else return 8;
        }

        return 0;
    }
    protected Position encontrarReyContrario(PieceColor colorAtacante, Piece[][] tablero){
        for (int i = 0; i < tablero.length;i++){
            for(int j = 0; j < tablero.length;j++){
                if(tablero[i][j] == null) continue;
                if(tablero[i][j].getPieceType().equals(PieceType.REY) &&
                        !tablero[i][j].getPieceColor().equals(colorAtacante)) return tablero[i][j].getPosition();
            }
        }
        return null;
    }
    protected void busquedaDescubierta(Position coords,int indiceBusqueda){
        switch (indiceBusqueda){
            case 1:coords.setFila(coords.getFila()+1); ;break;
            case 2:coords.setFila(coords.getFila()+1);coords.setColumna(coords.getColumna()+1); ;break;
            case 3:coords.setColumna(coords.getColumna()+1);break;
            case 4:coords.setFila(coords.getFila()-1);coords.setColumna(coords.getColumna()+1);break;
            case 5:coords.setFila(coords.getFila()-1)  ;break;
            case 6:coords.setFila(coords.getFila()-1);coords.setColumna(coords.getColumna()-1);break;
            case 7:coords.setColumna(coords.getColumna()-1);break;
            case 8:coords.setFila(coords.getFila()+1);coords.setColumna(coords.getColumna()-1);break;
        }
    }
    protected Piece posibleDescubierto(Position coords,Piece[][] tablero,int indiceBusqueda){
        Piece pieza = null;
        busquedaDescubierta(coords,indiceBusqueda);
        while(puedeLlegar(coords.getFila(), coords.getColumna())){
            if(tablero[coords.getFila()][coords.getColumna()] != null) return tablero[coords.getFila()][coords.getColumna()];
            busquedaDescubierta(coords,indiceBusqueda);
        }
        return pieza;
    }
    public abstract boolean jaqueAlRey(Piece pieza,Piece[][] tablero);
    public Map informacionDejaque(Piece pieza, Piece[][] tablero, boolean piezaDescubierta) {
        Map informacion = new HashMap();
        if(piezaDescubierta){
            informacion.put("jaque",jaqueAlRey(pieza,tablero));
            return informacion;
        }
        //Vamos a buscar si la pieza estaba alineada con el rey. En el caso que de true, setearemos como parametro
        //la pieza que tiene atras esta pieza, para que luego verifiquemos si se produjo un jaque al descubierto
        int indiceBusqueda = 0;

        indiceBusqueda = reyAlinedo(pieza.getPosicionAnterior(),tablero,pieza.getPieceColor());
        if( indiceBusqueda > 0){
            //Esto se debe calcular con la posicion anterior de la pieza, no la actuak
            Piece posibleJaque = posibleDescubierto(pieza.getPosicionAnterior(),tablero,indiceBusqueda);
            if(posibleJaque != null) informacion.put("posibleDescubierto",posibleJaque);
        }
        informacion.put("jaque",jaqueAlRey(pieza,tablero));
        return informacion;
    }
    public abstract List<Position> zonasAtacadas(Piece pieza, Piece[][] tablero);
}
