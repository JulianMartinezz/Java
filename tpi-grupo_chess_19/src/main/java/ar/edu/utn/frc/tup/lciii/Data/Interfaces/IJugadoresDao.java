package ar.edu.utn.frc.tup.lciii.Data.Interfaces;

import ar.edu.utn.frc.tup.lciii.Models.Player;


import java.util.List;

public interface IJugadoresDao {
    List<Player> obtener(int idPartida);
}
