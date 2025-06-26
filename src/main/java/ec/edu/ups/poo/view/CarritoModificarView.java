package ec.edu.ups.poo.view;

import ec.edu.ups.poo.modelo.Carrito;
import ec.edu.ups.poo.modelo.ItemCarrito;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;

public class CarritoModificarView extends JInternalFrame {

    // Variables de instancia que coinciden con el diseño y el .form
    private JTextField txtCodigo; // Mapeado a 'txtCodigo' en el .form
    private JTable tblDetalleCarrito; // Mapeado a 'tblDetalleCarrito' en el .form
    private JButton btnModificar; // Mapeado a 'btnModificar' en el .form
    private JTable tblModificar; // Mapeado a 'tblModificar' en el .form
    private JPanel panelPrincipal; // Asumiendo que es el panel raíz del .form

    private DefaultTableModel modeloTablaItems; // Modelo para tblModificar
    private DefaultTableModel modeloTablaDetalleItem; // Modelo para tblDetalleCarrito

    public CarritoModificarView() {
        super("Modificar Carrito", true, true, true, true);
        setContentPane(panelPrincipal);
        // CORRECCIÓN: Eliminar la línea this.setContentPane(panelPrincipal);
        // El diseñador de GUI se encarga de inicializar panelPrincipal y establecerlo como contentPane.
        this.setSize(700, 500); // Ajusta el tamaño según tu diseño

        // Configuración de tblModificar (ítems del carrito)
        modeloTablaItems = new DefaultTableModel();
        modeloTablaItems.addColumn("Código Prod.");
        modeloTablaItems.addColumn("Nombre");
        modeloTablaItems.addColumn("Cantidad");
        modeloTablaItems.addColumn("Subtotal");
        tblModificar.setModel(modeloTablaItems);

        // Configuración de tblDetalleCarrito (detalle del ítem seleccionado)
        modeloTablaDetalleItem = new DefaultTableModel();
        modeloTablaDetalleItem.addColumn("Propiedad");
        modeloTablaDetalleItem.addColumn("Valor");
        tblDetalleCarrito.setModel(modeloTablaDetalleItem);

        // Listener para seleccionar fila en tblModificar y mostrar detalles en tblDetalleCarrito
        tblModificar.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tblModificar.getSelectedRow() != -1) {
                int selectedRow = tblModificar.getSelectedRow();
                mostrarDetalleItemSeleccionado(selectedRow);
            }
        });
    }

    // Método para cargar los ítems de un carrito en tblModificar
    public void cargarCarrito(Carrito carrito) {
        modeloTablaItems.setRowCount(0); // Limpiar tabla de ítems
        modeloTablaDetalleItem.setRowCount(0); // Limpiar tabla de detalles de ítem

        if (carrito != null) {
            for (ItemCarrito item : carrito.obtenerItems()) {
                modeloTablaItems.addRow(new Object[]{
                        item.getProducto().getCodigo(),
                        item.getProducto().getNombre(),
                        item.getCantidad(),
                        String.format("%.2f", item.getSubtotal())
                });
            }
        } else {
            mostrarMensaje("Carrito no encontrado o no tiene permisos.");
        }
    }

    // Método auxiliar para mostrar detalles del ítem seleccionado
    private void mostrarDetalleItemSeleccionado(int rowIndex) {
        modeloTablaDetalleItem.setRowCount(0); // Limpiar tabla de detalles

        if (rowIndex >= 0 && rowIndex < modeloTablaItems.getRowCount()) {
            // Obtener datos del ítem de la fila seleccionada
            Object codigoProd = modeloTablaItems.getValueAt(rowIndex, 0);
            Object nombreProd = modeloTablaItems.getValueAt(rowIndex, 1);
            Object cantidad = modeloTablaItems.getValueAt(rowIndex, 2);
            Object subtotal = modeloTablaItems.getValueAt(rowIndex, 3);

            modeloTablaDetalleItem.addRow(new Object[]{"Código Producto", codigoProd});
            modeloTablaDetalleItem.addRow(new Object[]{"Nombre", nombreProd});
            modeloTablaDetalleItem.addRow(new Object[]{"Cantidad", cantidad});
            modeloTablaDetalleItem.addRow(new Object[]{"Subtotal", subtotal});
        }
    }

    public void limpiarVista() {
        txtCodigo.setText("");
        modeloTablaItems.setRowCount(0);
        modeloTablaDetalleItem.setRowCount(0);
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // Getters para el controlador
    public JTextField getTxtCodigo() {
        return txtCodigo;
    }

    public JButton getBtnModificar() {
        return btnModificar;
    }

    public JTable getTblModificar() {
        return tblModificar;
    }

    public JTable getTblDetalleCarrito() {
        return tblDetalleCarrito;
    }
}