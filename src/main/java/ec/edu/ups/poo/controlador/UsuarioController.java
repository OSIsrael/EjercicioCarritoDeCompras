package ec.edu.ups.poo.controlador;

import ec.edu.ups.poo.dao.DAOFactory;
import ec.edu.ups.poo.dao.PreguntaDAO;
import ec.edu.ups.poo.dao.UsuarioDAO;
import ec.edu.ups.poo.modelo.Pregunta;
import ec.edu.ups.poo.modelo.Rol;
import ec.edu.ups.poo.modelo.Usuario;
import ec.edu.ups.poo.util.Idioma;
import ec.edu.ups.poo.util.ValidacionException;
import ec.edu.ups.poo.view.*;
import ec.edu.ups.poo.modelo.Genero;
import ec.edu.ups.poo.util.*;

import javax.swing.*;
import java.io.IOException;
import java.awt.event.ActionListener;
import java.util.*;


public class UsuarioController {
    private UsuarioDAO usuarioDAO;
    private PreguntaDAO preguntaDAO;
    private RegistrarUsuario registrarUsuarioView;
    private UsuarioAdminView usuarioAdminView;
    private Usuario usuarioAutenticado;
    private UsuarioBuscarView usuarioBuscarView;
    private UsuarioCrearView usuarioCrearView;
    private UsuarioModificarDatosView usuarioModificarDatosView;
    private OlvideContrasenaView olvideContrasenaView;


    private Usuario usuarioRecuperacion;
    private Integer idPreguntaRecuperacion;

    public UsuarioController(UsuarioDAO usuarioDAO,PreguntaDAO preguntaDAO, Usuario usuarioAutenticado,
                             RegistrarUsuario registrarUsuarioView, UsuarioAdminView usuarioAdminView,
                             UsuarioBuscarView usuarioBuscarView, UsuarioCrearView usuarioCrearView,
                             UsuarioModificarDatosView usuarioModificarDatosView) {
        this.usuarioDAO = usuarioDAO;
        this.preguntaDAO = preguntaDAO;
        this.usuarioAutenticado = usuarioAutenticado;
        this.registrarUsuarioView = registrarUsuarioView;
        this.usuarioAdminView = usuarioAdminView;
        this.usuarioBuscarView = usuarioBuscarView;
        this.usuarioCrearView = usuarioCrearView;
        this.usuarioModificarDatosView = usuarioModificarDatosView;
        this.olvideContrasenaView = new OlvideContrasenaView();
        configurarEventosIdioma();
        configurarEventos();
    }

    private void configurarEventosIdioma() {

        ActionListener cambiarIdiomaListener = e -> {
            String lang = "es", country = "EC";


            if (e.getSource() instanceof JMenuItem) {
                JMenuItem sourceItem = (JMenuItem) e.getSource();
                if (sourceItem.getText().equals(Idioma.get("menu.idiomas.ingles"))) {
                    lang = "en"; country = "US";
                } else if (sourceItem.getText().equals(Idioma.get("menu.idiomas.frances"))) {
                    lang = "fr"; country = "FR";
                }
            }
            Idioma.setIdioma(lang, country);


            if (registrarUsuarioView != null) registrarUsuarioView.actualizarTextos();
            if (olvideContrasenaView != null) olvideContrasenaView.actualizarTextos();
        };


        if (registrarUsuarioView != null) {
            registrarUsuarioView.getMenuItemEspañol().addActionListener(cambiarIdiomaListener);
            registrarUsuarioView.getMenuItemIngles().addActionListener(cambiarIdiomaListener);
            registrarUsuarioView.getMenuItemFrances().addActionListener(cambiarIdiomaListener);
        }
        if (olvideContrasenaView != null) {
            olvideContrasenaView.getMenuItemEspañol().addActionListener(cambiarIdiomaListener);
            olvideContrasenaView.getMenuItemIngles().addActionListener(cambiarIdiomaListener);
            olvideContrasenaView.getMenuItemFrances().addActionListener(cambiarIdiomaListener);
        }
    }


