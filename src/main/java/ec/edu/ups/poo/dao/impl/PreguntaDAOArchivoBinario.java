package ec.edu.ups.poo.dao.impl;

import ec.edu.ups.poo.dao.PreguntaDAO;
import ec.edu.ups.poo.modelo.Pregunta;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PreguntaDAOArchivoBinario implements PreguntaDAO {
    private final String path;

    public PreguntaDAOArchivoBinario(String path) {
        this.path = path;
    }

    private List<Pregunta> cargarPreguntas() throws IOException {
        File archivo = new File(path);
        if (!archivo.exists() || archivo.length() == 0) {
            return new ArrayList<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path))) {
            return (List<Pregunta>) ois.readObject();
        } catch (ClassNotFoundException e) {
            throw new IOException("El archivo de preguntas está corrupto.", e);
        }
    }

    private void guardarPreguntas(List<Pregunta> preguntas) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path))) {
            oos.writeObject(preguntas);
        }
    }

    @Override
    public void crear(Pregunta pregunta) throws IOException {
        List<Pregunta> preguntas = cargarPreguntas();
        preguntas.add(pregunta);
        guardarPreguntas(preguntas);
    }

    @Override
    public Pregunta buscarPorId(int id) throws IOException {
        return cargarPreguntas().stream().filter(p -> p.getId() == id).findFirst().orElse(null);
    }

    @Override
    public List<Pregunta> listarTodas() throws IOException {
        return cargarPreguntas();
    }

    @Override
    public void eliminar(int id) throws IOException {
        List<Pregunta> preguntas = cargarPreguntas();
        preguntas.removeIf(p -> p.getId() == id);
        guardarPreguntas(preguntas);
    }
}
