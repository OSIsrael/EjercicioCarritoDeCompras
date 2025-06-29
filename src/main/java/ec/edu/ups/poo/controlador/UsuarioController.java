package ec.edu.ups.poo.controlador;

import ec.edu.ups.poo.dao.PreguntaDAO;
import ec.edu.ups.poo.dao.UsuarioDAO;
import ec.edu.ups.poo.modelo.Pregunta;
import ec.edu.ups.poo.modelo.Rol;
import ec.edu.ups.poo.modelo.Usuario;
import ec.edu.ups.poo.view.*;

import javax.swing.*;
import java.util.*;

public class UsuarioController {
    private UsuarioDAO usuarioDAO;
    private PreguntaDAO preguntaDAO;
    private LoginView loginView;
    private RegistrarUsuario registrarUsuarioView;
    private UsuarioAdminView usuarioAdminView;
    private Usuario usuarioAutenticado;
    private UsuarioBuscarView usuarioBuscarView;
    private UsuarioCrearView usuarioCrearView;
    private UsuarioModificarDatosView usuarioModificarDatosView;
    private OlvideContrasenaView olvideContrasenaView;

    // Variables para el flujo de recuperación
    private Usuario usuarioRecuperacion;
    private Integer idPreguntaRecuperacion;

    public UsuarioController(UsuarioDAO usuarioDAO,PreguntaDAO preguntaDAO,LoginView loginView,
                             RegistrarUsuario registrarUsuarioView, UsuarioAdminView usuarioAdminView,
                             UsuarioBuscarView usuarioBuscarView, UsuarioCrearView usuarioCrearView,
                             UsuarioModificarDatosView usuarioModificarDatosView
                             ) {
        this.usuarioDAO = usuarioDAO;
        this.preguntaDAO = preguntaDAO;
        this.loginView = loginView;
        this.registrarUsuarioView = registrarUsuarioView;
        this.usuarioAdminView = usuarioAdminView;
        this.usuarioBuscarView = usuarioBuscarView;
        this.usuarioCrearView = usuarioCrearView;
        this.usuarioModificarDatosView = usuarioModificarDatosView;
        this.olvideContrasenaView = new OlvideContrasenaView();
        configurarEventos();
    }

