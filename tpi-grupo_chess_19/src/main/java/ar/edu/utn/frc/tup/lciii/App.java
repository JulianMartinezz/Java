package ar.edu.utn.frc.tup.lciii;

import ar.edu.utn.frc.tup.lciii.Models.Piece;
import ar.edu.utn.frc.tup.lciii.Models.Player;
import ar.edu.utn.frc.tup.lciii.Services.ChessGame;
import ar.edu.utn.frc.tup.lciii.Services.ChessMatch;

import java.util.List;
import java.util.Map;

/**
 * Hello to TPI Chess
 *
 */
public class App 
{

    /**
     * This is the main program
     * 
     */
    private static ChessMatch partida = new ChessMatch();
    public static void main( String[] args ) {
        partida.mensajeBienvenida();
        Map eleccion = null;
        boolean playAgain = true;
        Piece piezaElegida = null;
        ChessGame juego = null;
        List<Player> jugadores = null;
        int idPartida = -1;
        boolean partidaGuardada = false;
        boolean guardar = false;
        String seleccion = partida.seleccionMenu();
        if(seleccion.equals("1")){
            jugadores = partida.crearJugadores();

        } else if(seleccion.equals("2")){
            partidaGuardada = true;
        }
        do {
            if(seleccion.equals("3")){
                System.out.println("Gracias por jugar");
                break;
            }
            if(!partidaGuardada){
                juego = new ChessGame(jugadores.get(0),jugadores.get(1));
                juego.iniciarPartida();
            }else{
                idPartida = partida.elegirPartidaGuardada();
                jugadores = partida.recuperarJugadores(idPartida);
                juego = new ChessGame(jugadores.get(0),jugadores.get(1));
                juego.iniciarPartidaGuardada(idPartida);
            }
            do{
                juego.imprimirTablero();

                eleccion = juego.elegirPiezaOGurdar();
                if(eleccion.get("decision").equals("GUARDAR"))break;
                piezaElegida = (Piece) eleccion.get("piezaElegida");

                while(!juego.pedirMovimiento(piezaElegida)){
                    if(juego.cambiarPieza()){
                        //Si hago break solo saldra de este ciclo
                        eleccion = juego.elegirPiezaOGurdar();
                        if(eleccion.get("decision").equals("GUARDAR")){
                            guardar = true;
                            break;
                        }
                        piezaElegida = (Piece) eleccion.get("piezaElegida");
                    }
                }
                if (guardar){
                    guardar = false;
                    break;
                }
            }while (!juego.terminado());
            juego.imprimirTablero();
            juego.mensajeSalida();
            playAgain = partida.jugarDeNuevo();
            if(!playAgain) break;
            partidaGuardada = false;
            seleccion = partida.seleccionMenu();
            if(seleccion.equals("2")){
                partidaGuardada = true;
            }
        }while (playAgain);
    }
}
