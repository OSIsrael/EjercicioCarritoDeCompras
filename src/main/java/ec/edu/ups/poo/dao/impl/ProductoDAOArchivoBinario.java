package ec.edu.ups.poo.dao.impl;

import ec.edu.ups.poo.dao.ProductoDAO;
import ec.edu.ups.poo.modelo.Producto;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProductoDAOArchivoBinario implements ProductoDAO {
    private final String path;

    public ProductoDAOArchivoBinario(String path) {
        this.path = path;
    }

    private List<Producto> cargarProductos() throws IOException {
        File archivo = new File(path);
        if (!archivo.exists() || archivo.length() == 0) {
            return new ArrayList<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path))) {
            return (List<Producto>) ois.readObject();
        } catch (ClassNotFoundException e) {
            throw new IOException("El archivo de productos está corrupto.", e);
        }
    }

    private void guardarProductos(List<Producto> productos) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path))) {
            oos.writeObject(productos);
        }
    }

    @Override
    public void crear(Producto producto) throws IOException {
        List<Producto> productos = cargarProductos();
        productos.add(producto);
        guardarProductos(productos);
    }

    @Override
    public Producto buscarPorCodigo(int codigo) throws IOException {
        return cargarProductos().stream().filter(p -> p.getCodigo() == codigo).findFirst().orElse(null);
    }

    @Override
    public List<Producto> buscarPorNombre(String nombre) throws IOException {
        String nombreBusqueda = nombre.trim().toLowerCase();
        return cargarProductos().stream()
                .filter(p -> p.getNombre().toLowerCase().contains(nombreBusqueda))
                .collect(Collectors.toList());
    }

    @Override
    public void actualizar(Producto producto) throws IOException {
        List<Producto> productos = cargarProductos();
        for (int i = 0; i < productos.size(); i++) {
            if (productos.get(i).getCodigo() == producto.getCodigo()) {
                productos.set(i, producto);
                guardarProductos(productos);
                return;
            }
        }
    }

    @Override
    public void eliminar(int codigo) throws IOException {
        List<Producto> productos = cargarProductos();
        productos.removeIf(p -> p.getCodigo() == codigo);
        guardarProductos(productos);
    }

    @Override
    public List<Producto> listarTodos() throws IOException {
        return cargarProductos();
    }
}
