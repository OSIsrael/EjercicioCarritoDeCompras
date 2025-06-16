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
    private JDesktopPane jDesktopPane;

    public PrincipalView() {
        jDesktopPane = new JDesktopPane();
        menuBar = new JMenuBar();
        menuProducto = new JMenu("Producto");
        menuItemCrearProducto = new JMenuItem("Crear Producto");
        menuItemEditarProducto = new JMenuItem("Editar Producto");
        menuItemEliminarProducto = new JMenuItem("Eliminar Producto");
        menuItemBuscarProducto = new JMenuItem("Buscar Producto");

        menuBar.add(menuProducto);
        menuProducto.add(menuItemCrearProducto);
        menuProducto.add(menuItemEditarProducto);
        menuProducto.add(menuItemEliminarProducto);
        menuProducto.add(menuItemBuscarProducto);



        setJMenuBar(menuBar);
        setContentPane(jDesktopPane);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Sistema de Compras");
        setSize(500, 500);
        setLocationRelativeTo(null);
        setVisible(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

    }
    public static void main(String[] args) {
        new PrincipalView();
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
}
