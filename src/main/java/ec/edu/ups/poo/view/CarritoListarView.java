package ec.edu.ups.poo.view;

import ec.edu.ups.poo.modelo.Carrito;
import ec.edu.ups.poo.modelo.ItemCarrito;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class CarritoListarView extends JInternalFrame {
    // Variables de instancia que coinciden con el diseño
    private JButton btnListar;
    private JTable tblCarritos;
    private JTable tblDetallesCarrito; // NUEVA TABLA para los detalles
    private JPanel panelPrincipal; // Panel raíz del .form

    private DefaultTableModel modeloTabla;
    private DefaultTableModel modeloDetallesCarrito; // Modelo para la tabla de detalles

    public CarritoListarView() {
        super("Listado de Carritos", true, true, true, true);

        // Panel principal y layout
        panelPrincipal = new JPanel(new BorderLayout());

        // Tabla de carritos
        modeloTabla = new DefaultTableModel();
        modeloTabla.addColumn("Código Carrito");
        modeloTabla.addColumn("Usuario");
        modeloTabla.addColumn("Fecha Creación");
        modeloTabla.addColumn("Total");
        tblCarritos = new JTable(modeloTabla);

        // Tabla de detalles
        modeloDetallesCarrito = new DefaultTableModel();
        modeloDetallesCarrito.addColumn("Código Producto");
        modeloDetallesCarrito.addColumn("Nombre");
        modeloDetallesCarrito.addColumn("Cantidad");
        modeloDetallesCarrito.addColumn("Subtotal");
        tblDetallesCarrito = new JTable(modeloDetallesCarrito);

        // Botón de listar (opcional, si lo usas)
        btnListar = new JButton("Listar");

        // Paneles para tablas
        JPanel panelTablas = new JPanel(new GridLayout(2, 1));
        panelTablas.add(new JScrollPane(tblCarritos));
        panelTablas.add(new JScrollPane(tblDetallesCarrito));

        // Añadir todo al panel principal
        panelPrincipal.add(panelTablas, BorderLayout.CENTER);
        panelPrincipal.add(btnListar, BorderLayout.SOUTH);

        setContentPane(panelPrincipal);
        this.setSize(700, 500);
    }

    public void cargarCarritos(List<Carrito> carritos) {
        modeloTabla.setRowCount(0); // Limpiar la tabla antes de cargar
        if (carritos != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            for (Carrito carrito : carritos) {
                String nombreUsuario = (carrito.getUsuario() != null) ? carrito.getUsuario().getUsername() : "N/A";
                Object[] fila = {
                        carrito.getCodigo(),
                        nombreUsuario,
                        sdf.format(carrito.getFechaCreacion()),
                        String.format("%.2f", carrito.calcularTotal())
                };
                modeloTabla.addRow(fila);
            }
        }
    }

    // Método para mostrar los detalles del carrito seleccionado
    public void mostrarDetallesCarrito(Carrito carrito) {
        modeloDetallesCarrito.setRowCount(0); // Limpiar tabla de detalles
        if (carrito != null) {
            for (ItemCarrito item : carrito.obtenerItems()) {
                modeloDetallesCarrito.addRow(new Object[]{
                        item.getProducto().getCodigo(),
                        item.getProducto().getNombre(),
                        item.getCantidad(),
                        String.format("%.2f", item.getSubtotal())
                });
            }
        }
    }

    // Getters para el controlador
    public JButton getBtnListar() {
        return btnListar;
    }

    public JTable getTblCarritos() {
        return tblCarritos;
    }

    public JTable getTblDetallesCarrito() {
        return tblDetallesCarrito;
    }
}