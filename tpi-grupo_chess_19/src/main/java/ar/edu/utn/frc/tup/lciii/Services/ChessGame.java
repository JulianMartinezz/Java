package ar.edu.utn.frc.tup.lciii.Services;

import ar.edu.utn.frc.tup.lciii.Data.Impl.PartidasDaoImp;
import ar.edu.utn.frc.tup.lciii.Data.Impl.PiezasDao;
import ar.edu.utn.frc.tup.lciii.Data.Interfaces.IPartidasDao;
import ar.edu.utn.frc.tup.lciii.Data.Interfaces.IPiezasDao;
import ar.edu.utn.frc.tup.lciii.Models.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChessGame {
    private IPiezasDao piezasDao;
    private IPartidasDao partidasDao;
    private int idPartida;
    private String notacion;
    private EstrategiaPieza estrategia;
    private Board tablero;
    private Player jugador1;
    private List<Position> zonasAtacadasBlancas;
    private Player jugador2;
    private List<Position> zonasAtacadasNegras;
    private Player turno;
    private Player defensor;
    private Player ganador;
    private Scanner s;
    private Piece ultimaEnMover;
    private boolean huboJaque;
    private static final String YES_NO_REGEX = "[yYnN]";
    private static final String POSICION_INPUT_REGEX = "[0-7]{1} [0-7]{1}";

    public ChessGame(Player j1, Player j2) {
        this.piezasDao = new PiezasDao();
        this.partidasDao = new PartidasDaoImp();
        this.idPartida = -1;
        this.tablero = new Board();
        this.jugador1 = j1;
        this.jugador2 = j2;
        this.turno = j1;
        this.defensor = j2;
        this.ganador = null;
        this.s = new Scanner(System.in);
        this.zonasAtacadasBlancas = new ArrayList<>();
        this.zonasAtacadasNegras = new ArrayList<>();
        this.ultimaEnMover = null;
        this.huboJaque = false;
        this.estrategia = null;
        this.notacion = "";
    }
    public void iniciarPartida(){
     int idPartida = partidasDao.crear(this.jugador1.getNombre(),this.jugador2.getNombre());
     this.idPartida = idPartida;
     List<Piece> piezasIniciales = piezasDao.obtenerTodas(idPartida);
     llenarTablero(piezasIniciales);
     //llenarTablero(PieceColor.BLANCA,tablero);
     //llenarTablero(PieceColor.NEGRA,tablero);
     calcularZonasAtacadas();
     System.out.println("Turno: " + this.turno.getNombre()+"("+this.turno.getColor()+")");
 }
    public void iniciarPartidaGuardada(int idPartida){
        this.idPartida = idPartida;
        List<Piece> piezasIniciales = piezasDao.obtenerTodas(idPartida);
        llenarTablero(piezasIniciales);
        calcularZonasAtacadas();
        this.ultimaEnMover = piezasDao.ultimaEnMover(idPartida);


        if(ultimaEnMover.getPieceColor().equals(this.jugador1.getColor())){
            this.turno = jugador2;
            this.defensor = jugador1;
        }else{
            this.turno = jugador1;
            this.defensor = jugador2;
        }
        System.out.println("Turno: " + this.turno.getNombre()+"("+this.turno.getColor()+")");
    }
    private void llenarTablero(List<Piece> piezas){
        piezas.forEach(pieza -> {
            this.tablero.getTablero()[pieza.getPosition().getFila()][pieza.getPosition().getColumna()] = pieza;
        });
    }
    public void cambiarEstrategia(PieceType tipo){
        switch (tipo){
            case PEON: this.estrategia = new EstrategiaPeon();break;
            case TORRE: this.estrategia = new EstrategiaTorre();break;
            case CABALLO: this.estrategia = new EstrategiaCaballo();break;
            case ALFIL: this.estrategia = new EstrategiaAlfil();break;
            case REY: this.estrategia = new EstrategiaRey();break;
            case REINA: this.estrategia = new EstrategiaReina();break;
        }
    }
    public void imprimirTablero() {
        StringBuilder sb = new StringBuilder();
        sb.append("  --------------------------------------\n");
        boolean aux = false;
        for(int f = tablero.getTablero().length-1; f >= 0; f--) {
            sb.append(f + " |");
            for(int c = 0; c < tablero.getTablero().length; c++) {
                if(this.tablero.getTablero()[f][c] == null){
                    sb.append(" \u2A37 ");
                }else{
                    sb.append(" " + this.tablero.getTablero()[f][c].getValor() + " ");
                }
                sb.append("|");
            }
            sb.append("\n");
            sb.append("  --------------------------------------\n");
        }
        sb.append("    A    B    C   D    E    F    G    H\n");
        System.out.println(sb);
    }
    public List<Position> movimientosDisponibles(Piece pieza){
        cambiarEstrategia(pieza.getPieceType());
        return estrategia.obtenerMovimientosPosibles(pieza,this.tablero.getTablero());
    }
    private Map informacionJaque(Piece pieza){
        cambiarEstrategia(pieza.getPieceType());
        Map informacion = estrategia.informacionDejaque(pieza,this.tablero.getTablero(),false);
        if(informacion.containsKey("posibleDescubierto")){
            cambiarEstrategia(((Piece)informacion.get("posibleDescubierto")).getPieceType());
            boolean jaqueDescubierto = Boolean.parseBoolean(estrategia.informacionDejaque((Piece) informacion.get("posibleDescubierto"),this.tablero.getTablero(),true).get("jaque").toString());
            informacion.put("jaqueAlDescubierto",jaqueDescubierto);
        }else informacion.put("jaqueAlDescubierto",false);

        return informacion;
    }
    public int actualizarPosicionPieza(Piece pieza, Position nuevaPosicion)   {
        int comio = -1;
        Position coordenadasAnteriores = pieza.getPosition();
        pieza.getMovRealizados().add(nuevaPosicion);
        this.notacion = TraductorNotacion.obtenerInstancia().convertirNotacion(pieza,nuevaPosicion, tablero.getTablero());
        if(this.tablero.getTablero()[nuevaPosicion.getFila()][nuevaPosicion.getColumna()] != null){
            //this.tablero[nuevaPosicion.getFila()][nuevaPosicion.getColumna()].FueraDeJuego();
            comio = this.tablero.getTablero()[nuevaPosicion.getFila()][nuevaPosicion.getColumna()].getIdPieza();
            pieza.getPiezsComidas().add(this.tablero.getTablero()[nuevaPosicion.getFila()][nuevaPosicion.getColumna()]);
        }
        //Actualizamos posicion de pieza atacante
        this.tablero.getTablero()[pieza.getPosition().getFila()][pieza.getPosition().getColumna()].setPosition(nuevaPosicion);

        //Movemos pieza atacante a nueva posicion
        //Tendra el vaor de la pieza que pasamos por parametro o de la nueva pieza?
        this.tablero.getTablero()[nuevaPosicion.getFila()][nuevaPosicion.getColumna()] = pieza;
        this.tablero.getTablero()[nuevaPosicion.getFila()][nuevaPosicion.getColumna()].setPosicionAnterior(coordenadasAnteriores);
        //Posicion anterior de pieza, seteamos a null para que no existan fichas duplicadas
        this.tablero.getTablero()[coordenadasAnteriores.getFila()][coordenadasAnteriores.getColumna()] = null;

        return comio;

    }
    private void actualizarListasZonasAtacadas(){
        zonasAtacadasBlancas.removeAll(zonasAtacadasBlancas);
        zonasAtacadasNegras.removeAll(zonasAtacadasNegras);
        for(int i = 0; i < tablero.getTablero().length;i++){
            for(int j = 0; j < tablero.getTablero().length;j++){
                if(tablero.getTablero()[i][j] == null) continue;
                if(tablero.getTablero()[i][j].getPieceColor().equals(PieceColor.BLANCA)){
                    actualizarLista(zonasAtacadasBlancas,tablero.getTablero()[i][j]);
                }else{
                    actualizarLista(zonasAtacadasNegras,tablero.getTablero()[i][j]);
                }

            }
        }
    }
    private void actualizarLista(List<Position> zonas,Piece pieza){
        for(int i = 0; i < pieza.getZonaAtacada().size();i++){
            if(!zonas.contains(pieza.getZonaAtacada().get(i))) zonas.add(pieza.getZonaAtacada().get(i));
        }
    }
    private void calcularZonasAtacadasPieza(Piece pieza){
        cambiarEstrategia(pieza.getPieceType());
        pieza.setZonaAtacada(estrategia.zonasAtacadas(pieza,this.tablero.getTablero()));
    }
    private void calcularZonasAtacadas(){
        for(int i = 0; i < tablero.getTablero().length;i++){
            for(int j = 0; j < tablero.getTablero().length;j++){
                if(tablero.getTablero()[i][j] != null)
                    calcularZonasAtacadasPieza(tablero.getTablero()[i][j]);
            }
        }
        actualizarListasZonasAtacadas();
    }

    private void cambiarTurno(){
        if(this.turno.equals(this.jugador1)){
            this.turno = jugador2;
            this.defensor = jugador1;
        }else{
            this.turno = jugador1;
            this.defensor = jugador2;

        }
        System.out.println("Le toca a: " + this.turno.getNombre()+".("+this.turno.getColor()+")");
    }
    private boolean inputValido(String input){
        Pattern p = Pattern.compile(ChessGame.POSICION_INPUT_REGEX);
        Matcher matcher = p.matcher(input);
        return matcher.matches();
    }
    private Piece piezaCorrespondiente(Position p){
        Piece piezaElegida = this.tablero.getTablero()[p.getFila()][p.getColumna()];
        if(piezaElegida == null) return null;
        if(!piezaElegida.getPieceColor().equals(this.turno.getColor())) return null;

        //Seteamos todos los movimientos que puede hacer esa pieza en una lista de la propia pieza
        this.tablero.getTablero()[p.getFila()][p.getColumna()].setMovDisponibles(movimientosDisponibles(piezaElegida));
        return piezaElegida;
    }
    private Position obtenerPosicion(String res,String pos) {
        Position position = null;
        String posicion = convertirLetraNumero(pos);
        if (inputValido(posicion)) {
            String[] coords = posicion.split(" ");
            position = new Position(Integer.parseInt(coords[0]),Integer.parseInt(coords[1]));

        } else {
            System.out.println("Los datos no estan siendo ingresados correctamente. Recuerde que "+
                    "debe debe ingresar un nummero y una letra separados por un espacio. " +
                    "Ejemplo: 1 b");
        }

        return position;
    }

    private String convertirLetraNumero(String input) {
        if(input.length() < 3) return " ";
        input = input.toUpperCase();
        char[] letras = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'};
        int posicion = -1;
        for (int i = 0; i < letras.length; i++) {
            if (letras[i] == input.charAt(2)) {
                posicion = i;
                break;
            }
        }
        input = input.substring(0, 2) + posicion + input.substring(3);
        return input;
    }

    public Map elegirPiezaOGurdar() {
        Piece pieza = null;
        String respuesta = null;
        Map informacion = new HashMap();
        informacion.put("decidido",false);
        do{
            System.out.println("Ingrese coordenadas de pieza o  GUARDAR para salir de la partida");
            respuesta = s.nextLine();
            if(respuesta.toLowerCase().equals("guardar")){
                informacion.clear();
                informacion.put("decision","GUARDAR");
                informacion.replace("decidido",true);
                break;
            }
            Position posicion = obtenerPosicion("pieza",respuesta);
            if(posicion != null){
                pieza = piezaCorrespondiente(obtenerPosicion("pieza",respuesta));
                dibujarMovimientosDisponibles(pieza.getMovDisponibles());
                if(pieza.getMovDisponibles().size() == 0)continue;
                if(pieza != null){

                    informacion.clear();
                    informacion.put("decision","ELEGIR");
                    informacion.put("piezaElegida",pieza);
                    informacion.put("decidido",true);

                }else System.out.println("No tienes piezas en ese casillero");
            }

        }while(informacion.get("decidido").equals(false));
        return informacion;
    }

    public boolean pedirMovimiento(Piece p) {

        Position position = null;
        String mover = "";
        do{
            System.out.println("Donde quiere mover la pieza?");
            mover = s.nextLine();
            position = obtenerPosicion("movimiento",mover);
        }while(position == null);
        if(movimientoDisponible(p,position)) {
            Piece copia = crearCopia(p);
            int comidas = p.getPiezsComidas().size();
            int idPiezaComida = actualizarPosicionPieza(p,position);
            calcularZonasAtacadas();
            //Si sigue siendo jaque del jugador anterior, el movimiento no se puede realizar
            if(huboJaque  == true && jaqueAlRey(this.turno.getColor().equals(PieceColor.BLANCA) ? PieceColor.NEGRA:PieceColor.BLANCA)){
                System.out.println("Ese movimiento no es valido ya que su rey sigue en jaque");
                //Vuellvo a poner la pieza que movio ultima en su lugar anterior.
                this.tablero.getTablero()[copia.getPosition().getFila()][copia.getPosition().getColumna()] = copia;
                if(comidas < p.getPiezsComidas().size()){
                    this.tablero.getTablero()[position.getFila()][position.getColumna()] = copia.getPiezsComidas().get(copia.getPiezsComidas().size() - 1);
                    this.tablero.getTablero()[copia.getPosition().getFila()][copia.getPosition().getColumna()].getPiezsComidas().remove(copia.getPiezsComidas().size() - 1);
                }else{
                    this.tablero.getTablero()[position.getFila()][position.getColumna()] = null;
                }
                calcularZonasAtacadas();
                return false;
            }else if(huboJaque == false && jaqueAlRey(this.turno.getColor().equals(PieceColor.BLANCA) ? PieceColor.NEGRA:PieceColor.BLANCA)){
                System.out.println("Ese movimiento no es valido ya que estas dejando TU rey en jaque");
                this.tablero.getTablero()[copia.getPosition().getFila()][copia.getPosition().getColumna()] = copia;
                if(comidas < p.getPiezsComidas().size()){
                    this.tablero.getTablero()[position.getFila()][position.getColumna()] = copia.getPiezsComidas().get(copia.getPiezsComidas().size() - 1);
                    this.tablero.getTablero()[copia.getPosition().getFila()][copia.getPosition().getColumna()].getPiezsComidas().remove(copia.getPiezsComidas().size() - 1);
                }else{
                    this.tablero.getTablero()[position.getFila()][position.getColumna()] = null;
                }
                calcularZonasAtacadas();
                return false;
            }else{
                this.ultimaEnMover = tablero.getTablero()[position.getFila()][position.getColumna()];
                piezasDao.moverPieza(ultimaEnMover.getIdPieza(),ultimaEnMover.getPosition());
                if(idPiezaComida != -1)piezasDao.eliminarPieza(idPiezaComida);
                huboJaque = false;
                return true;
            }

        } else {
            System.out.println("La ficha elegida no puede mover a esa casilla!" +
                    System.lineSeparator() + "Elija otra casilla...");
            return false;
        }
    }
    private static Piece crearCopia(Piece p) {
        Piece copia = new Piece(p.getPieceType(), p.getPieceColor(), p.getPosition().getFila(), p.getPosition().getColumna(), p.getValor());
        copia.setZonaAtacada(p.getZonaAtacada());
        copia.setMovDisponibles(p.getMovDisponibles());
        copia.setMovRealizados(p.getMovRealizados());
        copia.setPiezsComidas(p.getPiezsComidas());
        return copia;
    }
    private boolean movimientoDisponible(Piece pieza,Position p){
        return pieza.getMovDisponibles().stream().anyMatch(posicion -> posicion.getFila() == p.getFila() && posicion.getColumna() == p.getColumna());
    }

    private void dibujarMovimientosDisponibles(List<Position> md){

        if(md.size() == 0) System.out.println("Esta pieza no tiene movimientos disponibles. Seleccione otra\"");
        else{
            System.out.print("Los movimientos disponibles para esa pieza son: ");
            StringBuilder sb = convertirNumerosLetras(md);
            System.out.println(sb);
        }
    }

    public static StringBuilder convertirNumerosLetras(List<Position> posiciones) {
        StringBuilder sb = new StringBuilder();
        for (Position posicion : posiciones) {
            int fila = posicion.getFila();
            int columna = posicion.getColumna();

            char[] letras = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'};
            char letraColumna = letras[columna];

            sb.append(fila + " " + letraColumna + " | ");
        }
        return sb;
    }

    public boolean cambiarPieza() {
        Boolean respuesta = null;
        do {
            System.out.println("¿Quieres cambiar la pieza elegida? (y/n)");
            String input = s.nextLine();
            respuesta = recibirRespuesta(input);
        } while (respuesta == null);
        return respuesta;
    }

    private Boolean recibirRespuesta(String input){
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

    public boolean terminado() {
        //Recordemos que esta clava a una pieza
        Map informacion = informacionJaque(this.ultimaEnMover);
        if(jaqueAlRey(this.turno.getColor())){
            System.out.println("JAQUE");

            huboJaque = true;
            List<Position> atacante = null;
            if(this.turno.getColor().equals(PieceColor.BLANCA)) atacante = zonasAtacadasBlancas;
            else atacante = zonasAtacadasNegras;
            if(salirDeJaque(this.turno.getColor(),atacante)){
                System.out.println("Notacion: " + this.notacion + "+");
                cambiarTurno();
                return false;
            }
            //Si el rey no puede mover o comer para evitar el jaque,
            //las posiciones que ataca deberia salir de la lista de zonas atacadas
            if(informacion.get("jaque").equals(true)){
                //ACA EST EL PROBLEMA
                if(comerPiezaAtacante(this.ultimaEnMover)){
                    System.out.println("Notacion: " + this.notacion + "+");
                    cambiarTurno();
                    return false;
                }
                //System.out.println("Nadie puede comer esa pieza");
            }
            if(informacion.get("jaqueAlDescubierto").equals(true)){
                if(comerPiezaAtacante((Piece)informacion.get("posibleJaqueDescubierto"))){
                    System.out.println("Notacion: " + this.notacion + "+");
                    cambiarTurno();
                    return false;
                }
            }
            if(!(informacion.get("jaque").equals(true) && informacion.get("jaqueAlDescubierto").equals(true))){
                if(informacion.get("jaque").equals(true)){
                    if(taparJaque(this.ultimaEnMover)){
                        System.out.println("Notacion: " + this.notacion + "+");
                        cambiarTurno();
                        return false;
                    }
                }else{
                    if(!taparJaque((Piece)informacion.get("posibleJaqueDescubierto"))){
                        System.out.println("Notacion: " + this.notacion + "+");
                        cambiarTurno();
                        return false;
                    }
                }

            }
        }else{
            System.out.println("Notacion: " + this.notacion);
            cambiarTurno();
            return false;
        }
        System.out.println("Notacion: " + this.notacion + "#");
        this.ganador = this.turno;
        partidasDao.finalizar(this.idPartida);
        return true;
    }
    //Debe comprobar si hay alguna pieza que pueda salvar el jaque tapando
    private boolean taparJaque(Piece piezaAtacante){
        if(piezaAtacante.getPieceType().equals(PieceType.CABALLO) ||
                piezaAtacante.getPieceType().equals(PieceType.PEON)) return false;
        List<Position> posicionesATapar = buscarPosicinesATapar(piezaAtacante);
        if(posicionesATapar.size() == 0) return false;
        if(piezaAtacante.getPieceColor().equals(PieceColor.BLANCA)){
            //Buscar los movimientos posibles de las piezas negras
            for(int i = 0; i <tablero.getTablero().length;i++){
                for(int j = 0; j < tablero.getTablero().length;j++){
                    if(tablero.getTablero()[i][j] == null) continue;
                    if(tablero.getTablero()[i][j].getPieceColor().equals(PieceColor.NEGRA)) {
                        cambiarEstrategia(tablero.getTablero()[i][j].getPieceType());
                        tablero.getTablero()[i][j].setMovDisponibles(estrategia.obtenerMovimientosPosibles(tablero.getTablero()[i][j], tablero.getTablero()));
                    }
                }
            }
            for(int i = 0; i <tablero.getTablero().length;i++){
                for(int j = 0; j < tablero.getTablero().length;j++){
                    for(int k = 0; k < posicionesATapar.size();k++){
                        if(tablero.getTablero()[i][j] == null) continue;
                        if(tablero.getTablero()[i][j].getPieceColor().equals(PieceColor.BLANCA)) continue;
                        if(tablero.getTablero()[i][j].getPieceType().equals(PieceType.REY))continue;
                        if(tablero.getTablero()[i][j].getMovDisponibles().contains(posicionesATapar.get(k))) return true;
                    }
                }
            }

        }else{
            for(int i = 0; i <tablero.getTablero().length;i++){
                for(int j = 0; j < tablero.getTablero().length;j++){
                    if(tablero.getTablero()[i][j] == null) continue;
                    if(tablero.getTablero()[i][j].getPieceColor().equals(PieceColor.BLANCA)) {
                        cambiarEstrategia(tablero.getTablero()[i][j].getPieceType());
                        tablero.getTablero()[i][j].setMovDisponibles(estrategia.obtenerMovimientosPosibles(tablero.getTablero()[i][j], tablero.getTablero()));
                    }
                }
            }
            for(int i = 0; i <tablero.getTablero().length;i++){
                for(int j = 0; j < tablero.getTablero().length;j++){
                    for(int k = 0; k < posicionesATapar.size();k++){
                        if(tablero.getTablero()[i][j] == null) continue;
                        if(tablero.getTablero()[i][j].getPieceColor().equals(PieceColor.NEGRA)) continue;
                        if(tablero.getTablero()[i][j].getPieceType().equals(PieceType.REY))continue;
                        if(tablero.getTablero()[i][j].getMovDisponibles().contains(posicionesATapar.get(k))) return true;
                    }
                }
            }
        }
        return false;
    }
    //Busca las posiciones que hay que tapar para poder salvar el jaque
    private List<Position> buscarPosicinesATapar(Piece piezaAtacante){
        List<Position> resultado = new ArrayList<>();
        Piece reyEnJaaque = null;
        for (int i = 0; i < tablero.getTablero().length;i++){
            for(int j = 0; j < tablero.getTablero().length;j++){
                if(tablero.getTablero()[i][j] == null) continue;
                if(tablero.getTablero()[i][j].getPieceType().equals(PieceType.REY) &&
                        !tablero.getTablero()[i][j].getPieceColor().equals(piezaAtacante.getPieceColor())) reyEnJaaque = tablero.getTablero()[i][j];
            }
        }
        if(piezaAtacante.getPosition().getFila() == reyEnJaaque.getPosition().getFila()){
            if(piezaAtacante.getPosition().getColumna() < reyEnJaaque.getPosition().getColumna()){
                for(int i = 1; i < Math.abs(piezaAtacante.getPosition().getColumna() - reyEnJaaque.getPosition().getColumna());i++){
                    resultado.add(new Position(piezaAtacante.getPosition().getFila(),piezaAtacante.getPosition().getColumna() + i));
                }
                return resultado;
            }else{
                for(int i = 1; i < Math.abs(piezaAtacante.getPosition().getColumna() - reyEnJaaque.getPosition().getColumna());i++){
                    resultado.add(new Position(piezaAtacante.getPosition().getFila(),piezaAtacante.getPosition().getColumna() - i));
                }
                return resultado;
            }
        }
        if(piezaAtacante.getPosition().getColumna() == reyEnJaaque.getPosition().getColumna()){
            if(piezaAtacante.getPosition().getFila() < reyEnJaaque.getPosition().getFila()){
                for(int i = 1; i < Math.abs(piezaAtacante.getPosition().getFila() - reyEnJaaque.getPosition().getFila());i++){
                    resultado.add(new Position(piezaAtacante.getPosition().getFila() + i,piezaAtacante.getPosition().getColumna()));
                }
                return resultado;
            }else{
                for(int i = 1; i < Math.abs(piezaAtacante.getPosition().getFila() - reyEnJaaque.getPosition().getFila());i++){
                    resultado.add(new Position(piezaAtacante.getPosition().getFila() - i,piezaAtacante.getPosition().getColumna()));
                }
                return resultado;
            }
        }
        //IAb a DA
        if((piezaAtacante.getPosition().getFila() - reyEnJaaque.getPosition().getFila()) == (piezaAtacante.getPosition().getColumna() - reyEnJaaque.getPosition().getColumna())){
            if(piezaAtacante.getPosition().getColumna() < reyEnJaaque.getPosition().getColumna()){
                for(int i = 1; i < Math.abs(piezaAtacante.getPosition().getFila() - reyEnJaaque.getPosition().getFila());i++){
                    resultado.add(new Position(piezaAtacante.getPosition().getFila() + i,piezaAtacante.getPosition().getColumna() + i));
                }
                return resultado;
            }else{
                for(int i = 1; i < Math.abs(piezaAtacante.getPosition().getFila() - reyEnJaaque.getPosition().getFila());i++){
                    resultado.add(new Position(piezaAtacante.getPosition().getFila() - i,piezaAtacante.getPosition().getColumna() - i));
                }
                return resultado;
            }
        }
        //Diagonales IA a DAb
        if(reyEnJaaque.getPosition().getFila() == piezaAtacante.getPosition().getColumna() && reyEnJaaque.getPosition().getColumna() == piezaAtacante.getPosition().getFila()){
            if(piezaAtacante.getPosition().getColumna() < reyEnJaaque.getPosition().getColumna()){
                for(int i = 1; i < Math.abs(piezaAtacante.getPosition().getFila() - reyEnJaaque.getPosition().getFila());i++){
                    resultado.add(new Position(piezaAtacante.getPosition().getFila() - i,piezaAtacante.getPosition().getColumna() + i));
                }
                return resultado;
            }else{
                for(int i = 1; i < Math.abs(piezaAtacante.getPosition().getFila() - reyEnJaaque.getPosition().getFila());i++){
                    resultado.add(new Position(piezaAtacante.getPosition().getFila() + i,piezaAtacante.getPosition().getColumna() - i));
                }
                return resultado;
            }
        }


        return resultado;
    }
    private boolean comerPiezaAtacante(Piece piezaAtacante){
        //Si ya calculamos antes que el rey no puede moverse ni comer la pieza que hace jaque, produciria un error tomar las casillas defendidas por el rey
        //Por esto eliminamos las casillas protegidas por el rey
        Position reyContrarioPos = encontrarReyContrario(piezaAtacante.getPieceColor());
        Piece rey = tablero.getTablero()[reyContrarioPos.getFila()][reyContrarioPos.getColumna()];
        if(piezaAtacante.getPieceColor().equals(PieceColor.BLANCA)){
            for (int i = 0; i < rey.getZonaAtacada().size();i++){
                if(zonasAtacadasNegras.contains(rey.getZonaAtacada().get(i)))zonasAtacadasNegras.remove(rey.getZonaAtacada().get(i));
            }
            for(int i = 0; i < zonasAtacadasNegras.size();i++){
                if(zonasAtacadasNegras.get(i).getFila() == piezaAtacante.getPosition().getFila() &&
                        zonasAtacadasNegras.get(i).getColumna() == piezaAtacante.getPosition().getColumna()){
                    return true;
                }
            }
        }else{
            for (int i = 0; i < rey.getZonaAtacada().size();i++){
                if(zonasAtacadasBlancas.contains(rey.getZonaAtacada().get(i)))zonasAtacadasBlancas.remove(rey.getZonaAtacada().get(i));
            }
            for(int i = 0; i < zonasAtacadasBlancas.size();i++){
                if(zonasAtacadasBlancas.get(i).getFila() == piezaAtacante.getPosition().getFila() &&
                        zonasAtacadasBlancas.get(i).getColumna() == piezaAtacante.getPosition().getColumna()){
                    return true;
                }
            }
        }
        return false;
    }
    //Busca si rey puede comer o moverse para evitar el jaque
    private boolean salirDeJaque(PieceColor colorAtacante,List<Position> casillasAtacadas){
        cambiarEstrategia(PieceType.REY);
        Piece reyEnJaaque = null;
        for (int i = 0; i < tablero.getTablero().length;i++){
            for(int j = 0; j < tablero.getTablero().length;j++){
                if(tablero.getTablero()[i][j] == null) continue;
                if(tablero.getTablero()[i][j].getPieceType().equals(PieceType.REY) &&
                        !tablero.getTablero()[i][j].getPieceColor().equals(colorAtacante)) reyEnJaaque = tablero.getTablero()[i][j];
            }
        }
        List<Position> posiblesMovimientos =  estrategia.obtenerMovimientosPosibles(reyEnJaaque,tablero.getTablero());
        for (int i = 0; i < posiblesMovimientos.size();i++){
            if(casillasAtacadas.contains(posiblesMovimientos.get(i))) posiblesMovimientos.remove(posiblesMovimientos.get(i));
        }
        return posiblesMovimientos.size() > 0;
    }
    private boolean jaqueAlRey(PieceColor colorAtacante){
        if (colorAtacante.equals(PieceColor.BLANCA)) {
            Position reyContrario = encontrarReyContrario(PieceColor.BLANCA);
            for(int i = 0; i < zonasAtacadasBlancas.size();i++){
                if(zonasAtacadasBlancas.get(i).getFila() ==reyContrario.getFila()  && zonasAtacadasBlancas.get(i).getColumna() == reyContrario.getColumna()){
                    return true;
                }
            }
        }else{
            Position reyContrario = encontrarReyContrario(PieceColor.NEGRA);
            for(int i = 0; i < zonasAtacadasNegras.size();i++){
                if(zonasAtacadasNegras.get(i).getFila() ==reyContrario.getFila()  && zonasAtacadasNegras.get(i).getColumna() == reyContrario.getColumna()){
                    return true;
                }
            }
        }
        return false;
    }
    private Position encontrarReyContrario(PieceColor colorAtacante){
        for (int i = 0; i < this.tablero.getTablero().length;i++){
            for(int j = 0; j < this.tablero.getTablero().length;j++){
                if(this.tablero.getTablero()[i][j] == null) continue;
                if(this.tablero.getTablero()[i][j].getPieceType().equals(PieceType.REY) &&
                        !this.tablero.getTablero()[i][j].getPieceColor().equals(colorAtacante))
                    return this.tablero.getTablero()[i][j].getPosition();
            }
        }
        return null;
    }
    public void mensajeSalida() {
        if(this.ganador == null){
            System.out.println("Gracias por jugar");

        }else{
            System.out.println("JAQUE MATE");
            System.out.println("Gracias por jugar");
            System.out.println("Ganador: " + this.ganador.getNombre()+"("+this.ganador.getColor()+")");
        }
    }
}
