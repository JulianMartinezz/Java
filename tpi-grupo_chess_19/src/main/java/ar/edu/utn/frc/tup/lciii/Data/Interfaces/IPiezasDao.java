package ar.edu.utn.frc.tup.lciii.Data.Interfaces;



import ar.edu.utn.frc.tup.lciii.Models.Piece;
import ar.edu.utn.frc.tup.lciii.Models.Position;

import java.util.List;

public interface IPiezasDao {
    List<Piece> obtenerTodas(int idPartida);
    void moverPieza(int idPieza, Position coordDestino);
    void eliminarPieza(int idPieza);
    Piece ultimaEnMover(int idPartida);
}
