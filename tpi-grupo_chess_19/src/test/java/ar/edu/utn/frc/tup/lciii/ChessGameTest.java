package ar.edu.utn.frc.tup.lciii;

import ar.edu.utn.frc.tup.lciii.Models.*;
import ar.edu.utn.frc.tup.lciii.Services.ChessGame;
import ar.edu.utn.frc.tup.lciii.Services.EstrategiaPeon;
import ar.edu.utn.frc.tup.lciii.Services.EstrategiaPieza;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;


public class ChessGameTest {
    private final InputStream systemIn = System.in;
    private final PrintStream systemOut = System.out;
    private ByteArrayInputStream testIn;
    private ByteArrayOutputStream testOut;
    private ChessGame game = crearGame();

    @BeforeEach
    public void setUpOutput() {
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
    }

    @AfterEach
    public void restoreSystemInputOutput() {
        System.setIn(systemIn);
        System.setOut(systemOut);
    }

    private void provideInput(String data) {
        testIn = new ByteArrayInputStream(data.getBytes());
        System.setIn(testIn);
    }
    private void clearOutput() {//Limpia el getOutput();
        testOut.reset();
    }
    private String getOutput() {
        return testOut.toString();
    }

    public ChessGame crearGame(){
        Player pj1 = new Player();
        Player pj2 = new Player();
        ChessGame game = new ChessGame(pj1, pj2);
        game.iniciarPartida();
        return game;
    }
    @Test
    public void testMovimientosDisponiblesPEON() {
        Piece pieza = new Piece(PieceType.PEON, PieceColor.BLANCA,1, 0, PieceStatus.ENJUEGO.name());
        List<Position> movimientos = game.movimientosDisponibles(pieza);

        Assertions.assertNotNull(movimientos);
        Assertions.assertEquals(movimientos.size(), 2);
    }
    @Test
    public void testMovimientosDisponiblesCABALLO() {
        Piece pieza = new Piece(PieceType.CABALLO, PieceColor.BLANCA,0, 1, PieceStatus.ENJUEGO.name());
        List<Position> movimientos = game.movimientosDisponibles(pieza);

        Assertions.assertNotNull(movimientos);
        Assertions.assertEquals(movimientos.size(), 2);
    }
    @Test
    public void testMovimientosDisponiblesREINA() {
        Piece pieza = new Piece(PieceType.REINA, PieceColor.BLANCA,3, 4, PieceStatus.ENJUEGO.name());
        List<Position> movimientos = game.movimientosDisponibles(pieza);

        Assertions.assertNotNull(movimientos);
        Assertions.assertEquals(movimientos.size(), 19);
    }
    @Test
    public void testMovimientosDisponiblesREY() {
        Piece pieza = new Piece(PieceType.REY, PieceColor.BLANCA,3, 4, PieceStatus.ENJUEGO.name());
        List<Position> movimientos = game.movimientosDisponibles(pieza);

        Assertions.assertNotNull(movimientos);
        Assertions.assertEquals(movimientos.size(), 8);
    }
    @Test
    public void testMovimientosDisponiblesTORRE() {
        Piece pieza = new Piece(PieceType.TORRE, PieceColor.NEGRA,3, 7, PieceStatus.ENJUEGO.name());
        List<Position> movimientos = game.movimientosDisponibles(pieza);
        Assertions.assertNotNull(movimientos);
        Assertions.assertEquals(movimientos.size(), 11);
    }
    @Test//Valida con el peon
    public void testCambiarEstrategiaPEON() throws NoSuchFieldException, IllegalAccessException {
        game.cambiarEstrategia(PieceType.PEON);

        Field campo = game.getClass().getDeclaredField("estrategia");
        campo.setAccessible(true);
        EstrategiaPieza estrategiaPieza = (EstrategiaPieza) campo.get(game);

        Assertions.assertTrue(estrategiaPieza instanceof EstrategiaPeon);//indica si es una instancia de EstrategiaPeon

    }
    @Test
    public void testCambiarEstrategia() throws NoSuchFieldException, IllegalAccessException {
        //Obtiene un conjunto de todos los tipos de piezas disponibles en el enumerado PieceType
        EnumSet<PieceType> tiposDePieza = EnumSet.allOf(PieceType.class);

        for (PieceType tipo : tiposDePieza) {
            game.cambiarEstrategia(tipo);

            Field field = game.getClass().getDeclaredField("estrategia");
            field.setAccessible(true);
            EstrategiaPieza estrategiaPieza = (EstrategiaPieza)field.get(game);

            //Verificar si el nombre simple de la estrategia asignada es igual al nombre esperado.
            Assertions.assertTrue(estrategiaPieza.getClass().getSimpleName().equalsIgnoreCase("Estrategia" + tipo.toString()));
        }
    }
    @Test
    public void testMensajeSalidaGanador() throws NoSuchFieldException, IllegalAccessException {

        ChessGame game = crearGame();
        clearOutput();
        Field atributo = game.getClass().getDeclaredField("ganador");
        atributo.setAccessible(true);

        Player ganador = new Player();
        ganador.setNombre("Jugador1");
        ganador.setColor(PieceColor.NEGRA);
        atributo.set(game, ganador);

        game.mensajeSalida();
        String output = getOutput().replace(System.lineSeparator(), "\n");
        String expectedOutput = "JAQUE MATE\nGracias por jugar\nGanador: Jugador1(NEGRA)\n";
        Assertions.assertEquals(expectedOutput, output);
    }
    @Test
    public void testMensajeSalidaSinGanador(){
        ChessGame game = crearGame();
        clearOutput();//Limpia el getOutput();
        game.mensajeSalida();
        String output = getOutput().replace(System.lineSeparator(), "\n");
        String expectedOutput = "Gracias por jugar\n";
        Assertions.assertEquals(expectedOutput, output);
    }
    @Test
    public void testEncontrarReyContrario() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        PieceColor colorAtacante = PieceColor.BLANCA;
        Position expectedPosition = new Position(7,4);

