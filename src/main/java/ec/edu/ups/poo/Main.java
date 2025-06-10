package ec.edu.ups.poo;

import ec.edu.ups.poo.modelo.Item;
import ec.edu.ups.poo.modelo.Producto;
import ec.edu.ups.poo.servicio.CarritoDeCompra;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== SISTEMA DE CARRITO DE COMPRAS ===\n");

        Producto laptop = new Producto("P001", "Laptop Dell", 799.99);
        Producto mouse = new Producto("P002", "Mouse Logitech", 25.50);
        Producto teclado = new Producto("P003", "Teclado Mecánico", 89.99);

        System.out.println("Productos disponibles:");
        System.out.println("1. " + laptop);
        System.out.println("2. " + mouse);
        System.out.println("3. " + teclado);
        System.out.println();


        CarritoDeCompra carrito = new CarritoDeCompra();

        System.out.println("Agregando productos al carrito...");

        Item itemLaptop = new Item(laptop, 1);
        carrito.adicionarItem(itemLaptop);
        System.out.println("✓ Agregado: " + itemLaptop);

        Item itemMouse = new Item(mouse, 2);
        carrito.adicionarItem(itemMouse);
        System.out.println("✓ Agregado: " + itemMouse);

        Item itemTeclado = new Item(teclado, 1);
        carrito.adicionarItem(itemTeclado);
        System.out.println("✓ Agregado: " + itemTeclado);

        System.out.println("\n" + carrito);


        System.out.println("\nAgregando más mouses...");
        Item masMousesA = new Item(mouse, 1);
        carrito.adicionarItem(masMousesA);
        System.out.println("✓ Agregado 1 mouse más");

        System.out.println("\nCarrito actualizado:");
        System.out.println(carrito);

        // Remover un producto
        System.out.println("\nRemoviendo el teclado...");
        carrito.removerItem(teclado);

        System.out.println("\nCarrito final:");
        System.out.println(carrito);
    }
}