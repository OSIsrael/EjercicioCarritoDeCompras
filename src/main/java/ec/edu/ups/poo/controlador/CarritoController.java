package ec.edu.ups.poo.controlador;

import ec.edu.ups.poo.dao.CarritoDAO;
import ec.edu.ups.poo.dao.ProductoDAO;
import ec.edu.ups.poo.modelo.Carrito;
import ec.edu.ups.poo.modelo.ItemCarrito;
import ec.edu.ups.poo.modelo.Producto;
import ec.edu.ups.poo.modelo.Usuario;
import ec.edu.ups.poo.view.CarritoAnadirView;
import ec.edu.ups.poo.view.CarritoListarView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class CarritoController {
    private final CarritoDAO carritoDAO;
    private final CarritoAnadirView carritoAnadirView;
    private final ProductoDAO productoDAO;
    private final Usuario usuarioAutenticado;
    private final CarritoListarView carritoListarView;
    private Carrito carritoActual;

    public CarritoController(CarritoDAO carritoDAO, ProductoDAO productoDAO, CarritoAnadirView carritoAnadirView, Usuario usuarioAutenticado, CarritoListarView carritoListarView) {
        this.carritoDAO = carritoDAO;
        this.carritoAnadirView = carritoAnadirView;
        this.productoDAO = productoDAO;
        this.usuarioAutenticado = usuarioAutenticado;
        this.carritoListarView = carritoListarView;

        iniciarNuevoCarrito();
        configurarEventosEnVistas();
    }

    private void iniciarNuevoCarrito() {
        this.carritoActual = new Carrito();
        this.carritoActual.setUsuario(this.usuarioAutenticado);
    }

    private void configurarEventosEnVistas() {
        carritoAnadirView.getBtnAnadir().addActionListener(e -> anadirProducto());
        carritoAnadirView.getBtnGuardar().addActionListener(e -> guardarCarrito());
        carritoAnadirView.getBtnLimpiar().addActionListener(e -> limpiarCarritoActual());

        // Configurar menú contextual en la tabla del carrito
        JTable tablaItems = carritoAnadirView.getTblMostrar();
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem eliminarItem = new JMenuItem("Eliminar Ítem");
        JMenuItem actualizarCantidadItem = new JMenuItem("Actualizar Cantidad");

        popupMenu.add(eliminarItem);
        popupMenu.add(actualizarCantidadItem);
        tablaItems.setComponentPopupMenu(popupMenu);

        eliminarItem.addActionListener(e -> {
            int filaSeleccionada = tablaItems.getSelectedRow();
            if (filaSeleccionada >= 0) {
                int codigoProducto = (int) tablaItems.getValueAt(filaSeleccionada, 0);
                carritoActual.eliminarProducto(codigoProducto);
                actualizarVistaCarrito();
            }
        });

        actualizarCantidadItem.addActionListener(e -> {
            int filaSeleccionada = tablaItems.getSelectedRow();
            if (filaSeleccionada >= 0) {
                int codigoProducto = (int) tablaItems.getValueAt(filaSeleccionada, 0);
                String nuevaCantidadStr = JOptionPane.showInputDialog(carritoAnadirView, "Ingrese la nueva cantidad:", "Actualizar Cantidad", JOptionPane.PLAIN_MESSAGE);

                if (nuevaCantidadStr != null && esNumeroEntero(nuevaCantidadStr)) {
                    int nuevaCantidad = Integer.parseInt(nuevaCantidadStr);
                    carritoActual.actualizarCantidad(codigoProducto, nuevaCantidad);
                    actualizarVistaCarrito();
                } else if (nuevaCantidadStr != null) {
                    carritoAnadirView.mostrarMensaje("Por favor, ingrese un número válido.");
                }
            }
        });
    }

    private boolean esNumeroEntero(String texto) {
        if (texto == null || texto.trim().isEmpty()) return false;
        for (char c : texto.toCharArray()) {
            if (!Character.isDigit(c)) return false;
        }
        return true;
    }

    private void anadirProducto() {
        String codigoStr = carritoAnadirView.getTxtCodigo().getText();
        if (!esNumeroEntero(codigoStr)) {
            carritoAnadirView.mostrarMensaje("El código del producto debe ser un número.");
            return;
        }
        int codigo = Integer.parseInt(codigoStr);
        Producto producto = productoDAO.buscarPorCodigo(codigo);

        if (producto == null) {
            carritoAnadirView.mostrarMensaje("Producto no encontrado.");
            return;
        }

        Object cantidadObj = carritoAnadirView.getCbxCanridad().getSelectedItem();
        if (cantidadObj == null) {
            carritoAnadirView.mostrarMensaje("Seleccione una cantidad.");
            return;
        }
        int cantidad = Integer.parseInt(cantidadObj.toString());

        carritoActual.agregarProducto(producto, cantidad);
        actualizarVistaCarrito();
        limpiarCamposProductoAnadir();
    }

    private void guardarCarrito() {
        if (carritoActual.estaVacio()) {
            carritoAnadirView.mostrarMensaje("El carrito está vacío.");
            return;
        }
        carritoDAO.crear(carritoActual);
        carritoAnadirView.mostrarMensaje("Carrito guardado correctamente.");
        limpiarCarritoActual();
    }

    private void limpiarCarritoActual() {
        iniciarNuevoCarrito();
        actualizarVistaCarrito();
        limpiarCamposProductoAnadir();
    }

    private void actualizarVistaCarrito() {
        cargarProductosEnTabla();
        mostrarTotales();
    }

    private void cargarProductosEnTabla() {
        DefaultTableModel modelo = (DefaultTableModel) carritoAnadirView.getTblMostrar().getModel();
        modelo.setRowCount(0);
        for (ItemCarrito item : carritoActual.obtenerItems()) {
            modelo.addRow(new Object[]{
                    item.getProducto().getCodigo(),
                    item.getProducto().getNombre(),
                    item.getProducto().getPrecio(),
                    item.getCantidad(),
                    item.getSubtotal()
            });
        }
    }

    private void mostrarTotales() {
        carritoAnadirView.getTxtSubtotal().setText(String.format("%.2f", carritoActual.calcularSubtotal()));
        carritoAnadirView.getTxtIva().setText(String.format("%.2f", carritoActual.calcularIva()));
        carritoAnadirView.getTxtTotal().setText(String.format("%.2f", carritoActual.calcularTotal()));
    }

    private void limpiarCamposProductoAnadir() {
        carritoAnadirView.getTxtCodigo().setText("");
        carritoAnadirView.getTxtNombre().setText("");
        carritoAnadirView.getTxtPrecio().setText("");
        carritoAnadirView.getCbxCanridad().setSelectedIndex(0);
    }

    public void listarTodosLosCarritos() {
        List<Carrito> carritos = carritoDAO.listarTodos();
        carritoListarView.cargarCarritos(carritos);
    }
}