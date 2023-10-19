package ar.edu.utn.frc.tup.lciii.Services;

import ar.edu.utn.frc.tup.lciii.Models.Piece;
import ar.edu.utn.frc.tup.lciii.Models.PieceType;
import ar.edu.utn.frc.tup.lciii.Models.Position;

import java.util.ArrayList;
import java.util.List;

public class EstrategiaReina extends EstrategiaPieza{
    @Override
    public List<Position> obtenerMovimientosPosibles(Piece pieza, Piece[][] tablero) {
        List<Position> casillerosPosibles =  new ArrayList<>();
        Integer[] coordsActuales = new Integer[2];
        for (DIRECCIONES direction: DIRECCIONES.values()){
            Piece piezaEnCoordenadaActual = null;
            coordsActuales[0] = pieza.getPosition().getFila();
            coordsActuales[1] = pieza.getPosition().getColumna();
            avanzar(coordsActuales,direction);
            //Si despues de avanzar sigue en la misma posicion, significa que esta pieza no tiene
            //habilitado esa direccion.
            if(pieza.getPosition().getFila() != coordsActuales[0] || pieza.getPosition().getColumna() != coordsActuales[1] ){
                while(puedeLlegar(coordsActuales[0],coordsActuales[1])){
                    piezaEnCoordenadaActual = hayPieza(coordsActuales,tablero);
                    //Esto significa que no hay ninguna pieza en ese casiller
                    if(piezaEnCoordenadaActual == null) {
                        casillerosPosibles.add(new Position(coordsActuales[0], coordsActuales[1]));
                    }else{
                        if(!pieza.getPieceColor().equals(piezaEnCoordenadaActual.getPieceColor())){
                            if(!piezaEnCoordenadaActual.getPieceType().equals(PieceType.REY)){
                                casillerosPosibles.add(new Position(coordsActuales[0], coordsActuales[1]));
                            }
                        }
                        break;
                    }
                    piezaEnCoordenadaActual = null;
                    avanzar(coordsActuales,direction);
                }

            }

        }
        return casillerosPosibles;
    }

    //Movemos pieza en una direccion
    public void avanzar(Integer[] coords,DIRECCIONES direction){
        switch (direction){
            case ESTE: coords[1]++;
                break;
            case SURESTE:coords[0]--;coords[1]++;
                break;

            case SUR:coords[0]--;
                break;

            case SUROESTE:coords[0]--;coords[1]--;
                break;

            case OESTE:coords[1]--;
                break;

            case NOROESTE:coords[0]++;coords[1]--;
                break;

            case NORTE:coords[0]++;
                break;

            case NORESTE:coords[0]++;coords[1]++;
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
            //Muevo coordenada de pieza
            avanzar(coordsActuales,direction);
            //Si despues de avanzar sigue en la misma posicion, significa que esta pieza no tiene
            //habilitado esa direccion.

            if (pieza.getPosition().getFila() != coordsActuales[0] || pieza.getPosition().getColumna() != coordsActuales[1]) {
                while (puedeLlegar(coordsActuales[0], coordsActuales[1])) {
                    piezaEnCoordenadaActual = hayPieza(coordsActuales, tablero);
                    if (piezaEnCoordenadaActual != null) {
                        if (pieza.getPieceColor().equals(piezaEnCoordenadaActual.getPieceColor())) break;
                        if(!pieza.getPieceColor().equals(piezaEnCoordenadaActual.getPieceColor()) && piezaEnCoordenadaActual.getPieceType().equals(PieceType.REY)) return true;
                        break;
                    }
                    avanzar(coordsActuales, direction);
                }
            }
        }
        return false;
    }

    @Override
    public List<Position> zonasAtacadas(Piece pieza, Piece[][] tablero) {
        List<Position> zonasAtacadas =  new ArrayList<>();
        Integer[] coordsActuales = new Integer[2];
        for (DIRECCIONES direction: DIRECCIONES.values()){
            Piece piezaEnCoordenadaActual = null;
            coordsActuales[0] = pieza.getPosition().getFila();
            coordsActuales[1] = pieza.getPosition().getColumna();
            avanzar(coordsActuales,direction);

            if(pieza.getPosition().getFila() == coordsActuales[0] && pieza.getPosition().getColumna() == coordsActuales[1]) continue;;
            while(puedeLlegar(coordsActuales[0],coordsActuales[1])){
                piezaEnCoordenadaActual = hayPieza(coordsActuales,tablero);

                if(piezaEnCoordenadaActual == null) {
                    zonasAtacadas.add(new Position(coordsActuales[0], coordsActuales[1]));
                }else{
                    zonasAtacadas.add(new Position(coordsActuales[0], coordsActuales[1]));
                    break;
                }
                avanzar(coordsActuales,direction);
            }
        }
        return zonasAtacadas;
    }
}
