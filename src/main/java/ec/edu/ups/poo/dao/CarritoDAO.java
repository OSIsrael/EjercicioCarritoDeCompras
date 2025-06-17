package ec.edu.ups.poo.dao;

import ec.edu.ups.poo.modelo.Carrito;

import java.util.List;

public interface CarritoDAO {
    void crear(Carrito carrito);
    void eliminar(int codigo);
    void actualzar(Carrito carrito);
    Carrito buscarPorCodigo(int codigo);
    List<Carrito> listarTodos();

}
