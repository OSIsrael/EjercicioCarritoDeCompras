package ec.edu.ups.poo.dao.impl;

import ec.edu.ups.poo.dao.PreguntaDAO;
import ec.edu.ups.poo.modelo.Pregunta;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PreguntaDAOArchivoTexto implements PreguntaDAO {
    private final String path;
    private static final String SEPARADOR = ";";

    public PreguntaDAOArchivoTexto(String path) {
        this.path = path;
    }

    private List<Pregunta> cargarPreguntas() throws IOException {
        List<Pregunta> preguntas = new ArrayList<>();
        File archivo = new File(path);
        if (!archivo.exists()) return preguntas;

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] campos = linea.split(SEPARADOR, 2);
                if (campos.length == 2) {
                    preguntas.add(new Pregunta(Integer.parseInt(campos[0]), campos[1]));
                }
            }
        }
        return preguntas;
    }

    private void guardarPreguntas(List<Pregunta> preguntas) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            for (Pregunta p : preguntas) {
                bw.write(p.getId() + SEPARADOR + p.getPregunta());
                bw.newLine();
            }
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
