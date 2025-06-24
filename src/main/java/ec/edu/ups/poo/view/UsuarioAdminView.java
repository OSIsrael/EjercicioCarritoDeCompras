package ec.edu.ups.poo.view;

import ec.edu.ups.poo.modelo.Rol;
import ec.edu.ups.poo.modelo.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class UsuarioAdminView extends JInternalFrame {
    private JTable tblUsuarios;
    private DefaultTableModel modeloTabla;
    private JComboBox<Rol> cbxRol;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JButton btnRefrescar;
    private JPanel panelPrincipal;
    private JLabel lblRol;
    private JLabel lblUsuarioSeleccionado;

    public UsuarioAdminView() {
        super("Gestión de Usuarios", true, true, true, true);
        inicializarComponentes();
        configurarLayout();
        configurarEventos();
        this.setSize(700, 500);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    }

    private void inicializarComponentes() {
        // Panel principal
        panelPrincipal = new JPanel(new BorderLayout());

        // Configurar tabla
        modeloTabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer la tabla no editable
            }
        };
        modeloTabla.addColumn("Username");
        modeloTabla.addColumn("Rol");

        tblUsuarios = new JTable(modeloTabla);
        tblUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblUsuarios.getTableHeader().setReorderingAllowed(false);

        // ComboBox para roles
        cbxRol = new JComboBox<>();
        cbxRol.addItem(Rol.ADMINISTRADOR);
        cbxRol.addItem(Rol.USUARIO);

        // Botones
        btnActualizar = new JButton("Actualizar Rol");
        btnEliminar = new JButton("Eliminar Usuario");
        btnRefrescar = new JButton("Refrescar Lista");

        // Labels
        lblRol = new JLabel("Nuevo Rol:");
        lblUsuarioSeleccionado = new JLabel("Seleccione un usuario de la tabla");
        lblUsuarioSeleccionado.setFont(new Font("Arial", Font.ITALIC, 12));
        lblUsuarioSeleccionado.setForeground(Color.GRAY);
    }

    private void configurarLayout() {
        // Panel superior con información
        JPanel panelInfo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelInfo.add(new JLabel("Gestión de Usuarios del Sistema"));
        panelInfo.setBorder(BorderFactory.createEtchedBorder());

        // Panel central con la tabla
        JScrollPane scrollPane = new JScrollPane(tblUsuarios);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Lista de Usuarios"));

        // Panel inferior con controles
        JPanel panelControles = new JPanel(new BorderLayout());

        // Subpanel para información del usuario seleccionado
        JPanel panelSeleccion = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelSeleccion.add(lblUsuarioSeleccionado);

        // Subpanel para cambio de rol
        JPanel panelRol = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelRol.add(lblRol);
        panelRol.add(cbxRol);
        panelRol.add(btnActualizar);

        // Subpanel para botones de acción
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.add(btnRefrescar);
        panelBotones.add(btnEliminar);

        // Organizar panel de controles
        panelControles.add(panelSeleccion, BorderLayout.NORTH);
        panelControles.add(panelRol, BorderLayout.CENTER);
        panelControles.add(panelBotones, BorderLayout.SOUTH);
        panelControles.setBorder(BorderFactory.createTitledBorder("Acciones"));

        // Agregar todo al panel principal
        panelPrincipal.add(panelInfo, BorderLayout.NORTH);
        panelPrincipal.add(scrollPane, BorderLayout.CENTER);
        panelPrincipal.add(panelControles, BorderLayout.SOUTH);

        this.setContentPane(panelPrincipal);
    }

    private void configurarEventos() {
        // Listener para selección en la tabla
        tblUsuarios.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tblUsuarios.getSelectedRow() != -1) {
                int selectedRow = tblUsuarios.getSelectedRow();
                String username = (String) modeloTabla.getValueAt(selectedRow, 0);
                Rol currentRol = (Rol) modeloTabla.getValueAt(selectedRow, 1);

                // Actualizar información mostrada
                lblUsuarioSeleccionado.setText("Usuario seleccionado: " + username);
                cbxRol.setSelectedItem(currentRol);

                // Habilitar botones
                btnActualizar.setEnabled(true);
                btnEliminar.setEnabled(true);
            } else {
                // Deshabilitar botones si no hay selección
                lblUsuarioSeleccionado.setText("Seleccione un usuario de la tabla");
                btnActualizar.setEnabled(false);
                btnEliminar.setEnabled(false);
            }
        });

        // Inicialmente deshabilitar botones
        btnActualizar.setEnabled(false);
        btnEliminar.setEnabled(false);
    }

    public void cargarUsuarios(List<Usuario> usuarios) {
        modeloTabla.setRowCount(0); // Limpiar tabla

        for (Usuario usuario : usuarios) {
            Object[] fila = {
                    usuario.getUsername(),
                    usuario.getRol()
            };
            modeloTabla.addRow(fila);
        }

        // Limpiar selección
        tblUsuarios.clearSelection();
        lblUsuarioSeleccionado.setText("Usuarios cargados: " + usuarios.size());
    }

    public void limpiarSeleccion() {
        tblUsuarios.clearSelection();
        lblUsuarioSeleccionado.setText("Seleccione un usuario de la tabla");
        btnActualizar.setEnabled(false);
        btnEliminar.setEnabled(false);
    }

    // Getters para el controlador
    public JTable getTblUsuarios() {
        return tblUsuarios;
    }

    public JComboBox<Rol> getCbxRol() {
        return cbxRol;
    }

    public JButton getBtnActualizar() {
        return btnActualizar;
    }

    public JButton getBtnEliminar() {
        return btnEliminar;
    }

    public JButton getBtnRefrescar() {
        return btnRefrescar;
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public boolean confirmarAccion(String mensaje) {
        int opcion = JOptionPane.showConfirmDialog(
                this,
                mensaje,
                "Confirmar Acción",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );
        return opcion == JOptionPane.YES_OPTION;
    }
}