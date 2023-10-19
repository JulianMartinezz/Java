package ar.edu.utn.frc.tup.lciii.Services;

import ar.edu.utn.frc.tup.lciii.Models.Piece;
import ar.edu.utn.frc.tup.lciii.Models.PieceType;
import ar.edu.utn.frc.tup.lciii.Models.Position;

import java.util.ArrayList;
import java.util.List;

public class EstrategiaCaballo extends EstrategiaPieza{
    @Override
    public List<Position> obtenerMovimientosPosibles(Piece pieza, Piece[][] tablero) {
        List<Position> casillerosPosibles = new ArrayList<>();
        Integer[] coordsActuales = new Integer[2];
        for (DIRECCIONES direction: DIRECCIONES.values()){
            Piece piezaEnCoordenadaActual = null;
            coordsActuales[0] = pieza.getPosition().getFila();
            coordsActuales[1] = pieza.getPosition().getColumna();
            avanzar(coordsActuales,direction);
            //Si despues de avanzar sigue en la misma posicion, significa que esta pieza no tiene
            //habilitado esa direccion.
            if(pieza.getPosition().getFila() != coordsActuales[0] || pieza.getPosition().getColumna() != coordsActuales[1] ){
                if(puedeLlegar(coordsActuales[0],coordsActuales[1])){
                    piezaEnCoordenadaActual = hayPieza(coordsActuales, tablero);
                    //Esto significa que no hay ninguna pieza en ese casillero
                    if(piezaEnCoordenadaActual == null) {
                        casillerosPosibles.add(new Position(coordsActuales[0], coordsActuales[1]));
                    }else{
                        if(!pieza.getPieceColor().equals(piezaEnCoordenadaActual.getPieceColor())){
                            if(!piezaEnCoordenadaActual.getPieceType().equals(PieceType.REY)){
                                casillerosPosibles.add(new Position(coordsActuales[0], coordsActuales[1]));
                            }
                        }
                    }

                }
            }

        }
        return casillerosPosibles;
    }
    @Override
    public void avanzar(Integer[] coords, DIRECCIONES direction) {
        switch (direction){
            //Derecha abajo
            case ESTE: coords[0]--;coords[1]+=2;
                break;
            //Abajo derecha
            case SURESTE:coords[0]-=2;coords[1]++;
                break;
            //Abajo Izquierda
            case SUR:coords[0]-=2;coords[1]--;
                break;
            //Izquierda Abajo
            case SUROESTE:coords[0]--;coords[1]-=2;
                break;
            //Izquierda Arriba
            case OESTE:coords[0]++;coords[1]-=2;
                break;
            //Arriba Izquierda
            case NOROESTE:coords[0]+=2;coords[1]--;
                break;
            //Arriba derecha
            case NORTE:coords[0]+=2;coords[1]++;
                break;
            //Derecha arriba
            case NORESTE:coords[0]++;coords[1]+=2;
                break;
        }
    }

    @Override
    public boolean jaqueAlRey(Piece pieza, Piece[][] tablero) {
        Integer[] coordsActuales = new Integer[2];

        for (DIRECCIONES direction: DIRECCIONES.values()){
            Piece piezaEnCoordenadaActual = null;
            coordsActuales[0] = pieza.getPosition().getFila();
            coordsActuales[1] = pieza.getPosition().getColumna();
            avanzar(coordsActuales,direction);
            //Si despues de avanzar sigue en la misma posicion, significa que esta pieza no tiene
            //habilitado esa direccion.
            if(pieza.getPosition().getFila() != coordsActuales[0] || pieza.getPosition().getColumna() != coordsActuales[1] ){
                if(puedeLlegar(coordsActuales[0],coordsActuales[1])){
                    piezaEnCoordenadaActual = hayPieza(coordsActuales, tablero);
                    //Esto significa que no hay ninguna pieza en ese casillero
                    if(piezaEnCoordenadaActual != null) {
                        if(!pieza.getPieceColor().equals(piezaEnCoordenadaActual.getPieceColor())){
                            if(piezaEnCoordenadaActual.getPieceType().equals(PieceType.REY)) return true;
                        }
                    }

                }
            }

        }
        return false;
    }

    @Override
    public List<Position> zonasAtacadas(Piece pieza, Piece[][] tablero) {
        List<Position> zonasAtacadas = new ArrayList<>();
        Integer[] coordsActuales = new Integer[2];
        for (DIRECCIONES direction: DIRECCIONES.values()){
            coordsActuales[0] = pieza.getPosition().getFila();
            coordsActuales[1] = pieza.getPosition().getColumna();
            avanzar(coordsActuales,direction);
            if(pieza.getPosition().getFila() == coordsActuales[0] && pieza.getPosition().getColumna() == coordsActuales[1]) continue;
            if(!puedeLlegar(coordsActuales[0],coordsActuales[1])) continue;
            zonasAtacadas.add(new Position(coordsActuales[0],coordsActuales[1]));
        }
        return zonasAtacadas;
    }
}
