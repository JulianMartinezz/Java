package ar.edu.utn.frc.tup.lciii.Data;

import ar.edu.utn.frc.tup.lciii.Models.Partida;
import ar.edu.utn.frc.tup.lciii.Models.Piece;
import ar.edu.utn.frc.tup.lciii.Models.PieceColor;
import ar.edu.utn.frc.tup.lciii.Models.PieceType;
import ar.edu.utn.frc.tup.lciii.Models.Player;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DbConnection {
    private static DbConnection instance;
    private  String url;
    private String userName;
    private String password;
    private DbConnection(){
        url = "";
        userName = "";
        password="";
    }

    public static DbConnection obtenerInstancia(){
        if (instance == null) {
            instance = new DbConnection();
        }
        return instance;
    }
    public void ejecutarSpSinRes(String query, List<Param> paramList){
        try(Connection cnn = DriverManager.getConnection(url,userName,password);){
            try(CallableStatement statement = cnn.prepareCall(query);){
                for(Param p: paramList){
                    if(p.getTipo().equals("string")) statement.setString(p.getIndice(),p.getValor().toString());
                    if(p.getTipo().equals("int"))statement.setInt(p.getIndice(),(int)p.getValor());
                }
                statement.execute();
            }catch (Exception exc){
                System.out.println("Problemas con el Procedimiento");
                exc.printStackTrace();
            }
        }catch (Exception exc){
            System.out.println("Problemas con la conexion");
            exc.printStackTrace();
        }
    }
    public Piece ultimaEnMover(int partida){
        Piece pieza = null;
        try(Connection cnn = DriverManager.getConnection(url,userName,password);){
            String query = "{call dbo.ultimaEnMover(?)}";
            try(CallableStatement stPieza = cnn.prepareCall(query);){
                stPieza.setInt(1,partida);
                ResultSet rs = stPieza.executeQuery();
                while(rs.next()){
                    Piece p = new Piece(convertirTipo(rs.getString(7)),convertirColor(rs.getString(3)),rs.getInt(5),rs.getInt(6),"");
                    p.setIdPieza(rs.getInt(4));
                    pieza = p;
                }
            }catch (Exception exception){
                System.out.println("Problemas al recuperar la ultima pieza en mover");
                exception.printStackTrace();
            }
        }catch (Exception exc){
            System.out.println("Problemas con la conexion");
            exc.printStackTrace();
        }
        return pieza;
    }
    public List<Player> recuperarJugadores(int idPartida){
        List<Player> jugadores = new ArrayList<>();
        try(Connection cnn = DriverManager.getConnection(url,userName,password);){
            String query = "{call dbo.recuperarJugadoresDePartida(?)}";
            try(CallableStatement stJugadores = cnn.prepareCall(query);){
                stJugadores.setInt(1,idPartida);
                ResultSet rs = stJugadores.executeQuery();
                while(rs.next()){
                    Player p = new Player();
                    p.setNombre(rs.getString(2));
                    if(rs.getString(3).equals("BLANCA")) p.setColor(PieceColor.BLANCA);
                    else p.setColor(PieceColor.NEGRA);
                    jugadores.add(p);
                }
            }catch (Exception exception){
                System.out.println("Problemas al recuperar jugadores");
                exception.printStackTrace();

            }
        }catch (Exception exc){
            System.out.println("Problemas con la conexion");
            exc.printStackTrace();
        }
        return jugadores;
    }
    public List<Partida> obtenerPartidasGuardadas(){
        List<Partida> partidas = new ArrayList<>();
        try(Connection cnn = DriverManager.getConnection(url,userName,password);) {
            String query = "{call dbo.recuperarPartidasGuardadas()}";
            try(CallableStatement stPartidas = cnn.prepareCall(query);){
                ResultSet rs = stPartidas.executeQuery();
                while(rs.next()){
                    Partida p = new Partida();
                    p.setIdPartida(rs.getInt(1));
                    p.setFechaInicio(rs.getTimestamp(2).toLocalDateTime());
                    p.setJugadores(rs.getString(3));
                    partidas.add(p);
                }
            }catch (Exception exc){
                System.out.println("No se pudieron recuperar las partidas guardadas");
                exc.printStackTrace();
            }
        }catch (Exception exc){
            System.out.println("Problemas con la conexion");
            exc.printStackTrace();
        }
        return partidas;
    }


    public List<Piece> obtenerPiezas(int idPartida){
        ResultSet rs = null;
        List<Piece> piezas = new ArrayList<>();
        try(Connection cnn = DriverManager.getConnection(url,userName,password);){
            String queryPiezas = "{call dbo.obtenerPiezas(?)}";
            try(CallableStatement stmObtenerPiezas =cnn.prepareCall(queryPiezas);){
                stmObtenerPiezas.setInt(1,idPartida);
                rs = stmObtenerPiezas.executeQuery();
                while(rs.next()){
                    if(rs.getBoolean(5) == false) continue;
                    Piece p = new Piece(convertirTipo(rs.getString(6)),convertirColor(rs.getString(7)),rs.getInt(3),rs.getInt(4),"\\"+rs.getString(8));
                    p.setIdPieza(rs.getInt(2));
                    piezas.add(p);
                }
            }catch (Exception exc){
                System.out.print("No se obtuvieron piezas por problemas del servidor");
                exc.printStackTrace();
            }
        }catch(Exception exc){
            exc.printStackTrace();
        }
        return piezas;
    }
    private PieceType convertirTipo(String tipo){
        PieceType tipoGenerado = null;
        switch (tipo){
            case "PEON": tipoGenerado =PieceType.PEON;break;
            case "TORRE":tipoGenerado= PieceType.TORRE;break;
            case "CABALLO":tipoGenerado= PieceType.CABALLO;break;
            case "ALFIL":tipoGenerado= PieceType.ALFIL;break;
            case "REINA":tipoGenerado =PieceType.REINA;break;
            case "REY":tipoGenerado= PieceType.REY;break;
        }
        return tipoGenerado;
    }
    private PieceColor convertirColor(String color){
        PieceColor colorGenerado = null;
        switch (color){
            case "BLANCA":colorGenerado = PieceColor.BLANCA;break;
            case "NEGRA":colorGenerado = PieceColor.NEGRA;break;

        }
        return colorGenerado;
    }
    public  int iniciarPartida(String jugador1,String jugador2){
        int idJ1 = 0;
        int idJ2 = 0;
        int idJ1Piezas = 0;
        int idJ2Piezas = 0;
        int idPartida = 0;
        try(Connection cnn = DriverManager.getConnection(url,userName,password);){
            cnn.setAutoCommit(false);
            String queryJugadores = "{call dbo.crearJugador(?,?)}";
            String queryPartida = "{call dbo.crearPartida(?,?,?,?,?)}";
            String queryPiezas = "{call dbo.inicializarPiezas(?,?)}";
            try(CallableStatement statementJugadores = cnn.prepareCall(queryJugadores);
                CallableStatement statementPartida = cnn.prepareCall(queryPartida);
                CallableStatement statementPiezas = cnn.prepareCall(queryPiezas);){
                //Jugadores-Habria que validar si el nombre de usuario ya existe previamente
                statementJugadores.setString(1,jugador1);
                statementJugadores.registerOutParameter(2, Types.INTEGER);
                statementJugadores.execute();
                idJ1 = statementJugadores.getInt(2);
                statementJugadores.setString(1,jugador2);
                statementJugadores.registerOutParameter(2,Types.INTEGER);
                statementJugadores.execute();
                idJ2 = statementJugadores.getInt(2);

                //partida
                statementPartida.setInt(1,idJ1);
                statementPartida.setInt(2,idJ2);
                statementPartida.registerOutParameter(3,Types.INTEGER);
                statementPartida.registerOutParameter(4,Types.INTEGER);
                statementPartida.registerOutParameter(5,Types.INTEGER);
                //Execute si no devuelve ResultSet. Si devuelve un ResultSet executeQuery
                statementPartida.execute();
                idJ1Piezas = statementPartida.getInt(3);
                idJ2Piezas = statementPartida.getInt(4);
                idPartida = statementPartida.getInt(5);
                //Piezas
                statementPiezas.setInt(1,idJ1Piezas);
                statementPiezas.setInt(2,idJ2Piezas);
                statementPiezas.execute();
                /*System.out.println("Partida número: " + idPartida);
                System.out.println("Id piezas jugador 1 :" + idJ1Piezas);
                System.out.println("Id piezas jugador 2 :" + idJ2Piezas);*/
                cnn.commit();
            }catch (Exception exception){
                if(cnn != null) cnn.rollback();
                System.out.println("No se registro nada");
            }
            cnn.commit();
        }catch (Exception exception){
            System.out.println("Algo salio mal.");
            exception.printStackTrace();
        }
        return idPartida;
    }



}
