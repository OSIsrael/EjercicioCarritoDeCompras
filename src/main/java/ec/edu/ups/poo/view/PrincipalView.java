package ec.edu.ups.poo.view;

import ec.edu.ups.poo.modelo.Producto;
import ec.edu.ups.poo.util.MensajeInternacionalizacionHandler;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PrincipalView extends JFrame {
    private MensajeInternacionalizacionHandler mensajeInternacionalizacionHandler;
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
    private JMenu menuIdiomas;
    private JMenuItem menuItemIngles;
    private JMenuItem menuItemEspañol;
    private JMenuItem menuItemFrances;


    public PrincipalView() {
        mensajeInternacionalizacionHandler=new MensajeInternacionalizacionHandler("es","EC");
        initcomponents();

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
        menuProducto.setVisible(false);
        menuAdmin.setVisible(false); // Oculta todo el menú de administración
    }

    public JMenuItem getMenuItemIngles() {
        return menuItemIngles;
    }

    public JMenuItem getMenuItemEspañol() {
        return menuItemEspañol;
    }

    public JMenuItem getMenuItemFrances() {
        return menuItemFrances;
    }
    private void initcomponents(){
        jDesktopPane = new JDesktopPane();
        menuBar = new JMenuBar();
        menuProducto = new JMenu(mensajeInternacionalizacionHandler.get("menu.producto"));
        menuItemCrearProducto = new JMenuItem(mensajeInternacionalizacionHandler.get("menu.producto.crear"));
        menuItemEditarProducto = new JMenuItem(mensajeInternacionalizacionHandler.get("menu.producto.editar"));
        menuItemEliminarProducto = new JMenuItem(mensajeInternacionalizacionHandler.get("menu.producto.eliminar"));
        menuItemBuscarProducto = new JMenuItem(mensajeInternacionalizacionHandler.get("menu.producto.buscar"));
        menuCarrito = new JMenu(mensajeInternacionalizacionHandler.get("menu.carrito"));
        menuItemCarrito = new JMenuItem(mensajeInternacionalizacionHandler.get("menu.carrito.anadir"));
        menuItemCarritoListar=new JMenuItem(mensajeInternacionalizacionHandler.get("menu.carrito.listar"));
        menuBar.add(menuProducto);
        menuProducto.add(menuItemCrearProducto);
        menuProducto.add(menuItemEditarProducto);
        menuProducto.add(menuItemEliminarProducto);
        menuProducto.add(menuItemBuscarProducto);
        menuBar.add(menuCarrito);
        menuCarrito.add(menuItemCarrito);
        menuCarrito.add(menuItemCarritoListar);
        menuAdmin=new JMenu(mensajeInternacionalizacionHandler.get("menu.administrador"));
        menuItemGestionarUsuarios=new JMenuItem(mensajeInternacionalizacionHandler.get("menu.administrador.gestionar"));
        menuAdmin.add(menuItemGestionarUsuarios);
        menuBar.add(menuAdmin);
        cerrarSesion=new JMenu(mensajeInternacionalizacionHandler.get("menu.cerrarSesion"));
        menuItemCerrarSesion=new JMenuItem(mensajeInternacionalizacionHandler.get("menu.cerrarSesion.cerrar"));
        cerrarSesion.add(menuItemCerrarSesion);
        menuBar.add(cerrarSesion);
        menuIdiomas=new JMenu(mensajeInternacionalizacionHandler.get("menu.idiomas"));
        menuItemIngles=new JMenuItem(mensajeInternacionalizacionHandler.get("menu.idiomas.ingles"));
        menuItemEspañol=new JMenuItem(mensajeInternacionalizacionHandler.get("menu.idiomas.español"));
        menuItemFrances=new JMenuItem(mensajeInternacionalizacionHandler.get("menu.idiomas.frances"));
        menuIdiomas.add(menuItemIngles);
        menuIdiomas.add(menuItemEspañol);
        menuIdiomas.add(menuItemFrances);
        menuBar.add(menuIdiomas);



        setJMenuBar(menuBar);
        setContentPane(jDesktopPane);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle(mensajeInternacionalizacionHandler.get("titulo"));
        setSize(500, 500);
        setLocationRelativeTo(null);
        setVisible(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }
    public void cambiarIdiomas(String lenguaje,String pais){
        mensajeInternacionalizacionHandler.setLenguaje(lenguaje,pais);
        setTitle(mensajeInternacionalizacionHandler.get("titulo"));
        menuProducto.setText(mensajeInternacionalizacionHandler.get("menu.producto"));
        menuItemCrearProducto.setText(mensajeInternacionalizacionHandler.get("menu.producto.crear"));
        menuItemEditarProducto.setText(mensajeInternacionalizacionHandler.get("menu.producto.editar"));
        menuItemEliminarProducto.setText(mensajeInternacionalizacionHandler.get("menu.producto.eliminar"));
        menuItemBuscarProducto.setText(mensajeInternacionalizacionHandler.get("menu.producto.buscar"));
        menuCarrito.setText(mensajeInternacionalizacionHandler.get("menu.carrito"));
        menuItemCarrito.setText(mensajeInternacionalizacionHandler.get("menu.carrito.anadir"));
        menuItemCarritoListar.setText(mensajeInternacionalizacionHandler.get("menu.carrito.listar"));
        menuAdmin.setText(mensajeInternacionalizacionHandler.get("menu.administrador"));
        menuItemGestionarUsuarios.setText(mensajeInternacionalizacionHandler.get("menu.administrador.gestionar"));
        cerrarSesion.setText(mensajeInternacionalizacionHandler.get("menu.cerrarSesion"));
        menuItemCerrarSesion.setText(mensajeInternacionalizacionHandler.get("menu.cerrarSesion.cerrar"));
        menuIdiomas.setText(mensajeInternacionalizacionHandler.get("menu.idiomas"));
        menuItemIngles.setText(mensajeInternacionalizacionHandler.get("menu.idiomas.ingles"));
        menuItemEspañol.setText(mensajeInternacionalizacionHandler.get("menu.idiomas.español"));
        menuItemFrances.setText(mensajeInternacionalizacionHandler.get("menu.idiomas.frances"));

    }
}
