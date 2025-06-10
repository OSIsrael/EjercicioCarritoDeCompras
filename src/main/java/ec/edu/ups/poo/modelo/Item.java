package ec.edu.ups.poo.modelo;

import java.util.Objects;

public class Item {
    private Producto producto;
    private double cantidad;

    public Item() {
    }

    public Item(Producto producto, double cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public double getSubtotal() {
        return producto.getPrecio() * cantidad;
    }

    @Override
    public String toString() {
        return "Item{" +
                "producto=" + producto +
                ", cantidad=" + cantidad +
                ", subtotal=" + getSubtotal() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(producto, item.producto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(producto);
    }
}