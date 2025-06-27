package ec.edu.ups.poo.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CarritoAnadirView extends JInternalFrame {
    private JPanel panelPrincipal;
    private JTextField txtCodigo;
    private JButton btnBuscar;
    private JTextField txtNombre;
    private JTextField txtPrecio;
    private JButton btnAnadir;
    private JTable tblMostrar;
    private JTextField txtSubtotal;
    private JTextField txtIva;
    private JTextField txtTotal;
    private JButton btnGuardar;
    private JButton btnLimpiar;
    private JComboBox<String> cbxCanridad;
    private JPanel lblDatosProducto;
    private JLabel lblCodigo;
    private JLabel lblPrecio;
    private JLabel lblNombre;
    private JLabel lblCantidad;
    private JLabel lblSubtotal;
    private JLabel lblIva;
    private JLabel lblTotal;

    public CarritoAnadirView() {
        super("Carrito de Compras", true, true, false, true);
        setContentPane(panelPrincipal);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 600);
        setMinimumSize(new Dimension(550, 500));

        cargarDatos();
        configurarTabla();
        configurarComponentes();
    }

    private void cargarDatos() {
        cbxCanridad.removeAllItems();
        for (int i = 1; i <= 20; i++) {
            cbxCanridad.addItem(String.valueOf(i));
        }
    }

    private void configurarTabla() {
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer que la tabla no sea editable
            }
        };

        Object[] columnas = new Object[]{"Código", "Nombre", "Precio", "Cantidad", "Subtotal"};
        model.setColumnIdentifiers(columnas);
        tblMostrar.setModel(model);

        // Configurar el ancho de las columnas
        tblMostrar.getColumnModel().getColumn(0).setPreferredWidth(80);  // Código
        tblMostrar.getColumnModel().getColumn(1).setPreferredWidth(200); // Nombre
        tblMostrar.getColumnModel().getColumn(2).setPreferredWidth(80);  // Precio
        tblMostrar.getColumnModel().getColumn(3).setPreferredWidth(80);  // Cantidad
        tblMostrar.getColumnModel().getColumn(4).setPreferredWidth(100); // Subtotal

        // Configurar selección de fila
        tblMostrar.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblMostrar.setRowSelectionAllowed(true);
        tblMostrar.setColumnSelectionAllowed(false);

        // Agregar tooltip para informar sobre el clic derecho
        tblMostrar.setToolTipText("Clic derecho en una fila para modificar o eliminar el item");
    }

    private void configurarComponentes() {
        // Configurar campos de texto para que sean más visibles cuando están deshabilitados
        txtNombre.setBackground(new Color(240, 240, 240));
        txtPrecio.setBackground(new Color(240, 240, 240));
        txtSubtotal.setBackground(new Color(240, 240, 240));
        txtIva.setBackground(new Color(240, 240, 240));
        txtTotal.setBackground(new Color(240, 240, 240));

        // Configurar botones con colores
        btnGuardar.setBackground(new Color(46, 125, 50));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);

        btnLimpiar.setBackground(new Color(211, 47, 47));
        btnLimpiar.setForeground(Color.WHITE);
        btnLimpiar.setFocusPainted(false);

        btnAnadir.setBackground(new Color(25, 118, 210));
        btnAnadir.setForeground(Color.WHITE);
        btnAnadir.setFocusPainted(false);

        btnBuscar.setBackground(new Color(255, 152, 0));
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.setFocusPainted(false);

        // Configurar tooltips informativos
        btnGuardar.setToolTipText("Guardar el carrito actual en la base de datos");
        btnLimpiar.setToolTipText("Vaciar completamente el carrito");
        btnAnadir.setToolTipText("Agregar el producto al carrito");
        btnBuscar.setToolTipText("Buscar producto por código");

        // Inicializar campos de totales en 0
        txtSubtotal.setText("0.00");
        txtIva.setText("0.00");
        txtTotal.setText("0.00");
    }

    // Métodos getter y setter
    public JPanel getPanelPrincipal() {
        return panelPrincipal;
    }

    public void setPanelPrincipal(JPanel panelPrincipal) {
        this.panelPrincipal = panelPrincipal;
    }

    public JTextField getTxtCodigo() {
        return txtCodigo;
    }

    public void setTxtCodigo(JTextField txtCodigo) {
        this.txtCodigo = txtCodigo;
    }

    public JButton getBtnBuscar() {
        return btnBuscar;
    }

    public void setBtnBuscar(JButton btnBuscar) {
        this.btnBuscar = btnBuscar;
    }

    public JTextField getTxtNombre() {
        return txtNombre;
    }

    public void setTxtNombre(JTextField txtNombre) {
        this.txtNombre = txtNombre;
    }

    public JTextField getTxtPrecio() {
        return txtPrecio;
    }

    public void setTxtPrecio(JTextField txtPrecio) {
        this.txtPrecio = txtPrecio;
    }

    public JButton getBtnAnadir() {
        return btnAnadir;
    }

    public void setBtnAnadir(JButton btnAnadir) {
        this.btnAnadir = btnAnadir;
    }

    public JTable getTblMostrar() {
        return tblMostrar;
    }

    public void setTblMostrar(JTable tblMostrar) {
        this.tblMostrar = tblMostrar;
    }

    public JTextField getTxtSubtotal() {
        return txtSubtotal;
    }

    public void setTxtSubtotal(JTextField txtSubtotal) {
        this.txtSubtotal = txtSubtotal;
    }

    public JTextField getTxtIva() {
        return txtIva;
    }

    public void setTxtIva(JTextField txtIva) {
        this.txtIva = txtIva;
    }

    public JTextField getTxtTotal() {
        return txtTotal;
    }

    public void setTxtTotal(JTextField txtTotal) {
        this.txtTotal = txtTotal;
    }

    public JButton getBtnGuardar() {
        return btnGuardar;
    }

    public void setBtnGuardar(JButton btnGuardar) {
        this.btnGuardar = btnGuardar;
    }

    public JButton getBtnLimpiar() {
        return btnLimpiar;
    }

    public void setBtnLimpiar(JButton btnLimpiar) {
        this.btnLimpiar = btnLimpiar;
    }

    public JComboBox<String> getCbxCanridad() {
        return cbxCanridad;
    }

    public void setCbxCanridad(JComboBox<String> cbxCanridad) {
        this.cbxCanridad = cbxCanridad;
    }

    public void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    public void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void mostrarAdvertencia(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Advertencia", JOptionPane.WARNING_MESSAGE);
    }

    public int mostrarConfirmacion(String mensaje) {
        return JOptionPane.showConfirmDialog(this, mensaje, "Confirmación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
    }

    public void limpiarCamposProducto() {
        txtCodigo.setText("");
        txtNombre.setText("");
        txtPrecio.setText("");
        cbxCanridad.setSelectedIndex(0);
    }

    public void establecerProducto(String codigo, String nombre, String precio) {
        txtCodigo.setText(codigo);
        txtNombre.setText(nombre);
        txtPrecio.setText(precio);
    }
}