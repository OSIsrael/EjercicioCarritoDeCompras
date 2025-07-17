package ec.edu.ups.poo.controlador;

import ec.edu.ups.poo.dao.PreguntaDAO;
import ec.edu.ups.poo.modelo.Pregunta;

import java.io.IOException;
import java.util.List;

public class PreguntaController {
    private PreguntaDAO preguntaDAO;

    public PreguntaController(PreguntaDAO preguntaDAO) {
        this.preguntaDAO = preguntaDAO;
    }

    public void agregarPregunta(Pregunta pregunta) throws IOException {
        preguntaDAO.crear(pregunta);
    }

    public List<Pregunta> obtenerTodasLasPreguntas() throws IOException {
        return preguntaDAO.listarTodas();
    }

    public Pregunta buscarPorId(int id) throws IOException {
        return preguntaDAO.buscarPorId(id);
    }
}