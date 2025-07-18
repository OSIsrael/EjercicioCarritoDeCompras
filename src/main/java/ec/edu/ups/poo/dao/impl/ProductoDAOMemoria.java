package ec.edu.ups.poo.dao.impl;

import ec.edu.ups.poo.dao.ProductoDAO;
import ec.edu.ups.poo.modelo.Producto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ProductoDAOMemoria implements ProductoDAO {

    private List<Producto> productos;

    public ProductoDAOMemoria() {
        productos = new ArrayList<Producto>();
    }

    @Override
    public void crear(Producto producto) throws IOException {
        productos.add(producto);
    }

    @Override
    public Producto buscarPorCodigo(int codigo) throws IOException {
        for (Producto producto : productos) {
            if (producto.getCodigo() == codigo) {
                return producto;
            }
        }
        return null;
    }

    @Override
    public List<Producto> buscarPorNombre(String nombre) throws IOException {
        List<Producto> productosEncontrados = new ArrayList<>();


        if (nombre == null || nombre.trim().isEmpty()) {
            return new ArrayList<>(productos);
        }

        String nombreBusqueda = nombre.trim().toLowerCase();

        for (Producto producto : productos) {

            if (producto.getNombre().toLowerCase().contains(nombreBusqueda)) {
                productosEncontrados.add(producto);
            }
        }
        return productosEncontrados;
    }

    @Override
    public void actualizar(Producto producto) throws IOException {
        for (int i = 0; i < productos.size(); i++) {
            if (productos.get(i).getCodigo() == producto.getCodigo()) {
                productos.set(i, producto);
                break;
            }
        }
    }

    @Override
    public void eliminar(int codigo) throws IOException {
        Iterator<Producto> iterator = productos.iterator();
        while (iterator.hasNext()) {
            Producto producto = iterator.next();
            if (producto.getCodigo() == codigo) {
                iterator.remove();
                break;
            }
        }
    }

    @Override
    public List<Producto> listarTodos() throws IOException {
        return new ArrayList<>(productos);
    }
}