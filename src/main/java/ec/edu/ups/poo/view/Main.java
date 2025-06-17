package ec.edu.ups.poo.view;

import ec.edu.ups.poo.controlador.ProductoController;
import ec.edu.ups.poo.dao.ProductoDAO;
import ec.edu.ups.poo.dao.impl.ProductoDAOMemoria;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {

                //instancias (singleton)
                ProductoDAO productoDAO = new ProductoDAOMemoria();

                //instancias de vistas
                PrincipalView principalView = new PrincipalView();
                ProductoAnadirView productoAnadirView = new ProductoAnadirView();
                ProductoListaView productoListaView = new ProductoListaView();
                ProductoEliminar productoEliminar = new ProductoEliminar();
                ProductoEditar productoEditar = new ProductoEditar();
                CarritoAnadirView carritoAnadirView = new CarritoAnadirView();

                //intancia controller
                ProductoController productoController = new ProductoController(productoDAO,productoAnadirView,productoListaView,productoEditar,productoEliminar,carritoAnadirView);



                principalView.getMenuItemCrearProducto().addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (!productoAnadirView.isVisible()) {
                            principalView.getjDesktopPane().add(productoAnadirView);
                            productoAnadirView.setVisible(true);
                        }
                    }
                });
                principalView.getMenuItemBuscarProducto().addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (!productoListaView.isVisible()) {
                            principalView.getjDesktopPane().add(productoListaView);
                            productoListaView.setVisible(true);
                        }
                    }
                });
                principalView.getMenuItemEliminarProducto().addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (!productoEliminar.isVisible()) {
                            principalView.getjDesktopPane().add(productoEliminar);
                            productoEliminar.setVisible(true);
                        }
                    }
                });
                principalView.getMenuItemEditarProducto().addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (!productoEditar.isVisible()) {
                            principalView.getjDesktopPane().add(productoEditar);
                            productoEditar.setVisible(true);
                        }
                    }
                });
                principalView.getMenuItemCarrito().addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (!carritoAnadirView.isVisible()) {
                            principalView.getjDesktopPane().add(carritoAnadirView);
                            carritoAnadirView.setVisible(true);
                        }
                    }
                });
            }
        });
    }
}