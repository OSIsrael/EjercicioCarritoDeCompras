package ec.edu.ups.poo.view;

import javax.swing.*;

public class ItemCarritoView extends JFrame {
    private JButton btnAgregrar;
    private JComboBox txtEscoger;
    private JTextField txtEscogaCantidad;
    private JButton btnEliminar;
    private JLabel txtTitle;
    private JLabel txtIngrese;
    private JLabel txtCantidas;
    private JPanel panelCarrito;

    public ItemCarritoView() {
        setContentPane(panelCarrito);
        setTitle("Datos del Producto");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        //setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
        //pack();
    }

    public JButton getBtnAgregrar() {
        return btnAgregrar;
    }

    public void setBtnAgregrar(JButton btnAgregrar) {
        this.btnAgregrar = btnAgregrar;
    }

    public JComboBox getTxtEscoger() {
        return txtEscoger;
    }

    public void setTxtEscoger(JComboBox txtEscoger) {
        this.txtEscoger = txtEscoger;
    }

    public JTextField getTxtEscogaCantidad() {
        return txtEscogaCantidad;
    }

    public void setTxtEscogaCantidad(JTextField txtEscogaCantidad) {
        this.txtEscogaCantidad = txtEscogaCantidad;
    }

    public JButton getBtnEliminar() {
        return btnEliminar;
    }

    public void setBtnEliminar(JButton btnEliminar) {
        this.btnEliminar = btnEliminar;
    }

    public JLabel getTxtTitle() {
        return txtTitle;
    }

    public void setTxtTitle(JLabel txtTitle) {
        this.txtTitle = txtTitle;
    }

    public JLabel getTxtIngrese() {
        return txtIngrese;
    }

    public void setTxtIngrese(JLabel txtIngrese) {
        this.txtIngrese = txtIngrese;
    }

    public JLabel getTxtCantidas() {
        return txtCantidas;
    }

    public void setTxtCantidas(JLabel txtCantidas) {
        this.txtCantidas = txtCantidas;
    }

    public JPanel getPanelCarrito() {
        return panelCarrito;
    }

    public void setPanelCarrito(JPanel panelCarrito) {
        this.panelCarrito = panelCarrito;
    }
    public static void main(String[] args) {
        new ItemCarritoView();
    }
}
