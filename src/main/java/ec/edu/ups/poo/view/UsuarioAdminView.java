package ec.edu.ups.poo.view;

import ec.edu.ups.poo.modelo.Rol;
import ec.edu.ups.poo.modelo.Usuario;
import ec.edu.ups.poo.util.Idioma;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class UsuarioAdminView extends JInternalFrame {
    private JTable tblUsuarios;
    private DefaultTableModel modeloTabla;
    private JPanel panelPrincipal;
    private JComboBox<Rol> cbxRol;
    private JButton btnActualizar;
    private JButton btnRefrescar;
    private JButton btnEliminar;
    private JLabel lblRol;
    private JLabel lblUsuarioSeleccionado;

    public UsuarioAdminView() {
        super("", true, true, true, true);
        setContentPane(panelPrincipal);

        // Inicializar el modelo de la tabla
        modeloTabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Se deshabilita la edición en la tabla para prevenir inconsistencias y simplificar la lógica.
                return false;
            }
        };
        tblUsuarios.setModel(modeloTabla);

        // Poblar el ComboBox de Rol
        cbxRol.addItem(Rol.ADMINISTRADOR);
        cbxRol.addItem(Rol.USUARIO);

        // Configurar iconos de botones
        btnRefrescar.setIcon(new ImageIcon(getClass().getResource("/icons/actualizar.png")));
        btnActualizar.setIcon(new ImageIcon(getClass().getResource("/icons/actualizar.png")));
        btnEliminar.setIcon(new ImageIcon(getClass().getResource("/icons/eliminar.png")));

        // Configurar selección de la tabla y estado inicial de botones
        configurarSeleccionTabla();

        this.setSize(700, 500);
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        actualizarTextos();
    }

    private void configurarSeleccionTabla() {
        tblUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblUsuarios.getTableHeader().setReorderingAllowed(false);
        tblUsuarios.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tblUsuarios.getSelectedRow() != -1) {
                int selectedRow = tblUsuarios.getSelectedRow();
                String username = (String) modeloTabla.getValueAt(selectedRow, 0);
                Rol currentRol = (Rol) modeloTabla.getValueAt(selectedRow, 1);

                lblUsuarioSeleccionado.setText(Idioma.get("usuario.admin.lbl.seleccionado") + ": " + username);
                cbxRol.setSelectedItem(currentRol);

                btnActualizar.setEnabled(true);
                btnEliminar.setEnabled(true);
            } else {
                lblUsuarioSeleccionado.setText(Idioma.get("usuario.admin.lbl.seleccione"));
                btnActualizar.setEnabled(false);
                btnEliminar.setEnabled(false);
            }
        });

        btnActualizar.setEnabled(false);
        btnEliminar.setEnabled(false);
    }

    public void actualizarTextos() {
        setTitle(Idioma.get("usuario.admin.titulo.ventana"));
        modeloTabla.setColumnIdentifiers(new Object[]{
                Idioma.get("usuario.admin.tbl.col.username"),
                Idioma.get("usuario.admin.tbl.col.rol")
        });
        btnActualizar.setText(Idioma.get("usuario.admin.btn.actualizar"));
        btnEliminar.setText(Idioma.get("usuario.admin.btn.eliminar"));
        btnRefrescar.setText(Idioma.get("usuario.admin.btn.refrescar"));
        lblRol.setText(Idioma.get("usuario.admin.lbl.rol"));
        lblUsuarioSeleccionado.setText(Idioma.get("usuario.admin.lbl.seleccione"));
        tblUsuarios.setToolTipText(Idioma.get("usuario.admin.tbl.tooltip"));
    }

    public void cargarUsuarios(List<Usuario> usuarios) {
        modeloTabla.setRowCount(0);

        for (Usuario usuario : usuarios) {
            Object[] fila = {
                    usuario.getUsername(),
                    usuario.getRol()
            };
            modeloTabla.addRow(fila);
        }

        tblUsuarios.clearSelection();
        lblUsuarioSeleccionado.setText(Idioma.get("usuario.admin.lbl.cargados") + ": " + usuarios.size());
    }

    public void limpiarSeleccion() {
        tblUsuarios.clearSelection();
        lblUsuarioSeleccionado.setText(Idioma.get("usuario.admin.lbl.seleccione"));
        btnActualizar.setEnabled(false);
        btnEliminar.setEnabled(false);
    }

    public JTable getTblUsuarios() {
        return tblUsuarios;
    }

    public DefaultTableModel getModeloTabla() {
        return modeloTabla;
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

    public void mostrarMensaje(String mensajeKey) {
        JOptionPane.showMessageDialog(this, Idioma.get(mensajeKey), Idioma.get("usuario.admin.msj.info"), JOptionPane.INFORMATION_MESSAGE);
    }

    public void mostrarError(String mensajeKey) {
        JOptionPane.showMessageDialog(this, Idioma.get(mensajeKey), Idioma.get("usuario.admin.msj.error"), JOptionPane.ERROR_MESSAGE);
    }

    public boolean confirmarAccion(String mensajeKey, Object... params) {
        String mensaje = java.text.MessageFormat.format(Idioma.get(mensajeKey), params);
        int opcion = JOptionPane.showConfirmDialog(
                this,
                mensaje,
                Idioma.get("usuario.admin.msj.confirmar"),
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );
        return opcion == JOptionPane.YES_OPTION;
    }
}