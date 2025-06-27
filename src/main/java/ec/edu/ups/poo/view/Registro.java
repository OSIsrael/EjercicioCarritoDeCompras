package ec.edu.ups.poo.view;

import ec.edu.ups.poo.modelo.Pregunta;
import javax.swing.*;
import java.util.List;

public class Registro extends JDialog {
    // ...otros campos...
    private JComboBox<Pregunta> cmbPregunta1;
    private JTextField txtRespuesta1;
    private JComboBox<Pregunta> cmbPregunta2;
    private JTextField txtRespuesta2;

    public Registro(List<Pregunta> preguntasDisponibles) {
        // ...
        cmbPregunta1 = new JComboBox<>(preguntasDisponibles.toArray(new Pregunta[0]));
        txtRespuesta1 = new JTextField();
        cmbPregunta2 = new JComboBox<>(preguntasDisponibles.toArray(new Pregunta[0]));
        txtRespuesta2 = new JTextField();
        // ...agrega al panel, diseña como gustes...
    }

    public Pregunta getPregunta1() { return (Pregunta)cmbPregunta1.getSelectedItem(); }
    public String getRespuesta1() { return txtRespuesta1.getText(); }
    public Pregunta getPregunta2() { return (Pregunta)cmbPregunta2.getSelectedItem(); }
    public String getRespuesta2() { return txtRespuesta2.getText(); }
}