package ec.edu.ups.poo.dao.impl;

import ec.edu.ups.poo.dao.ProductoDAO;
import ec.edu.ups.poo.modelo.Producto;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProductoDAOArchivoTexto implements ProductoDAO {
    private final String path;
    private static final String SEPARADOR = ";";

    public ProductoDAOArchivoTexto(String path) {
        this.path = path;
    }

    private List<Producto> cargarProductos() throws IOException {
        List<Producto> productos = new ArrayList<>();
        File archivo = new File(path);
        if (!archivo.exists()) {
            return productos;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] campos = linea.split(SEPARADOR);
                if (campos.length == 3) {
                    productos.add(new Producto(Integer.parseInt(campos[0]), campos[1], Double.parseDouble(campos[2])));
                }
            }
        }
        return productos;
    }

    private void guardarProductos(List<Producto> productos) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            for (Producto p : productos) {
                bw.write(p.getCodigo() + SEPARADOR + p.getNombre() + SEPARADOR + p.getPrecio());
                bw.newLine();
            }
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
        List<Producto> productos = cargarProductos();
        return productos.stream().filter(p -> p.getCodigo() == codigo).findFirst().orElse(null);
    }

    @Override
    public List<Producto> buscarPorNombre(String nombre) throws IOException {
        List<Producto> productos = cargarProductos();
        String nombreBusqueda = nombre.trim().toLowerCase();
        return productos.stream()
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
