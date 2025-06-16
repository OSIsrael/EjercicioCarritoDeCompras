package ec.edu.ups.poo.view;

import ec.edu.ups.poo.modelo.Producto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class ProductoEliminar extends JFrame {
    private JPanel panelEliminarProducto;
    private JLabel lblNombre;
    private JTextField txtNombre;
    private JButton LISTARButton;
    private JButton ELIMINARButton;
    private JTable tblEliminarProducto;
    private DefaultTableModel modelo;

    public ProductoEliminar() {
        setContentPane(panelEliminarProducto);
        setTitle("ELIMINAR PRODUCTOS");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);
        setVisible(true);

        // Inicializar el modelo de la tabla
        modelo = new DefaultTableModel();
        Object[] columnas = {"CÓDIGO", "NOMBRE", "PRECIO"};
        modelo.setColumnIdentifiers(columnas);
        tblEliminarProducto.setModel(modelo);

        // Configurar selección de fila para mostrar información
        tblEliminarProducto.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int filaSeleccionada = tblEliminarProducto.getSelectedRow();
                if (filaSeleccionada >= 0) {
                    String nombre = (String) tblEliminarProducto.getValueAt(filaSeleccionada, 1);
                    txtNombre.setText(nombre);
                }
            }
        });
    }

    public JPanel getPanelEliminarProducto() {
        return panelEliminarProducto;
    }

    public JLabel getLblNombre() {
        return lblNombre;
    }

    public JTextField getTxtNombre() {
        return txtNombre;
    }

    public JButton getBtnListar() {
        return LISTARButton;
    }

    public JButton getBtnEliminar() {
        return ELIMINARButton;
    }

    public JTable getTblEliminarProducto() {
        return tblEliminarProducto;
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
        tblEliminarProducto.clearSelection();
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }
}