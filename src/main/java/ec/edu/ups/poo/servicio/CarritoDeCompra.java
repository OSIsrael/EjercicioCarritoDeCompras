package ec.edu.ups.poo.servicio;
import ec.edu.ups.poo.modelo.Item;
import ec.edu.ups.poo.modelo.Producto;

import java.util.ArrayList;
import java.util.List;

public class CarritoDeCompra implements Carrito{
    private List<Item> items;

    public CarritoDeCompra() {
        this.items = new ArrayList<>();
    }

    @Override
    public double calcular(List<Item> items) {
        double total = 0.0;
        for (Item item : items) {
            total += item.getSubtotal();
        }
        return total;
    }

    @Override
    public void adicionarItem(Item item) {
        for (Item existingItem : items) {
            if (existingItem.equals(item)) {
                existingItem.setCantidad(existingItem.getCantidad() + item.getCantidad());
                return;
            }
        }
        items.add(item);
    }

    public List<Item> getItems() {
        return new ArrayList<>(items);
    }

    public double getTotal() {
        return calcular(items);
    }

    public void removerItem(Producto producto) {
        items.removeIf(item -> item.getProducto().equals(producto));
    }

    public void limpiarCarrito() {
        items.clear();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Carrito de Compras:\n");
        for (Item item : items) {
            sb.append("- ").append(item).append("\n");
        }
        sb.append("Total: $").append(String.format("%.2f", getTotal()));
        return sb.toString();
    }
}
