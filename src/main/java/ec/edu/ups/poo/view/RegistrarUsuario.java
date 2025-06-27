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

        // Inicializar combos de preguntas y campos de respuesta
        cmbPregunta1 = new JComboBox<>(preguntasDisponibles.toArray(new Pregunta[0]));
        txtRespuesta1 = new JTextField();
        cmbPregunta2 = new JComboBox<>(preguntasDisponibles.toArray(new Pregunta[0]));
        txtRespuesta2 = new JTextField();

        // Agrega los campos al panelPrincipal o panel1 según tu layout
        panelPrincipal.add(new JLabel("Pregunta de seguridad 1:"));
        panelPrincipal.add(cmbPregunta1);
        panelPrincipal.add(new JLabel("Respuesta 1:"));
        panelPrincipal.add(txtRespuesta1);

        panelPrincipal.add(new JLabel("Pregunta de seguridad 2:"));
        panelPrincipal.add(cmbPregunta2);
        panelPrincipal.add(new JLabel("Respuesta 2:"));
        panelPrincipal.add(txtRespuesta2);
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

    public Pregunta getPregunta1() { return (Pregunta) cmbPregunta1.getSelectedItem(); }
    public String getRespuesta1() { return txtRespuesta1.getText(); }
    public Pregunta getPregunta2() { return (Pregunta) cmbPregunta2.getSelectedItem(); }
    public String getRespuesta2() { return txtRespuesta2.getText(); }

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