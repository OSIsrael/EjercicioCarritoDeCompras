package ec.edu.ups.poo.view;

import ec.edu.ups.poo.controlador.CarritoController;
import ec.edu.ups.poo.controlador.ProductoController;
import ec.edu.ups.poo.controlador.UsuarioController;
import ec.edu.ups.poo.dao.DAOFactory;
import ec.edu.ups.poo.dao.CarritoDAO;
import ec.edu.ups.poo.dao.ProductoDAO;
import ec.edu.ups.poo.dao.UsuarioDAO;
import ec.edu.ups.poo.modelo.Pregunta;
import ec.edu.ups.poo.modelo.Producto;
import ec.edu.ups.poo.modelo.Rol;
import ec.edu.ups.poo.modelo.Usuario;
import ec.edu.ups.poo.dao.PreguntaDAO;
import ec.edu.ups.poo.util.CedulaInvalidaException;
import ec.edu.ups.poo.util.Idioma;
import ec.edu.ups.poo.modelo.Genero;
import ec.edu.ups.poo.util.PasswordInvalidaException;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        crearArchivosPorDefectoSiNoExisten();
        SwingUtilities.invokeLater(Main::mostrarLogin);
    }

    private static void mostrarVentana(PrincipalView principal, JInternalFrame ventana) {
        if (!ventana.isVisible()) {
            principal.getjDesktopPane().add(ventana);
            ventana.setVisible(true);
        }
        ventana.moveToFront();
    }

    private static void mostrarLogin() {
        LoginView loginView = new LoginView();

        RegistrarUsuario registrarUsuarioView = new RegistrarUsuario();

        loginView.getBtnIniciarSesion().addActionListener(e -> {
            try {
                String tipoStorage = (String) loginView.getCbxTipoStorage().getSelectedItem();
                String ruta = loginView.getTxtRuta().getText().trim();

                if (("TEXTO".equals(tipoStorage) || "BINARIO".equals(tipoStorage)) && ruta.isEmpty()) {
                    loginView.mostrar("login.msj.rutavacia");
                    return;
                }


                DAOFactory factory = new DAOFactory(tipoStorage, ruta);
                UsuarioDAO usuarioDAO = factory.getUsuarioDAO();
                ProductoDAO productoDAO = factory.getProductoDAO();
                PreguntaDAO preguntaDAO = factory.getPreguntaDAO();

                cargarDatosPorDefecto(usuarioDAO, productoDAO, preguntaDAO);


                String username = loginView.getTxtUsername().getText().trim();
                String password = new String(loginView.getTxtPassword().getPassword());
                Usuario usuarioAutenticado = usuarioDAO.autenticar(username, password);

                if (usuarioAutenticado != null) {
                    loginView.dispose();
                    iniciarAplicacionPrincipal(usuarioAutenticado, factory);
                } else {
                    loginView.mostrar("usuario.controller.msj.credenciales.invalidas");
                }
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(loginView, "Error de archivo al iniciar sesión: " + ex.getMessage(), "Error de E/S", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(loginView, "Error al iniciar sesión: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        ActionListener preLoginActionListener = e -> {
            String tipoStorage = (String) loginView.getCbxTipoStorage().getSelectedItem();
            String ruta = loginView.getTxtRuta().getText().trim();

            if (("TEXTO".equals(tipoStorage) || "BINARIO".equals(tipoStorage)) && ruta.isEmpty()) {
                loginView.mostrar("login.msj.rutavacia");
                return;
            }

            DAOFactory tempFactory = new DAOFactory(tipoStorage, ruta);
            UsuarioDAO tempUsuarioDAO = tempFactory.getUsuarioDAO();
            ProductoDAO tempProductoDAO = tempFactory.getProductoDAO();
            PreguntaDAO tempPreguntaDAO = tempFactory.getPreguntaDAO();

            try {
                cargarDatosPorDefecto(tempUsuarioDAO, tempProductoDAO, tempPreguntaDAO);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(loginView, "Error de archivo al preparar el registro: " + ex.getMessage(), "Error de E/S", JOptionPane.ERROR_MESSAGE);
                return;
            }

            UsuarioController tempController = new UsuarioController(
                    tempUsuarioDAO, tempPreguntaDAO, null,
                    registrarUsuarioView, null, null, null, null
            );

            if (e.getSource() == loginView.getBtnRegistrarse()) {
                try {
                    tempController.abrirVentanaRegistro();
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(loginView, "Error de archivo al abrir el registro: " + ex.getMessage(), "Error de E/S", JOptionPane.ERROR_MESSAGE);
                }
            } else if (e.getSource() == loginView.getBtnOlvide()) {
                tempController.abrirOlvideContrasena();
            }

        };

        loginView.getBtnRegistrarse().addActionListener(preLoginActionListener);
        loginView.getBtnOlvide().addActionListener(preLoginActionListener);

        loginView.setVisible(true);
    }

    private static void iniciarAplicacionPrincipal(Usuario usuarioAutenticado, DAOFactory factory) {

        // Obtenemos los DAOs de la factory
        UsuarioDAO usuarioDAO = factory.getUsuarioDAO();
        ProductoDAO productoDAO = factory.getProductoDAO();
        CarritoDAO carritoDAO = factory.getCarritoDAO();
        PreguntaDAO preguntaDAO = factory.getPreguntaDAO();

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

        // Vistas de Usuario
        UsuarioAdminView usuarioAdminView = new UsuarioAdminView();
        UsuarioBuscarView usuarioBuscarView = new UsuarioBuscarView();
        UsuarioCrearView usuarioCrearView = new UsuarioCrearView();
        UsuarioModificarDatosView usuarioModificarDatosView = new UsuarioModificarDatosView();
        RegistrarUsuario registrarUsuarioView = new RegistrarUsuario();

        // Instanciar controladores, inyectando los DAOs
        UsuarioController usuarioController = new UsuarioController(
                usuarioDAO, preguntaDAO, usuarioAutenticado, registrarUsuarioView, usuarioAdminView,
                usuarioBuscarView, usuarioCrearView, usuarioModificarDatosView
        );
        
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

    private static void crearArchivosPorDefectoSiNoExisten() {
        String basePath = "data";
        String[] subDirs = {"text", "binary"};
        String[] fileNames = {"usuarios", "productos", "carritos", "preguntas"};

        for (int i = 0; i < subDirs.length; i++) {
            String dirPath = basePath + File.separator + subDirs[i];
            File directory = new File(dirPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            boolean isBinaryMode = "binary".equals(subDirs[i]);
            String extension = isBinaryMode ? ".dat" : ".txt";

            for (String fileName : fileNames) {
                File file = new File(dirPath + File.separator + fileName + extension);
                if (!file.exists()) {
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        System.err.println("No se pudo crear el archivo por defecto: " + file.getAbsolutePath());
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private static void cargarDatosPorDefecto(UsuarioDAO usuarioDAO, ProductoDAO productoDAO, PreguntaDAO preguntaDAO) throws IOException {
        // Cargar Preguntas
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

        // Cargar Usuarios
        try {
            if (usuarioDAO.buscarPorUsername("1712345675") == null) {
                usuarioDAO.crear(new Usuario("1712345675", "Admin_123", Rol.ADMINISTRADOR, Genero.MASCULINO, "Admin", "Principal", "000000000", 30, "MEMORIA"));
            }
            if (usuarioDAO.buscarPorUsername("0987654324") == null) {
                usuarioDAO.crear(new Usuario("0987654324", "User-456", Rol.USUARIO, Genero.OTROS, "User", "Demo", "111111111", 18, "MEMORIA"));
            }
            // Cargar Productos (se comprueba si ya existen para no duplicar)
            if(productoDAO.buscarPorCodigo(1) == null) productoDAO.crear(new Producto(1, "Laptop Gamer", 1200.00));
            if(productoDAO.buscarPorCodigo(2) == null) productoDAO.crear(new Producto(2, "Teclado Mecánico", 85.50));
            if(productoDAO.buscarPorCodigo(3) == null) productoDAO.crear(new Producto(3, "Mouse Inalámbrico", 30.00));
            if(productoDAO.buscarPorCodigo(4) == null) productoDAO.crear(new Producto(4, "Monitor 27 pulgadas", 350.00));
        } catch (CedulaInvalidaException | PasswordInvalidaException e) {
            System.err.println("Error al inicializar datos por defecto (datos inválidos): " + e.getMessage());
        } catch (IOException e) { // MEJORA: Manejar el error de E/S en lugar de lanzar una excepción que detenga la app.
            JOptionPane.showMessageDialog(null, "No se pudieron cargar los datos por defecto. Verifique los permisos de la carpeta.\nError: " + e.getMessage(), "Error de Archivo", JOptionPane.ERROR_MESSAGE);
        } // El try-catch ahora envuelve también a los productos
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
            // La lógica de quién ve qué ya está en el controller, aquí solo se invoca.
            try {
                carritoController.listarCarritosDelUsuario();
            } catch (IOException ex) {
                // MEJORA DE ROBUSTEZ: Mostrar error sin crashear.
                JOptionPane.showMessageDialog(principalView, Idioma.get("carrito.controller.msj.error.archivo") + ": " + ex.getMessage(), "Error de Archivo", JOptionPane.ERROR_MESSAGE);
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
}