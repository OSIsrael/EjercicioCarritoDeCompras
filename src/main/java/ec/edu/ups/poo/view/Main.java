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
import ec.edu.ups.poo.modelo.Pregunta;
import ec.edu.ups.poo.modelo.Producto;
import ec.edu.ups.poo.modelo.Rol;
import ec.edu.ups.poo.modelo.Usuario;
import ec.edu.ups.poo.dao.PreguntaDAO;
import ec.edu.ups.poo.dao.impl.PreguntaDAOMemoria;
import ec.edu.ups.poo.util.Idioma;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main {

    private static UsuarioDAO usuarioDAO;
    private static ProductoDAO productoDAO;
    private static CarritoDAO carritoDAO;
    private static PreguntaDAO preguntaDAO;

    public static void main(String[] args) {

        // Inicializar DAOs una sola vez
        usuarioDAO = new UsuarioDAOMemoria();
        productoDAO = new ProductoDAOMemoria();
        carritoDAO = new CarritoDAOMemoria();
        preguntaDAO = new PreguntaDAOMemoria();
        preguntaDAO.crear(new Pregunta(1, "¿Nombre de tu primera mascota?"));
        preguntaDAO.crear(new Pregunta(2, "¿Ciudad de nacimiento?"));
        preguntaDAO.crear(new Pregunta(3, "¿Comida favorita?"));
        preguntaDAO.crear(new Pregunta(4, "¿Animal favorito?"));
        preguntaDAO.crear(new Pregunta(5, "¿Nombre de tu mejor amigo de la infancia?"));
        preguntaDAO.crear(new Pregunta(6, "¿Comó se llama tu escuela primaria?"));
        preguntaDAO.crear(new Pregunta(7,"¿Cuál es el segundo nombre de tu padre?"));
        preguntaDAO.crear(new Pregunta(8,"¿Cuál fue tu primer empleo?"));
        preguntaDAO.crear(new Pregunta(9,"¿Cómo se llama tu película favorita?"));
        preguntaDAO.crear(new Pregunta(10,"¿Cuál es el nombre de tu primer profeso(a)? "));






        // Cargar datos iniciales si no existen
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

        SwingUtilities.invokeLater(Main::mostrarLogin);
    }

    private static void mostrarLogin() {
        LoginView loginView = new LoginView();
        RegistrarUsuario registrarUsuarioView = new RegistrarUsuario();
        UsuarioAdminView usuarioAdminView = new UsuarioAdminView();
        UsuarioBuscarView usuarioBuscarView = new UsuarioBuscarView();
        UsuarioCrearView usuarioCrearView = new UsuarioCrearView();
        UsuarioModificarDatosView usuarioModificarDatosView = new UsuarioModificarDatosView();
        for (Pregunta pregunta: preguntaDAO.listarTodas()) {
            registrarUsuarioView.getCbxPregunta1().addItem(pregunta);
            registrarUsuarioView.getCbxPregunta2().addItem(pregunta);
        }

        UsuarioController usuarioController = new UsuarioController(
                usuarioDAO,preguntaDAO, loginView, registrarUsuarioView, usuarioAdminView,
                usuarioBuscarView, usuarioCrearView, usuarioModificarDatosView);

        loginView.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                Usuario usuarioAutenticado = usuarioController.getUsuarioAutenticado();
                if (usuarioAutenticado != null) {
                    iniciarAplicacionPrincipal(usuarioAutenticado, usuarioController, usuarioAdminView, usuarioBuscarView, usuarioCrearView, usuarioModificarDatosView);
                } else {
                    System.exit(0);
                }
            }
        });
        loginView.setVisible(true);
    }

    private static void iniciarAplicacionPrincipal(Usuario usuarioAutenticado,
                                                   UsuarioController usuarioController,
                                                   UsuarioAdminView usuarioAdminView,
                                                   UsuarioBuscarView usuarioBuscarView,
                                                   UsuarioCrearView usuarioCrearView,
                                                   UsuarioModificarDatosView usuarioModificarDatosView) {

        // Instanciar vistas
        PrincipalView principalView = new PrincipalView();
        ProductoAnadirView productoAnadirView = new ProductoAnadirView();
        ProductoListaView productoListaView = new ProductoListaView();
        ProductoEliminar productoEliminar = new ProductoEliminar();
        ProductoEditar productoEditar = new ProductoEditar();

        CarritoAnadirView carritoAnadirView = new CarritoAnadirView();
        CarritoListarView carritoListarView = new CarritoListarView();
        CarritoBuscarView carritoBuscarView = new CarritoBuscarView();
        CarritoModificarView carritoModificarView = new CarritoModificarView();
        CarritoEliminarView carritoEliminarView = new CarritoEliminarView();

        // Instanciar controladores
        ProductoController productoController = new ProductoController(
                productoDAO, productoAnadirView, productoListaView,
                productoEditar, productoEliminar, carritoAnadirView);

        // CarritoController debe filtrar los carritos según el usuario autenticado, ya sea admin o usuario normal
        CarritoController carritoController = new CarritoController(
                carritoDAO, productoDAO, carritoAnadirView, usuarioAutenticado,
                carritoListarView, carritoBuscarView, carritoModificarView, carritoEliminarView);

        // Configura los menús según el rol
        if (usuarioAutenticado.getRol() == Rol.USUARIO) {
            principalView.configurarParaRolUsuario();
        } else if (usuarioAutenticado.getRol() == Rol.ADMINISTRADOR) {
            principalView.configurarParaRolAdmin();
        }

        principalView.mostrarMensaje("Bienvenido: " + usuarioAutenticado.getUsername() + " (" + usuarioAutenticado.getRol() + ")");

        // Asignar eventos
        configurarEventosProductos(principalView, productoAnadirView, productoListaView, productoEditar, productoEliminar);
        configurarEventosCarrito(principalView, carritoAnadirView, carritoListarView, carritoBuscarView, carritoModificarView, carritoEliminarView, carritoController, usuarioAutenticado);
        configurarEventosUsuarios(principalView, usuarioController, usuarioAdminView, usuarioBuscarView, usuarioCrearView, usuarioModificarDatosView, usuarioAutenticado);
        configurarEventosIdioma(principalView, carritoAnadirView,carritoBuscarView,carritoEliminarView,carritoListarView,carritoModificarView,productoAnadirView,productoEditar,productoEliminar,productoListaView,usuarioAdminView,usuarioBuscarView,usuarioCrearView,usuarioModificarDatosView);
        configurarEventosCerrarSesion(principalView);


        principalView.setVisible(true);
    }

    private static void configurarEventosProductos(PrincipalView principalView, ProductoAnadirView productoAnadirView, ProductoListaView productoListaView, ProductoEditar productoEditar, ProductoEliminar productoEliminar) {
        principalView.getMenuItemCrearProducto().addActionListener(e -> mostrarVentana(principalView, productoAnadirView));
        principalView.getMenuItemBuscarProducto().addActionListener(e -> mostrarVentana(principalView, productoListaView));
        principalView.getMenuItemEditarProducto().addActionListener(e -> mostrarVentana(principalView, productoEditar));
        principalView.getMenuItemEliminarProducto().addActionListener(e -> mostrarVentana(principalView, productoEliminar));
    }

    // Carrito: solo admins pueden listar todos los carritos, usuarios ven solo los suyos
    private static void configurarEventosCarrito(
            PrincipalView principalView,
            CarritoAnadirView carritoAnadirView,
            CarritoListarView carritoListarView,
            CarritoBuscarView carritoBuscarView,
            CarritoModificarView carritoModificarView,
            CarritoEliminarView carritoEliminarView,
            CarritoController carritoController,
            Usuario usuarioAutenticado
    ) {
        principalView.getMenuItemCarrito().addActionListener(e -> mostrarVentana(principalView, carritoAnadirView));

        // Listar: Admins ven todos, usuarios solo los suyos
        principalView.getMenuItemCarritoListar().addActionListener(e -> {
            if (usuarioAutenticado.getRol() == Rol.ADMINISTRADOR) {
                carritoController.listarCarritosDelUsuario();
            } else {
                carritoController.listarCarritosDelUsuario();
            }
            mostrarVentana(principalView, carritoListarView);
        });

        principalView.getMenuItemCarritoBuscar().addActionListener(e -> {
            carritoBuscarView.limpiarVista();
            mostrarVentana(principalView, carritoBuscarView);
        });
        principalView.getMenuItemCarritoModificar().addActionListener(e -> {
            carritoModificarView.limpiarVista();
            mostrarVentana(principalView, carritoModificarView);
        });
        principalView.getMenuItemCarritoEliminar().addActionListener(e -> {
            carritoEliminarView.limpiarVista();
            mostrarVentana(principalView, carritoEliminarView);
        });
    }

    private static void configurarEventosUsuarios(
            PrincipalView principalView,
            UsuarioController usuarioController,
            UsuarioAdminView usuarioAdminView,
            UsuarioBuscarView usuarioBuscarView,
            UsuarioCrearView usuarioCrearView,
            UsuarioModificarDatosView usuarioModificarDatosView,
            Usuario usuarioAutenticado
    ) {
        // Solo admins pueden gestionar/crear/buscar usuarios
        if (usuarioAutenticado.getRol() == Rol.ADMINISTRADOR) {
            principalView.getMenuItemGestionarUsuarios().addActionListener(e -> {
                usuarioController.listarUsuarios();
                mostrarVentana(principalView, usuarioAdminView);
            });
            principalView.getMenuItemBuscarUsuario().addActionListener(e -> {
                usuarioBuscarView.limpiarVista();
                usuarioController.listarTodosAction();
                mostrarVentana(principalView, usuarioBuscarView);
            });
            principalView.getMenuItemCrearUsuario().addActionListener(e -> {
                usuarioCrearView.getTxtUsuario().setText("");
                usuarioCrearView.getTxtContrasena().setText("");
                mostrarVentana(principalView, usuarioCrearView);
            });
        }

        // Solo usuarios normales pueden modificar sus datos
        if (usuarioAutenticado.getRol() == Rol.USUARIO) {
            principalView.getMenuItemUser().addActionListener(e -> {
                usuarioModificarDatosView.mostrarDatosUsuario(usuarioAutenticado);
                mostrarVentana(principalView, usuarioModificarDatosView);
            });
        }
    }

    private static void configurarEventosCerrarSesion(PrincipalView principalView) {
        principalView.getMenuItemCerrarSesion().addActionListener(e -> {
            int opcion = JOptionPane.showConfirmDialog(principalView, Idioma.get("menu.cerrar"), Idioma.get("menu.cerrarse"), JOptionPane.YES_NO_OPTION);
            if (opcion == JOptionPane.YES_OPTION) {
                principalView.dispose();
                mostrarLogin();
            }
        });
    }

    private static void configurarEventosIdioma(
            PrincipalView principalView,
            CarritoAnadirView carritoAnadirView,
            CarritoBuscarView carritoBuscarView,
            CarritoEliminarView carritoEliminarView,
            CarritoListarView carritoListarView,
            CarritoModificarView carritoModificarView,
            ProductoAnadirView productoAnadirView,
            ProductoEditar productoEditar,
            ProductoEliminar productoEliminar,
            ProductoListaView productoListaView,
            UsuarioAdminView usuarioAdminView,
            UsuarioBuscarView usuarioBuscarView,
            UsuarioCrearView usuarioCrearView,
            UsuarioModificarDatosView usuarioModificarDatosView
    ) {
        ActionListener cambiarIdiomaListener = e -> {
            String lang = "es", country = "EC";
            if (e.getSource() == principalView.getMenuItemIngles()) {
                lang = "en"; country = "US";
            } else if (e.getSource() == principalView.getMenuItemFrances()) {
                lang = "fr"; country = "FR";
            }
            Idioma.setIdioma(lang, country);
            principalView.actualizarTextos();
            carritoAnadirView.actualizarTextos();
            carritoBuscarView.actualizarTextos();
            carritoEliminarView.actualizarTextos();
            carritoListarView.actualizarTextos();
            carritoModificarView.actualizarTextos();
            productoAnadirView.actualizarTextos();
            productoEditar.actualizarTextos();
            productoEliminar.actualizarTextos();
            productoListaView.actualizarTextos();
            usuarioAdminView.actualizarTextos();
            usuarioBuscarView.actualizarTextos();
            usuarioCrearView.actualizarTextos();
            usuarioModificarDatosView.actualizarTextos();
        };
        principalView.getMenuItemEspañol().addActionListener(cambiarIdiomaListener);
        principalView.getMenuItemFrances().addActionListener(cambiarIdiomaListener);
        principalView.getMenuItemIngles().addActionListener(cambiarIdiomaListener);
    }

    private static void mostrarVentana(PrincipalView principal, JInternalFrame ventana) {
        if (!ventana.isVisible()) {
            principal.getjDesktopPane().add(ventana);
            ventana.setVisible(true);
        }
        ventana.moveToFront();
    }
}