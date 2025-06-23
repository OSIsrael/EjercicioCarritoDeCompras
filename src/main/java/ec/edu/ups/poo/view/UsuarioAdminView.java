package ec.edu.ups.poo.view;

import ec.edu.ups.poo.modelo.Rol;
import ec.edu.ups.poo.modelo.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class UsuarioAdminView extends JInternalFrame {
    private JTable tblUsuarios;
    private DefaultTableModel modeloTabla;
    private JComboBox<Rol> cbxRol; // Para seleccionar el rol al editar
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JPanel panelPrincipal; // Asumiendo que tienes un panel principal en el .form

    public UsuarioAdminView() {
        super("Gestión de Usuarios", true, true, true, true);
        // Si no usas un diseñador visual, debes crear los componentes aquí
        // Ejemplo: panelPrincipal = new JPanel(); ...
        this.setContentPane(panelPrincipal);
        this.setSize(600, 400);

        // Configurar ComboBox de Roles
        cbxRol = new JComboBox<>(); // Asegúrate que tu .form lo cree
        cbxRol.addItem(Rol.ADMINISTRADOR);
        cbxRol.addItem(Rol.USUARIO);

        // Configurar Tabla
        modeloTabla = new DefaultTableModel();
        modeloTabla.addColumn("Username");
        modeloTabla.addColumn("Rol");
        tblUsuarios = new JTable(modeloTabla); // Asegúrate que tu .form lo cree
        // Es recomendable poner la tabla en un JScrollPane
        // JScrollPane scrollPane = new JScrollPane(tblUsuarios);
        // panelPrincipal.add(scrollPane);

        // Listener para cargar datos del usuario seleccionado en el ComboBox
        tblUsuarios.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tblUsuarios.getSelectedRow() != -1) {
                int selectedRow = tblUsuarios.getSelectedRow();
                Rol currentRol = (Rol) modeloTabla.getValueAt(selectedRow, 1);
                cbxRol.setSelectedItem(currentRol);
            }
        });
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

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }
}