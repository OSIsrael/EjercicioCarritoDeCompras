package ec.edu.ups.poo.view;

import ec.edu.ups.poo.modelo.Producto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ProductoEditar extends JInternalFrame {
    private JPanel panelEditarProducto;
    private JLabel lblNombre;
    private JTextField txtNombre;
    private JButton btnListar1;
    private JButton btnEditar;
    private JTable tblEditarProducto;
    private DefaultTableModel modelo;

    public ProductoEditar() {
        setContentPane(panelEditarProducto);
        setTitle("EDITAR PRODUCTOS");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 500);
        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        setMaximizable(true);
        //setVisible(true);

        modelo = new DefaultTableModel();
        Object[] columnas = {"CÓDIGO", "NOMBRE", "PRECIO"};
        modelo.setColumnIdentifiers(columnas);
        tblEditarProducto.setModel(modelo);


        tblEditarProducto.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int filaSeleccionada = tblEditarProducto.getSelectedRow();
                if (filaSeleccionada >= 0) {
                    String nombre = (String) tblEditarProducto.getValueAt(filaSeleccionada, 1);
                    txtNombre.setText(nombre);
                }
            }
        });
    }

    public JPanel getPanelEditarProducto() {
        return panelEditarProducto;
    }

    public JLabel getLblNombre() {
        return lblNombre;
    }

    public JTextField getTxtNombre() {
        return txtNombre;
    }

    public JButton getBtnListar1() {
        return btnListar1;
    }

    public JButton getBtnEditar() {
        return btnEditar;
    }

    public JTable getTblEditarProducto() {
        return tblEditarProducto;
    }

    public void cargarDatos(List<Producto> productos) {
        modelo.setNumRows(0);
        for (Producto producto : productos) {
            Object[] filaProducto = {producto.getCodigo(), producto.getNombre(), producto.getPrecio()};
            modelo.addRow(filaProducto);
        }
    }

    public void limpiarCampos() {
        txtNombre.setText("");
        tblEditarProducto.clearSelection();
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }
}