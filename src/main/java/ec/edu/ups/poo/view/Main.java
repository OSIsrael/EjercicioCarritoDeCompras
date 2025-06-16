package ec.edu.ups.poo.view;

import ec.edu.ups.poo.controlador.ProductoController;
import ec.edu.ups.poo.dao.ProductoDAO;
import ec.edu.ups.poo.dao.impl.ProductoDAOMemoria;

public class Main {
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {

                ProductoAnadirView productoView = new ProductoAnadirView();
                ProductoDAO productoDAO = new ProductoDAOMemoria();
                ProductoListaView productoListaView = new ProductoListaView();
                new ProductoController(productoDAO, productoView,productoListaView);
            }
        });
    }
}
