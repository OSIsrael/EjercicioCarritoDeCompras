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
        System.out.println("=== Iniciando aplicación ===");

        // --- Inicializar DAOs ---
        UsuarioDAO usuarioDAO = new UsuarioDAOMemoria();

        // Mostrar estadísticas iniciales
        if (usuarioDAO instanceof UsuarioDAOMemoria) {
            ((UsuarioDAOMemoria) usuarioDAO).mostrarEstadisticas();
        }

        // --- Inicializar Vistas ---
        LoginView loginView = new LoginView();
        RegistrarUsuario registrarUsuarioView = new RegistrarUsuario();
        UsuarioAdminView usuarioAdminView = new UsuarioAdminView();

        // --- Inicializar Controlador de Usuario ---
        UsuarioController usuarioController = new UsuarioController(
                usuarioDAO,
                loginView,
                registrarUsuarioView,
                usuarioAdminView
        );

        // --- Configurar evento de cierre de login ---
        loginView.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                Usuario usuarioAutenticado = usuarioController.getUsuarioAutenticado();

                if (usuarioAutenticado != null) {
                    System.out.println("Login exitoso para: " + usuarioAutenticado.getUsername() +
                            " con rol: " + usuarioAutenticado.getRol());
                    iniciarAplicacionPrincipal(usuarioAutenticado, usuarioController, usuarioAdminView);
                } else {
                    System.out.println("Login cancelado o fallido. Cerrando aplicación.");
                    System.exit(0);
                }
            }
        });

        // Mostrar la ventana de login
        loginView.setVisible(true);
        System.out.println("Ventana de login mostrada");
    }

    private static void iniciarAplicacionPrincipal(Usuario usuarioAutenticado,
                                                   UsuarioController usuarioController,
                                                   UsuarioAdminView usuarioAdminView) {

        System.out.println("=== Iniciando aplicación principal ===");

        // --- Inicializar DAOs para productos y carrito ---
        ProductoDAO productoDAO = new ProductoDAOMemoria();
        CarritoDAO carritoDAO = new CarritoDAOMemoria();

        // --- Inicializar Vistas ---
        PrincipalView principalView = new PrincipalView();
        ProductoAnadirView productoAnadirView = new ProductoAnadirView();
        ProductoListaView productoListaView = new ProductoListaView();
        ProductoEliminar productoEliminar = new ProductoEliminar();
        ProductoEditar productoEditar = new ProductoEditar();
        CarritoAnadirView carritoAnadirView = new CarritoAnadirView();
        CarritoListarView carritoListarView = new CarritoListarView();

        // --- Inicializar Controladores ---
        ProductoController productoController = new ProductoController(
                productoDAO,
                productoAnadirView,
                productoListaView,
                productoEditar,
                productoEliminar,
                carritoAnadirView
        );

        CarritoController carritoController = new CarritoController(
                carritoDAO,
                productoDAO,
                carritoAnadirView,
                usuarioAutenticado,
                carritoListarView
        );

        // --- Configurar ventana principal ---
        principalView.mostrarMensaje("Bienvenido: " + usuarioAutenticado.getUsername() +
                " (" + usuarioAutenticado.getRol() + ")");

        // Configurar opciones de menú según el rol del usuario
        if (usuarioAutenticado.getRol() == Rol.USUARIO) {
            principalView.configurarParaRolUsuario(); // Desactiva opciones de admin
            System.out.println("Configuración de menú para usuario normal");
        } else {
            System.out.println("Configuración de menú para administrador");
        }

        // --- Configurar eventos de los menús ---
        configurarEventosProductos(principalView, productoAnadirView, productoListaView,
                productoEditar, productoEliminar);

        configurarEventosCarrito(principalView, carritoController, carritoAnadirView, carritoListarView);

        configurarEventosUsuarios(principalView, usuarioController, usuarioAdminView);

        configurarEventosCerrarSesion(principalView, usuarioController);
        configurarEventosIdioma(principalView);


        // --- Mostrar ventana principal ---
        principalView.setVisible(true);
        System.out.println("Aplicación principal iniciada exitosamente");
    }

    private static void configurarEventosProductos(PrincipalView principalView,
                                                   ProductoAnadirView productoAnadirView,
                                                   ProductoListaView productoListaView,
                                                   ProductoEditar productoEditar,
                                                   ProductoEliminar productoEliminar) {

        // Eventos para la gestión de Productos
        principalView.getMenuItemCrearProducto().addActionListener(e -> {
            System.out.println("Abriendo ventana crear producto");
            mostrarVentana(principalView, productoAnadirView);
        });

        principalView.getMenuItemBuscarProducto().addActionListener(e -> {
            System.out.println("Abriendo ventana listar productos");
            mostrarVentana(principalView, productoListaView);
        });

        principalView.getMenuItemEditarProducto().addActionListener(e -> {
            System.out.println("Abriendo ventana editar producto");
            mostrarVentana(principalView, productoEditar);
        });

        principalView.getMenuItemEliminarProducto().addActionListener(e -> {
            System.out.println("Abriendo ventana eliminar producto");
            mostrarVentana(principalView, productoEliminar);
        });
    }

    private static void configurarEventosCarrito(PrincipalView principalView,
                                                 CarritoController carritoController,
                                                 CarritoAnadirView carritoAnadirView,
                                                 CarritoListarView carritoListarView) {

        // Eventos para el Carrito
        principalView.getMenuItemCarrito().addActionListener(e -> {
            System.out.println("Abriendo carrito de compras");
            mostrarVentana(principalView, carritoAnadirView);
        });

        principalView.getMenuItemCarritoListar().addActionListener(e -> {
            System.out.println("Listando carritos de compras");
            carritoController.listarTodosLosCarritos(); // Cargar datos antes de mostrar
            mostrarVentana(principalView, carritoListarView);
        });
    }

    private static void configurarEventosUsuarios(PrincipalView principalView,
                                                  UsuarioController usuarioController,
                                                  UsuarioAdminView usuarioAdminView) {

        // Eventos para la Gestión de Usuarios (solo para ADMIN)
        principalView.getMenuItemGestionarUsuarios().addActionListener(e -> {
            System.out.println("Abriendo gestión de usuarios");
            usuarioController.listarUsuarios(); // Cargar usuarios antes de mostrar
            mostrarVentana(principalView, usuarioAdminView);
        });
    }

    private static void configurarEventosCerrarSesion(PrincipalView principalView,
                                                      UsuarioController usuarioController) {

        principalView.getMenuItemCerrarSesion().addActionListener(e -> {
            System.out.println("Cerrando sesión");

            // Confirmar cierre de sesión
            int opcion = JOptionPane.showConfirmDialog(
                    principalView,
                    "¿Está seguro de que desea cerrar sesión?",
                    "Cerrar Sesión",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (opcion == JOptionPane.YES_OPTION) {
                usuarioController.cerrarSesion();
                principalView.dispose();
                System.out.println("Sesión cerrada, volviendo al login");
                mostrarLogin();
            }
        });
    }

    private static void mostrarVentana(PrincipalView principal, JInternalFrame ventana) {
        if (!ventana.isVisible()) {
            principal.getjDesktopPane().add(ventana);
            ventana.setVisible(true);

            // Centrar la ventana interna
            try {
                ventana.setSelected(true);
                ventana.moveToFront();
            } catch (Exception e) {
                System.out.println("Error al centrar ventana: " + e.getMessage());
            }
        } else {
            // Si ya está visible, traerla al frente
            try {
                ventana.setSelected(true);
                ventana.moveToFront();
            } catch (Exception e) {
                System.out.println("Error al traer ventana al frente: " + e.getMessage());
            }
        }
    }
    private static void configurarEventosIdioma(PrincipalView principalView){
        principalView.getMenuItemEspañol().addActionListener(e -> {
            principalView.cambiarIdiomas("es","EC");

        });
        principalView.getMenuItemFrances().addActionListener(e -> {
            principalView.cambiarIdiomas("fr","FR");

        });
        principalView.getMenuItemIngles().addActionListener(e -> {
            principalView.cambiarIdiomas("en","US");
        });
    }
}

