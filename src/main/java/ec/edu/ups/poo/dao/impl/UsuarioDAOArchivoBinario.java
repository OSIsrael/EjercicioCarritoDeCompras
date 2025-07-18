package ec.edu.ups.poo.dao.impl;

import ec.edu.ups.poo.dao.UsuarioDAO;
import ec.edu.ups.poo.modelo.Rol;
import ec.edu.ups.poo.modelo.Usuario;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UsuarioDAOArchivoBinario implements UsuarioDAO {
    private final String path;

    public UsuarioDAOArchivoBinario(String path) {
        this.path = path;
    }

    private List<Usuario> cargarUsuarios() throws IOException {
        File archivo = new File(path);
        if (!archivo.exists() || archivo.length() == 0) {
            return new ArrayList<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path))) {
            return (List<Usuario>) ois.readObject();
        } catch (ClassNotFoundException e) {
            throw new IOException("El archivo de datos de usuario está corrupto o es incompatible.", e);
        }
    }

    private void guardarUsuarios(List<Usuario> usuarios) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path))) {
            oos.writeObject(usuarios);
        }
    }

    @Override
    public Usuario autenticar(String username, String password) throws IOException {
        return cargarUsuarios().stream()
                .filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void crear(Usuario usuario) throws IOException {
        List<Usuario> usuarios = cargarUsuarios();
        if (usuarios.stream().noneMatch(u -> u.getUsername().equals(usuario.getUsername()))) {
            usuarios.add(usuario);
            guardarUsuarios(usuarios);
        }
    }

    @Override
    public Usuario buscarPorUsername(String username) throws IOException {
        return cargarUsuarios().stream().filter(u -> u.getUsername().equals(username)).findFirst().orElse(null);
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
    public List<Usuario> listarTodos() throws IOException { return cargarUsuarios(); }

    @Override
    public List<Usuario> listarPorRol(Rol rol) throws IOException {
        return cargarUsuarios().stream().filter(u -> u.getRol() == rol).collect(Collectors.toList());
    }
}