    private void configurarEventos() {
        // Eventos de LoginView
        this.loginView.getBtnIniciarSesion().addActionListener(e -> login());
        this.loginView.getBtnOlvide().addActionListener(e ->  abrirOlvideContrasena());
        this.loginView.getBtnRegistrarse().addActionListener(e -> abrirVentanaRegistro());

        // Eventos de RegistrarUsuario
        this.registrarUsuarioView.getBtnRegistrarse().addActionListener(e -> registrarUsuario());

        // Eventos de OlvideContrasenaView

        olvideContrasenaView.getBtnValidar().addActionListener(e -> validarUsuarioYMostrarPregunta());
        olvideContrasenaView.getBtnCambiar().addActionListener(e -> cambiarContrasena());
        // Eventos de UsuarioAdminView
        this.usuarioAdminView.getBtnActualizar().addActionListener(e -> actualizarUsuario());
        this.usuarioAdminView.getBtnEliminar().addActionListener(e -> eliminarUsuario());
        this.usuarioAdminView.getBtnRefrescar().addActionListener(e -> listarUsuarios());

        //Eventos UsuarioBuscarView
        if (usuarioBuscarView != null) {
            usuarioBuscarView.getBtnBuscar().addActionListener(e -> buscarUsuarioAction());
            usuarioBuscarView.getBtnListar().addActionListener(e -> listarTodosAction());
        }
        if (usuarioCrearView != null) {
            usuarioCrearView.getBtnCrear().addActionListener(e -> crearUsuarioDesdeCrearView());
            usuarioCrearView.getBtnBorrar().addActionListener(e -> usuarioCrearView.getTxtUsuario().setText(""));
            usuarioCrearView.getBtnBorrar().addActionListener(e -> usuarioCrearView.getTxtContrasena().setText(""));
        }
        if (usuarioModificarDatosView != null) {
            usuarioModificarDatosView.getBtnModificar().addActionListener(e -> modificarDatosUsuario());
            usuarioModificarDatosView.getBtnBorrar().addActionListener(e -> usuarioModificarDatosView.limpiarCampos());
        }
    }
    private void abrirOlvideContrasena() {
        olvideContrasenaView.getTxtUsername().setText("");
        olvideContrasenaView.getLblPregunta().setText("");
        olvideContrasenaView.getTxtRespuesta().setText("");
        olvideContrasenaView.getLblNuevaContrasena().setVisible(false);
        olvideContrasenaView.getTxtNuevaContrasena().setVisible(false);
        olvideContrasenaView.getBtnCambiar().setVisible(false);
        olvideContrasenaView.setVisible(true);
    }
    private void validarUsuarioYMostrarPregunta() {
        String username = olvideContrasenaView.getTxtUsername().getText().trim();
        Usuario usuario = usuarioDAO.buscarPorUsername(username);
        if (usuario == null) {
            JOptionPane.showMessageDialog(olvideContrasenaView, "Usuario no encontrado.");
            return;
        }
        usuarioRecuperacion = usuario;
        List<Integer> idsPreguntas = new ArrayList<>(usuario.getPreguntasSeguridad().keySet());
        if (idsPreguntas.isEmpty()) {
            JOptionPane.showMessageDialog(olvideContrasenaView, "No hay preguntas de seguridad para este usuario.");
            return;
        }
        // Selecciona una pregunta al azar
        idPreguntaRecuperacion = idsPreguntas.get(new Random().nextInt(idsPreguntas.size()));
        Pregunta pregunta = preguntaDAO.buscarPorId(idPreguntaRecuperacion);
        if (pregunta != null) {
            olvideContrasenaView.getLblPregunta().setText(pregunta.getPregunta());
        } else {
            olvideContrasenaView.getLblPregunta().setText("Pregunta no encontrada.");
        }
        // Hacer visibles los campos de respuesta y demás
        olvideContrasenaView.getLblNuevaContrasena().setVisible(true);
        olvideContrasenaView.getTxtNuevaContrasena().setVisible(true);
        olvideContrasenaView.getBtnCambiar().setVisible(true);
    }
    private void cambiarContrasena() {
        String respuesta = olvideContrasenaView.getTxtRespuesta().getText().trim();
        String respuestaCorrecta = usuarioRecuperacion.getPreguntasSeguridad().get(idPreguntaRecuperacion);
        if (!respuesta.equals(respuestaCorrecta)) {
            JOptionPane.showMessageDialog(olvideContrasenaView, "Respuesta incorrecta.");
            return;
        }
        String nuevaContra = new String(olvideContrasenaView.getTxtNuevaContrasena().getPassword());
        if (nuevaContra.isEmpty()) {
            JOptionPane.showMessageDialog(olvideContrasenaView, "Ingrese una nueva contraseña.");
            return;
        }
        usuarioRecuperacion.setPassword(nuevaContra);
        usuarioDAO.actualizar(usuarioRecuperacion);
        JOptionPane.showMessageDialog(olvideContrasenaView, "Contraseña actualizada correctamente.");
        olvideContrasenaView.dispose();
    }
    private void login() {
        String username = loginView.getTxtUsername().getText().trim();
        String password = new String(loginView.getTxtPassword().getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            loginView.mostrar("Por favor, ingrese usuario y contraseña.");
            return;
        }

        Usuario usuario = usuarioDAO.buscarPorUsername(username);
        if (usuario == null) {
            loginView.mostrar("Usuario no encontrado.");
            return;
        }
        if (!usuario.getPassword().equals(password)) {
            loginView.mostrar("Contraseña incorrecta.");
            return;
        }
        this.usuarioAutenticado = usuario;
        loginView.dispose();
    }

    public Usuario getUsuarioAutenticado() {
        return usuarioAutenticado;
    }

    private void abrirVentanaRegistro() {
        registrarUsuarioView.limpiarCampos();
        registrarUsuarioView.setVisible(true);
    }

