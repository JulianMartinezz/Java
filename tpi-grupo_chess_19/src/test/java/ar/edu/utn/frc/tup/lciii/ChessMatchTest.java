package ar.edu.utn.frc.tup.lciii;

import ar.edu.utn.frc.tup.lciii.Data.Impl.JugadoresDaoImpl;
import ar.edu.utn.frc.tup.lciii.Data.Impl.PartidasDaoImp;
import ar.edu.utn.frc.tup.lciii.Models.Partida;
import ar.edu.utn.frc.tup.lciii.Models.PieceColor;
import ar.edu.utn.frc.tup.lciii.Models.Player;
import ar.edu.utn.frc.tup.lciii.Services.ChessMatch;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ChessMatchTest {
    private final InputStream systemIn = System.in;
    private final PrintStream systemOut = System.out;
    private ByteArrayInputStream testIn;
    private ByteArrayOutputStream testOut;
    private ChessMatch match = new ChessMatch();
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

    private String getOutput() {
        return testOut.toString();
    }
    @Test
    public void testMensajeBienvenida() {
        match.mensajeBienvenida();
        String output = getOutput().replace(System.lineSeparator(), "\n");
        Assertions.assertEquals("Bienvenido a Ajedrez\n", output);
    }
    public void testRecibirRespuesta() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String input = "y";
        Boolean resultado = true;
        Method method = ChessMatch.class.getDeclaredMethod("recibirRespuesta", String.class);
        method.setAccessible(true);
        Boolean actual = (Boolean) method.invoke(match, input);
        Assertions.assertEquals(resultado, actual);

        input = "n";
        resultado = false;
        actual = (Boolean) method.invoke(match, input);
        Assertions.assertEquals(resultado, actual);

        input = "invalid";
        String salida = "La opción ingresada no es valida.\n";
        method.invoke(match, input);
        //Reemplazamos los separaradores de linea del sistema con \n asi pasa la prueba
        String output = getOutput().replace(System.lineSeparator(), "\n");
        Assertions.assertEquals(salida, output);
    }
    @Test
    public void testJugarDeNuevo() {
        provideInput("Y\nN");
        //Simula ingreso de Y
        ChessMatch match = new ChessMatch();
        boolean resultado = match.jugarDeNuevo();
        Assertions.assertTrue(resultado);
        //Simula ingreso de Y
        resultado = match.jugarDeNuevo();
        Assertions.assertFalse(resultado);
    }
    @Test
    public void testCrearJugadores() {

        provideInput("Jugador1\nJugador2\n");

        ChessMatch match = new ChessMatch();

        List<Player> jugadores = match.crearJugadores();

        Assertions.assertNotNull(jugadores);
        Assertions.assertEquals(2, jugadores.size());

        Player jugador1 = jugadores.get(0);
        Assertions.assertEquals("Jugador1", jugador1.getNombre());
        Assertions.assertEquals(PieceColor.BLANCA, jugador1.getColor());

        Player jugador2 = jugadores.get(1);
        Assertions.assertEquals("Jugador2", jugador2.getNombre());
        Assertions.assertEquals(PieceColor.NEGRA, jugador2.getColor());
    }
    @Test
    public void testRespuestaPartida() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Method method = ChessMatch.class.getDeclaredMethod("respuestaPartida", String.class);
        method.setAccessible(true);

        // Caso de prueba 1: Entrada válida
        String input = "1";
        String result = (String) method.invoke(match, input);
        Assertions.assertEquals(input, result);

        input = "2";
        result = (String) method.invoke(match, input);
        Assertions.assertEquals(input, result);

        input = "3";
        result = (String) method.invoke(match, input);
        Assertions.assertEquals(input, result);

        input = "4";
        result =(String) method.invoke(match, input);
        Assertions.assertEquals("-1", result);

        input = "abc";
        result = (String) method.invoke(match, input);
        Assertions.assertEquals("-1", result);
    }
    @Test
    public void testValidarPartida() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {

        Method method = ChessMatch.class.getDeclaredMethod("validarPartida", List.class, String.class);
        method.setAccessible(true);

        // Datos de prueba
        LocalDateTime fecha = LocalDateTime.of(2023, 6, 1, 10, 0);
        List<Partida> partidas = new ArrayList<>();
        partidas.add(new Partida(1, fecha, "Jugador1"));
        partidas.add(new Partida(2, fecha, "Jugador2"));
        partidas.add(new Partida(3,  fecha, "Jugador3"));

        //Partida existente
        int resultado = (int) method.invoke(match,partidas, "2");
        Assertions.assertEquals(2, resultado);

        //Partida no existente
        resultado = (int) method.invoke(match,partidas, "4");
        Assertions.assertEquals(-1, resultado);
    }
   /* @Test
    public void testElegirPartidaGuardada() {
    //Funciona con los datos de la BD
        provideInput("15");
        ChessMatch match = new ChessMatch();
        int resultado = match.elegirPartidaGuardada();

        Assertions.assertEquals(15, resultado);
    }*/
    @Test
    public void testRecuperarPartidasGuardadas() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, NoSuchFieldException {
        Method method = ChessMatch.class.getDeclaredMethod("recuperarPartidasGuardadas");
        method.setAccessible(true);

        LocalDateTime fecha = LocalDateTime.of(2023, 6, 1, 10, 0);
        List<Partida> partidas = new ArrayList<>();
        partidas.add(new Partida(1, fecha, "Jugador1"));
        partidas.add(new Partida(2, fecha, "Jugador2"));
        partidas.add(new Partida(3,  fecha, "Jugador3"));

        PartidasDaoImp partidasDaoMock = mock(PartidasDaoImp.class);
        when(partidasDaoMock.obtenerGuardadas()).thenReturn(partidas);

        Field partidasDaoField = ChessMatch.class.getDeclaredField("partidasDao");
        partidasDaoField.setAccessible(true);
        partidasDaoField.set(match, partidasDaoMock);

        List<Partida> resultado = (List<Partida>) method.invoke(match);

        Assertions.assertEquals(partidas, resultado);
        verify(partidasDaoMock, times(1)).obtenerGuardadas();//Se asegura que el metodo solo ocurra una vez

    }
    @Test
    public void testMostrarPartidasGuardadas() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = ChessMatch.class.getDeclaredMethod("mostrarPartidadGuadadas", List.class);
        method.setAccessible(true);
        ChessMatch match = new ChessMatch();

        LocalDateTime fecha1 = LocalDateTime.of(2023, 6, 1, 10, 0);
        LocalDateTime fecha2 = LocalDateTime.of(2023, 6, 2, 12, 0);
        List<Partida> partidas = new ArrayList<>();
        partidas.add(new Partida(1, fecha1, "Jugador1"));
        partidas.add(new Partida(2, fecha2, "Jugador2"));

        method.invoke(match, partidas);

        String salida = "Partidas Guardadas:\n" +
                "1  2023-06-01T10:00  Jugador1\n" +
                "2  2023-06-02T12:00  Jugador2\n\n";

        String output = getOutput().replace(System.lineSeparator(), "\n");
        Assertions.assertEquals(salida, output);
    }
    @Test
    public void testRecuperarJugadores() throws NoSuchFieldException, IllegalAccessException {

        int idPartida = 1;
        List<Player> jugadoresEsperados = new ArrayList<>();
        jugadoresEsperados.add(new Player("Jugador1", PieceColor.BLANCA));
        jugadoresEsperados.add(new Player("Jugador2", PieceColor.NEGRA));

        JugadoresDaoImpl jugadoresDaoMock = mock(JugadoresDaoImpl.class);
        when(jugadoresDaoMock.obtener(idPartida)).thenReturn(jugadoresEsperados);

        Field jugadoresMock = ChessMatch.class.getDeclaredField("jugadoresDao");
        jugadoresMock.setAccessible(true);
        jugadoresMock.set(match, jugadoresDaoMock);

        List<Player> jugadoresObtenidos = match.recuperarJugadores(idPartida);

        Assertions.assertEquals(jugadoresEsperados, jugadoresObtenidos);
    }


}
