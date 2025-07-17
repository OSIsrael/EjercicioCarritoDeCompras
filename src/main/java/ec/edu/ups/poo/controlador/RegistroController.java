package ec.edu.ups.poo.controlador;

import ec.edu.ups.poo.dao.UsuarioDAO;
import ec.edu.ups.poo.modelo.Rol;
import ec.edu.ups.poo.modelo.Usuario;
import ec.edu.ups.poo.view.RegistrarUsuario;
import ec.edu.ups.poo.modelo.Genero;
import ec.edu.ups.poo.util.CedulaInvalidaException;
import ec.edu.ups.poo.util.PasswordInvalidaException;
import ec.edu.ups.poo.util.ValidacionException;
import ec.edu.ups.poo.util.Idioma;

import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import ec.edu.ups.poo.modelo.Pregunta;
import java.util.HashMap;

public class RegistroController {
    private final UsuarioDAO usuarioDAO;
    private final RegistrarUsuario registrarUsuarioView;

    public RegistroController(UsuarioDAO usuarioDAO, RegistrarUsuario registrarUsuarioView) {
        this.usuarioDAO = usuarioDAO;
        this.registrarUsuarioView = registrarUsuarioView;
        configurarEventos();
    }

    private void configurarEventos() {

        registrarUsuarioView.getBtnRegistrarse().addActionListener(e -> registrarUsuario());
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
            

            if (username.isEmpty() || password.isEmpty() || nombre.isEmpty() || apellido.isEmpty() ||
                    telefono.isEmpty() || edadStr.isEmpty()) {
                throw new ValidacionException(Idioma.get("registro.msj.camposprincipales.vacios"));
            }

            if (usuarioDAO.buscarPorUsername(username) != null) {
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


            Usuario nuevoUsuario = new Usuario(username, password, Rol.USUARIO, genero, nombre, apellido, telefono, edad);


            Map<Integer, String> preguntasParaGuardar = new HashMap<>();
            for (Map.Entry<Pregunta, String> entry : preguntasRespondidas.entrySet()) {
                preguntasParaGuardar.put(entry.getKey().getId(), entry.getValue());
            }
            nuevoUsuario.setPreguntasSeguridad(preguntasParaGuardar);


            usuarioDAO.crear(nuevoUsuario);


            registrarUsuarioView.mostrarMensaje(Idioma.get("registro.msj.registrado"));
            registrarUsuarioView.limpiarCampos();
            registrarUsuarioView.dispose();

        } catch (CedulaInvalidaException | PasswordInvalidaException e) {

            JOptionPane.showMessageDialog(registrarUsuarioView, e.getMessage(), "Error de Datos", JOptionPane.WARNING_MESSAGE);

        } catch (ValidacionException e) {

            JOptionPane.showMessageDialog(registrarUsuarioView, e.getMessage(), "Error de Validación", JOptionPane.WARNING_MESSAGE);

        } catch (Exception e) {

            JOptionPane.showMessageDialog(registrarUsuarioView, "Ocurrió un error inesperado: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}