        Method method = ChessGame.class.getDeclaredMethod("encontrarReyContrario", PieceColor.class);
        method.setAccessible(true);
        Position actualPosition = (Position) method.invoke(game, colorAtacante);

        Assertions.assertEquals(expectedPosition, actualPosition);
    }
    @Test
    public void testRecibirRespuesta() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String input = "y";
        Boolean resultado = true;
        Method method = ChessGame.class.getDeclaredMethod("recibirRespuesta", String.class);
        method.setAccessible(true);
        Boolean actual = (Boolean) method.invoke(game, input);
        Assertions.assertEquals(resultado, actual);

        input = "n";
        resultado = false;
        actual = (Boolean) method.invoke(game, input);
        Assertions.assertEquals(resultado, actual);

        input = "invalid";
        String expectedOutput = "La opción ingresada no es valida.\n";
        method.invoke(game, input);
        String output = getOutput().replace(System.lineSeparator(), "\n");
        Assertions.assertEquals(expectedOutput, output);
    }
    @Test
    public void testCambiarPieza(){
        provideInput("Y\nN");
        ChessGame game = crearGame();

        boolean respuesta = game.cambiarPieza();
        Assertions.assertTrue(respuesta);

        respuesta = game.cambiarPieza();
        Assertions.assertFalse(respuesta);
    }
    @Test
    public void testConvertirNumerosLetras() {
        List<Position> posiciones = new ArrayList<>();
        posiciones.add(new Position(1, 0));
        posiciones.add(new Position(2, 3));
        posiciones.add(new Position(3, 1));

        StringBuilder resultado = game.convertirNumerosLetras(posiciones);
        String resultadoEsperado = "1 A | 2 D | 3 B | ";
        Assertions.assertEquals(resultadoEsperado, resultado.toString());
    }
    @Test
    public void testDibujarConMovimientosDisponibles() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        List<Position> movimientosDisponiblesVacio = new ArrayList<>();

        Method method = ChessGame.class.getDeclaredMethod("dibujarMovimientosDisponibles", List.class);
        method.setAccessible(true);

        //CON MOVIMIENTOS
        List<Position> movimientosDisponibles = new ArrayList<>();
        movimientosDisponibles.add(new Position(1, 0));
        movimientosDisponibles.add(new Position(2, 3));
        movimientosDisponibles.add(new Position(3, 1));
        method.invoke(game, movimientosDisponibles);
        String resultado = "Los movimientos disponibles para esa pieza son: 1 A | 2 D | 3 B | \n";
        String output = getOutput().replace(System.lineSeparator(), "\n");
        Assertions.assertEquals(resultado, output);
    }
   /* @Test
    public void testDibujarSinMovimientosDisponibles() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        List<Position> movimientosDisponiblesVacio = new ArrayList<>();

        Method method = ChessGame.class.getDeclaredMethod("dibujarMovimientosDisponibles", List.class);
        method.setAccessible(true);

        method.invoke(game, movimientosDisponiblesVacio);
        String resultado = "Esta pieza no tiene movimientos disponibles. Seleccione otra\n";
        String output1 = getOutput().replace(System.lineSeparator(), "\n");
        Assertions.assertEquals(resultado, output1);
    }*/
    @Test
    public void testLlenarTablero() throws  IllegalAccessException, NoSuchFieldException {

        Field campo = game.getClass().getDeclaredField("tablero");
        campo.setAccessible(true);
        Board tablero = (Board) campo.get(game);

        for (int i = 0; i < 1; i++) {//Verifica si las piezas son blancas
            for (int j = 0; j < 8; j++) {
                Assertions.assertEquals(PieceColor.BLANCA, tablero.getTablero()[i][j].getPieceColor());
            }
        }
        for (int j = 0; j < 8; j++) {//verifica la fila de peones
            Assertions.assertEquals(PieceType.PEON, tablero.getTablero()[1][j].getPieceType());
        }
        Assertions.assertEquals(PieceType.TORRE, tablero.getTablero()[0][0].getPieceType());
        Assertions.assertEquals(PieceType.CABALLO, tablero.getTablero()[0][1].getPieceType());
        Assertions.assertEquals(PieceType.ALFIL, tablero.getTablero()[0][2].getPieceType());
        Assertions.assertEquals(PieceType.REINA, tablero.getTablero()[0][3].getPieceType());
        Assertions.assertEquals(PieceType.REY, tablero.getTablero()[0][4].getPieceType());
        Assertions.assertEquals(PieceType.ALFIL, tablero.getTablero()[0][5].getPieceType());
        Assertions.assertEquals(PieceType.CABALLO, tablero.getTablero()[0][6].getPieceType());
        Assertions.assertEquals(PieceType.TORRE, tablero.getTablero()[0][7].getPieceType());

        //verifica el espacio vacio
        for (int i = 2; i < 6; i++) {//Verifica si las piezas son Negras
            for (int j = 0; j < 8; j++) {
                Assertions.assertEquals(null, tablero.getTablero()[i][j]);
            }
        }

        for (int i = 6; i < 8; i++) {//Verifica si las piezas son Negras
            for (int j = 0; j < 8; j++) {
                Assertions.assertEquals(PieceColor.NEGRA, tablero.getTablero()[i][j].getPieceColor());
            }
        }
        for (int j = 0; j < 8; j++) {//verifica la fila de peones
            Assertions.assertEquals(PieceType.PEON, tablero.getTablero()[6][j].getPieceType());
        }

        Assertions.assertEquals(PieceType.TORRE, tablero.getTablero()[7][0].getPieceType());
        Assertions.assertEquals(PieceType.CABALLO, tablero.getTablero()[7][1].getPieceType());
        Assertions.assertEquals(PieceType.ALFIL, tablero.getTablero()[7][2].getPieceType());
        Assertions.assertEquals(PieceType.REINA, tablero.getTablero()[7][3].getPieceType());
        Assertions.assertEquals(PieceType.REY, tablero.getTablero()[7][4].getPieceType());
        Assertions.assertEquals(PieceType.ALFIL, tablero.getTablero()[7][5].getPieceType());
        Assertions.assertEquals(PieceType.CABALLO, tablero.getTablero()[7][6].getPieceType());
        Assertions.assertEquals(PieceType.TORRE, tablero.getTablero()[7][7].getPieceType());
    }
   /* @Test
    public void testImprimirTablero() throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Method method = ChessGame.class.getDeclaredMethod("imprimirTablero");
        method.setAccessible(true);
        method.invoke(game);

        String expectedOutput = "  --------------------------------------\n" +
                "7 | \u2656 | \u2658 | \u2657 | \u2655 | \u2654 | \u2657 | \u2658 | \u2656 |" + "\n" +
                "  --------------------------------------\n" +
                "6 | \u2659 | \u2659 | \u2659 | \u2659 | \u2659 | \u2659 | \u2659 | \u2659 |" + "\n" +
                "  --------------------------------------\n" +
                "5 | \u2A37 | \u2A37 | \u2A37 | \u2A37 | \u2A37 | \u2A37 | \u2A37 | \u2A37 |" + "\n" +
                "  --------------------------------------\n" +
                "4 | \u2A37 | \u2A37 | \u2A37 | \u2A37 | \u2A37 | \u2A37 | \u2A37 | \u2A37 |" + "\n" +
                "  --------------------------------------\n" +
                "3 | \u2A37 | \u2A37 | \u2A37 | \u2A37 | \u2A37 | \u2A37 | \u2A37 | \u2A37 |" + "\n" +
                "  --------------------------------------\n" +
                "2 | \u2A37 | \u2A37 | \u2A37 | \u2A37 | \u2A37 | \u2A37 | \u2A37 | \u2A37 |" + "\n" +
                "  --------------------------------------\n" +
                "1 | \u265F | \u265F | \u265F | \u265F | \u265F | \u265F | \u265F | \u265F |" + "\n" +
                "  --------------------------------------\n" +
                "0 | \u265C | \u265E | \u265D | \u265B | \u265A | \u265D | \u265E | \u265C |" + "\n" +
                "  --------------------------------------\n" +
                "    A    B    C   D    E    F    G    H\n\n";
        String output = getOutput().replace(System.lineSeparator(), "\n");
        Assertions.assertEquals(expectedOutput, output);
    }*/
    @Test
    public void testCambiarTurno() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        Field defensor = ChessGame.class.getDeclaredField("defensor");
        defensor.setAccessible(true);

        Field turno = ChessGame.class.getDeclaredField("turno");
        turno.setAccessible(true);

        Player jugador1 = new Player("Jugador1", PieceColor.NEGRA);
        Player jugador2 = new Player("Jugador2", PieceColor.BLANCA);

        ChessGame game = new ChessGame(jugador1, jugador2);

        Method method = ChessGame.class.getDeclaredMethod("cambiarTurno");
        method.setAccessible(true);
        method.invoke(game);

        Player turnoValue = (Player) turno.get(game);
        Player defensorValue = (Player) defensor.get(game);

        Assertions.assertEquals(jugador2, turnoValue);
        Assertions.assertEquals(jugador1, defensorValue);
    }
    @Test
    public void testInputValido() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Method method = ChessGame.class.getDeclaredMethod("inputValido", String.class);
        method.setAccessible(true);
        String input = "1 1";
        boolean resultado = (boolean) method.invoke(game, input);
        Assertions.assertTrue(resultado);
        input = "6 7";
        resultado = (boolean) method.invoke(game, input);
        Assertions.assertTrue(resultado);
        input = "8 8";
        resultado = (boolean) method.invoke(game, input);
        Assertions.assertFalse(resultado);
        input = "9 8";
        resultado = (boolean) method.invoke(game, input);
        Assertions.assertFalse(resultado);
    }
    @Test
    public void testConvertirLetraNumero() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = ChessGame.class.getDeclaredMethod("convertirLetraNumero", String.class);
        method.setAccessible(true);
        String input = "1 A";
        String resultado = (String) method.invoke(game, input);
        Assertions.assertEquals(resultado, "1 0");
        input = "4 D";
        resultado = (String) method.invoke(game, input);
        Assertions.assertEquals(resultado, "4 3");
        input = "6 F";
        resultado = (String) method.invoke(game, input);
        Assertions.assertEquals(resultado, "6 5");

    }

}
