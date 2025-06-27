package ec.edu.ups.poo.view;

import ec.edu.ups.poo.modelo.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class UsuarioBuscarView extends JInternalFrame {
    private JPanel panelPrincipal;
    private JTextField txtUsername;
    private JButton btnBuscar;
    private JButton btnListar;
    private JTable tblUsuarios;
    private JLabel lblUsuario;
    private DefaultTableModel modeloTablaUsuarios;

    private List<Usuario> ultimoListadoUsuarios;

    public UsuarioBuscarView()  {
        super("Buscar Usuarios", true, true, true, true);

        // Inicialización manual de componentes
        panelPrincipal = new JPanel(new BorderLayout());
        JPanel panelSuperior = new JPanel();
        panelSuperior.add(new JLabel("Usuario:"));
        txtUsername = new JTextField(12);
        btnBuscar = new JButton("Buscar");
        btnListar = new JButton("Listar");
        panelSuperior.add(txtUsername);
        panelSuperior.add(btnBuscar);
        panelSuperior.add(btnListar);

        modeloTablaUsuarios = new DefaultTableModel(
                new Object[]{"Username", "Rol"}, 0
        ) {
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tblUsuarios = new JTable(modeloTablaUsuarios);

        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);
        panelPrincipal.add(new JScrollPane(tblUsuarios), BorderLayout.CENTER);

        setContentPane(panelPrincipal);
        setSize(700, 500);
    }

    // Mostrar todos los usuarios
    public void cargarUsuarios(List<Usuario> usuarios) {
        modeloTablaUsuarios.setRowCount(0);
        ultimoListadoUsuarios = usuarios;
        if (usuarios != null) {
            for (Usuario u : usuarios) {
                modeloTablaUsuarios.addRow(new Object[]{u.getUsername(), u.getRol()});
            }
        }
    }

    // Mostrar solo un usuario encontrado
    public void mostrarUsuario(Usuario usuario) {
        modeloTablaUsuarios.setRowCount(0);
        if (usuario != null) {
            modeloTablaUsuarios.addRow(new Object[]{usuario.getUsername(), usuario.getRol()});
        } else {
            mostrarMensaje("Usuario no encontrado.");
        }
    }

    public void limpiarVista() {
        txtUsername.setText("");
        modeloTablaUsuarios.setRowCount(0);
        ultimoListadoUsuarios = null;
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    // Getters
    public JTextField getTxtUsername() { return txtUsername; }
    public JButton getBtnBuscar() { return btnBuscar; }
    public JButton getBtnListar() { return btnListar; }
    public JTable getTblUsuarios() { return tblUsuarios; }
}