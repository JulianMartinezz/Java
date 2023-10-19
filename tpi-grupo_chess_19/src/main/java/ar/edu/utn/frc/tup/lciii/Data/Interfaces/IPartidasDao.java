package ar.edu.utn.frc.tup.lciii.Data.Interfaces;



import ar.edu.utn.frc.tup.lciii.Models.Partida;

import java.util.List;

public interface IPartidasDao {
    int crear(String j1,String j2);
    List<Partida> obtenerGuardadas();
    void finalizar(int idPartida);
}
