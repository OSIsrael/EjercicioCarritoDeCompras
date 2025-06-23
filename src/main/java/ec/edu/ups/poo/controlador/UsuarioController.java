package ec.edu.ups.poo.controlador;

import ec.edu.ups.poo.dao.UsuarioDAO;
import ec.edu.ups.poo.modelo.Usuario;
import ec.edu.ups.poo.view.LoginView;
import ec.edu.ups.poo.view.RegistrarUsuario;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UsuarioController {
    private final UsuarioDAO usuarioDAO;
    private final LoginView loginView;
    private Usuario usuario;

    public UsuarioController(UsuarioDAO usuarioDAO, LoginView loginView) {
        this.usuarioDAO = usuarioDAO;
        this.loginView = loginView;
        this.usuario = null;
        configurarEventosEnVistas();
    }

    private void configurarEventosEnVistas() {
        // Evento para iniciar sesión
        loginView.getBtnIniciarSesion().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                autenticar();
            }
        });

        // Evento para registrarse
        loginView.getBtnRegistrarse().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                abrirVentanaRegistro();
            }
        });
    }

    private void autenticar() {
        String username = loginView.getTxtUsername().getText().trim();
        String password = new String(loginView.getTxtPassword().getPassword());

        if (username.isEmpty()) {
            loginView.mostrar("El nombre de usuario no puede estar vacío");
            return;
        }

        if (password.isEmpty()) {
            loginView.mostrar("La contraseña no puede estar vacía");
            return;
        }

        usuario = usuarioDAO.autenticar(username, password);
        if (usuario == null) {
            loginView.mostrar("Usuario o contraseña incorrectos");
        } else {
            loginView.dispose();
        }
    }

    private void abrirVentanaRegistro() {
        RegistrarUsuario registrarUsuario = new RegistrarUsuario();
        RegistroController registroController = new RegistroController(usuarioDAO, registrarUsuario);
        registrarUsuario.setVisible(true);
    }

    public Usuario getUsuarioAutenticado() {
        return usuario;
    }
}