package ec.edu.ups.poo.view;

import javax.swing.*;
import ec.edu.ups.poo.modelo.Usuario;
import ec.edu.ups.poo.util.Idioma;

public class UsuarioModificarDatosView extends JInternalFrame {
    private JPanel panelPrincipal;
    private JButton btnModificar;
    private JButton btnBorrar;
    private JTextField txtUsuario;
    private JPasswordField txtContra;
    private JLabel lblUsuario;
    private JLabel lblContrasena;

    public UsuarioModificarDatosView() {
        super("", true, true, true, true);
        setContentPane(panelPrincipal);
        this.setSize(700, 500);
        actualizarTextos();
    }

    public void actualizarTextos() {
        setTitle(Idioma.get("usuario.modificar.titulo"));
        lblUsuario.setText(Idioma.get("usuario.modificar.lbl.usuario"));
        lblContrasena.setText(Idioma.get("usuario.modificar.lbl.contrasena"));
        btnModificar.setText(Idioma.get("usuario.modificar.btn.modificar"));
        btnBorrar.setText(Idioma.get("usuario.modificar.btn.borrar"));
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

    public void mostrarMensaje(String mensajeKey) {
        JOptionPane.showMessageDialog(this, Idioma.get(mensajeKey), Idioma.get("usuario.modificar.msj.info"), JOptionPane.INFORMATION_MESSAGE);
    }
}