    private void registrarUsuario() {
        String username = registrarUsuarioView.getTxtUsuarioRe().getText().trim();
        String password = new String(registrarUsuarioView.getTxtContraRe().getPassword());
        Pregunta pregunta1 = (Pregunta) registrarUsuarioView.getCbxPregunta1().getSelectedItem();
        Pregunta pregunta2 = (Pregunta) registrarUsuarioView.getCbxPregunta2().getSelectedItem();
        String respuesta1 = registrarUsuarioView.getTxtRespuesta1().getText().trim();
        String respuesta2 = registrarUsuarioView.getTxtRespuesta2().getText().trim();

        if (username.isEmpty() || password.isEmpty() || pregunta1 == null || pregunta2 == null ||
                respuesta1.isEmpty() || respuesta2.isEmpty() || pregunta1.getId() == pregunta2.getId()) {
            registrarUsuarioView.mostrarMensaje("Debe llenar todos los campos y seleccionar preguntas distintas.");
            return;
        }
        if (usuarioDAO.buscarPorUsername(username) != null) {
            registrarUsuarioView.mostrarMensaje("El usuario ya existe.");
            return;
        }
        Usuario nuevo = new Usuario(username, password, Rol.USUARIO);
        Map<Integer, String> preguntasSeguridad = new HashMap<>();
        preguntasSeguridad.put(pregunta1.getId(), respuesta1);
        preguntasSeguridad.put(pregunta2.getId(), respuesta2);
        nuevo.setPreguntasSeguridad(preguntasSeguridad);
        usuarioDAO.crear(nuevo);
        registrarUsuarioView.mostrarMensaje("Usuario registrado exitosamente.");
        registrarUsuarioView.limpiarCampos();
        registrarUsuarioView.dispose();
    }


    public void listarUsuarios() {
        List<Usuario> todosLosUsuarios = usuarioDAO.listarTodos();
        usuarioAdminView.cargarUsuarios(todosLosUsuarios);
    }

    public void listarUsuariosPorRol(Rol rol) {
        List<Usuario> usuariosFiltrados = usuarioDAO.listarPorRol(rol);
        usuarioAdminView.cargarUsuarios(usuariosFiltrados);
    }

    private void actualizarUsuario() {
        int fila = usuarioAdminView.getTblUsuarios().getSelectedRow();

        if (fila < 0) {
            usuarioAdminView.mostrarError("Seleccione un usuario de la tabla.");
            return;
        }

        String nuevoUsername = (String) usuarioAdminView.getModeloTabla().getValueAt(fila, 0);
        Rol rolActual = (Rol) usuarioAdminView.getTblUsuarios().getValueAt(fila, 1);
        Rol nuevoRol = (Rol) usuarioAdminView.getCbxRol().getSelectedItem();

        // Verificar si hay cambios
        if (rolActual == nuevoRol && nuevoUsername.equals(usuarioDAO.buscarPorUsername(nuevoUsername).getUsername())) {
            usuarioAdminView.mostrarMensaje("No hay cambios en el usuario seleccionado.");
            return;
        }

        Usuario usuarioBuscado = usuarioDAO.buscarPorUsername(nuevoUsername);
        if (usuarioBuscado != null && !usuarioAdminView.getTblUsuarios().getValueAt(fila, 0).equals(usuarioBuscado.getUsername())) {
            usuarioAdminView.mostrarError("El nombre de usuario ya está en uso.");
            return;
        }

        Usuario usuario = usuarioDAO.buscarPorUsername((String) usuarioAdminView.getTblUsuarios().getValueAt(fila, 0));
        if (usuario == null) {
            usuarioAdminView.mostrarError("Usuario no encontrado.");
            return;
        }

        String mensaje = "¿Está seguro de modificar el usuario a '" + nuevoUsername +
                "' y rol " + nuevoRol + "?";

        if (!usuarioAdminView.confirmarAccion(mensaje)) {
            return;
        }

        usuario.setUsername(nuevoUsername);
        usuario.setRol(nuevoRol);
        usuarioDAO.actualizar(usuario);

        listarUsuarios();
        usuarioAdminView.mostrarMensaje("Usuario actualizado exitosamente.");
    }

