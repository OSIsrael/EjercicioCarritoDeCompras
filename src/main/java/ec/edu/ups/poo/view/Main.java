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
import ec.edu.ups.poo.modelo.Producto;
import ec.edu.ups.poo.modelo.Rol;
import ec.edu.ups.poo.modelo.Usuario;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main {

    // DAOs como campos estáticos para que sean instancias únicas y persistan
    private static UsuarioDAO usuarioDAO;
    private static ProductoDAO productoDAO;
    private static CarritoDAO carritoDAO;

    public static void main(String[] args) {

        // Inicializar DAOs UNA SOLA VEZ
        usuarioDAO = new UsuarioDAOMemoria();
        productoDAO = new ProductoDAOMemoria();
        carritoDAO = new CarritoDAOMemoria();

        // Cargar datos iniciales UNA SOLA VEZ
        // CORRECCIÓN: Usar el método 'buscar' o 'buscarPorUsername' según tu DAO
        // Si tu DAO tiene 'buscarPorUsername', úsalo. Si no, cámbialo a 'buscar'.
        if (usuarioDAO.buscarPorUsername("admin") == null) {
            usuarioDAO.crear(new Usuario("admin", "admin", Rol.ADMINISTRADOR));
        }
        if (usuarioDAO.buscarPorUsername("user") == null) {
            usuarioDAO.crear(new Usuario("user", "user", Rol.USUARIO));
        }
        productoDAO.crear(new Producto(1, "Laptop Gamer", 1200.00));
        productoDAO.crear(new Producto(2, "Teclado Mecánico", 85.50));
        productoDAO.crear(new Producto(3, "Mouse Inalámbrico", 30.00));
        productoDAO.crear(new Producto(4, "Monitor 27 pulgadas", 350.00));
        System.out.println("Datos iniciales (usuarios y productos) cargados.");

        SwingUtilities.invokeLater(() -> mostrarLogin());
    }

    private static void mostrarLogin() {
        System.out.println("=== Iniciando proceso de Login ===");
        LoginView loginView = new LoginView();
        RegistrarUsuario registrarUsuarioView = new RegistrarUsuario();
        UsuarioAdminView usuarioAdminView = new UsuarioAdminView();

        UsuarioController usuarioController = new UsuarioController(
                usuarioDAO, loginView, registrarUsuarioView, usuarioAdminView);

        loginView.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                Usuario usuarioAutenticado = usuarioController.getUsuarioAutenticado();
                if (usuarioAutenticado != null) {
                    System.out.println("Login exitoso para: " + usuarioAutenticado.getUsername());
                    iniciarAplicacionPrincipal(usuarioAutenticado, usuarioController, usuarioAdminView);
                } else {
                    System.out.println("Login cancelado. Cerrando aplicación.");
                    System.exit(0);
                }
            }
        });
        loginView.setVisible(true);
    }

    private static void iniciarAplicacionPrincipal(Usuario usuarioAutenticado,
                                                   UsuarioController usuarioController,
                                                   UsuarioAdminView usuarioAdminView) {
        System.out.println("=== Iniciando aplicación principal para " + usuarioAutenticado.getUsername() + " ===");

        // --- Inicializar Vistas ---
        PrincipalView principalView = new PrincipalView();
        ProductoAnadirView productoAnadirView = new ProductoAnadirView();
        ProductoListaView productoListaView = new ProductoListaView();
        ProductoEliminar productoEliminar = new ProductoEliminar();
        ProductoEditar productoEditar = new ProductoEditar();

        // Vistas de Carrito (todas las que hemos creado)
        CarritoAnadirView carritoAnadirView = new CarritoAnadirView();
        CarritoListarView carritoListarView = new CarritoListarView();
        CarritoBuscarView carritoBuscarView = new CarritoBuscarView();
        CarritoModificarView carritoModificarView = new CarritoModificarView();
        CarritoEliminarView carritoEliminarView = new CarritoEliminarView();

        // --- Inicializar Controladores (usando los DAOs únicos) ---
        ProductoController productoController = new ProductoController(
                productoDAO, productoAnadirView, productoListaView,
                productoEditar, productoEliminar, carritoAnadirView);

        CarritoController carritoController = new CarritoController(
                carritoDAO, productoDAO, carritoAnadirView, usuarioAutenticado,
                carritoListarView, carritoModificarView, carritoBuscarView, carritoEliminarView); // Pasa todas las vistas

        // --- Configurar ventana principal ---
        principalView.mostrarMensaje("Bienvenido: " + usuarioAutenticado.getUsername() + " (" + usuarioAutenticado.getRol() + ")");

        if (usuarioAutenticado.getRol() == Rol.USUARIO) {
            principalView.configurarParaRolUsuario();
        }

        // --- Configurar eventos de los menús ---
        configurarEventosProductos(principalView, productoAnadirView, productoListaView, productoEditar, productoEliminar);
        configurarEventosCarrito(principalView, carritoAnadirView, carritoListarView, carritoBuscarView, carritoModificarView, carritoEliminarView, carritoController);
        configurarEventosUsuarios(principalView, usuarioController, usuarioAdminView);
        configurarEventosCerrarSesion(principalView);
        configurarEventosIdioma(principalView);

        principalView.setVisible(true);
    }

    private static void configurarEventosProductos(PrincipalView principalView, ProductoAnadirView productoAnadirView, ProductoListaView productoListaView, ProductoEditar productoEditar, ProductoEliminar productoEliminar) {
        principalView.getMenuItemCrearProducto().addActionListener(e -> mostrarVentana(principalView, productoAnadirView));
        principalView.getMenuItemBuscarProducto().addActionListener(e -> mostrarVentana(principalView, productoListaView));
        principalView.getMenuItemEditarProducto().addActionListener(e -> mostrarVentana(principalView, productoEditar));
        principalView.getMenuItemEliminarProducto().addActionListener(e -> mostrarVentana(principalView, productoEliminar));
    }

    private static void configurarEventosCarrito(PrincipalView principalView, CarritoAnadirView carritoAnadirView,
                                                 CarritoListarView carritoListarView, CarritoBuscarView carritoBuscarView,
                                                 CarritoModificarView carritoModificarView, CarritoEliminarView carritoEliminarView,
                                                 CarritoController carritoController) {
        principalView.getMenuItemCarrito().addActionListener(e -> mostrarVentana(principalView, carritoAnadirView));

        principalView.getMenuItemCarritoListar().addActionListener(e -> {
            carritoController.listarTodosLosCarritos(); // Cargar datos antes de mostrar
            mostrarVentana(principalView, carritoListarView);
        });

        principalView.getMenuItemCarritoBuscar().addActionListener(e -> {
            carritoBuscarView.limpiarVista(); // Limpiar antes de mostrar
            mostrarVentana(principalView, carritoBuscarView);
        });

        principalView.getMenuItemCarritoModificar().addActionListener(e -> {
            carritoModificarView.limpiarVista(); // Limpiar antes de mostrar
            mostrarVentana(principalView, carritoModificarView);
        });

        principalView.getMenuItemCarritoEliminar().addActionListener(e -> {
            carritoEliminarView.limpiarVista(); // Limpiar antes de mostrar
            mostrarVentana(principalView, carritoEliminarView);
        });
    }

    private static void configurarEventosUsuarios(PrincipalView principalView, UsuarioController usuarioController, UsuarioAdminView usuarioAdminView) {
        principalView.getMenuItemGestionarUsuarios().addActionListener(e -> {
            usuarioController.listarUsuarios();
            mostrarVentana(principalView, usuarioAdminView);
        });
    }

    private static void configurarEventosCerrarSesion(PrincipalView principalView) {
        principalView.getMenuItemCerrarSesion().addActionListener(e -> {
            int opcion = JOptionPane.showConfirmDialog(principalView, "¿Está seguro de que desea cerrar sesión?", "Cerrar Sesión", JOptionPane.YES_NO_OPTION);
            if (opcion == JOptionPane.YES_OPTION) {
                principalView.dispose();
                mostrarLogin();
            }
        });
    }

    private static void configurarEventosIdioma(PrincipalView principalView) {
        principalView.getMenuItemEspañol().addActionListener(e -> principalView.cambiarIdiomas("es", "EC"));
        principalView.getMenuItemFrances().addActionListener(e -> principalView.cambiarIdiomas("fr", "FR"));
        principalView.getMenuItemIngles().addActionListener(e -> principalView.cambiarIdiomas("en", "US"));
    }

    private static void mostrarVentana(PrincipalView principal, JInternalFrame ventana) {

        if (!ventana.isVisible()) {
            principal.getjDesktopPane().add(ventana);
            ventana.setVisible(true);
        }
        ventana.moveToFront();
    }
}