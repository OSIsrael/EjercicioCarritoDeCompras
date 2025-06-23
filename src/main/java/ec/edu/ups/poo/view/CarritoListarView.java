package ec.edu.ups.poo.view;

import ec.edu.ups.poo.modelo.Carrito;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class CarritoListarView extends JInternalFrame{
    private JButton btnListar;
    private JTable tblCarritos;
    private JPanel panelPrincipal;
    private DefaultTableModel modeloTabla;
    public CarritoListarView(){
        super("Listado de Carritos", true, true, true, true);
        setSize(600, 400);

        modeloTabla = new DefaultTableModel();
        modeloTabla.addColumn("Código Carrito");
        modeloTabla.addColumn("Usuario");
        modeloTabla.addColumn("Fecha");
        modeloTabla.addColumn("Total");

        tblCarritos = new JTable(modeloTabla);
        JScrollPane scrollPane = new JScrollPane(tblCarritos);

        add(scrollPane);
    }
    public void cargarCarritos(List<Carrito> carritos) {
        modeloTabla.setRowCount(0); // Limpiar tabla
        for (Carrito carrito : carritos) {
            Object[] fila = {
                    carrito.getCodigo(),
                    carrito.getUsuario().getUsername(),
                    carrito.getFechaCreacion().getTime(), // Muestra la fecha
                    carrito.calcularTotal()
            };
            modeloTabla.addRow(fila);
        }
    }

    public JButton getBtnListar() {
        return btnListar;
    }

    public void setBtnListar(JButton btnListar) {
        this.btnListar = btnListar;
    }

    public JTable getTblCarritos() {
        return tblCarritos;
    }

    public void setTblCarritos(JTable tblCarritos) {
        this.tblCarritos = tblCarritos;
    }

    public JPanel getPanelPrincipal() {
        return panelPrincipal;
    }

    public void setPanelPrincipal(JPanel panelPrincipal) {
        this.panelPrincipal = panelPrincipal;
    }

    public DefaultTableModel getModeloTabla() {
        return modeloTabla;
    }

    public void setModeloTabla(DefaultTableModel modeloTabla) {
        this.modeloTabla = modeloTabla;
    }
}
