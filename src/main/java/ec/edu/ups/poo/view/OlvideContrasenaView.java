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

        setContentPane(panelPrincipal);
    }

    public JPanel getPanelPrincipal() {
        return panelPrincipal;
    }

    public void setPanelPrincipal(JPanel panelPrincipal) {
        this.panelPrincipal = panelPrincipal;
    }

    public JLabel getLblUsuario() {
        return lblUsuario;
    }

    public void setLblUsuario(JLabel lblUsuario) {
        this.lblUsuario = lblUsuario;
    }

    public JTextField getTxtUsername() {
        return txtUsername;
    }

    public void setTxtUsername(JTextField txtUsername) {
        this.txtUsername = txtUsername;
    }

    public JLabel getLblPregunta() {
        return lblPregunta;
    }

    public void setLblPregunta(JLabel lblPregunta) {
        this.lblPregunta = lblPregunta;
    }

    public JTextField getTxtRespuesta() {
        return txtRespuesta;
    }

    public void setTxtRespuesta(JTextField txtRespuesta) {
        this.txtRespuesta = txtRespuesta;
    }

    public JButton getBtnValidar() {
        return btnValidar;
    }

    public void setBtnValidar(JButton btnValidar) {
        this.btnValidar = btnValidar;
    }

    public JLabel getLblNuevaContrasena() {
        return lblNuevaContrasena;
    }

    public void setLblNuevaContrasena(JLabel lblNuevaContrasena) {
        this.lblNuevaContrasena = lblNuevaContrasena;
    }

    public JPasswordField getTxtNuevaContrasena() {
        return txtNuevaContrasena;
    }

    public void setTxtNuevaContrasena(JPasswordField txtNuevaContrasena) {
        this.txtNuevaContrasena = txtNuevaContrasena;
    }

    public JButton getBtnCambiar() {
        return btnCambiar;
    }

    public void setBtnCambiar(JButton btnCambiar) {
        this.btnCambiar = btnCambiar;
    }
}