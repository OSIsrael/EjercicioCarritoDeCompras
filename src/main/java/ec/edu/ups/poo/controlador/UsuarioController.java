package ec.edu.ups.poo.controlador;

import ec.edu.ups.poo.dao.UsuarioDAO;
import ec.edu.ups.poo.modelo.Rol;
import ec.edu.ups.poo.modelo.Usuario;
import ec.edu.ups.poo.view.LoginView;
import ec.edu.ups.poo.view.RegistrarUsuario;
import ec.edu.ups.poo.view.UsuarioAdminView;

import java.util.List;

public class UsuarioController {
    private UsuarioDAO usuarioDAO;
    private LoginView loginView;
    private RegistrarUsuario registrarUsuarioView;
    private UsuarioAdminView usuarioAdminView;
    private Usuario usuarioAutenticado;

    public UsuarioController(UsuarioDAO usuarioDAO, LoginView loginView, RegistrarUsuario registrarUsuarioView, UsuarioAdminView usuarioAdminView) {
        this.usuarioDAO = usuarioDAO;
        this.loginView = loginView;
        this.registrarUsuarioView = registrarUsuarioView;
        this.usuarioAdminView = usuarioAdminView;
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
        List<Usuario> todosLosUsuarios = usuarioDAO.listarPorRol(Rol.USUARIO);
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

        String username = (String) usuarioAdminView.getTblUsuarios().getValueAt(fila, 0);
        Rol rolActual = (Rol) usuarioAdminView.getTblUsuarios().getValueAt(fila, 1);
        Rol nuevoRol = (Rol) usuarioAdminView.getCbxRol().getSelectedItem();

        // Verificar si hay cambios
        if (rolActual == nuevoRol) {
            usuarioAdminView.mostrarMensaje("El usuario ya tiene el rol seleccionado.");
            return;
        }

        // Buscar el usuario
        Usuario usuario = usuarioDAO.buscarPorUsername(username);
        if (usuario == null) {
            usuarioAdminView.mostrarError("Usuario no encontrado.");
            return;
        }

        // Verificar si se intenta cambiar el rol del usuario autenticado
        if (usuarioAutenticado.getUsername().equals(username)) {
            usuarioAdminView.mostrarError("No puede cambiar su propio rol.");
            return;
        }

        // Confirmar la acción
        String mensaje = "¿Está seguro de cambiar el rol del usuario '" + username +
                "' de " + rolActual + " a " + nuevoRol + "?";

        if (!usuarioAdminView.confirmarAccion(mensaje)) {
            return;
        }

        // Actualizar el rol
        usuario.setRol(nuevoRol);
        usuarioDAO.actualizar(usuario);

        // Refrescar la lista
        listarUsuarios();
        usuarioAdminView.mostrarMensaje("Rol actualizado exitosamente.");
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
}