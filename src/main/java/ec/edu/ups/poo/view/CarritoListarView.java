package ec.edu.ups.poo.view;

import ec.edu.ups.poo.modelo.Carrito;
import ec.edu.ups.poo.modelo.ItemCarrito;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class CarritoListarView extends JInternalFrame {
    private JButton btnListar;
    private JTable tblCarritos;
    private JTable tblDetallesCarrito;
    private JPanel panelPrincipal;

    private DefaultTableModel modeloTablaCarritos;
    private DefaultTableModel modeloDetallesCarrito;

    private List<Carrito> ultimoListado;

    public CarritoListarView() {
        super("Listado de Carritos", true, true, true, true);

        panelPrincipal = new JPanel(new BorderLayout());

        // Modelo de la tabla de carritos
        modeloTablaCarritos = new DefaultTableModel(
                new Object[]{"Código", "Fecha de Creación", "N° Items", "Cantidad Total", "Usuario"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblCarritos = new JTable(modeloTablaCarritos);

        // Modelo de la tabla de detalles
        modeloDetallesCarrito = new DefaultTableModel(
                new Object[]{"Código Prod.", "Nombre", "Cantidad", "Subtotal"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblDetallesCarrito = new JTable(modeloDetallesCarrito);

        btnListar = new JButton("Listar");

        JPanel panelTablas = new JPanel(new GridLayout(2, 1));
        panelTablas.add(new JScrollPane(tblCarritos));
        panelTablas.add(new JScrollPane(tblDetallesCarrito));

        panelPrincipal.add(panelTablas, BorderLayout.CENTER);
        panelPrincipal.add(btnListar, BorderLayout.SOUTH);

        setContentPane(panelPrincipal);
        this.setSize(800, 500);

        // Listener de selección
        tblCarritos.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting() && tblCarritos.getSelectedRow() != -1 && ultimoListado != null) {
                    int fila = tblCarritos.getSelectedRow();
                    if (fila >= 0 && fila < ultimoListado.size()) {
                        Carrito carrito = ultimoListado.get(fila);
                        mostrarDetallesCarrito(carrito);
                    }
                }
            }
        });
    }

    /**
     * Carga la lista de carritos en la tabla principal.
     * @param carritos Carritos a mostrar (solo del usuario autenticado)
     */
    public void cargarCarritos(List<Carrito> carritos) {
        modeloTablaCarritos.setRowCount(0);
        ultimoListado = carritos;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        if (carritos != null) {
            for (Carrito carrito : carritos) {
                int cantidadItems = carrito.getItems().size();
                int cantidadTotal = carrito.getItems().stream().mapToInt(ItemCarrito::getCantidad).sum();
                String nombreUsuario = (carrito.getUsuario() != null) ? carrito.getUsuario().getUsername() : "N/A";
                modeloTablaCarritos.addRow(new Object[]{
                        carrito.getCodigo(),
                        sdf.format(carrito.getFechaCreacion().getTime()),
                        cantidadItems,
                        cantidadTotal,
                        nombreUsuario
                });
            }
        }
        modeloDetallesCarrito.setRowCount(0); // limpia detalles
    }

    /**
     * Muestra los detalles del carrito seleccionado en la tabla inferior,
     * incluyendo productos y al final, subtotal, iva y total.
     */
    public void mostrarDetallesCarrito(Carrito carrito) {
        modeloDetallesCarrito.setRowCount(0);
        if (carrito != null) {
            for (ItemCarrito item : carrito.obtenerItems()) {
                modeloDetallesCarrito.addRow(new Object[]{
                        item.getProducto().getCodigo(),
                        item.getProducto().getNombre(),
                        item.getCantidad(),
                        String.format("%.2f", item.getSubtotal())
                });
            }
            // Fila vacía
            modeloDetallesCarrito.addRow(new Object[]{"", "", "", ""});
            // Subtotal, IVA y Total
            modeloDetallesCarrito.addRow(new Object[]{"", "Subtotal", "", String.format("%.2f", carrito.calcularSubtotal())});
            modeloDetallesCarrito.addRow(new Object[]{"", "IVA", "", String.format("%.2f", carrito.calcularIva())});
            modeloDetallesCarrito.addRow(new Object[]{"", "Total", "", String.format("%.2f", carrito.calcularTotal())});
        }
    }

    // Getters
    public JButton getBtnListar() { return btnListar; }
    public JTable getTblCarritos() { return tblCarritos; }
    public JTable getTblDetallesCarrito() { return tblDetallesCarrito; }
}