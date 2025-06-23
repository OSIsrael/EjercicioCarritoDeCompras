package ec.edu.ups.poo.controlador;

import ec.edu.ups.poo.dao.UsuarioDAO;
import ec.edu.ups.poo.modelo.Rol;
import ec.edu.ups.poo.modelo.Usuario;
import ec.edu.ups.poo.view.LoginView;
import ec.edu.ups.poo.view.RegistrarUsuario;
import ec.edu.ups.poo.view.UsuarioAdminView;

import javax.swing.*;
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

        // CORRECCIÓN: Usar el nombre de componente correcto de tu LoginView
        this.loginView.getBtnIniciarSesion().addActionListener(e -> login());
        this.loginView.getBtnRegistrarse().addActionListener(e -> abrirVentanaRegistro());

        this.registrarUsuarioView.getBtnRegistrarse().addActionListener(e -> registrarUsuario());

        this.usuarioAdminView.getBtnActualizar().addActionListener(e -> actualizarUsuario());
        this.usuarioAdminView.getBtnEliminar().addActionListener(e -> eliminarUsuario());
    }

    private void login() {
        String username = loginView.getTxtUsername().getText().trim();
        String password = new String(loginView.getTxtPassword().getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            // CORRECCIÓN: Usar el nombre de método correcto de tu LoginView
            loginView.mostrar("Por favor, ingrese usuario y contraseña.");
            return;
        }
        // CORRECCIÓN: Usar el nombre de método estandarizado del DAO
        Usuario usuario = usuarioDAO.buscarPorUsername(username);
        if (usuario == null || !usuario.getPassword().equals(password)) {
            loginView.mostrar("Usuario o contraseña incorrectos.");
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

        if (username.isEmpty() || password.isEmpty()) {
            registrarUsuarioView.mostrarMensaje("Los campos no pueden estar vacíos.");
            return;
        }
        // CORRECCIÓN: Usar el nombre de método estandarizado del DAO
        if (usuarioDAO.buscarPorUsername(username) != null) {
            registrarUsuarioView.mostrarMensaje("El nombre de usuario ya existe.");
            return;
        }
        usuarioDAO.crear(new Usuario(username, password, Rol.USUARIO));
        registrarUsuarioView.mostrarMensaje("Usuario registrado exitosamente.");
        registrarUsuarioView.dispose();
    }

    public void listarUsuarios() {
        // CORRECCIÓN: Usar el nombre de método estandarizado del DAO
        List<Usuario> usuarios = usuarioDAO.listarPorRol(Rol.USUARIO);
        usuarioAdminView.cargarUsuarios(usuarios);
    }

    private void actualizarUsuario() {
        int fila = usuarioAdminView.getTblUsuarios().getSelectedRow();
        if (fila < 0) {
            usuarioAdminView.mostrarMensaje("Seleccione un usuario de la tabla.");
            return;
        }
        String username = (String) usuarioAdminView.getTblUsuarios().getValueAt(fila, 0);
        Rol nuevoRol = (Rol) usuarioAdminView.getCbxRol().getSelectedItem();
        // CORRECCIÓN: Usar el nombre de método estandarizado del DAO
        Usuario usuario = usuarioDAO.buscarPorUsername(username);

        if (usuario != null) {
            usuario.setRol(nuevoRol);
            usuarioDAO.actualizar(usuario);
            listarUsuarios();
            usuarioAdminView.mostrarMensaje("Usuario actualizado.");
        }
    }

    private void eliminarUsuario() {
        int fila = usuarioAdminView.getTblUsuarios().getSelectedRow();
        if (fila < 0) {
            usuarioAdminView.mostrarMensaje("Seleccione un usuario para eliminar.");
            return;
        }
        String username = (String) usuarioAdminView.getTblUsuarios().getValueAt(fila, 0);
        if (usuarioAutenticado.getUsername().equals(username)) {
            usuarioAdminView.mostrarMensaje("No puede eliminarse a sí mismo.");
            return;
        }
        int confirmacion = JOptionPane.showConfirmDialog(usuarioAdminView, "¿Eliminar al usuario '" + username + "'?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirmacion == JOptionPane.YES_OPTION) {
            usuarioDAO.eliminar(username);
            listarUsuarios();
            usuarioAdminView.mostrarMensaje("Usuario eliminado.");
        }
    }
}