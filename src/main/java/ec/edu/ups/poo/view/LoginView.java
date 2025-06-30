package ec.edu.ups.poo.view;

import javax.swing.*;

public class LoginView extends JFrame{
    private JPanel panelPrincipal;
    private JPanel panelSecundario;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnIniciarSesion;
    private JButton btnRegistrarse;// NUEVO
    private JLabel lblUsuario;
    private JLabel lblContrasena;
    private JButton btnOlvide;

    public LoginView(){
        setContentPane(panelPrincipal);
        setTitle("Iniciar Sesion");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600,400);
        setLocationRelativeTo(null);
        btnOlvide.setIcon(new ImageIcon(getClass().getResource("/icons/pregunta.png")));
        btnRegistrarse.setIcon(new ImageIcon(getClass().getResource("/icons/usuario.png")));
        btnIniciarSesion.setIcon(new ImageIcon(getClass().getResource("/icons/entrar.png")));


    }

    public JPanel getPanelPrincipal() {
        return panelPrincipal;
    }

    public void setPanelPrincipal(JPanel panelPrincipal) {
        this.panelPrincipal = panelPrincipal;
    }

    public JPanel getPanelSecundario() {
        return panelSecundario;
    }

    public void setPanelSecundario(JPanel panelSecundario) {
        this.panelSecundario = panelSecundario;
    }

    public JTextField getTxtUsername() {
        return txtUsername;
    }

    public void setTxtUsername(JTextField txtUsername) {
        this.txtUsername = txtUsername;
    }

    public JPasswordField getTxtPassword() {
        return txtPassword;
    }

    public void setTxtPassword(JPasswordField txtPassword) {
        this.txtPassword = txtPassword;
    }

    public JButton getBtnIniciarSesion() {
        return btnIniciarSesion;
    }

    public void setBtnIniciarSesion(JButton btnIniciarSesion) {
        this.btnIniciarSesion = btnIniciarSesion;
    }

    public JButton getBtnRegistrarse() {
        return btnRegistrarse;
    }

    public void setBtnRegistrarse(JButton btnRegistrarse) {
        this.btnRegistrarse = btnRegistrarse;
    }

    public JButton getBtnOlvide() {
        return btnOlvide;
    }

    public void setBtnOlvide(JButton btnOlvide) {
        this.btnOlvide = btnOlvide;
    }

    public void mostrar (String mensaje){
        JOptionPane.showMessageDialog(this, mensaje);
    }
}