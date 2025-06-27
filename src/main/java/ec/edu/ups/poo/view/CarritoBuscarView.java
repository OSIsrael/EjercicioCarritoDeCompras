package ec.edu.ups.poo.view;

import ec.edu.ups.poo.modelo.Carrito;
import ec.edu.ups.poo.modelo.ItemCarrito;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class CarritoBuscarView extends JInternalFrame {
    private JPanel panelPrincipal;
    private JTextField txtBuscarCarrito;
    private JButton btnBuscarCarrito;
    private JButton btnListar;
    private JTable tblBuscarCarrito;
    private JTable tblDetalleCarrito;

    private DefaultTableModel modeloTablaCarritos;
    private DefaultTableModel modeloTablaDetalle;

    private List<Carrito> ultimoListadoCarritos; // Para referencia rápida en selección

    public CarritoBuscarView() {
        super("Buscar Carrito", true, true, true, true);

        // Inicialización de componentes
        panelPrincipal = new JPanel(new BorderLayout());

        JPanel panelBusqueda = new JPanel();
        panelBusqueda.add(new JLabel("Código de Carrito:"));
        txtBuscarCarrito = new JTextField(8);
        btnBuscarCarrito = new JButton("Buscar");
        btnListar = new JButton("Listar");
        panelBusqueda.add(txtBuscarCarrito);
        panelBusqueda.add(btnBuscarCarrito);
        panelBusqueda.add(btnListar);

        modeloTablaCarritos = new DefaultTableModel(
                new Object[]{"Código", "Usuario", "Fecha", "Total"}, 0
        ) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tblBuscarCarrito = new JTable(modeloTablaCarritos);

        modeloTablaDetalle = new DefaultTableModel(
                new Object[]{"Código Prod.", "Nombre", "Cantidad", "Subtotal"}, 0
        ) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tblDetalleCarrito = new JTable(modeloTablaDetalle);

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                new JScrollPane(tblBuscarCarrito), new JScrollPane(tblDetalleCarrito));
        split.setResizeWeight(0.5);

        panelPrincipal.add(panelBusqueda, BorderLayout.NORTH);
        panelPrincipal.add(split, BorderLayout.CENTER);

        setContentPane(panelPrincipal);
        setSize(700, 500);

        // Listener para seleccionar carrito y mostrar detalles
        tblBuscarCarrito.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tblBuscarCarrito.getSelectedRow() != -1 && ultimoListadoCarritos != null) {
                int fila = tblBuscarCarrito.getSelectedRow();
                if (fila >= 0 && fila < ultimoListadoCarritos.size()) {
                    mostrarDetalleCarrito(ultimoListadoCarritos.get(fila));
                }
            }
        });
    }

    // Muestra todos los carritos en la tabla principal
    public void cargarCarritosUsuario(List<Carrito> carritos) {
        modeloTablaCarritos.setRowCount(0);
        modeloTablaDetalle.setRowCount(0);
        ultimoListadoCarritos = carritos;
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
    }

    // Muestra solo un carrito (por código)
    public void mostrarCarrito(Carrito carrito) {
        modeloTablaCarritos.setRowCount(0);
        modeloTablaDetalle.setRowCount(0);
        if (carrito != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            String nombreUsuario = (carrito.getUsuario() != null) ? carrito.getUsuario().getUsername() : "N/A";
            modeloTablaCarritos.addRow(new Object[]{
                    carrito.getCodigo(),
                    nombreUsuario,
                    sdf.format(carrito.getFechaCreacion().getTime()),
                    String.format("%.2f", carrito.calcularTotal())
            });
            mostrarDetalleCarrito(carrito);
            ultimoListadoCarritos = List.of(carrito); // Para permitir click en tabla única
        } else {
            mostrarMensaje("Carrito no encontrado o no tiene permisos.");
            ultimoListadoCarritos = null;
        }
    }

    // Muestra los ítems de un carrito en la tabla de detalles
    public void mostrarDetalleCarrito(Carrito carrito) {
        modeloTablaDetalle.setRowCount(0);
        if (carrito != null) {
            for (ItemCarrito item : carrito.obtenerItems()) {
                modeloTablaDetalle.addRow(new Object[]{
                        item.getProducto().getCodigo(),
                        item.getProducto().getNombre(),
                        item.getCantidad(),
                        String.format("%.2f", item.getSubtotal())
                });
            }
        }
    }

    public void limpiarVista() {
        txtBuscarCarrito.setText("");
        modeloTablaCarritos.setRowCount(0);
        modeloTablaDetalle.setRowCount(0);
        ultimoListadoCarritos = null;
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje);
    }

    // Getters para el controlador
    public JTextField getTxtBuscarCarrito() { return txtBuscarCarrito; }
    public JButton getBtnBuscarCarrito() { return btnBuscarCarrito; }
    public JButton getBtnListar() { return btnListar; }
    public JTable getTblBuscarCarrito() { return tblBuscarCarrito; }
    public JTable getTblDetalleCarrito() { return tblDetalleCarrito; }
}