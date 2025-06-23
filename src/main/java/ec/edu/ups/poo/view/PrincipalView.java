package ec.edu.ups.poo.view;

import ec.edu.ups.poo.modelo.Producto;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PrincipalView extends JFrame {
    private JMenuBar menuBar;
    private JMenu menuProducto;
    private JMenuItem menuItemCrearProducto;
    private JMenuItem menuItemEditarProducto;
    private JMenuItem menuItemEliminarProducto;
    private JMenuItem menuItemBuscarProducto;
    private JMenuItem menuItemCarritoListar;
    private JDesktopPane jDesktopPane;
    private JMenu menuCarrito;
    private JMenuItem menuItemCarrito;
    private JMenu menuAdmin;
    private JMenuItem menuItemGestionarUsuarios;
    private JMenu cerrarSesion;
    private JMenuItem menuItemCerrarSesion;


    public PrincipalView() {
        jDesktopPane = new JDesktopPane();
        menuBar = new JMenuBar();
        menuProducto = new JMenu("Producto");
        menuItemCrearProducto = new JMenuItem("Crear Producto");
        menuItemEditarProducto = new JMenuItem("Editar Producto");
        menuItemEliminarProducto = new JMenuItem("Eliminar Producto");
        menuItemBuscarProducto = new JMenuItem("Buscar Producto");
        menuCarrito = new JMenu("Carrito");
        menuItemCarrito = new JMenuItem("Agregar");
        menuItemCarritoListar=new JMenuItem("Listar Carritos");
        menuBar.add(menuProducto);
        menuProducto.add(menuItemCrearProducto);
        menuProducto.add(menuItemEditarProducto);
        menuProducto.add(menuItemEliminarProducto);
        menuProducto.add(menuItemBuscarProducto);
        menuBar.add(menuCarrito);
        menuCarrito.add(menuItemCarrito);
        menuCarrito.add(menuItemCarritoListar);
        menuAdmin=new JMenu("Administracion");
        menuItemGestionarUsuarios=new JMenuItem("Gestionar Usuarios");
        menuAdmin.add(menuItemGestionarUsuarios);
        menuBar.add(menuAdmin);
        cerrarSesion=new JMenu("CERRAR SESION");
        menuItemCerrarSesion=new JMenuItem("Cerrar Sesion");
        cerrarSesion.add(menuItemCerrarSesion);
        menuBar.add(cerrarSesion);


        setJMenuBar(menuBar);
        setContentPane(jDesktopPane);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Sistema de Compras");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setVisible(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

    }


    public JMenuItem getMenuItemCrearProducto() {
        return menuItemCrearProducto;
    }

    public JMenuItem getMenuItemEditarProducto() {
        return menuItemEditarProducto;
    }

    public JMenuItem getMenuItemEliminarProducto() {
        return menuItemEliminarProducto;
    }

    public JMenuItem getMenuItemBuscarProducto() {
        return menuItemBuscarProducto;
    }

    public JDesktopPane getjDesktopPane() {
        return jDesktopPane;
    }

    public JMenuItem getMenuItemCarrito() {
        return menuItemCarrito;
    }

    public void setMenuItemCarrito(JMenuItem menuItemCarrito) {
        this.menuItemCarrito = menuItemCarrito;
    }

    public JMenu getMenuCarrito() {
        return menuCarrito;
    }

    public void setMenuCarrito(JMenu menuCarrito) {
        this.menuCarrito = menuCarrito;
    }

    public JMenuItem getMenuItemGestionarUsuarios() {
        return menuItemGestionarUsuarios;
    }

    public void setMenuItemGestionarUsuarios(JMenuItem menuItemGestionarUsuarios) {
        this.menuItemGestionarUsuarios = menuItemGestionarUsuarios;
    }

    public JMenuItem getMenuItemCarritoListar() {
        return menuItemCarritoListar;
    }
    public JMenuItem getMenuItemCerrarSesion(){return menuItemCerrarSesion;}
    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }
    public void desactivar(){
        getMenuItemCrearProducto().setEnabled(false);
        getMenuItemEditarProducto().setEnabled(false);
        getMenuItemEliminarProducto().setEnabled(false);
        getMenuItemBuscarProducto().setEnabled(false);

    }
    public void configurarParaRolUsuario() {
        // ... (desactivar otras opciones)
        menuAdmin.setVisible(false); // Oculta todo el menú de administración
    }
}
