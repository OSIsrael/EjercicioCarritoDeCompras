package ec.edu.ups.poo.dao.impl;

import ec.edu.ups.poo.dao.CarritoDAO;
import ec.edu.ups.poo.modelo.Carrito;
import ec.edu.ups.poo.modelo.Usuario;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CarritoDAOArchivoBinario implements CarritoDAO {
    private final String path;
    private int nextCodigo = 1;

    public CarritoDAOArchivoBinario(String path) {
        this.path = path;
    }

    private List<Carrito> cargarCarritos() throws IOException {
        File archivo = new File(path);
        if (!archivo.exists() || archivo.length() == 0) {
            return new ArrayList<>();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path))) {
            List<Carrito> carritos = (List<Carrito>) ois.readObject();
            if (!carritos.isEmpty()) {
                nextCodigo = carritos.stream().mapToInt(Carrito::getCodigo).max().orElse(0) + 1;
            }
            return carritos;
        } catch (ClassNotFoundException e) {
            throw new IOException("El archivo de carritos está corrupto.", e);
        }
    }

    private void guardarCarritos(List<Carrito> carritos) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path))) {
            oos.writeObject(carritos);
        }
    }

    @Override
    public void crear(Carrito carrito) throws IOException {
        List<Carrito> carritos = cargarCarritos();
        if (carrito.getCodigo() == 0) {
            carrito.setCodigo(nextCodigo++);
        }
        carritos.add(carrito);
        guardarCarritos(carritos);
    }

    @Override
    public void eliminar(int codigo) throws IOException {
        List<Carrito> carritos = cargarCarritos();
        carritos.removeIf(c -> c.getCodigo() == codigo);
        guardarCarritos(carritos);
    }

    @Override
    public void actualizar(Carrito carrito) throws IOException {
        List<Carrito> carritos = cargarCarritos();
        for (int i = 0; i < carritos.size(); i++) {
            if (carritos.get(i).getCodigo() == carrito.getCodigo()) {
                carritos.set(i, carrito);
                guardarCarritos(carritos);
                return;
            }
        }
    }

    @Override
    public Carrito buscar(int codigo) throws IOException {
        return cargarCarritos().stream().filter(c -> c.getCodigo() == codigo).findFirst().orElse(null);
    }

    @Override
    public List<Carrito> listarTodos() throws IOException {
        return cargarCarritos();
    }

    @Override
    public List<Carrito> listarPorUsuario(Usuario usuario) throws IOException {
        return cargarCarritos().stream()
                .filter(c -> c.getUsuario() != null && c.getUsuario().equals(usuario))
                .collect(Collectors.toList());
    }
}
