package ec.edu.ups.poo.controlador;

import ec.edu.ups.poo.dao.UsuarioDAO;
import ec.edu.ups.poo.modelo.Rol;
import ec.edu.ups.poo.modelo.Usuario;
import ec.edu.ups.poo.view.*;

import javax.swing.*;
import java.util.List;

public class UsuarioController {
    private UsuarioDAO usuarioDAO;
    private LoginView loginView;
    private RegistrarUsuario registrarUsuarioView;
    private UsuarioAdminView usuarioAdminView;
    private Usuario usuarioAutenticado;
    private UsuarioBuscarView usuarioBuscarView;
    private UsuarioCrearView usuarioCrearView;
    private UsuarioModificarDatosView usuarioModificarDatosView;

    public UsuarioController(UsuarioDAO usuarioDAO, LoginView loginView, RegistrarUsuario registrarUsuarioView, UsuarioAdminView usuarioAdminView, UsuarioBuscarView usuarioBuscarView,UsuarioCrearView usuarioCrearView,UsuarioModificarDatosView usuarioModificarDatosView) {
        this.usuarioDAO = usuarioDAO;
        this.loginView = loginView;
        this.registrarUsuarioView = registrarUsuarioView;
        this.usuarioAdminView = usuarioAdminView;
        this.usuarioBuscarView = usuarioBuscarView;
        this.usuarioCrearView = usuarioCrearView;
        this.usuarioModificarDatosView = usuarioModificarDatosView;
        configurarEventos();
    }

    private void configurarEventos() {
        // Eventos de LoginView
        this.loginView.getBtnIniciarSesion().addActionListener(e -> login());
        this.loginView.getBtnRegistrarse().addActionListener(e -> abrirVentanaRegistro());

        // Eventos de RegistrarUsuario
        this.registrarUsuarioView.getBtnRegistrarse().addActionListener(e -> registrarUsuario());

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

    private void login() {
        String username = loginView.getTxtUsername().getText().trim();
        String password = new String(loginView.getTxtPassword().getPassword());

        // Validar campos vacíos
        if (username.isEmpty() || password.isEmpty()) {
            loginView.mostrar("Por favor, ingrese usuario y contraseña.");
            return;
        }

        // Buscar usuario
        Usuario usuario = usuarioDAO.buscarPorUsername(username);
        if (usuario == null) {
            loginView.mostrar("Usuario no encontrado.");
            return;
        }

        // Verificar contraseña
        if (!usuario.getPassword().equals(password)) {
            loginView.mostrar("Contraseña incorrecta.");
            return;
        }

        // Login exitoso
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

        // Validaciones
        if (username.isEmpty()) {
            registrarUsuarioView.mostrarMensaje("El nombre de usuario no puede estar vacío.");
            return;
        }

        if (password.isEmpty()) {
            registrarUsuarioView.mostrarMensaje("La contraseña no puede estar vacía.");
            return;
        }

        if (password.length() < 4) {
            registrarUsuarioView.mostrarMensaje("La contraseña debe tener al menos 4 caracteres.");
            return;
        }

        // Verificar si el usuario ya existe
        if (usuarioDAO.buscarPorUsername(username) != null) {
            registrarUsuarioView.mostrarMensaje("El nombre de usuario ya existe. Elija otro nombre.");
            return;
        }

        // Crear usuario
        Usuario nuevoUsuario = new Usuario(username, password, Rol.USUARIO);
        usuarioDAO.crear(nuevoUsuario);

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

        String nuevoUsername = (String) usuarioAdminView.getModeloTabla().getValueAt(fila, 0); // <-- Nuevo username editable
        Rol rolActual = (Rol) usuarioAdminView.getTblUsuarios().getValueAt(fila, 1);
        Rol nuevoRol = (Rol) usuarioAdminView.getCbxRol().getSelectedItem();

        // Verificar si hay cambios
        if (rolActual == nuevoRol && nuevoUsername.equals(usuarioDAO.buscarPorUsername(nuevoUsername).getUsername())) {
            usuarioAdminView.mostrarMensaje("No hay cambios en el usuario seleccionado.");
            return;
        }

        // Verificar si el nombre ya existe (y no es el mismo usuario)
        Usuario usuarioBuscado = usuarioDAO.buscarPorUsername(nuevoUsername);
        if (usuarioBuscado != null && !usuarioAdminView.getTblUsuarios().getValueAt(fila, 0).equals(usuarioBuscado.getUsername())) {
            usuarioAdminView.mostrarError("El nombre de usuario ya está en uso.");
            return;
        }

        // Buscar el usuario original (antes del cambio de username)
        // Por ejemplo, podrías guardar el username original en otro campo oculto, o usar un modelo con objetos Usuario.
        // Aquí suponemos que tienes una referencia directa al usuario original, si no, puedes adaptar esto.

        Usuario usuario = usuarioDAO.buscarPorUsername((String) usuarioAdminView.getTblUsuarios().getValueAt(fila, 0));
        if (usuario == null) {
            usuarioAdminView.mostrarError("Usuario no encontrado.");
            return;
        }

        // Verificar si se intenta cambiar el rol del usuario autenticado
        // (ajustar según tu lógica de autenticación)
        // if (usuarioAutenticado.getUsername().equals(nuevoUsername)) { ... }

        // Confirmar la acción
        String mensaje = "¿Está seguro de modificar el usuario a '" + nuevoUsername +
                "' y rol " + nuevoRol + "?";

        if (!usuarioAdminView.confirmarAccion(mensaje)) {
            return;
        }

        // Actualizar datos
        usuario.setUsername(nuevoUsername);
        usuario.setRol(nuevoRol);
        usuarioDAO.actualizar(usuario);

        // Refrescar la lista
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

        // Verificar que no se elimine a sí mismo
        if (usuarioAutenticado.getUsername().equals(username)) {
            usuarioAdminView.mostrarError("No puede eliminarse a sí mismo.");
            return;
        }

        // Verificar que el usuario existe
        Usuario usuario = usuarioDAO.buscarPorUsername(username);
        if (usuario == null) {
            usuarioAdminView.mostrarError("Usuario no encontrado.");
            return;
        }

        // Confirmar eliminación
        String mensaje = "¿Está seguro de eliminar al usuario '" + username + "'?\n" +
                "Esta acción no se puede deshacer.";

        if (!usuarioAdminView.confirmarAccion(mensaje)) {
            return;
        }

        // Eliminar usuario
        usuarioDAO.eliminar(username);

        // Refrescar la lista y limpiar selección
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
        // Refresca otras vistas
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
        // Si el username cambió y está ocupado por otro usuario
        if (!nuevoUsername.equals(usuarioAutenticado.getUsername()) && usuarioDAO.buscarPorUsername(nuevoUsername) != null) {
            mostrarMensaje("El nombre de usuario ya está en uso.");
            return;
        }
        // Actualiza los datos
        usuarioAutenticado.setUsername(nuevoUsername);
        usuarioAutenticado.setPassword(nuevaContra);
        usuarioDAO.actualizar(usuarioAutenticado);

        mostrarMensaje("Datos actualizados correctamente.");
        usuarioModificarDatosView.mostrarDatosUsuario(usuarioAutenticado);

        // Refrescar otras vistas si corresponde
        if (usuarioAdminView != null) listarUsuarios();
        if (usuarioBuscarView != null) listarTodosAction();
    }

    private void mostrarMensaje(String mensaje) {
        if (usuarioModificarDatosView != null) {
            JOptionPane.showMessageDialog(usuarioModificarDatosView, mensaje);
        }
    }
}