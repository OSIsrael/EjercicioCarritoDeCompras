package ec.edu.ups.poo.view;

import javax.swing.*;
import ec.edu.ups.poo.modelo.Usuario;

public class UsuarioModificarDatosView extends JInternalFrame {
    private JPanel panelPrincipal;
    private JButton btnModificar;
    private JButton btnBorrar;
    private JTextField txtUsuario;
    private JPasswordField txtContra;

    public UsuarioModificarDatosView() {
        super("Modificar Datos", true, true, true, true);
        setContentPane(panelPrincipal);
        this.setSize(700, 500);
    }

    public void mostrarDatosUsuario(Usuario usuario) {
        if (usuario != null) {
            txtUsuario.setText(usuario.getUsername());
            txtContra.setText(usuario.getPassword());
        }
    }

    public void limpiarCampos() {
        txtUsuario.setText("");
        txtContra.setText("");
    }

    public JPanel getPanelPrincipal() {
        return panelPrincipal;
    }

    public void setPanelPrincipal(JPanel panelPrincipal) {
        this.panelPrincipal = panelPrincipal;
    }

    public JButton getBtnModificar() {
        return btnModificar;
    }

    public void setBtnModificar(JButton btnModificar) {
        this.btnModificar = btnModificar;
    }

    public JButton getBtnBorrar() {
        return btnBorrar;
    }

    public void setBtnBorrar(JButton btnBorrar) {
        this.btnBorrar = btnBorrar;
    }

    public JTextField getTxtUsuario() {
        return txtUsuario;
    }

    public void setTxtUsuario(JTextField txtUsuario) {
        this.txtUsuario = txtUsuario;
    }

    public JPasswordField getTxtContra() {
        return txtContra;
    }

    public void setTxtContra(JPasswordField txtContra) {
        this.txtContra = txtContra;
    }
}