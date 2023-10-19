package ar.edu.utn.frc.tup.lciii.Services;

import ar.edu.utn.frc.tup.lciii.Data.Impl.JugadoresDaoImpl;
import ar.edu.utn.frc.tup.lciii.Data.Impl.PartidasDaoImp;
import ar.edu.utn.frc.tup.lciii.Data.Interfaces.IJugadoresDao;
import ar.edu.utn.frc.tup.lciii.Data.Interfaces.IPartidasDao;
import ar.edu.utn.frc.tup.lciii.Models.Partida;
import ar.edu.utn.frc.tup.lciii.Models.PieceColor;
import ar.edu.utn.frc.tup.lciii.Models.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class ChessMatch {
    private static IPartidasDao partidasDao;
    private static IJugadoresDao jugadoresDao;
    private static final String MENU_REGEX = "[1-3]";
    private Scanner s;
    private static final String YES_NO_REGEX = "[yYnN]";
    public ChessMatch() {

        this.s = new Scanner(System.in);
        this.partidasDao = new PartidasDaoImp();
        this.jugadoresDao = new JugadoresDaoImpl();
    }
    public void mensajeBienvenida() {
        System.out.println("Bienvenido a Ajedrez");
    }

    public List<Player> crearJugadores() {
        Player player1 = new Player();
        Player player2 = new Player();
        List<Player> jugadores = new ArrayList<>();
        System.out.println("Ingrese nombre Jugador (BLANCAS)");
        player1.setNombre(s.nextLine());
        player1.setColor(PieceColor.BLANCA);
        jugadores.add(player1);
        System.out.println("Ingrese nombre Jugador (NEGRAS)");
        player2.setNombre(s.nextLine());
        player2.setColor(PieceColor.NEGRA);
        jugadores.add(player2);
        return jugadores;
    }
    public String seleccionMenu(){
        String respuesta = "-1";
        do {
            System.out.println("Ingrese el número de la opcion seleccionada");
            System.out.println("1.Nueva partida");
            System.out.println("2.Partida Guardada");
            System.out.println("3.Salir");
            String input = s.nextLine();
            respuesta = respuestaPartida(input);
        } while (respuesta == "-1");
        return respuesta;

    }
    private  String respuestaPartida(String input) {
        Pattern pattern = Pattern.compile(MENU_REGEX);
        String respuesta = "-1";
        if (pattern.matcher(input).matches()) {
            respuesta = input;
        } else {
            System.out.println("La opción ingresada no es valida.");
        }
        return respuesta;
    }
    public int elegirPartidaGuardada(){
        int resultado = -1;
        List<Partida> partidas = recuperarPartidasGuardadas();
        mostrarPartidadGuadadas(partidas);
        do{
            System.out.println("Selecionne id de la partida");
            String partida = s.nextLine();
            resultado = validarPartida(partidas,partida);
        }while( resultado == -1);
        return resultado;
    }
    private int validarPartida(List<Partida> partidas, String p){
        int resultado = -1;

        for(int i = 0; i < partidas.size();i++) {
            if (partidas.get(i).getIdPartida() == Integer.parseInt(p)) {
                resultado = partidas.get(i).getIdPartida();
            }
        }
        return resultado;

    }
    private List<Partida> recuperarPartidasGuardadas(){
        List<Partida> partidas = partidasDao.obtenerGuardadas();
        return partidas;
    }
    private void mostrarPartidadGuadadas(List<Partida> partidas){
        StringBuilder sb = new StringBuilder();
        partidas.forEach(p -> {
            sb.append(p.getIdPartida() + "  " + p.getFechaInicio().toString() + "  " + p.getJugadores() + "\n");
        });
        System.out.println("Partidas Guardadas:");
        System.out.println(sb);
    }
    public List<Player> recuperarJugadores(int idPartida){
        List<Player> jugadores = jugadoresDao.obtener(idPartida);
        return jugadores;
    }
    private  Boolean recibirRespuesta(String input) {
        Pattern pattern = Pattern.compile(YES_NO_REGEX);
        Boolean respuesta = null;
        if (pattern.matcher(input).matches()) {
            if(input.toLowerCase().equals("y")) {
                respuesta = true;
            } else {
                respuesta = false;
            }
        } else {
            System.out.println("La opción ingresada no es valida.");
        }
        return respuesta;
    }
    public boolean jugarDeNuevo() {
        Boolean respuesta = null;
        do {
            System.out.println("¿Quieres volver a jugar? (y/n)");
            String input = s.nextLine();
            respuesta = recibirRespuesta(input);
        } while (respuesta == null);
        return respuesta;
    }
}
