package ec.edu.ups.poo.modelo;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

public class Carrito {
    private int codigo;
    private GregorianCalendar fechaCreacion;
    private List<ItemCarrito> items;

    public Carrito() {
        items = new ArrayList<>();
        fechaCreacion = new GregorianCalendar();
    }

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
        return items;
    }

    public void setItems(List<ItemCarrito> items) {
        this.items = items;
    }

    public void agregarProducto(Producto producto, int cantidad) {
        // Verificar si el producto ya existe
        for (ItemCarrito item : items) {
            if (item.getProducto().getCodigo() == producto.getCodigo()) {
                // Si existe, incrementar la cantidad
                item.setCantidad(item.getCantidad() + cantidad);
                return;
            }
        }
        // Si no existe, agregar nuevo item
        items.add(new ItemCarrito(producto, cantidad));
    }

    public boolean eliminarProducto(int codigoProducto) {
        Iterator<ItemCarrito> it = items.iterator();
        while (it.hasNext()) {
            ItemCarrito item = it.next();
            if (item.getProducto().getCodigo() == codigoProducto) {
                it.remove();
                return true; // Producto eliminado exitosamente
            }
        }
        return false; // Producto no encontrado
    }

    public void vaciarCarrito() {
        items.clear();
    }

    public List<ItemCarrito> obtenerItems() {
        return new ArrayList<>(items); // Retornar una copia para evitar modificaciones externas
    }

    public boolean estaVacio() {
        return items.isEmpty();
    }

    public int getCantidadItems() {
        return items.size();
    }

    public int getCantidadTotalProductos() {
        int total = 0;
        for (ItemCarrito item : items) {
            total += item.getCantidad();
        }
        return total;
    }

    public boolean contieneProducto(int codigoProducto) {
        for (ItemCarrito item : items) {
            if (item.getProducto().getCodigo() == codigoProducto) {
                return true;
            }
        }
        return false;
    }

    public ItemCarrito obtenerItem(int codigoProducto) {
        for (ItemCarrito item : items) {
            if (item.getProducto().getCodigo() == codigoProducto) {
                return item;
            }
        }
        return null;
    }

    public boolean actualizarCantidadProducto(int codigoProducto, int nuevaCantidad) {
        if (nuevaCantidad <= 0) {
            return eliminarProducto(codigoProducto);
        }

        for (ItemCarrito item : items) {
            if (item.getProducto().getCodigo() == codigoProducto) {
                item.setCantidad(nuevaCantidad);
                return true;
            }
        }
        return false;
    }

    public double calcularSubtotal() {
        double subtotal = 0;
        for (ItemCarrito item : items) {
            subtotal += item.getSubtotal();
        }
        return Math.round(subtotal * 100.0) / 100.0; // Redondear a 2 decimales
    }

    public double calcularIva() {
        double iva = calcularSubtotal() * 0.12;
        return Math.round(iva * 100.0) / 100.0; // Redondear a 2 decimales
    }

    public double calcularTotal() {
        double total = calcularSubtotal() + calcularIva();
        return Math.round(total * 100.0) / 100.0; // Redondear a 2 decimales
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Carrito #").append(codigo).append("\n");
        sb.append("Fecha: ").append(fechaCreacion.getTime()).append("\n");
        sb.append("Items: ").append(items.size()).append("\n");
        sb.append("Total: $").append(calcularTotal()).append("\n");
        return sb.toString();
    }
}