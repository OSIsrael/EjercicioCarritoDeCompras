package ec.edu.ups.poo.view;

import ec.edu.ups.poo.controlador.ProductoController;
import ec.edu.ups.poo.dao.ProductoDAO;
import ec.edu.ups.poo.dao.impl.ProductoDAOMemoria;

public class Main {
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {

                ProductoAnadirView productoAnadirView = new ProductoAnadirView();
                ProductoListaView productoListaView = new ProductoListaView();
                ProductoEditar productoEditarView = new ProductoEditar();
                ProductoEliminar productoEliminarView = new ProductoEliminar();

                ProductoDAO productoDAO = new ProductoDAOMemoria();


                new ProductoController(productoDAO, productoAnadirView, productoListaView,
                        productoEditarView, productoEliminarView);
            }
        });
    }
}