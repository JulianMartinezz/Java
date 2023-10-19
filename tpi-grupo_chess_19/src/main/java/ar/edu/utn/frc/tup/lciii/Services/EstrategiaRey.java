package ar.edu.utn.frc.tup.lciii.Services;

import ar.edu.utn.frc.tup.lciii.Models.Piece;
import ar.edu.utn.frc.tup.lciii.Models.PieceColor;
import ar.edu.utn.frc.tup.lciii.Models.Position;

import java.util.ArrayList;
import java.util.List;

public class EstrategiaRey extends EstrategiaPieza{
    @Override
    public List<Position> obtenerMovimientosPosibles(Piece pieza, Piece[][] tablero) {
        List<Position> casillerosPosibles = new ArrayList<>();
        List<Piece> piezasContrarias = encontrarPiezasContrarias(pieza.getPieceColor(),tablero);
        boolean zonaAtacada = false;
        Integer[] coordsActuales = new Integer[2];

        for (DIRECCIONES direction: DIRECCIONES.values()){
            Piece piezaEnCoordenadaActual = null;

            coordsActuales[0] = pieza.getPosition().getFila();
            coordsActuales[1] = pieza.getPosition().getColumna();
            avanzar(coordsActuales,direction);

            if(puedeLlegar(coordsActuales[0],coordsActuales[1])) {
                piezaEnCoordenadaActual = hayPieza(coordsActuales, tablero);
                //Esto significa que no hay ninguna pieza en ese casiller
                if (piezaEnCoordenadaActual != null && piezaEnCoordenadaActual.getPieceColor().equals(pieza.getPieceColor())) continue;
                for (int i = 0; i < piezasContrarias.size(); i++) {
                    for(int j = 0; j < piezasContrarias.get(i).getZonaAtacada().size(); j++){
                        if (piezasContrarias.get(i).getZonaAtacada().get(j).getFila() == coordsActuales[0] &&
                                piezasContrarias.get(i).getZonaAtacada().get(j).getColumna() == coordsActuales[1]) {
                            zonaAtacada = true;
                            break;
                        }
                    }

                }
                if(!zonaAtacada) casillerosPosibles.add(new Position(coordsActuales[0], coordsActuales[1]));
                zonaAtacada = false;
            }

        }
        return casillerosPosibles;
    }
    @Override
    public void avanzar(Integer[] coords, DIRECCIONES direction) {
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
    private List<Piece> encontrarPiezasContrarias(PieceColor colorAtacante, Piece[][] tablero){
        List<Piece> piezas = new ArrayList<>();
        for(int i = 0; i < tablero.length;i++){
            for(int j = 0; j < tablero.length;j++){
                if(tablero[i][j] != null && !tablero[i][j].getPieceColor().equals(colorAtacante)){
                    piezas.add(tablero[i][j]);
                }
            }
        }
        return piezas;
    }
    @Override
    public boolean jaqueAlRey(Piece pieza, Piece[][] tablero) {
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

            if(!puedeLlegar(coordsActuales[0],coordsActuales[1])) continue;
            zonasAtacadas.add(new Position(coordsActuales[0],coordsActuales[1]));
        }
        return zonasAtacadas;
    }
}
