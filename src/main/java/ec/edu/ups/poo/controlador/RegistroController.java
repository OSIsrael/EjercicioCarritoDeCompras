package ec.edu.ups.poo.controlador;

import ec.edu.ups.poo.dao.UsuarioDAO;
import ec.edu.ups.poo.modelo.Rol;
import ec.edu.ups.poo.modelo.Usuario;
import ec.edu.ups.poo.view.RegistrarUsuario;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegistroController {
    private final UsuarioDAO usuarioDAO;
    private final RegistrarUsuario registrarUsuario;

    public RegistroController(UsuarioDAO usuarioDAO, RegistrarUsuario registrarUsuario) {
        this.usuarioDAO = usuarioDAO;
        this.registrarUsuario = registrarUsuario;
        configurarEventos();
    }

    private void configurarEventos() {
        registrarUsuario.getBtnRegistrarse().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarUsuario();
            }
        });
    }

    private void registrarUsuario() {
        String username = registrarUsuario.getTxtUsuarioRe().getText().trim();
        String password = new String(registrarUsuario.getTxtContraRe().getPassword());

        // Validaciones
        if (username.isEmpty()) {
            registrarUsuario.mostrarMensaje("El nombre de usuario no puede estar vacío");
            return;
        }

        if (password.isEmpty()) {
            registrarUsuario.mostrarMensaje("La contraseña no puede estar vacía");
            return;
        }

        if (password.length() < 4) {
            registrarUsuario.mostrarMensaje("La contraseña debe tener al menos 4 caracteres");
            return;
        }

        // Verificar si el usuario ya existe
        Usuario usuarioExistente = usuarioDAO.buscarPorUsername(username);
        if (usuarioExistente != null) {
            registrarUsuario.mostrarMensaje("El usuario ya existe. Elija otro nombre de usuario");
            return;
        }

        // Crear nuevo usuario con rol USUARIO por defecto
        Usuario nuevoUsuario = new Usuario(username, password, Rol.USUARIO);
        usuarioDAO.crear(nuevoUsuario);

        registrarUsuario.mostrarMensaje("Usuario registrado exitosamente");
        registrarUsuario.limpiarCampos();
        registrarUsuario.dispose(); // Cerrar ventana de registro
    }
}