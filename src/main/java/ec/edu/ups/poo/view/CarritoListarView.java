package ec.edu.ups.poo.view;

import ec.edu.ups.poo.modelo.Carrito;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class CarritoListarView extends JInternalFrame {
    // Variables de instancia que coinciden con el diseño
    private JButton btnListar;
    private JTable tblCarritos;
    private JPanel panelPrincipal; // Asumiendo que es el panel raíz del .form

    private DefaultTableModel modeloTabla;

    public CarritoListarView() {
        super("Listado de Carritos", true, true, true, true);
        // CORRECCIÓN: Eliminar la línea this.setContentPane(panelPrincipal);
        // El diseñador de GUI se encarga de inicializar panelPrincipal y establecerlo como contentPane.
        this.setSize(700, 400); // Ajusta el tamaño según tu diseño

        // Configuración de la tabla
        modeloTabla = new DefaultTableModel();
        modeloTabla.addColumn("Código Carrito");
        modeloTabla.addColumn("Usuario");
        modeloTabla.addColumn("Fecha Creación");
        modeloTabla.addColumn("Total");

        tblCarritos.setModel(modeloTabla);
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

    // Getters para el controlador
    public JButton getBtnListar() {
        return btnListar;
    }

    public JTable getTblCarritos() {
        return tblCarritos;
    }
}