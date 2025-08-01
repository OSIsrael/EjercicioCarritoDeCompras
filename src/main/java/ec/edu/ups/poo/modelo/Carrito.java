package ec.edu.ups.poo.modelo;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

public class Carrito {
    private int codigo;
    private GregorianCalendar fechaCreacion;
    private List<ItemCarrito> items;
    private Usuario usuario;

    public Carrito() {
        items = new ArrayList<>();
        fechaCreacion = new GregorianCalendar();
    }

    // --- GETTERS Y SETTERS ---
    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public GregorianCalendar getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(GregorianCalendar fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public List<ItemCarrito> getItems() {

        return new ArrayList<>(items);
    }

    public void setItems(List<ItemCarrito> items) {
        this.items = items;
    }

    public Usuario getUsuario() { // <-- GETTER AÑADIDO
        return usuario;
    }

    public void setUsuario(Usuario usuario) { // <-- SETTER AÑADIDO
        this.usuario = usuario;
    }

    // --- LÓGICA DE NEGOCIO ---
    public void agregarProducto(Producto producto, int cantidad) {
        if (producto == null || cantidad <= 0) {
            return; // No agregar si los datos son inválidos
        }
        // Si el ítem ya existe, actualiza la cantidad
        for (ItemCarrito item : items) {
            if (item.getProducto().getCodigo() == producto.getCodigo()) {
                item.setCantidad(item.getCantidad() + cantidad);
                return;
            }
        }
        // Si es nuevo, lo añade a la lista
        items.add(new ItemCarrito(producto, cantidad));
    }

    public void eliminarProducto(int codigoProducto) {
        items.removeIf(item -> item.getProducto().getCodigo() == codigoProducto);
    }

    public void actualizarCantidad(int codigoProducto, int nuevaCantidad) {
        if (nuevaCantidad <= 0) {
            eliminarProducto(codigoProducto);
            return;
        }
        for (ItemCarrito item : items) {
            if (item.getProducto().getCodigo() == codigoProducto) {
                item.setCantidad(nuevaCantidad);
                return;
            }
        }
    }

    public double calcularSubtotal() {
        double subtotal = 0;
        for (ItemCarrito item : items) {
            subtotal += item.getSubtotal();
        }
        return subtotal;
    }

    public double calcularIva() {
        return calcularSubtotal() * 0.12;
    }

    public double calcularTotal() {
        return calcularSubtotal() + calcularIva();
    }

    public List<ItemCarrito> obtenerItems() {
        return new ArrayList<>(items);
    }

    public boolean estaVacio() {
        return items.isEmpty();
    }
}