package ec.edu.ups.poo.view;

import javax.swing.*;

import ec.edu.ups.poo.modelo.Genero;
import ec.edu.ups.poo.modelo.Pregunta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegistrarUsuario extends JFrame {
    private JPanel panel1;
    private JPanel panelPrincipal;
    private JTextField txtUsuarioRe;
    private JPasswordField txtContraRe;
    private JButton btnRegistrarse;
    private JComboBox<Pregunta> cbxPregunta1;
    private JTextField txtRespuesta1;
    private JTextField txtNombre;
    private JTextField txtApellido;
    private JTextField txtTelefono;
    private JTextField txtEdad;
    private JComboBox<Genero> cbxGenero;

    private JButton btnAgregarPregunta;
    private JLabel lblPreguntasContador;
    private final Map<Pregunta, String> preguntasRespondidas;
    private List<Pregunta> todasLasPreguntas;



    public RegistrarUsuario() {
        this.preguntasRespondidas=new HashMap<>();
        this.todasLasPreguntas=new ArrayList<>();

        setContentPane(panel1);
        setTitle("Registrar Usuario");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 900);
        setLocationRelativeTo(null);

        if (cbxGenero == null) {
            cbxGenero = new JComboBox<>();
        }
        cbxGenero.removeAllItems();
        cbxGenero.addItem(Genero.MASCULINO);
        cbxGenero.addItem(Genero.FEMININO);
        cbxGenero.addItem(Genero.OTROS);
        btnRegistrarse.setIcon(new ImageIcon(getClass().getResource("/icons/entrar.png")));
        btnAgregarPregunta.setIcon(new ImageIcon(getClass().getResource("/icons/anadir.png")));
        btnAgregarPregunta.addActionListener(e -> agregarPreguntaRespondida());

    }
    public void setPreguntasDisponibles(List<Pregunta> preguntas) {
        this.todasLasPreguntas = new ArrayList<>(preguntas);
        actualizarComboBox();
    }
    private void actualizarComboBox() {
        if (cbxPregunta1 == null) return;
        cbxPregunta1.removeAllItems();
        for (Pregunta p : todasLasPreguntas) {
            // Solo añade las preguntas que aún no han sido respondidas
            if (!preguntasRespondidas.containsKey(p)) {
                cbxPregunta1.addItem(p);
            }
        }
    }
    private void agregarPreguntaRespondida() {
        Pregunta preguntaSeleccionada = (Pregunta) cbxPregunta1.getSelectedItem();
        String respuesta = txtRespuesta1.getText().trim();

        // Validaciones
        if (preguntaSeleccionada == null) {
            mostrarMensaje("Por favor, seleccione una pregunta.");
            return;
        }
        if (respuesta.isEmpty()) {
            mostrarMensaje("Por favor, ingrese una respuesta.");
            return;
        }
        preguntasRespondidas.put(preguntaSeleccionada, respuesta);

        // Actualizar UI
        actualizarComboBox(); // Esto quitará la pregunta recién respondida de la lista
        txtRespuesta1.setText(""); // Limpiar para la siguiente
        lblPreguntasContador.setText("Preguntas respondidas: " + preguntasRespondidas.size());

        mostrarMensaje("Pregunta añadida. Faltan " + (3 - preguntasRespondidas.size()) + " para poder registrarse.");
    }
    public Map<Pregunta, String> getPreguntasYRespuestas() {
        return this.preguntasRespondidas;
    }



    public JTextField getTxtNombre() {
        return txtNombre;
    }

    public void setTxtNombre(JTextField txtNombre) {
        this.txtNombre = txtNombre;
    }

    public JTextField getTxtApellido() {
        return txtApellido;
    }

    public void setTxtApellido(JTextField txtApellido) {
        this.txtApellido = txtApellido;
    }

    public JTextField getTxtTelefono() {
        return txtTelefono;
    }

    public void setTxtTelefono(JTextField txtTelefono) {
        this.txtTelefono = txtTelefono;
    }

    public JTextField getTxtEdad() {
        return txtEdad;
    }

    public void setTxtEdad(JTextField txtEdad) {
        this.txtEdad = txtEdad;
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




    public JTextField getTxtRespuesta1() {
        return txtRespuesta1;
    }

    public void setTxtRespuesta1(JTextField txtRespuesta1) {
        this.txtRespuesta1 = txtRespuesta1;
    }



    public JComboBox getCbxGenero() {
        return cbxGenero;
    }

    public JButton getBtnAgregarPregunta() {
        return btnAgregarPregunta;
    }

    public void setCbxGenero(JComboBox<Genero> cbxGenero) {
        this.cbxGenero = cbxGenero;
    }


    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }


    public void limpiarCampos() {
        txtUsuarioRe.setText("");
        txtContraRe.setText("");
        txtNombre.setText("");
        txtApellido.setText("");
        txtTelefono.setText("");
        txtEdad.setText("");
        txtRespuesta1.setText("");
        preguntasRespondidas.clear();
        actualizarComboBox();
        lblPreguntasContador.setText("Preguntas respondidas: 0");
        if (cbxPregunta1 != null && cbxPregunta1.getItemCount() > 0) cbxPregunta1.setSelectedIndex(0);
        if (cbxGenero != null && cbxGenero.getItemCount() > 0) cbxGenero.setSelectedIndex(0);
    }



}