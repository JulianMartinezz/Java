package ar.edu.utn.frc.tup.lciii.Services;

import ar.edu.utn.frc.tup.lciii.Models.Piece;
import ar.edu.utn.frc.tup.lciii.Models.PieceColor;
import ar.edu.utn.frc.tup.lciii.Models.PieceType;
import ar.edu.utn.frc.tup.lciii.Models.Position;

import java.util.ArrayList;
import java.util.List;

public class EstrategiaPeon extends EstrategiaPieza{
    @Override
    public List<Position> obtenerMovimientosPosibles(Piece pieza, Piece[][] tablero) {
        List<Position> casillerosPosibles = new ArrayList<>();
        Integer[] coordsActuales = new Integer[2];
        for (DIRECCIONES direction: DIRECCIONES.values()){
            if(pieza.getPieceColor().equals(PieceColor.BLANCA) && (direction.equals(DIRECCIONES.SUR) ||
                    direction.equals(DIRECCIONES.SURESTE) ||  direction.equals(DIRECCIONES.SUROESTE))) continue;
            if(pieza.getPieceColor().equals(PieceColor.NEGRA) && (direction.equals(DIRECCIONES.NORTE) ||
                    direction.equals(DIRECCIONES.NORESTE) || direction.equals(DIRECCIONES.NOROESTE))) continue;
            Piece piezaEnCoordenadaActual = null;
            coordsActuales[0] = pieza.getPosition().getFila();
            coordsActuales[1] = pieza.getPosition().getColumna();
            avanzar(coordsActuales,direction);
            //Si despues de avanzar sigue en la misma posicion, significa que esta pieza no tiene
            //habilitado esa direccion.
            if(pieza.getPosition().getFila() == coordsActuales[0] && pieza.getPosition().getColumna() == coordsActuales[1] ) continue;
            if(!puedeLlegar(coordsActuales[0],coordsActuales[1])) continue;
            piezaEnCoordenadaActual = hayPieza(coordsActuales, tablero);
            //Esto significa que no hay ninguna pieza en ese casillerp
            if(piezaEnCoordenadaActual == null) {
                //Con esto validamos que no pueda avanzar en diagonal si no hay ninguna pieza
                if(pieza.getPieceColor().equals(PieceColor.BLANCA) && direction.equals(DIRECCIONES.NORTE)) casillerosPosibles.add(new Position(coordsActuales[0], coordsActuales[1]));
                if(pieza.getPieceColor().equals(PieceColor.BLANCA) && direction.equals(DIRECCIONES.NORTE) && pieza.getPosition().getFila() == 1)
                    casillerosPosibles.add(new Position(coordsActuales[0] + 1, coordsActuales[1]));
                if(pieza.getPieceColor().equals(PieceColor.NEGRA) && direction.equals(DIRECCIONES.SUR)) casillerosPosibles.add(new Position(coordsActuales[0], coordsActuales[1]));
                if(pieza.getPieceColor().equals(PieceColor.NEGRA) && direction.equals(DIRECCIONES.SUR) && pieza.getPosition().getFila() == 6)
                    casillerosPosibles.add(new Position(coordsActuales[0] - 1, coordsActuales[1]));
            }else{
                if(!pieza.getPieceColor().equals(piezaEnCoordenadaActual.getPieceColor())){
                    //Con esto validamos que el peon no pueda comer hacia delante y solo lo haga en diagonal
                    if(pieza.getPieceColor().equals(PieceColor.BLANCA) && !direction.equals(DIRECCIONES.NORTE)){
                        if(!piezaEnCoordenadaActual.getPieceType().equals(PieceType.REY)){
                            casillerosPosibles.add(new Position(coordsActuales[0], coordsActuales[1]));
                        }
                    }
                    if(pieza.getPieceColor().equals(PieceColor.NEGRA) && !direction.equals(DIRECCIONES.SUR)){
                        if(!piezaEnCoordenadaActual.getPieceType().equals(PieceType.REY)){
                            casillerosPosibles.add(new Position(coordsActuales[0], coordsActuales[1]));
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
            case SUR:coords[0]--;
                break;
            case SURESTE:coords[0]--;coords[1]++;
                break;
            case SUROESTE:coords[0]--;coords[1]--;
                break;
            case NORTE:coords[0]++;
                break;
            case NORESTE: coords[0]++;coords[1]++;break;
            case NOROESTE: coords[0]++;coords[1]--; break;
        }
    }

    @Override
    public boolean jaqueAlRey(Piece pieza, Piece[][] tablero) {
        Integer[] coordsActuales = new Integer[2];
        for (DIRECCIONES direction: DIRECCIONES.values()){
            if(pieza.getPieceColor().equals(PieceColor.BLANCA) && (direction.equals(DIRECCIONES.SUR) ||
                    direction.equals(DIRECCIONES.SURESTE) ||  direction.equals(DIRECCIONES.SUROESTE))) continue;
            if(pieza.getPieceColor().equals(PieceColor.NEGRA) && (direction.equals(DIRECCIONES.NORTE) ||
                    direction.equals(DIRECCIONES.NORESTE) || direction.equals(DIRECCIONES.NOROESTE))) continue;
            Piece piezaEnCoordenadaActual = null;
            coordsActuales[0] = pieza.getPosition().getFila();
            coordsActuales[1] = pieza.getPosition().getColumna();
            avanzar(coordsActuales,direction);
            //Si despues de avanzar sigue en la misma posicion, significa que esta pieza no tiene
            //habilitado esa direccion.
            if(pieza.getPosition().getFila() == coordsActuales[0] && pieza.getPosition().getColumna() == coordsActuales[1] ) continue;
            if(!puedeLlegar(coordsActuales[0],coordsActuales[1])) continue;
            piezaEnCoordenadaActual = hayPieza(coordsActuales, tablero);
            //Esto significa que no hay ninguna pieza en ese casillerp
            if(piezaEnCoordenadaActual != null) {
                if(!pieza.getPieceColor().equals(piezaEnCoordenadaActual.getPieceColor())){
                    //Con esto validamos que el peon no pueda comer hacia delante y solo lo haga en diagonal
                    if(pieza.getPieceColor().equals(PieceColor.BLANCA) && !direction.equals(DIRECCIONES.NORTE)){
                        if(piezaEnCoordenadaActual.getPieceType().equals(PieceType.REY))return true;
                    }
                    if(pieza.getPieceColor().equals(PieceColor.NEGRA) && !direction.equals(DIRECCIONES.SUR)){
                        if(piezaEnCoordenadaActual.getPieceType().equals(PieceType.REY))return true;
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
        for (DIRECCIONES direction : DIRECCIONES.values()) {
            if (pieza.getPieceColor().equals(PieceColor.BLANCA) && (direction.equals(DIRECCIONES.SURESTE) ||
                    direction.equals(DIRECCIONES.SUROESTE))) continue;
            if (pieza.getPieceColor().equals(PieceColor.NEGRA) && (direction.equals(DIRECCIONES.NORESTE) ||
                    direction.equals(DIRECCIONES.NOROESTE))) continue;
            if (direction.equals(DIRECCIONES.NORTE) || direction.equals(DIRECCIONES.SUR)) continue;
            coordsActuales[0] = pieza.getPosition().getFila();
            coordsActuales[1] = pieza.getPosition().getColumna();
            avanzar(coordsActuales, direction);
            if (pieza.getPosition().getFila() == coordsActuales[0] && pieza.getPosition().getColumna() == coordsActuales[1])
                continue;
            if (!puedeLlegar(coordsActuales[0], coordsActuales[1])) continue;
            zonasAtacadas.add(new Position(coordsActuales[0], coordsActuales[1]));
        }

        return zonasAtacadas;
    }
}
