package ec.edu.ups.poo.view;

import javax.swing.*;

import ec.edu.ups.poo.modelo.Pregunta;

public class RegistrarUsuario extends JFrame {
    private JPanel panel1;
    private JPanel panelPrincipal;
    private JTextField txtUsuarioRe;
    private JPasswordField txtContraRe;
    private JButton btnRegistrarse;
    private JComboBox<Pregunta> cbxPregunta1;
    private JComboBox<Pregunta> cbxPregunta2;
    private JTextField txtRespuesta1;
    private JTextField txtRespuesta2;



    public RegistrarUsuario() {
        setContentPane(panel1);
        setTitle("Registrar Usuario");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);

    }

    public JPanel getPanel1() {
        return panel1;
    }

    public void setPanel1(JPanel panel1) {
        this.panel1 = panel1;
    }

    public JPanel getPanelPrincipal() {
        return panelPrincipal;
    }

    public void setPanelPrincipal(JPanel panelPrincipal) {
        this.panelPrincipal = panelPrincipal;
    }

    public JTextField getTxtUsuarioRe() {
        return txtUsuarioRe;
    }

    public void setTxtUsuarioRe(JTextField txtUsuarioRe) {
        this.txtUsuarioRe = txtUsuarioRe;
    }

    public JPasswordField getTxtContraRe() {
        return txtContraRe;
    }

    public void setTxtContraRe(JPasswordField txtContraRe) {
        this.txtContraRe = txtContraRe;
    }

    public JButton getBtnRegistrarse() {
        return btnRegistrarse;
    }

    public void setBtnRegistrarse(JButton btnRegistrarse) {
        this.btnRegistrarse = btnRegistrarse;
    }

    public JComboBox<Pregunta> getCbxPregunta1() {
        return cbxPregunta1;
    }

    public void setCbxPregunta1(JComboBox<Pregunta> cbxPregunta1) {
        this.cbxPregunta1 = cbxPregunta1;
    }

    public JComboBox<Pregunta> getCbxPregunta2() {
        return cbxPregunta2;
    }

    public void setCbxPregunta2(JComboBox<Pregunta> cbxPregunta2) {
        this.cbxPregunta2 = cbxPregunta2;
    }

    public JTextField getTxtRespuesta1() {
        return txtRespuesta1;
    }

    public void setTxtRespuesta1(JTextField txtRespuesta1) {
        this.txtRespuesta1 = txtRespuesta1;
    }

    public JTextField getTxtRespuesta2() {
        return txtRespuesta2;
    }

    public void setTxtRespuesta2(JTextField txtRespuesta2) {
        this.txtRespuesta2 = txtRespuesta2;
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    public void limpiarCampos() {
        txtUsuarioRe.setText("");
        txtContraRe.setText("");
        txtRespuesta1.setText("");
        txtRespuesta2.setText("");
        if (cbxPregunta1.getItemCount() > 0) cbxPregunta1.setSelectedIndex(0);
        if (cbxPregunta2.getItemCount() > 0) cbxPregunta2.setSelectedIndex(0);
    }


}