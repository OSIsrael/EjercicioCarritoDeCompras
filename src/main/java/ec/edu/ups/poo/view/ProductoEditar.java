package ec.edu.ups.poo.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ProductoEditar extends JFrame {
    private JPanel panelEditarProducto;
    private JLabel lblNombre;
    private JTextField txtNombre;
    private JButton btnListar1;
    private JButton btnEditar;
    private JTable tblEditarProducto;
    private DefaultTableModel modelo;

    public ProductoEditar() {
        setContentPane(panelEditarProducto);
        setTitle("EDITAR PRODUCTOS");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);
        setVisible(true);

        // Configurar modelo de la tabla
        modelo = new DefaultTableModel();
        Object[] columnas = {"ID", "NOMBRE", "PRECIO"};
        modelo.setColumnIdentifiers(columnas);
        tblEditarProducto.setModel(modelo);
    }

    public JPanel getPanelEditarProducto() {
        return panelEditarProducto;
    }

    public void setPanelEditarProducto(JPanel panelEditarProducto) {
        this.panelEditarProducto = panelEditarProducto;
    }

    public JLabel getLblNombre() {
        return lblNombre;
    }

    public void setLblNombre(JLabel lblNombre) {
        this.lblNombre = lblNombre;
    }

    public JTextField getTxtNombre() {
        return txtNombre;
    }

    public void setTxtNombre(JTextField txtNombre) {
        this.txtNombre = txtNombre;
    }

    public JButton getBtnListar1() {
        return btnListar1;
    }

    public void setBtnListar1(JButton btnListar1) {
        this.btnListar1 = btnListar1;
    }

    public JButton getBtnEditar() {
        return btnEditar;
    }

    public void setBtnEditar(JButton btnEditar) {
        this.btnEditar = btnEditar;
    }

    public JTable getTblEditarProducto() {
        return tblEditarProducto;
    }

    public void setTblEditarProducto(JTable tblEditarProducto) {
        this.tblEditarProducto = tblEditarProducto;
    }

    public DefaultTableModel getModelo() {
        return modelo;
    }

    public void setModelo(DefaultTableModel modelo) {
        this.modelo = modelo;
    }
}