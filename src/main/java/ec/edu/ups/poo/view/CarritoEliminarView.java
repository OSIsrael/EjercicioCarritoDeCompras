package ec.edu.ups.poo.view;

import ec.edu.ups.poo.modelo.Carrito;
import ec.edu.ups.poo.modelo.ItemCarrito;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;

public class CarritoEliminarView extends JInternalFrame {
    // Variables de instancia que coinciden con el diseño
    private JPanel panelPrincipal; // Asumiendo que es el panel raíz del .form
    private JTextField txtEliminarCarrito;
    private JButton btnEliminar;
    private JButton btnBuscar;
    private JTable tblEliminarCarrito; // Para el resumen del carrito a eliminar
    private JTable tblMostrarDetalleCarrito; // Para los ítems del carrito a eliminar

    private DefaultTableModel modeloTablaCarritos; // Para tblEliminarCarrito
    private DefaultTableModel modeloTablaDetalle; // Para tblMostrarDetalleCarrito

    // Variable para mantener el carrito seleccionado para eliminar
    private Carrito carritoSeleccionado;

    public CarritoEliminarView() {
        super("Eliminar Carrito", true, true, true, true);
        setContentPane(panelPrincipal);
        // CORRECCIÓN: Eliminar la línea this.setContentPane(panelPrincipal);
        // El diseñador de GUI se encarga de inicializar panelPrincipal y establecerlo como contentPane.
        this.setSize(700, 500); // Ajusta el tamaño según tu diseño

        // Configuración de tblEliminarCarrito (resumen del carrito)
        modeloTablaCarritos = new DefaultTableModel();
        modeloTablaCarritos.addColumn("Código");
        modeloTablaCarritos.addColumn("Usuario");
        modeloTablaCarritos.addColumn("Fecha");
        modeloTablaCarritos.addColumn("Total");
        tblEliminarCarrito.setModel(modeloTablaCarritos);

        // Configuración de tblMostrarDetalleCarrito (ítems del carrito)
        modeloTablaDetalle = new DefaultTableModel();
        modeloTablaDetalle.addColumn("Código Prod.");
        modeloTablaDetalle.addColumn("Nombre");
        modeloTablaDetalle.addColumn("Cantidad");
        modeloTablaDetalle.addColumn("Subtotal");
        tblMostrarDetalleCarrito.setModel(modeloTablaDetalle);

        // Deshabilitar el botón de eliminar hasta que se encuentre un carrito
        btnEliminar.setEnabled(false);
    }

    // Método para mostrar el carrito a eliminar
    public void mostrarCarrito(Carrito carrito) {
        modeloTablaCarritos.setRowCount(0); // Limpiar tabla de resumen
        modeloTablaDetalle.setRowCount(0); // Limpiar tabla de ítems
        btnEliminar.setEnabled(false); // Deshabilitar eliminar por defecto

        if (carrito != null) {
            this.carritoSeleccionado = carrito;
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            String nombreUsuario = (carrito.getUsuario() != null) ? carrito.getUsuario().getUsername() : "N/A";

            // Cargar resumen en tblEliminarCarrito
            modeloTablaCarritos.addRow(new Object[]{
                    carrito.getCodigo(),
                    nombreUsuario,
                    sdf.format(carrito.getFechaCreacion()),
                    String.format("%.2f", carrito.calcularTotal())
            });

            // Cargar ítems en tblMostrarDetalleCarrito
            for (ItemCarrito item : carrito.obtenerItems()) {
                modeloTablaDetalle.addRow(new Object[]{
                        item.getProducto().getCodigo(),
                        item.getProducto().getNombre(),
                        item.getCantidad(),
                        String.format("%.2f", item.getSubtotal())
                });
            }
            btnEliminar.setEnabled(true); // Habilitar eliminar
        } else {
            this.carritoSeleccionado = null;
            mostrarMensaje("Carrito no encontrado o no tiene permisos.");
        }
    }

    public void limpiarVista() {
        txtEliminarCarrito.setText("");
        modeloTablaCarritos.setRowCount(0);
        modeloTablaDetalle.setRowCount(0);
        btnEliminar.setEnabled(false);
        this.carritoSeleccionado = null;
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    public boolean confirmarAccion(String mensaje) {
        int respuesta = JOptionPane.showConfirmDialog(this, mensaje, "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        return respuesta == JOptionPane.YES_OPTION;
    }

    // Getters para el controlador
    public JTextField getTxtEliminarCarrito() {
        return txtEliminarCarrito;
    }

    public JButton getBtnEliminar() {
        return btnEliminar;
    }

    public JButton getBtnBuscar() {
        return btnBuscar;
    }

    public Carrito getCarritoSeleccionado() {
        return carritoSeleccionado;
    }
}