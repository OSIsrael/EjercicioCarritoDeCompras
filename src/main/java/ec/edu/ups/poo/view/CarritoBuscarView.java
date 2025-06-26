package ec.edu.ups.poo.view;

import ec.edu.ups.poo.modelo.Carrito;
import ec.edu.ups.poo.modelo.ItemCarrito;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;

public class CarritoBuscarView extends JInternalFrame {
    // Variables de instancia que coinciden con el diseño
    private JPanel panelPrincipal; // Asumiendo que es el panel raíz del .form
    private JTextField txtBuscarCarrito;
    private JButton btnBuscarCarrito;
    private JTable tblBuscarCarrito; // Para los ítems del carrito
    private JTable tblDetalleCarrito; // Para los detalles del ítem seleccionado

    private DefaultTableModel modeloTablaCarritos; // Para tblBuscarCarrito (ítems)
    private DefaultTableModel modeloTablaDetalle; // Para tblDetalleCarrito (detalle de ítem)

    public CarritoBuscarView() {
        super("Buscar Carrito", true, true, true, true);
        // CORRECCIÓN: Eliminar la línea this.setContentPane(panelPrincipal);
        // El diseñador de GUI se encarga de inicializar panelPrincipal y establecerlo como contentPane.
        this.setSize(700, 500); // Ajusta el tamaño según tu diseño

        // Configuración de tblBuscarCarrito (ítems del carrito)
        modeloTablaCarritos = new DefaultTableModel();
        modeloTablaCarritos.addColumn("Código Prod.");
        modeloTablaCarritos.addColumn("Nombre");
        modeloTablaCarritos.addColumn("Cantidad");
        modeloTablaCarritos.addColumn("Subtotal");
        tblBuscarCarrito.setModel(modeloTablaCarritos);

        // Configuración de tblDetalleCarrito (detalle del ítem seleccionado)
        modeloTablaDetalle = new DefaultTableModel();
        modeloTablaDetalle.addColumn("Propiedad");
        modeloTablaDetalle.addColumn("Valor");
        tblDetalleCarrito.setModel(modeloTablaDetalle);

        // Listener para seleccionar fila en tblBuscarCarrito y mostrar detalles en tblDetalleCarrito
        tblBuscarCarrito.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tblBuscarCarrito.getSelectedRow() != -1) {
                int selectedRow = tblBuscarCarrito.getSelectedRow();
                mostrarDetalleItemSeleccionado(selectedRow);
            }
        });
    }

    // Método para cargar los ítems de un carrito en tblBuscarCarrito
    public void mostrarCarrito(Carrito carrito) {
        modeloTablaCarritos.setRowCount(0); // Limpiar tabla de ítems
        modeloTablaDetalle.setRowCount(0); // Limpiar tabla de detalles de ítem

        if (carrito != null) {
            for (ItemCarrito item : carrito.obtenerItems()) {
                modeloTablaCarritos.addRow(new Object[]{
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
        modeloTablaDetalle.setRowCount(0); // Limpiar tabla de detalles

        if (rowIndex >= 0 && rowIndex < modeloTablaCarritos.getRowCount()) {
            Object codigoProd = modeloTablaCarritos.getValueAt(rowIndex, 0);
            Object nombreProd = modeloTablaCarritos.getValueAt(rowIndex, 1);
            Object cantidad = modeloTablaCarritos.getValueAt(rowIndex, 2);
            Object subtotal = modeloTablaCarritos.getValueAt(rowIndex, 3);

            modeloTablaDetalle.addRow(new Object[]{"Código Producto", codigoProd});
            modeloTablaDetalle.addRow(new Object[]{"Nombre", nombreProd});
            modeloTablaDetalle.addRow(new Object[]{"Cantidad", cantidad});
            modeloTablaDetalle.addRow(new Object[]{"Subtotal", subtotal});
        }
    }

    public void limpiarVista() {
        txtBuscarCarrito.setText("");
        modeloTablaCarritos.setRowCount(0);
        modeloTablaDetalle.setRowCount(0);
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    // Getters para el controlador
    public JTextField getTxtBuscarCarrito() {
        return txtBuscarCarrito;
    }

    public JButton getBtnBuscarCarrito() {
        return btnBuscarCarrito;
    }

    public JTable getTblBuscarCarrito() {
        return tblBuscarCarrito;
    }

    public JTable getTblDetalleCarrito() {
        return tblDetalleCarrito;
    }
}