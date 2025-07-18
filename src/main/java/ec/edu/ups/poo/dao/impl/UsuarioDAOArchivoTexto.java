package ec.edu.ups.poo.dao.impl;

import ec.edu.ups.poo.dao.UsuarioDAO;
import ec.edu.ups.poo.modelo.Genero;
import ec.edu.ups.poo.modelo.Rol;
import ec.edu.ups.poo.modelo.Usuario;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UsuarioDAOArchivoTexto implements UsuarioDAO {
    private final String path;
    private static final String SEPARADOR_CAMPO = ";";
    private static final String SEPARADOR_MAPA_ENTRADA = "\\|";
    private static final String SEPARADOR_MAPA_KV = ":";

    public UsuarioDAOArchivoTexto(String path) {
        this.path = path;
    }

    private List<Usuario> cargarUsuarios() throws IOException {
        List<Usuario> usuarios = new ArrayList<>();
        File archivo = new File(path);
        if (!archivo.exists()) {
            return usuarios;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                Usuario u = stringToUsuario(linea);
                if (u != null) { usuarios.add(u); }
            }
        }
        return usuarios;
    }

    private void guardarUsuarios(List<Usuario> usuarios) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            for (Usuario u : usuarios) {
                bw.write(usuarioToString(u));
                bw.newLine();
            }
        }
    }

    private String usuarioToString(Usuario usuario) {

        StringBuilder sb = new StringBuilder();
        sb.append(usuario.getUsername()).append(SEPARADOR_CAMPO);
        sb.append(usuario.getPassword()).append(SEPARADOR_CAMPO);
        sb.append(usuario.getRol()).append(SEPARADOR_CAMPO);
        sb.append(usuario.getGenero()).append(SEPARADOR_CAMPO);
        sb.append(usuario.getNombre()).append(SEPARADOR_CAMPO);
        sb.append(usuario.getApellido()).append(SEPARADOR_CAMPO);
        sb.append(usuario.getTelefono()).append(SEPARADOR_CAMPO);
        sb.append(usuario.getEdad()).append(SEPARADOR_CAMPO);
        sb.append(usuario.getTipoAlmacenamiento()).append(SEPARADOR_CAMPO);

        // Serializar el mapa de preguntas
        StringBuilder mapSb = new StringBuilder();
        for (Map.Entry<Integer, String> entry : usuario.getPreguntasSeguridad().entrySet()) {
            mapSb.append(entry.getKey()).append(SEPARADOR_MAPA_KV).append(entry.getValue()).append(SEPARADOR_MAPA_ENTRADA);
        }
        if (mapSb.length() > 0) {
            mapSb.deleteCharAt(mapSb.length() - 1);
        }
        sb.append(mapSb.toString());
        return sb.toString();
    }

    private Usuario stringToUsuario(String linea) {
        String[] campos = linea.split(SEPARADOR_CAMPO);
        if (campos.length < 9) return null;
        try {
            Usuario usuario = new Usuario(campos[0], campos[1], Rol.valueOf(campos[2]), Genero.valueOf(campos[3]),
                    campos[4], campos[5], campos[6], Integer.parseInt(campos[7]), campos[8]);

            // Deserializar mapa de preguntas
            if (campos.length > 9 && !campos[9].isEmpty()) {
                Map<Integer, String> preguntas = new HashMap<>();
                String[] pares = campos[9].split(SEPARADOR_MAPA_ENTRADA);
                for (String par : pares) {
                    String[] kv = par.split(SEPARADOR_MAPA_KV);
                    if (kv.length == 2) {
                        preguntas.put(Integer.parseInt(kv[0]), kv[1]);
                    }
                }
                usuario.setPreguntasSeguridad(preguntas);
            }
            return usuario;
        } catch (Exception e) {

            System.err.println("Error al parsear línea de usuario (datos corruptos?): " + linea + ". Error: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Usuario autenticar(String username, String password) throws IOException {
        Usuario usuario = buscarPorUsername(username);
        if (usuario != null && usuario.getPassword().equals(password)) {
            return usuario;
        }
        return null;
    }

    @Override
    public void crear(Usuario usuario) throws IOException {
        List<Usuario> usuarios = cargarUsuarios();
        if (buscarPorUsername(usuario.getUsername(), usuarios) == null) {
            usuarios.add(usuario);
            guardarUsuarios(usuarios);
        }
    }

    @Override
    public Usuario buscarPorUsername(String username) throws IOException {
        List<Usuario> usuarios = cargarUsuarios();
        return buscarPorUsername(username, usuarios);
    }


    private Usuario buscarPorUsername(String username, List<Usuario> usuarios) {
        return usuarios.stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }


    @Override
    public void actualizar(String usernameAnterior, Usuario usuario) throws IOException {
        List<Usuario> usuarios = cargarUsuarios();
        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getUsername().equals(usernameAnterior)) {
                usuarios.set(i, usuario);
                guardarUsuarios(usuarios);
                return;
            }
        }
    }

    @Override
    public void eliminar(String username) throws IOException {
        List<Usuario> usuarios = cargarUsuarios();
        usuarios.removeIf(u -> u.getUsername().equals(username));
        guardarUsuarios(usuarios);
    }

    @Override
    public List<Usuario> listarTodos() throws IOException {
        return cargarUsuarios();
    }

    @Override
    public List<Usuario> listarPorRol(Rol rol) throws IOException {
        return cargarUsuarios().stream()
                .filter(u -> u.getRol() == rol)
                .collect(Collectors.toList());
    }
}
