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

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main {

    public static void main(String[] args) {
        // Inicia la aplicación en el hilo de despacho de eventos de Swing
        SwingUtilities.invokeLater(() -> mostrarLogin());
    }


    private static void mostrarLogin() {
        // --- DAOs ---
        UsuarioDAO usuarioDAO = new UsuarioDAOMemoria();

        // Crear usuarios por defecto si no existen (para pruebas)
        if (usuarioDAO.buscarPorUsername("admin") == null) {
            usuarioDAO.crear(new Usuario("admin", "admin", Rol.ADMINISTRADOR));
        }
        if (usuarioDAO.buscarPorUsername("user") == null) {
            usuarioDAO.crear(new Usuario("user", "user", Rol.USUARIO));
        }

        // --- Vistas ---
        LoginView loginView = new LoginView();
        // Usamos tu clase RegistrarUsuario
        RegistrarUsuario registrarUsuarioView = new RegistrarUsuario();
        // Creamos la vista de administración de usuarios (necesaria para el controlador)
        UsuarioAdminView usuarioAdminView = new UsuarioAdminView();

        // --- Controlador de Usuario ---
        // Se pasa todas las vistas que el controlador de usuario gestiona
        UsuarioController usuarioController = new UsuarioController(usuarioDAO, loginView, registrarUsuarioView, usuarioAdminView);

        // 2. Esperar a que la ventana de login se cierre para continuar
        loginView.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                Usuario usuarioAutenticado = usuarioController.getUsuarioAutenticado();

                // 3. Si el login fue exitoso, iniciar la ventana principal
                if (usuarioAutenticado != null) {
                    // Pasamos el usuario autenticado, el controlador de usuario y la vista de admin
                    // para que la aplicación principal pueda usarlos.
                    iniciarAplicacionPrincipal(usuarioAutenticado, usuarioController, usuarioAdminView);
                } else {
                    System.out.println("Login cancelado o fallido. La aplicación se cerrará.");
                    System.exit(0); // Cierra la aplicación si no hay login
                }
            }
        });
        loginView.setVisible(true);
    }

    private static void iniciarAplicacionPrincipal(Usuario usuarioAutenticado, UsuarioController usuarioController, UsuarioAdminView usuarioAdminView) {

        ProductoDAO productoDAO = new ProductoDAOMemoria();
        CarritoDAO carritoDAO = new CarritoDAOMemoria();

        PrincipalView principalView = new PrincipalView();
        ProductoAnadirView productoAnadirView = new ProductoAnadirView();
        ProductoListaView productoListaView = new ProductoListaView();
        ProductoEliminar productoEliminar = new ProductoEliminar();
        ProductoEditar productoEditar = new ProductoEditar();
        CarritoAnadirView carritoAnadirView = new CarritoAnadirView();
        CarritoListarView carritoListarView = new CarritoListarView();

        ProductoController productoController = new ProductoController(productoDAO, productoAnadirView, productoListaView, productoEditar, productoEliminar, carritoAnadirView);
        CarritoController carritoController = new CarritoController(carritoDAO, productoDAO, carritoAnadirView, usuarioAutenticado, carritoListarView);

        principalView.mostrarMensaje("Bienvenido: " + usuarioAutenticado.getUsername());

        // Configurar opciones de menú según el rol del usuario
        if (usuarioAutenticado.getRol() == Rol.USUARIO) {
            principalView.configurarParaRolUsuario(); // Desactiva opciones de admin
        }

        // --- Asignación de Eventos a los Menús ---
        // Eventos para la gestión de Productos
        principalView.getMenuItemCrearProducto().addActionListener(e -> mostrarVentana(principalView, productoAnadirView));
        principalView.getMenuItemBuscarProducto().addActionListener(e -> mostrarVentana(principalView, productoListaView));
        principalView.getMenuItemEditarProducto().addActionListener(e -> mostrarVentana(principalView, productoEditar));
        principalView.getMenuItemEliminarProducto().addActionListener(e -> mostrarVentana(principalView, productoEliminar));

        // Eventos para el Carrito
        principalView.getMenuItemCarrito().addActionListener(e -> mostrarVentana(principalView, carritoAnadirView));
        principalView.getMenuItemCarritoListar().addActionListener(e -> {
            carritoController.listarTodosLosCarritos(); // Cargar datos antes de mostrar
            mostrarVentana(principalView, carritoListarView);
        });

        // Eventos para la Gestión de Usuarios (solo para ADMIN)
        principalView.getMenuItemGestionarUsuarios().addActionListener(e -> {
            usuarioController.listarUsuarios();
            mostrarVentana(principalView, usuarioAdminView);
        });


        principalView.getMenuItemCerrarSesion().addActionListener(e -> {
            principalView.dispose();
            mostrarLogin();
        });

        principalView.setVisible(true);
    }

    private static void mostrarVentana(PrincipalView principal, JInternalFrame ventana) {
        if (!ventana.isVisible()) {
            principal.getjDesktopPane().add(ventana);
            ventana.setVisible(true);
        }
    }
}