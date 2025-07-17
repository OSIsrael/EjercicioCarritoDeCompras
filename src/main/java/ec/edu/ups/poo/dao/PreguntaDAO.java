package ec.edu.ups.poo.dao;

import ec.edu.ups.poo.modelo.Pregunta;

import java.io.IOException;
import java.util.List;

public interface PreguntaDAO {
    void crear(Pregunta pregunta) throws IOException;
    Pregunta buscarPorId(int id) throws IOException;
    List<Pregunta> listarTodas() throws IOException;
    void eliminar(int id) throws IOException;
}