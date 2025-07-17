package ec.edu.ups.poo.dao.impl;

import ec.edu.ups.poo.dao.UsuarioDAO;
import ec.edu.ups.poo.modelo.Genero;
import ec.edu.ups.poo.modelo.Rol;
import ec.edu.ups.poo.modelo.Usuario;
import ec.edu.ups.poo.util.CedulaInvalidaException;
import ec.edu.ups.poo.util.PasswordInvalidaException;

import java.util.ArrayList;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class UsuarioDAOMemoria implements UsuarioDAO {

    private final List<Usuario> usuarios;

    public UsuarioDAOMemoria() {
        this.usuarios = new ArrayList<>();

    }

    @Override
    public Usuario autenticar(String username, String password) throws IOException {
        return usuarios.stream()
                .filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void crear(Usuario usuario) throws IOException {
        if (buscarPorUsername(usuario.getUsername()) == null) {
            usuarios.add(usuario);
        }
    }

    @Override
    public Usuario buscarPorUsername(String username) throws IOException {
        return usuarios.stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void actualizar(String usernameAnterior, Usuario usuario) throws IOException {

        for (int i = 0; i < usuarios.size(); i++) {
            if (usuarios.get(i).getUsername().equals(usernameAnterior)) {
                usuarios.set(i, usuario);
                return;
            }
        }
    }

    @Override
    public void eliminar(String username) throws IOException {
        usuarios.removeIf(u -> u.getUsername().equals(username));
    }

    @Override
    public List<Usuario> listarTodos() throws IOException {
        return new ArrayList<>(usuarios);
    }

    @Override
    public List<Usuario> listarPorRol(Rol rol) throws IOException {
        return usuarios.stream()
                .filter(u -> u.getRol() == rol)
                .collect(Collectors.toList());
    }
}