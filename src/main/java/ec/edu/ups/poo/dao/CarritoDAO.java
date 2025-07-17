package ec.edu.ups.poo.dao;

import ec.edu.ups.poo.modelo.Carrito;
import ec.edu.ups.poo.modelo.Usuario;

import java.io.IOException;
import java.util.List;

public interface CarritoDAO {
    void crear(Carrito carrito) throws IOException;
    void eliminar(int codigo) throws IOException;
    void actualizar(Carrito carrito) throws IOException;
    Carrito buscar(int codigo) throws IOException;
    List<Carrito> listarTodos() throws IOException;
    List<Carrito> listarPorUsuario(Usuario usuario) throws IOException;
}