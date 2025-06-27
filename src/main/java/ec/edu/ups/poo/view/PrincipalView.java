package ec.edu.ups.poo.view;

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
    private JMenuItem menuItemCarrito; // Para "Añadir al Carrito"
    private JMenuItem menuItemCarritoBuscar; // Para "Buscar Carrito"
    private JMenuItem menuItemCarritoModificar; // Para "Modificar Carrito"
    private JMenuItem menuItemCarritoEliminar;// Para "Eliminar Carrito"
    private JMenu menuAdmin;
    private JMenuItem menuItemGestionarUsuarios;
    private JMenuItem menuItemCerrarSesion;
    private JMenuItem menuItemBuscarUsuario;
    private JMenu menuIdiomas;
    private JMenuItem menuItemIngles;
    private JMenuItem menuItemEspañol;
    private JMenuItem menuItemFrances;


    public PrincipalView() {
        // Inicializa el manejador de internacionalización con el idioma por defecto
        mensajeInternacionalizacionHandler = new MensajeInternacionalizacionHandler("es", "EC");
        initcomponents(); // Llama al método para inicializar los componentes de la UI
    }

    // --- Getters para los JMenuItems ---
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

    public JMenuItem getMenuItemGestionarUsuarios() {
        return menuItemGestionarUsuarios;
    }

    public JMenuItem getMenuItemCarritoListar() {
        return menuItemCarritoListar;
    }

    public JMenuItem getMenuItemCerrarSesion() {
        return menuItemCerrarSesion;
    }

    public JMenuItem getMenuItemCarritoBuscar() {
        return menuItemCarritoBuscar;
    }

    public JMenuItem getMenuItemCarritoModificar() {
        return menuItemCarritoModificar;
    }

    public JMenuItem getMenuItemCarritoEliminar() {
        return menuItemCarritoEliminar;
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

    // --- Métodos de UI ---
    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    public JMenuItem getMenuItemBuscarUsuario() {
        return menuItemBuscarUsuario;
    }

    /**
     * Configura la visibilidad de los menús para un usuario con rol normal (no administrador).
     */
    public void configurarParaRolUsuario() {
        // CORRECCIÓN: Oculta solo las opciones de producto para administradores.
        menuItemCrearProducto.setVisible(false);
        menuItemEditarProducto.setVisible(false);
        menuItemEliminarProducto.setVisible(false);

        // MANTIENE VISIBLE la opción de buscar, para que el usuario pueda ver los productos.
        menuItemBuscarProducto.setVisible(true);

        // Oculta completamente el menú de Administración
        menuAdmin.setVisible(false);

        // Oculta la opción de listar todos los carritos (solo para admin)
    }

    /**
     * Inicializa todos los componentes de la interfaz gráfica.
     */
    private void initcomponents() {
        jDesktopPane = new JDesktopPane();
        menuBar = new JMenuBar();

        // Menú Producto
        menuProducto = new JMenu(mensajeInternacionalizacionHandler.get("menu.producto"));
        menuItemCrearProducto = new JMenuItem(mensajeInternacionalizacionHandler.get("menu.producto.crear"));
        menuItemEditarProducto = new JMenuItem(mensajeInternacionalizacionHandler.get("menu.producto.editar"));
        menuItemEliminarProducto = new JMenuItem(mensajeInternacionalizacionHandler.get("menu.producto.eliminar"));
        menuItemBuscarProducto = new JMenuItem(mensajeInternacionalizacionHandler.get("menu.producto.buscar"));
        menuProducto.add(menuItemCrearProducto);
        menuProducto.add(menuItemEditarProducto);
        menuProducto.add(menuItemEliminarProducto);
        menuProducto.add(menuItemBuscarProducto);
        menuBar.add(menuProducto);

        // Menú Carrito
        menuCarrito = new JMenu(mensajeInternacionalizacionHandler.get("menu.carrito"));
        menuItemCarrito = new JMenuItem(mensajeInternacionalizacionHandler.get("menu.carrito.anadir"));
        menuItemCarritoListar = new JMenuItem(mensajeInternacionalizacionHandler.get("menu.carrito.listar"));
        menuItemCarritoBuscar = new JMenuItem(mensajeInternacionalizacionHandler.get("menu.carrito.buscar"));
        menuItemCarritoModificar = new JMenuItem(mensajeInternacionalizacionHandler.get("menu.carrito.modificar"));
        menuItemCarritoEliminar = new JMenuItem(mensajeInternacionalizacionHandler.get("menu.carrito.eliminar"));
        menuCarrito.add(menuItemCarrito);
        menuCarrito.add(menuItemCarritoListar);
        menuCarrito.add(menuItemCarritoBuscar);
        menuCarrito.add(menuItemCarritoModificar);
        menuCarrito.add(menuItemCarritoEliminar);
        menuBar.add(menuCarrito);

        // Menú Administración
        menuAdmin = new JMenu(mensajeInternacionalizacionHandler.get("menu.administrador"));
        menuItemGestionarUsuarios = new JMenuItem(mensajeInternacionalizacionHandler.get("menu.administrador.gestionar"));
        menuItemBuscarUsuario = new JMenuItem(mensajeInternacionalizacionHandler.get("menu.administrador.buscar"));
        menuAdmin.add(menuItemGestionarUsuarios);
        menuAdmin.add(menuItemBuscarUsuario);
        menuBar.add(menuAdmin);

        // Opción Cerrar Sesión (directamente en la barra de menú, con un espaciador)
        menuItemCerrarSesion = new JMenuItem(mensajeInternacionalizacionHandler.get("menu.cerrarSesion.cerrar"));
        menuBar.add(Box.createHorizontalGlue()); // Empuja el siguiente componente a la derecha
        menuBar.add(menuItemCerrarSesion);

        // Menú Idiomas
        menuIdiomas = new JMenu(mensajeInternacionalizacionHandler.get("menu.idiomas"));
        menuItemIngles = new JMenuItem(mensajeInternacionalizacionHandler.get("menu.idiomas.ingles"));
        menuItemEspañol = new JMenuItem(mensajeInternacionalizacionHandler.get("menu.idiomas.español"));
        menuItemFrances = new JMenuItem(mensajeInternacionalizacionHandler.get("menu.idiomas.frances"));
        menuIdiomas.add(menuItemIngles);
        menuIdiomas.add(menuItemEspañol);
        menuIdiomas.add(menuItemFrances);
        menuBar.add(menuIdiomas);

        // Configuración de la ventana principal
        setJMenuBar(menuBar);
        setContentPane(jDesktopPane);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle(mensajeInternacionalizacionHandler.get("titulo"));
        setSize(800, 600); // Tamaño inicial
        setLocationRelativeTo(null); // Centrar en pantalla
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Inicia maximizada
    }

    /**
     * Cambia el idioma de la interfaz de usuario.
     * @param lenguaje El código de lenguaje (ej. "es", "en", "fr").
     * @param pais El código de país (ej. "EC", "US", "FR").
     */
    public void cambiarIdiomas(String lenguaje, String pais) {
        mensajeInternacionalizacionHandler.setLenguaje(lenguaje, pais);
        setTitle(mensajeInternacionalizacionHandler.get("titulo"));
        menuProducto.setText(mensajeInternacionalizacionHandler.get("menu.producto"));
        menuItemCrearProducto.setText(mensajeInternacionalizacionHandler.get("menu.producto.crear"));
        menuItemEditarProducto.setText(mensajeInternacionalizacionHandler.get("menu.producto.editar"));
        menuItemEliminarProducto.setText(mensajeInternacionalizacionHandler.get("menu.producto.eliminar"));
        menuItemBuscarProducto.setText(mensajeInternacionalizacionHandler.get("menu.producto.buscar"));
        menuCarrito.setText(mensajeInternacionalizacionHandler.get("menu.carrito"));
        menuItemCarrito.setText(mensajeInternacionalizacionHandler.get("menu.carrito.anadir"));
        menuItemCarritoListar.setText(mensajeInternacionalizacionHandler.get("menu.carrito.listar"));
        menuItemCarritoBuscar.setText(mensajeInternacionalizacionHandler.get("menu.carrito.buscar"));
        menuItemCarritoModificar.setText(mensajeInternacionalizacionHandler.get("menu.carrito.modificar"));
        menuItemCarritoEliminar.setText(mensajeInternacionalizacionHandler.get("menu.carrito.eliminar"));
        menuAdmin.setText(mensajeInternacionalizacionHandler.get("menu.administrador"));
        menuItemGestionarUsuarios.setText(mensajeInternacionalizacionHandler.get("menu.administrador.gestionar"));
        menuItemCerrarSesion.setText(mensajeInternacionalizacionHandler.get("menu.cerrarSesion.cerrar"));
        menuIdiomas.setText(mensajeInternacionalizacionHandler.get("menu.idiomas"));
        menuItemIngles.setText(mensajeInternacionalizacionHandler.get("menu.idiomas.ingles"));
        menuItemEspañol.setText(mensajeInternacionalizacionHandler.get("menu.idiomas.español"));
        menuItemFrances.setText(mensajeInternacionalizacionHandler.get("menu.idiomas.frances"));
    }
}