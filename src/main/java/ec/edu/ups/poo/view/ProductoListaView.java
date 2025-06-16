package ec.edu.ups.poo.view;

import ec.edu.ups.poo.modelo.Producto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class ProductoListaView extends JFrame {
    private JTextField txtBuscar;
    private JButton btnBuscar;
    private JTable tblProductos;
    private JPanel panelListarProducto;
    private JButton btnListar;
    private DefaultTableModel modelo;
    public ProductoListaView() {
        setContentPane(panelListarProducto);
        setTitle("LISTADO DE PRODUCTOS");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        //setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
        //pack();
        modelo = new DefaultTableModel();
        Object[] columnas={"ID","NOMBRE","PRECIO"};
        modelo.setColumnIdentifiers(columnas);
        tblProductos.setModel(modelo);
    }

    public JTextField getTxtBuscar() {
        return txtBuscar;
    }

    public void setTxtBuscar(JTextField txtBuscar) {
        this.txtBuscar = txtBuscar;
    }

    public JButton getBtnBuscar() {
        return btnBuscar;
    }

    public void setBtnBuscar(JButton btnBuscar) {
        this.btnBuscar = btnBuscar;
    }

    public JTable getTblProductos() {
        return tblProductos;
    }

    public void setTblProductos(JTable tblProductos) {
        this.tblProductos = tblProductos;
    }

    public JButton getBtnListar() {
        return btnListar;
    }

    public void setBtnListar(JButton btnListar) {
        this.btnListar = btnListar;
    }

    public void cargarDatos(List<Producto> productos) {
        modelo.setNumRows(0);

        for (Producto producto: productos) {
            Object[] filaProducto={producto.getCodigo(),producto.getNombre(),producto.getPrecio()};
            modelo.addRow(filaProducto);
        }

   }
}
