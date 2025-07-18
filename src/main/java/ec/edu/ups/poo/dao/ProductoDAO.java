package ec.edu.ups.poo.dao;

import ec.edu.ups.poo.modelo.Producto;

import java.io.IOException;
import java.util.List;

public interface ProductoDAO {
    void crear(Producto producto) throws IOException;
    Producto buscarPorCodigo(int codigo) throws IOException;
    List<Producto> buscarPorNombre(String nombre) throws IOException;
    void actualizar(Producto producto) throws IOException;
    void eliminar(int codigo) throws IOException;
    List<Producto> listarTodos() throws IOException;
}