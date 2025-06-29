package ec.edu.ups.poo.view;

import javax.swing.*;
import java.util.List;
import ec.edu.ups.poo.modelo.Pregunta;

public class RegistrarUsuario extends JFrame {
    private JPanel panel1;
    private JPanel panelPrincipal;
    private JTextField txtUsuarioRe;
    private JPasswordField txtContraRe;
    private JButton btnRegistrarse;
    private JComboBox cbxPregunta1;
    private JComboBox cbxPregunta2;

    // Nuevos campos para preguntas de seguridad
    private JComboBox<Pregunta> cmbPregunta1;
    private JTextField txtRespuesta1;
    private JComboBox<Pregunta> cmbPregunta2;
    private JTextField txtRespuesta2;

    public RegistrarUsuario(List<Pregunta> preguntasDisponibles) {
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

    public JComboBox getCbxPregunta1() {
        return cbxPregunta1;
    }

    public void setCbxPregunta1(JComboBox cbxPregunta1) {
        this.cbxPregunta1 = cbxPregunta1;
    }

    public JComboBox getCbxPregunta2() {
        return cbxPregunta2;
    }

    public void setCbxPregunta2(JComboBox cbxPregunta2) {
        this.cbxPregunta2 = cbxPregunta2;
    }

    public JComboBox<Pregunta> getCmbPregunta1() {
        return cmbPregunta1;
    }

    public void setCmbPregunta1(JComboBox<Pregunta> cmbPregunta1) {
        this.cmbPregunta1 = cmbPregunta1;
    }

    public JTextField getTxtRespuesta1() {
        return txtRespuesta1;
    }

    public void setTxtRespuesta1(JTextField txtRespuesta1) {
        this.txtRespuesta1 = txtRespuesta1;
    }

    public JComboBox<Pregunta> getCmbPregunta2() {
        return cmbPregunta2;
    }

    public void setCmbPregunta2(JComboBox<Pregunta> cmbPregunta2) {
        this.cmbPregunta2 = cmbPregunta2;
    }

    public JTextField getTxtRespuesta2() {
        return txtRespuesta2;
    }

    public void setTxtRespuesta2(JTextField txtRespuesta2) {
        this.txtRespuesta2 = txtRespuesta2;
    }
}