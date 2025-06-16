package ec.edu.ups.poo.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ProductoEliminar extends JFrame {
    private JPanel panelEliminarProducto;
    private JLabel lblNombre;
    private JTextField txtNombre;
    private JButton LISTARButton;
    private JButton ELIMINARButton;
    private JTable tblEliminarProducto;
    private DefaultTableModel modelo;

    public ProductoEliminar() {
        setContentPane(panelEliminarProducto);
        setTitle("ELIMINAR PRODUCTOS");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);
        setVisible(true);

        // Configurar modelo de la tabla
        modelo = new DefaultTableModel();
        Object[] columnas = {"ID", "NOMBRE", "PRECIO"};
        modelo.setColumnIdentifiers(columnas);
        tblEliminarProducto.setModel(modelo);
    }

    public JPanel getPanelEliminarProducto() {
        return panelEliminarProducto;
    }

    public void setPanelEliminarProducto(JPanel panelEliminarProducto) {
        this.panelEliminarProducto = panelEliminarProducto;
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

    public JButton getLISTARButton() {
        return LISTARButton;
    }

    public void setLISTARButton(JButton LISTARButton) {
        this.LISTARButton = LISTARButton;
    }

    public JButton getELIMINARButton() {
        return ELIMINARButton;
    }

    public void setELIMINARButton(JButton ELIMINARButton) {
        this.ELIMINARButton = ELIMINARButton;
    }

    public JTable getTblEliminarProducto() {
        return tblEliminarProducto;
    }

    public void setTblEliminarProducto(JTable tblEliminarProducto) {
        this.tblEliminarProducto = tblEliminarProducto;
    }

    public DefaultTableModel getModelo() {
        return modelo;
    }

    public void setModelo(DefaultTableModel modelo) {
        this.modelo = modelo;
    }
}