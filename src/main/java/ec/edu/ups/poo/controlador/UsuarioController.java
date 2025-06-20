package ec.edu.ups.poo.controlador;

import ec.edu.ups.poo.dao.UsuarioDAO;
import ec.edu.ups.poo.modelo.Usuario;
import ec.edu.ups.poo.view.LoginView;

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
        loginView.getBtnIniciarSesion().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                autenticar();
            }
        });
    }

    private void autenticar() {
        String username = loginView.getTxtUsername().getText();
        String password = new String(loginView.getTxtPassword().getPassword());
        usuario = usuarioDAO.autenticar(username, password);
        if (usuario == null) {
            loginView.mostrar("Usuario o contraseña incorrectos");
        } else {
            loginView.dispose();
        }
    }

    public Usuario getUsuarioAutenticado() {
            return  usuario;
    }
}


