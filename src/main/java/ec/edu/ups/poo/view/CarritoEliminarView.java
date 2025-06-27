package ec.edu.ups.poo.view;

import ec.edu.ups.poo.modelo.Carrito;
import ec.edu.ups.poo.modelo.ItemCarrito;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.text.SimpleDateFormat;
import java.util.List;

public class CarritoEliminarView extends JInternalFrame {
    // Variables de instancia que coinciden con el diseño
    private JPanel panelPrincipal;
    private JButton btnEliminar;
    private JButton btnListar;
    private JTable tblEliminarCarrito;
    private JTable tblMostrarDetalleCarrito;

    private DefaultTableModel modeloTablaCarritos;
    private DefaultTableModel modeloTablaDetalle;

    private Carrito carritoSeleccionado;
    private List<Carrito> ultimoListado; // Mantener referencia para selección

    public CarritoEliminarView() {
        super("Eliminar Carrito", true, true, true, true);
        setContentPane(panelPrincipal);
        this.setSize(700, 500);

        // Modelo para la tabla de carritos
        modeloTablaCarritos = new DefaultTableModel(
                new Object[]{"Código", "Usuario", "Fecha", "Total"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblEliminarCarrito.setModel(modeloTablaCarritos);

        // Modelo para la tabla de detalles
        modeloTablaDetalle = new DefaultTableModel(
                new Object[]{"Código Prod.", "Nombre", "Cantidad", "Subtotal"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblMostrarDetalleCarrito.setModel(modeloTablaDetalle);

        btnEliminar.setEnabled(false);

        // Listener: al seleccionar un carrito, muestra detalles y habilita Eliminar
        tblEliminarCarrito.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tblEliminarCarrito.getSelectedRow() != -1 && ultimoListado != null) {
                int fila = tblEliminarCarrito.getSelectedRow();
                if (fila >= 0 && fila < ultimoListado.size()) {
                    mostrarCarrito(ultimoListado.get(fila));
                }
            }
        });
    }

    // Mostrar TODOS los carritos en la tabla principal
    public void cargarCarritosUsuario(List<Carrito> carritos) {
        modeloTablaCarritos.setRowCount(0);
        modeloTablaDetalle.setRowCount(0);
        ultimoListado = carritos;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        for (Carrito carrito : carritos) {
            String nombreUsuario = (carrito.getUsuario() != null) ? carrito.getUsuario().getUsername() : "N/A";
            modeloTablaCarritos.addRow(new Object[]{
                    carrito.getCodigo(),
                    nombreUsuario,
                    sdf.format(carrito.getFechaCreacion().getTime()),
                    String.format("%.2f", carrito.calcularTotal())
            });
        }
        btnEliminar.setEnabled(false);
        carritoSeleccionado = null;
    }

    // Mostrar detalle de carrito seleccionado
    public void mostrarCarrito(Carrito carrito) {
        modeloTablaDetalle.setRowCount(0);
        carritoSeleccionado = null;
        if (carrito != null) {
            for (ItemCarrito item : carrito.obtenerItems()) {
                modeloTablaDetalle.addRow(new Object[]{
                        item.getProducto().getCodigo(),
                        item.getProducto().getNombre(),
                        item.getCantidad(),
                        String.format("%.2f", item.getSubtotal())
                });
            }
            btnEliminar.setEnabled(true);
            carritoSeleccionado = carrito;
        } else {
            btnEliminar.setEnabled(false);
        }
    }

    public void limpiarVista() {
        modeloTablaCarritos.setRowCount(0);
        modeloTablaDetalle.setRowCount(0);
        btnEliminar.setEnabled(false);
        carritoSeleccionado = null;
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    public boolean confirmarAccion(String mensaje) {
        int respuesta = JOptionPane.showConfirmDialog(this, mensaje, "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        return respuesta == JOptionPane.YES_OPTION;
    }

    // Getters
    public JButton getBtnEliminar() {
        return btnEliminar;
    }

    public JButton getBtnListar() {
        return btnListar;
    }

    public Carrito getCarritoSeleccionado() {
        return carritoSeleccionado;
    }
}