package ar.edu.utn.frc.tup.lciii.Data.Impl;


import ar.edu.utn.frc.tup.lciii.Data.DbConnection;
import ar.edu.utn.frc.tup.lciii.Data.Interfaces.IJugadoresDao;
import ar.edu.utn.frc.tup.lciii.Models.Player;

import java.util.List;

public class JugadoresDaoImpl implements IJugadoresDao {

    @Override
    public List<Player> obtener(int idPartida) {
        return DbConnection.obtenerInstancia().recuperarJugadores(idPartida);
    }
}
