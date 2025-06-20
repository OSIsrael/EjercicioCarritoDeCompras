package ec.edu.ups.poo.view;

import ec.edu.ups.poo.controlador.CarritoController;
import ec.edu.ups.poo.controlador.ProductoController;
import ec.edu.ups.poo.controlador.UsuarioController;
import ec.edu.ups.poo.dao.CarritoDAO;
import ec.edu.ups.poo.dao.ProductoDAO;
import ec.edu.ups.poo.dao.UsuarioDAO;
import ec.edu.ups.poo.dao.impl.CarritoDAOMemoria;
import ec.edu.ups.poo.dao.impl.ProductoDAOMemoria;
import ec.edu.ups.poo.dao.impl.UsuarioDAOMemoria;
import ec.edu.ups.poo.modelo.Rol;
import ec.edu.ups.poo.modelo.Usuario;

import java.awt.event.*;

public class Main {
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                //Iniciar sesion
                UsuarioDAO usuarioDAO = new UsuarioDAOMemoria();
                LoginView loginView = new LoginView();
                loginView.setVisible(true);
                UsuarioController usuarioController = new UsuarioController(usuarioDAO, loginView);

                loginView.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        Usuario usuarioA = usuarioController.getUsuarioAutenticado();
                        if (usuarioA != null) {
                            //instancias (singleton)
                            ProductoDAO productoDAO = new ProductoDAOMemoria();
                            CarritoDAO carritoDAO = new CarritoDAOMemoria();


                            //instancias de vistas
                            PrincipalView principalView = new PrincipalView();
                            ProductoAnadirView productoAnadirView = new ProductoAnadirView();
                            ProductoListaView productoListaView = new ProductoListaView();
                            ProductoEliminar productoEliminar = new ProductoEliminar();
                            ProductoEditar productoEditar = new ProductoEditar();
                            CarritoAnadirView carritoAnadirView = new CarritoAnadirView();

                            //intancia controller
                            ProductoController productoController = new ProductoController(productoDAO, productoAnadirView, productoListaView, productoEditar, productoEliminar, carritoAnadirView);
                            CarritoController carritoController = new CarritoController(carritoDAO, productoDAO, carritoAnadirView);
                            principalView.mostrarMensaje("Bienvenido: "+usuarioA.getUsername());
                            if (usuarioA.getRol().equals(Rol.USUARIO)){
                                principalView.desactivar();
                            }

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
                        } else {
                            System.out.println("Usuario no autenticado");
                        }
                    }
                });
            }
        });
    }

}