package ec.edu.ups.poo.controlador;

import ec.edu.ups.poo.dao.CarritoDAO;
import ec.edu.ups.poo.dao.ProductoDAO;
import ec.edu.ups.poo.modelo.Carrito;
import ec.edu.ups.poo.modelo.ItemCarrito;
import ec.edu.ups.poo.modelo.Producto;
import ec.edu.ups.poo.view.CarritoAnadirView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class CarritoController {
    private final CarritoDAO carritoDAO;
    private final CarritoAnadirView carritoAnadirView;
    private final ProductoDAO productoDAO;
    private Carrito carrito;
    private JPopupMenu popupMenu;
    private int filaSeleccionada = -1;

    public CarritoController(CarritoDAO carritoDAO, ProductoDAO productoDAO, CarritoAnadirView carritoAnadirView) {
        this.carritoDAO = carritoDAO;
        this.carritoAnadirView = carritoAnadirView;
        this.productoDAO = productoDAO;
        this.carrito = new Carrito();
        configurarEventosEnVistas();
        configurarPopupMenu();
    }

    private void configurarEventosEnVistas(){
        carritoAnadirView.getBtnAnadir().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                anadirProducto();
            }
        });

        carritoAnadirView.getBtnGuardar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarCarrito();
            }
        });

        carritoAnadirView.getBtnLimpiar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarCarrito();
            }
        });

        // Configurar eventos del mouse para la tabla
        carritoAnadirView.getTblMostrar().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    int fila = carritoAnadirView.getTblMostrar().rowAtPoint(e.getPoint());
                    if (fila >= 0) {
                        carritoAnadirView.getTblMostrar().setRowSelectionInterval(fila, fila);
                        filaSeleccionada = fila;
                        popupMenu.show(carritoAnadirView.getTblMostrar(), e.getX(), e.getY());
                    }
                }
            }
        });
    }

    private void configurarPopupMenu() {
        popupMenu = new JPopupMenu();

        JMenuItem modificarItem = new JMenuItem("Modificar Cantidad");
        modificarItem.addActionListener(e -> modificarCantidadItem());

        JMenuItem eliminarItem = new JMenuItem("Eliminar Item");
        eliminarItem.addActionListener(e -> eliminarItem());

        popupMenu.add(modificarItem);
        popupMenu.add(eliminarItem);
    }

    private void anadirProducto() {
        try {
            int codigo = Integer.parseInt(carritoAnadirView.getTxtCodigo().getText());
            Producto producto = productoDAO.buscarPorCodigo(codigo);

            if (producto == null) {
                carritoAnadirView.mostrarMensaje("Producto no encontrado");
                return;
            }

            int cantidad = Integer.parseInt(carritoAnadirView.getCbxCanridad().getSelectedItem().toString());

            // Verificar si el producto ya existe en el carrito
            boolean productoExiste = false;
            for (ItemCarrito item : carrito.obtenerItems()) {
                if (item.getProducto().getCodigo() == codigo) {
                    // Si existe, sumar la cantidad
                    item.setCantidad(item.getCantidad() + cantidad);
                    productoExiste = true;
                    break;
                }
            }

            // Si no existe, agregarlo como nuevo item
            if (!productoExiste) {
                carrito.agregarProducto(producto, cantidad);
            }

            cargarProductos();
            mostrarTotales();
            limpiarCamposProducto();

        } catch (NumberFormatException ex) {
            carritoAnadirView.mostrarMensaje("Por favor ingrese un código válido");
        }
    }

    private void cargarProductos(){
        List<ItemCarrito> items = carrito.obtenerItems();
        DefaultTableModel modelo = (DefaultTableModel) carritoAnadirView.getTblMostrar().getModel();
        modelo.setRowCount(0);

        for (ItemCarrito item : items) {
            String subtotal = String.valueOf(item.getProducto().getPrecio() * item.getCantidad());
            modelo.addRow(new Object[]{
                    item.getProducto().getCodigo(),
                    item.getProducto().getNombre(),
                    item.getProducto().getPrecio(),
                    item.getCantidad(),
                    subtotal
            });
        }
    }

    private void mostrarTotales(){
        String total = String.valueOf(carrito.calcularTotal());
        String subtotal = String.valueOf(carrito.calcularSubtotal());
        String iva = String.valueOf(carrito.calcularIva());

        carritoAnadirView.getTxtTotal().setText(total);
        carritoAnadirView.getTxtSubtotal().setText(subtotal);
        carritoAnadirView.getTxtIva().setText(iva);
    }

    private void guardarCarrito(){
        if (carrito.estaVacio()) {
            carritoAnadirView.mostrarMensaje("El carrito está vacío");
            return;
        }

        // Generar un código único para el carrito
        int codigoCarrito = (int) (Math.random() * 10000) + 1;
        carrito.setCodigo(codigoCarrito);

        carritoDAO.crear(carrito);
        carritoAnadirView.mostrarMensaje("Carrito guardado correctamente con código: " + codigoCarrito);

        // Crear un nuevo carrito después de guardar
        carrito = new Carrito();
        cargarProductos();
        mostrarTotales();
        limpiarCamposProducto();
    }

    private void limpiarCarrito() {
        int confirmacion = JOptionPane.showConfirmDialog(
                carritoAnadirView,
                "¿Está seguro de que desea limpiar el carrito?",
                "Confirmar limpieza",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacion == JOptionPane.YES_OPTION) {
            carrito.vaciarCarrito();
            cargarProductos();
            mostrarTotales();
            limpiarCamposProducto();
            carritoAnadirView.mostrarMensaje("Carrito limpiado correctamente");
        }
    }

    private void modificarCantidadItem() {
        if (filaSeleccionada >= 0 && filaSeleccionada < carrito.obtenerItems().size()) {
            ItemCarrito item = carrito.obtenerItems().get(filaSeleccionada);

            String nuevaCantidadStr = JOptionPane.showInputDialog(
                    carritoAnadirView,
                    "Ingrese la nueva cantidad:",
                    "Modificar Cantidad",
                    JOptionPane.QUESTION_MESSAGE
            );

            if (nuevaCantidadStr != null && !nuevaCantidadStr.trim().isEmpty()) {
                try {
                    int nuevaCantidad = Integer.parseInt(nuevaCantidadStr);
                    if (nuevaCantidad > 0) {
                        item.setCantidad(nuevaCantidad);
                        cargarProductos();
                        mostrarTotales();
                        carritoAnadirView.mostrarMensaje("Cantidad modificada correctamente");
                    } else {
                        carritoAnadirView.mostrarMensaje("La cantidad debe ser mayor a 0");
                    }
                } catch (NumberFormatException ex) {
                    carritoAnadirView.mostrarMensaje("Por favor ingrese un número válido");
                }
            }
        }
    }

    private void eliminarItem() {
        if (filaSeleccionada >= 0 && filaSeleccionada < carrito.obtenerItems().size()) {
            ItemCarrito item = carrito.obtenerItems().get(filaSeleccionada);

            int confirmacion = JOptionPane.showConfirmDialog(
                    carritoAnadirView,
                    "¿Está seguro de que desea eliminar el producto: " + item.getProducto().getNombre() + "?",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirmacion == JOptionPane.YES_OPTION) {
                carrito.eliminarProducto(item.getProducto().getCodigo());
                cargarProductos();
                mostrarTotales();
                carritoAnadirView.mostrarMensaje("Producto eliminado correctamente");
            }
        }
    }

    private void limpiarCamposProducto() {
        carritoAnadirView.getTxtCodigo().setText("");
        carritoAnadirView.getTxtNombre().setText("");
        carritoAnadirView.getTxtPrecio().setText("");
        carritoAnadirView.getCbxCanridad().setSelectedIndex(0);
    }
}