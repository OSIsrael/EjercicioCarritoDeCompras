package ec.edu.ups.poo.view;

import javax.swing.*;

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

    public RegistrarUsuario() {
        setContentPane(panel1);
        setTitle("Registrar Usuario");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);

    }

    // Getters para acceder a los componentes desde el controlador
    public JTextField getTxtUsuarioRe() {
        return txtUsuarioRe;
    }

    public JPasswordField getTxtContraRe() {
        return txtContraRe;
    }

    public JButton getBtnRegistrarse() {
        return btnRegistrarse;
    }

    public JComboBox<Pregunta> getCmbPregunta1() {
        return cmbPregunta1;
    }
    public JTextField getTxtRespuesta1() {
        return txtRespuesta1;
    }
    public JComboBox<Pregunta> getCmbPregunta2() {
        return cmbPregunta2;
    }
    public JTextField getTxtRespuesta2() {
        return txtRespuesta2;
    }



    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    public void limpiarCampos() {
        txtUsuarioRe.setText("");
        txtContraRe.setText("");
        txtRespuesta1.setText("");
        txtRespuesta2.setText("");
        cmbPregunta1.setSelectedIndex(0);
        cmbPregunta2.setSelectedIndex(0);
    }


}