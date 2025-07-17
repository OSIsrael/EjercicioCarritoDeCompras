package ec.edu.ups.poo.dao;

import ec.edu.ups.poo.modelo.Rol;
import ec.edu.ups.poo.modelo.Usuario;
import java.io.IOException;

import java.util.List;

public interface UsuarioDAO {
    Usuario autenticar(String username,String password) throws IOException;
    void crear(Usuario usuario) throws IOException;
    Usuario buscarPorUsername(String username) throws IOException;
    void actualizar(String usernameAnterior, Usuario usuario) throws IOException;
    void eliminar(String username) throws IOException;
    List<Usuario> listarTodos() throws IOException;
    List<Usuario> listarPorRol(Rol rol) throws IOException;
}
