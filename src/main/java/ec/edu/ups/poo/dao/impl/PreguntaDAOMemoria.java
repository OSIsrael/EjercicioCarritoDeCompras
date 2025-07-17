package ec.edu.ups.poo.dao.impl;

import ec.edu.ups.poo.dao.PreguntaDAO;
import ec.edu.ups.poo.modelo.Pregunta;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PreguntaDAOMemoria implements PreguntaDAO {
    private List<Pregunta> preguntas = new ArrayList<>();

    @Override
    public void crear(Pregunta pregunta) throws IOException {
        preguntas.add(pregunta);
    }

    @Override
    public Pregunta buscarPorId(int id) throws IOException {
        for (Pregunta p : preguntas) {
            if (p.getId() == id) return p;
        }
        return null;
    }

    @Override
    public List<Pregunta> listarTodas() throws IOException {
        return new ArrayList<>(preguntas);
    }

    @Override
    public void eliminar(int id) throws IOException {
        preguntas.removeIf(p -> p.getId() == id);
    }
}