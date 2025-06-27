package ec.edu.ups.poo.view;

import javax.swing.*;

public class OlvideContrasenaView extends JDialog {
    private JPanel panelPrincipal;
    private JLabel lblUsuario;
    private JTextField txtUsername;
    private JLabel lblPregunta;
    private JTextField txtRespuesta;
    private JButton btnValidar;
    private JLabel lblNuevaContrasena;
    private JPasswordField txtNuevaContrasena;
    private JButton btnCambiar;

    public OlvideContrasenaView() {
        setTitle("Recuperar Contraseña");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setModal(true);

        panelPrincipal = new JPanel();
        panelPrincipal.setLayout(new BoxLayout(panelPrincipal, BoxLayout.Y_AXIS));

        panelPrincipal.add(new JLabel("Nombre de usuario:"));
        txtUsername = new JTextField();
        panelPrincipal.add(txtUsername);

        lblPregunta = new JLabel("Pregunta de seguridad:");
        panelPrincipal.add(lblPregunta);

        txtRespuesta = new JTextField();
        panelPrincipal.add(new JLabel("Respuesta:"));
        panelPrincipal.add(txtRespuesta);

        btnValidar = new JButton("Validar respuesta");
        panelPrincipal.add(btnValidar);

        lblNuevaContrasena = new JLabel("Nueva contraseña:");
        lblNuevaContrasena.setVisible(false);
        panelPrincipal.add(lblNuevaContrasena);

        txtNuevaContrasena = new JPasswordField();
        txtNuevaContrasena.setVisible(false);
        panelPrincipal.add(txtNuevaContrasena);

        btnCambiar = new JButton("Cambiar contraseña");
        btnCambiar.setVisible(false);
        panelPrincipal.add(btnCambiar);

        setContentPane(panelPrincipal);
    }

    public String getUsername() {
        return txtUsername.getText();
    }

    public String getRespuesta() {
        return txtRespuesta.getText();
    }

    public String getNuevaContrasena() {
        return new String(txtNuevaContrasena.getPassword());
    }

    public JButton getBtnValidar() {
        return btnValidar;
    }

    public JButton getBtnCambiar() {
        return btnCambiar;
    }

    public void setPregunta(String pregunta) {
        lblPregunta.setText("Pregunta de seguridad: " + pregunta);
    }

    public void mostrarCamposNuevaContrasena(boolean mostrar) {
        lblNuevaContrasena.setVisible(mostrar);
        txtNuevaContrasena.setVisible(mostrar);
        btnCambiar.setVisible(mostrar);
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    public void limpiarCampos() {
        txtUsername.setText("");
        txtRespuesta.setText("");
        txtNuevaContrasena.setText("");
        mostrarCamposNuevaContrasena(false);
    }
}