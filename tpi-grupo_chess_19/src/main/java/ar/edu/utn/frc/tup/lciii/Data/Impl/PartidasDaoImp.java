package ar.edu.utn.frc.tup.lciii.Data.Impl;


import ar.edu.utn.frc.tup.lciii.Data.DbConnection;
import ar.edu.utn.frc.tup.lciii.Data.Interfaces.IPartidasDao;
import ar.edu.utn.frc.tup.lciii.Data.Param;
import ar.edu.utn.frc.tup.lciii.Models.Partida;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@NoArgsConstructor
public class PartidasDaoImp implements IPartidasDao {
    @Override
    public int crear(String j1, String j2) {

        return DbConnection.obtenerInstancia().iniciarPartida(j1,j2);
    }

    @Override
    public List<Partida> obtenerGuardadas() {
        return DbConnection.obtenerInstancia().obtenerPartidasGuardadas();
    }

    @Override
    public void finalizar(int idPartida) {
        List<Param> parametros = new ArrayList<>();
        parametros.add(new Param(idPartida,1,"int"));
        DbConnection.obtenerInstancia().ejecutarSpSinRes("{call dbo.finalizarPartida(?)}",parametros);
        //DbConnection.obtenerInstancia().finalizarPartida(idPartida);
    }
}