    private void eliminarUsuario() {
        int fila = usuarioAdminView.getTblUsuarios().getSelectedRow();

        if (fila < 0) {
            usuarioAdminView.mostrarError("Seleccione un usuario para eliminar.");
            return;
        }

        String username = (String) usuarioAdminView.getTblUsuarios().getValueAt(fila, 0);

        if (usuarioAutenticado.getUsername().equals(username)) {
            usuarioAdminView.mostrarError("No puede eliminarse a sí mismo.");
            return;
        }

        Usuario usuario = usuarioDAO.buscarPorUsername(username);
        if (usuario == null) {
            usuarioAdminView.mostrarError("Usuario no encontrado.");
            return;
        }

        String mensaje = "¿Está seguro de eliminar al usuario '" + username + "'?\n" +
                "Esta acción no se puede deshacer.";

        if (!usuarioAdminView.confirmarAccion(mensaje)) {
            return;
        }

        usuarioDAO.eliminar(username);

        listarUsuarios();
        usuarioAdminView.limpiarSeleccion();
        usuarioAdminView.mostrarMensaje("Usuario eliminado exitosamente.");
    }

    public void cerrarSesion() {
        this.usuarioAutenticado = null;
    }

    private void buscarUsuarioAction() {
        String username = usuarioBuscarView.getTxtUsername().getText().trim();
        if (username.isEmpty()) {
            usuarioBuscarView.mostrarMensaje("Ingrese un nombre de usuario.");
            return;
        }
        Usuario usuario = usuarioDAO.buscarPorUsername(username);
        usuarioBuscarView.mostrarUsuario(usuario);
    }

    public void listarTodosAction() {
        List<Usuario> usuarios = usuarioDAO.listarTodos();
        usuarioBuscarView.cargarUsuarios(usuarios);
    }

    private void crearUsuarioDesdeCrearView() {
        String username = usuarioCrearView.getTxtUsuario().getText().trim();
        String password = new String(usuarioCrearView.getTxtContrasena().getPassword());

        if (username.isEmpty()) {
            usuarioCrearView.mostrarMensaje("El nombre de usuario no puede estar vacío.");
            return;
        }
        if (password.isEmpty()) {
            usuarioCrearView.mostrarMensaje("La contraseña no puede estar vacía.");
            return;
        }
        if (usuarioDAO.buscarPorUsername(username) != null) {
            usuarioCrearView.mostrarMensaje("El usuario ya existe.");
            return;
        }
        Usuario nuevo = new Usuario(username, password, Rol.USUARIO);
        usuarioDAO.crear(nuevo);
        usuarioCrearView.mostrarMensaje("Usuario creado exitosamente.");
        usuarioCrearView.getTxtUsuario().setText("");
        usuarioCrearView.getTxtContrasena().setText("");
        if (usuarioAdminView != null) listarUsuarios();
        if (usuarioBuscarView != null) listarTodosAction();
    }

    private void modificarDatosUsuario() {
        String nuevoUsername = usuarioModificarDatosView.getTxtUsuario().getText().trim();
        String nuevaContra = new String(usuarioModificarDatosView.getTxtContra().getPassword());

        if (nuevoUsername.isEmpty() || nuevaContra.isEmpty()) {
            mostrarMensaje("Usuario y contraseña no pueden estar vacíos.");
            return;
        }
        if (!nuevoUsername.equals(usuarioAutenticado.getUsername()) && usuarioDAO.buscarPorUsername(nuevoUsername) != null) {
            mostrarMensaje("El nombre de usuario ya está en uso.");
            return;
        }
        usuarioAutenticado.setUsername(nuevoUsername);
        usuarioAutenticado.setPassword(nuevaContra);
        usuarioDAO.actualizar(usuarioAutenticado);

        mostrarMensaje("Datos actualizados correctamente.");
        usuarioModificarDatosView.mostrarDatosUsuario(usuarioAutenticado);

        if (usuarioAdminView != null) listarUsuarios();
        if (usuarioBuscarView != null) listarTodosAction();
    }

    private void mostrarMensaje(String mensaje) {
        if (usuarioModificarDatosView != null) {
            JOptionPane.showMessageDialog(usuarioModificarDatosView, mensaje);
        }
    }
}