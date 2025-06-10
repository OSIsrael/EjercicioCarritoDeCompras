package ec.edu.ups.poo.servicio;

import ec.edu.ups.poo.modelo.Item;

import java.util.List;

public interface Carrito {
    double calcular(List<Item> items);
    void adicionarItem(Item item);
}