    private void configurarEventos() {
        if (registrarUsuarioView != null) {
            this.registrarUsuarioView.getBtnRegistrarse().addActionListener(e -> registrarUsuario());
        }

        olvideContrasenaView.getBtnValidar().addActionListener(e -> {
            try {
                validarUsuarioYMostrarPregunta();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        olvideContrasenaView.getBtnCambiar().addActionListener(e -> cambiarContrasena());

        if (usuarioAdminView != null) {
            this.usuarioAdminView.getBtnActualizar().addActionListener(e -> {
                try {
                    actualizarUsuario();
                } catch (IOException ex) {

                    JOptionPane.showMessageDialog(usuarioAdminView, Idioma.get("usuario.controller.msj.error.archivo") + ": " + ex.getMessage(), "Error de Archivo", JOptionPane.ERROR_MESSAGE);
                }
            });
            this.usuarioAdminView.getBtnEliminar().addActionListener(e -> {
                try {
                    eliminarUsuario();
                } catch (IOException ex) {

                    JOptionPane.showMessageDialog(usuarioAdminView, Idioma.get("usuario.controller.msj.error.archivo") + ": " + ex.getMessage(), "Error de Archivo", JOptionPane.ERROR_MESSAGE);
                }
            });
            this.usuarioAdminView.getBtnRefrescar().addActionListener(e -> listarUsuarios());
        }

        if (usuarioBuscarView != null) {
            usuarioBuscarView.getBtnBuscar().addActionListener(e -> buscarUsuarioAction());
            usuarioBuscarView.getBtnListar().addActionListener(e -> listarTodosAction());
        }
        if (usuarioCrearView != null) {
            usuarioCrearView.getBtnCrear().addActionListener(e -> crearUsuarioDesdeCrearView());
            usuarioCrearView.getBtnBorrar().addActionListener(e -> {
                usuarioCrearView.getTxtUsuario().setText("");
                usuarioCrearView.getTxtContrasena().setText("");
            });
        }
        if (usuarioModificarDatosView != null) {
            usuarioModificarDatosView.getBtnModificar().addActionListener(e -> modificarDatosUsuario());
            usuarioModificarDatosView.getBtnBorrar().addActionListener(e -> usuarioModificarDatosView.limpiarCampos());
        }
    }
    public void abrirOlvideContrasena() {
        olvideContrasenaView.getTxtUsername().setText("");
        olvideContrasenaView.getLblPregunta().setText("");
        olvideContrasenaView.getTxtRespuesta().setText("");
        olvideContrasenaView.getLblNuevaContrasena().setVisible(false);
        olvideContrasenaView.getTxtNuevaContrasena().setVisible(false);
        olvideContrasenaView.getBtnCambiar().setVisible(false);
        olvideContrasenaView.setVisible(true);
    }

    private void validarUsuarioYMostrarPregunta() throws IOException {
        try {
            String username = olvideContrasenaView.getTxtUsername().getText().trim();
            Usuario usuario = usuarioDAO.buscarPorUsername(username);
            if (usuario == null) {
                JOptionPane.showMessageDialog(olvideContrasenaView, Idioma.get("usuario.controller.msj.noecontrado"));
                return;
            }
            usuarioRecuperacion = usuario;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(olvideContrasenaView, Idioma.get("usuario.controller.msj.error.archivo") + ": " + e.getMessage(), "Error de Archivo", JOptionPane.ERROR_MESSAGE);
            return;
        }
        List<Integer> idsPreguntas = new ArrayList<>(usuarioRecuperacion.getPreguntasSeguridad().keySet());
        if (idsPreguntas.isEmpty()) {
            JOptionPane.showMessageDialog(olvideContrasenaView, Idioma.get("usuario.controller.nopreguntas"));
            return;
        }

        idPreguntaRecuperacion = idsPreguntas.get(new Random().nextInt(idsPreguntas.size()));
        Pregunta pregunta = preguntaDAO.buscarPorId(idPreguntaRecuperacion);
        if (pregunta != null) {
            olvideContrasenaView.getLblPregunta().setText(pregunta.getPregunta());
        } else {
            olvideContrasenaView.getLblPregunta().setText("Pregunta no encontrada.");
        }

        olvideContrasenaView.getLblNuevaContrasena().setVisible(true);
        olvideContrasenaView.getTxtNuevaContrasena().setVisible(true);
        olvideContrasenaView.getBtnCambiar().setVisible(true);
    }
    private void cambiarContrasena() {
        try {
            String respuesta = olvideContrasenaView.getTxtRespuesta().getText().trim();
            String respuestaCorrecta = usuarioRecuperacion.getPreguntasSeguridad().get(idPreguntaRecuperacion);
            if (!respuesta.equals(respuestaCorrecta)) {
                JOptionPane.showMessageDialog(olvideContrasenaView, Idioma.get("usuario.controller.incorrecta"));
                return;
            }
            String nuevaContra = new String(olvideContrasenaView.getTxtNuevaContrasena().getPassword());
            if (nuevaContra.isEmpty()) {
                JOptionPane.showMessageDialog(olvideContrasenaView, Idioma.get("usuario.controller.nuevacontra"));
                return;
            }
            usuarioRecuperacion.setPassword(nuevaContra);
            usuarioDAO.actualizar(usuarioRecuperacion.getUsername(), usuarioRecuperacion);
            JOptionPane.showMessageDialog(olvideContrasenaView, Idioma.get("usuario.controller.msj.cambiado"));
            olvideContrasenaView.dispose();
        }catch (PasswordInvalidaException e){
            JOptionPane.showMessageDialog(olvideContrasenaView, e.getMessage(),"Error", JOptionPane.WARNING_MESSAGE);
        } catch (IOException e) { 

            JOptionPane.showMessageDialog(olvideContrasenaView, Idioma.get("usuario.controller.msj.error.archivo") + ": " + e.getMessage(), "Error de Archivo", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void abrirVentanaRegistro() throws IOException {

        if (registrarUsuarioView == null) {
            System.err.println("Error: Se intentó abrir la ventana de registro, pero no se proporcionó una al controlador.");
            return;
        }
        List<Pregunta> preguntas = preguntaDAO.listarTodas();
        registrarUsuarioView.setPreguntasDisponibles(preguntas);
        registrarUsuarioView.limpiarCampos();
        registrarUsuarioView.setVisible(true);
    }



    private void registrarUsuario() {
        try {

            String username = registrarUsuarioView.getTxtUsuarioRe().getText().trim();
            String password = new String(registrarUsuarioView.getTxtContraRe().getPassword());
            String nombre = registrarUsuarioView.getTxtNombre().getText().trim();
            String apellido = registrarUsuarioView.getTxtApellido().getText().trim();
            String telefono = registrarUsuarioView.getTxtTelefono().getText().trim();
            String edadStr = registrarUsuarioView.getTxtEdad().getText().trim();
            Genero genero = (Genero) registrarUsuarioView.getCbxGenero().getSelectedItem();
            Map<Pregunta, String> preguntasRespondidas = registrarUsuarioView.getPreguntasYRespuestas();

            String tipoStorage = (String) registrarUsuarioView.getCbxTipoGuardar().getSelectedItem();
            String ruta = registrarUsuarioView.getTxtRuta().getText().trim();


            if (username.isEmpty() || password.isEmpty() || nombre.isEmpty() || apellido.isEmpty() ||
                    telefono.isEmpty() || edadStr.isEmpty()) {
                throw new ValidacionException(Idioma.get("registro.msj.camposprincipales.vacios"));
            }
            
            if (("TEXTO".equals(tipoStorage) || "BINARIO".equals(tipoStorage)) && ruta.isEmpty()) {
                throw new ValidacionException(Idioma.get("registro.msj.rutavacia"));
            }


            DAOFactory factoryParaRegistrar = new DAOFactory(tipoStorage, ruta);
            UsuarioDAO daoParaRegistrar = factoryParaRegistrar.getUsuarioDAO();
            

            if (daoParaRegistrar.buscarPorUsername(username) != null) {
                throw new ValidacionException(Idioma.get("registro.msj.yaextite"));
            }

            int edad;
            try {
                edad = Integer.parseInt(edadStr);
                if (edad <= 0 || edad > 120) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException ex) {
                throw new ValidacionException(Idioma.get("registro.msj.edadinvalida"));
            }

            if (preguntasRespondidas.size() < 3) {
                throw new ValidacionException(Idioma.get("registro.msj.minpreguntas"));
            }


            Usuario nuevo = new Usuario(username, password, Rol.USUARIO, genero, nombre, apellido, telefono, edad, tipoStorage);

            Map<Integer, String> preguntasParaGuardar = new HashMap<>();
            for (Map.Entry<Pregunta, String> entry : preguntasRespondidas.entrySet()) {
                preguntasParaGuardar.put(entry.getKey().getId(), entry.getValue());
            }
            nuevo.setPreguntasSeguridad(preguntasParaGuardar);
            

            daoParaRegistrar.crear(nuevo);


            registrarUsuarioView.mostrarMensaje("registro.msj.registrado");
            registrarUsuarioView.limpiarCampos();
            registrarUsuarioView.dispose();

        } catch (CedulaInvalidaException | PasswordInvalidaException e) {

            JOptionPane.showMessageDialog(registrarUsuarioView, e.getMessage(), "Error de Datos", JOptionPane.WARNING_MESSAGE);
        } catch (ValidacionException e) {

            JOptionPane.showMessageDialog(registrarUsuarioView, e.getMessage(), "Error de Validación", JOptionPane.WARNING_MESSAGE);
        } catch (IOException e) {

            JOptionPane.showMessageDialog(registrarUsuarioView, Idioma.get("registro.msj.error.archivo") + ": " + e.getMessage(), "Error de Archivo", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
                    JOptionPane.showMessageDialog(registrarUsuarioView, "Ocurrió un error inesperado: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }


    public void listarUsuarios() {
        try {
            List<Usuario> todosLosUsuarios = usuarioDAO.listarTodos();
            usuarioAdminView.cargarUsuarios(todosLosUsuarios);
        } catch (IOException e) {
            usuarioAdminView.mostrarError(Idioma.get("usuario.controller.msj.error.archivo") + ": " + e.getMessage());
        }
    }

    public void listarUsuariosPorRol(Rol rol) {
        try {
            List<Usuario> usuariosFiltrados = usuarioDAO.listarPorRol(rol);
            usuarioAdminView.cargarUsuarios(usuariosFiltrados);
        } catch (IOException e) {
            usuarioAdminView.mostrarError(Idioma.get("usuario.controller.msj.error.archivo") + ": " + e.getMessage());
        }
    }

    private void actualizarUsuario() throws IOException {
        int fila = usuarioAdminView.getTblUsuarios().getSelectedRow();
        if (fila < 0) {
            usuarioAdminView.mostrarError(Idioma.get("usuario.controller.msj.seletable"));
            return;
        }


        String username = (String) usuarioAdminView.getTblUsuarios().getValueAt(fila, 0);
        Rol nuevoRol = (Rol) usuarioAdminView.getCbxRol().getSelectedItem();
        Usuario usuario;
        try {
            usuario = usuarioDAO.buscarPorUsername(username);
        } catch (IOException e) {
            usuarioAdminView.mostrarError(Idioma.get("usuario.controller.msj.error.archivo") + ": " + e.getMessage());
            return;
        }

        if (usuario == null) {
            usuarioAdminView.mostrarError("usuario.controller.msj.noecontrado");
            return;
        }

        if (usuario.getRol() == nuevoRol) {
            return;
        }

        if (!usuarioAdminView.confirmarAccion("usuario.controller.msj.seguro", username, nuevoRol)) {
            return;
        }
        usuario.setRol(nuevoRol);
        usuarioDAO.actualizar(username, usuario);

        listarUsuarios();
        usuarioAdminView.mostrarMensaje("usuario.controller.msj.actualizado");
    }



    private void eliminarUsuario() throws IOException {
        int fila = usuarioAdminView.getTblUsuarios().getSelectedRow();

        if (fila < 0) {
            usuarioAdminView.mostrarError("usuario.controller.msj.seleliminado");
            return;
        }

        String username = (String) usuarioAdminView.getTblUsuarios().getValueAt(fila, 0);


        if (username.equals(usuarioAutenticado.getUsername())) {
            usuarioAdminView.mostrarError("usuario.controller.msj.noauteliminar");
            return;
        }

        if (!usuarioAdminView.confirmarAccion("usuario.controller.msj.seguroelimar", username)) {
            return;
        }

        usuarioDAO.eliminar(username);

        listarUsuarios();
        usuarioAdminView.limpiarSeleccion();
        usuarioAdminView.mostrarMensaje("usuario.controller.msj.eliminado");
    }

    public void cerrarSesion() {
        this.usuarioAutenticado = null;
    }

    private void buscarUsuarioAction() {
        try {
            String username = usuarioBuscarView.getTxtUsername().getText().trim();
            if (username.isEmpty()) {
                usuarioBuscarView.mostrarMensaje(Idioma.get("usuario.controller.msj.ingreseusuario"));
                return;
            }
            Usuario usuario = usuarioDAO.buscarPorUsername(username);
            usuarioBuscarView.mostrarUsuario(usuario);
        } catch (IOException e) {
            usuarioBuscarView.mostrarMensaje(Idioma.get("usuario.controller.msj.error.archivo") + ": " + e.getMessage());
        }
    }

    public void listarTodosAction() {
        try {
            List<Usuario> usuarios = usuarioDAO.listarTodos();
            usuarioBuscarView.cargarUsuarios(usuarios);
        } catch (IOException e) {
            usuarioBuscarView.mostrarMensaje(Idioma.get("usuario.controller.msj.error.archivo") + ": " + e.getMessage());
        }
    }

    private void crearUsuarioDesdeCrearView() {
        try {
            String username = usuarioCrearView.getTxtUsuario().getText().trim();
            String password = new String(usuarioCrearView.getTxtContrasena().getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                throw new ValidacionException(Idioma.get("usuario.controller.msj.nousuariopass"));
            }
            if (usuarioDAO.buscarPorUsername(username) != null) {
                throw new ValidacionException(Idioma.get("usuario.controller.extiste"));
            }

            String tipoStorageSesion = usuarioAutenticado.getTipoAlmacenamiento();
            Usuario nuevo = new Usuario(username, password, Rol.USUARIO, Genero.OTROS, "N/A", "N/A", "N/A", 0, tipoStorageSesion);

            usuarioDAO.crear(nuevo);

            usuarioCrearView.mostrarMensaje("usuario.controller.msj.creado");
            usuarioCrearView.getTxtUsuario().setText("");
            usuarioCrearView.getTxtContrasena().setText("");
            if (usuarioAdminView != null) listarUsuarios();
            if (usuarioBuscarView != null) listarTodosAction();

        } catch (CedulaInvalidaException | PasswordInvalidaException | ValidacionException e) {
            JOptionPane.showMessageDialog(usuarioCrearView, e.getMessage(), "Error de Validación", JOptionPane.WARNING_MESSAGE);
        } catch (IOException e) { 

            JOptionPane.showMessageDialog(usuarioCrearView, Idioma.get("usuario.controller.msj.error.archivo") + ": " + e.getMessage(), "Error de Archivo", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modificarDatosUsuario() {
        try {
            String originalUsername = usuarioAutenticado.getUsername();
            String nuevoUsername = usuarioModificarDatosView.getTxtUsuario().getText().trim();
            String nuevaContra = new String(usuarioModificarDatosView.getTxtContra().getPassword());
            String nuevoNombre = usuarioModificarDatosView.getTxtNombre().getText().trim();
            String nuevoApellido = usuarioModificarDatosView.getTxtApellido().getText().trim();
            String nuevoTelefono = usuarioModificarDatosView.getTxtTelefono().getText().trim();
            String edadTexto = usuarioModificarDatosView.getTxtEdad().getText().trim();
            int nuevaEdad;
            try {
                nuevaEdad = Integer.parseInt(edadTexto);
            } catch (Exception ex) {
                throw new ValidacionException(Idioma.get("registro.msj.edadinvalida"));
            }
            Genero nuevoGenero = (Genero) usuarioModificarDatosView.getCbxGenero().getSelectedItem();

            if (nuevoUsername.isEmpty() || nuevaContra.isEmpty()) {
                throw new ValidacionException(Idioma.get("usuario.controller.msj.nousuariopass"));
            }
            if (!nuevoUsername.equals(usuarioAutenticado.getUsername()) && usuarioDAO.buscarPorUsername(nuevoUsername) != null) {
                throw new ValidacionException(Idioma.get("usuario.controller.msj.usoyaenuso"));
            }


            usuarioAutenticado.setUsername(nuevoUsername);
            usuarioAutenticado.setPassword(nuevaContra);
            usuarioAutenticado.setNombre(nuevoNombre);
            usuarioAutenticado.setApellido(nuevoApellido);
            usuarioAutenticado.setTelefono(nuevoTelefono);
            usuarioAutenticado.setEdad(nuevaEdad);
            usuarioAutenticado.setGenero(nuevoGenero);
            usuarioDAO.actualizar(originalUsername, usuarioAutenticado);

            mostrarMensaje(Idioma.get("usuario.controller.msj.actualizadodatos"));
            usuarioModificarDatosView.mostrarDatosUsuario(usuarioAutenticado);

            if (usuarioAdminView != null) listarUsuarios();
            if (usuarioBuscarView != null) listarTodosAction();

        } catch (CedulaInvalidaException | PasswordInvalidaException | ValidacionException e) {
            mostrarMensaje(e.getMessage());
        } catch (IOException e) { 

            mostrarMensaje(Idioma.get("usuario.controller.msj.error.archivo") + ": " + e.getMessage());
        }
    }

            private void mostrarMensaje(String mensaje) {
        if (usuarioModificarDatosView != null) {
            JOptionPane.showMessageDialog(usuarioModificarDatosView, mensaje);
        }
    }

}