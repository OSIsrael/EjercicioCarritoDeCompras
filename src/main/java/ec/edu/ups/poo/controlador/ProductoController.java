package ec.edu.ups.poo.controlador;

import ec.edu.ups.poo.dao.ProductoDAO;
import ec.edu.ups.poo.modelo.Producto;
import ec.edu.ups.poo.view.ProductoAnadirView;
import ec.edu.ups.poo.view.ProductoListaView;
import ec.edu.ups.poo.view.ProductoEditar;
import ec.edu.ups.poo.view.ProductoEliminar;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ProductoController {

    private final ProductoAnadirView productoAnadirView;
    private final ProductoListaView productoListaView;
    private final ProductoEditar productoEditarView;
    private final ProductoEliminar productoEliminarView;
    private final ProductoDAO productoDAO;

    public ProductoController(ProductoDAO productoDAO, ProductoAnadirView productoView,
                              ProductoListaView productoListaView, ProductoEditar productoEditarView,
                              ProductoEliminar productoEliminarView) {
        this.productoDAO = productoDAO;
        this.productoAnadirView = productoView;
        this.productoListaView = productoListaView;
        this.productoEditarView = productoEditarView;
        this.productoEliminarView = productoEliminarView;
        configurarEventos();
    }

    private void configurarEventos() {

        productoAnadirView.getBtnGuardar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarProducto();
            }
        });


        productoListaView.getBtnBuscar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarProducto();
            }
        });

        productoListaView.getBtnListar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listarProductos();
            }
        });


        productoEditarView.getBtnListar1().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listarProductosParaEditar();
            }
        });

        productoEditarView.getBtnEditar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editarProducto();
            }
        });


        productoEliminarView.getLISTARButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listarProductosParaEliminar();
            }
        });

        productoEliminarView.getELIMINARButton().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarProducto();
            }
        });
    }

    private void guardarProducto() {
        int codigo = Integer.parseInt(productoAnadirView.getTxtCodigo().getText());
        String nombre = productoAnadirView.getTxtNombre().getText();
        double precio = Double.parseDouble(productoAnadirView.getTxtPrecio().getText());

        productoDAO.crear(new Producto(codigo, nombre, precio));
        productoAnadirView.mostrarMensaje("Producto guardado correctamente");
        productoAnadirView.limpiarCampos();
        productoAnadirView.mostrarProductos(productoDAO.listarTodos());
    }

    private void buscarProducto() {
        String nombre = productoListaView.getTxtBuscar().getText();
        List<Producto> productosEncontrados = productoDAO.buscarPorNombre(nombre);
        productoListaView.cargarDatos(productosEncontrados);
    }

    private void listarProductos() {
        List<Producto> productos = productoDAO.listarTodos();
        productoListaView.cargarDatos(productos);
    }

    private void listarProductosParaEditar() {
        List<Producto> productos = productoDAO.listarTodos();
        cargarDatosEnTablaEditar(productos);
    }

    private void editarProducto() {
        int filaSeleccionada = productoEditarView.getTblEditarProducto().getSelectedRow();

        if (filaSeleccionada >= 0) {
            DefaultTableModel modelo = (DefaultTableModel) productoEditarView.getTblEditarProducto().getModel();

            int codigo = (Integer) modelo.getValueAt(filaSeleccionada, 0);
            String nuevoNombre = productoEditarView.getTxtNombre().getText();

            if (!nuevoNombre.isEmpty()) {
                Producto productoExistente = productoDAO.buscarPorCodigo(codigo);

                if (productoExistente != null) {
                    productoExistente.setNombre(nuevoNombre);
                    productoDAO.actualizar(productoExistente);

                    JOptionPane.showMessageDialog(productoEditarView.getPanelEditarProducto(),
                            "Producto actualizado correctamente");

                    productoEditarView.getTxtNombre().setText("");
                    listarProductosParaEditar();
                } else {
                    JOptionPane.showMessageDialog(productoEditarView.getPanelEditarProducto(),
                            "Producto no encontrado");
                }
            } else {
                JOptionPane.showMessageDialog(productoEditarView.getPanelEditarProducto(),
                        "Ingrese el nuevo nombre del producto");
            }
        } else {
            JOptionPane.showMessageDialog(productoEditarView.getPanelEditarProducto(),
                    "Seleccione un producto de la tabla");
        }
    }

    private void listarProductosParaEliminar() {
        List<Producto> productos = productoDAO.listarTodos();
        cargarDatosEnTablaEliminar(productos);
    }

    private void eliminarProducto() {
        int filaSeleccionada = productoEliminarView.getTblEliminarProducto().getSelectedRow();

        if (filaSeleccionada >= 0) {
            DefaultTableModel modelo = (DefaultTableModel) productoEliminarView.getTblEliminarProducto().getModel();

            int codigo = (Integer) modelo.getValueAt(filaSeleccionada, 0);
            String nombre = (String) modelo.getValueAt(filaSeleccionada, 1);

            int confirmacion = JOptionPane.showConfirmDialog(
                    productoEliminarView.getPanelEliminarProducto(),
                    "¿Está seguro de eliminar el producto: " + nombre + "?",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirmacion == JOptionPane.YES_OPTION) {
                productoDAO.eliminar(codigo);
                JOptionPane.showMessageDialog(productoEliminarView.getPanelEliminarProducto(),
                        "Producto eliminado correctamente");
                listarProductosParaEliminar();
            }
        } else {
            JOptionPane.showMessageDialog(productoEliminarView.getPanelEliminarProducto(),
                    "Seleccione un producto de la tabla");
        }
    }

    private void cargarDatosEnTablaEditar(List<Producto> productos) {
        DefaultTableModel modelo = (DefaultTableModel) productoEditarView.getTblEditarProducto().getModel();
        modelo.setRowCount(0);

        for (Producto producto : productos) {
            Object[] filaProducto = {producto.getCodigo(), producto.getNombre(), producto.getPrecio()};
            modelo.addRow(filaProducto);
        }
    }

    private void cargarDatosEnTablaEliminar(List<Producto> productos) {
        DefaultTableModel modelo = (DefaultTableModel) productoEliminarView.getTblEliminarProducto().getModel();
        modelo.setRowCount(0);

        for (Producto producto : productos) {
            Object[] filaProducto = {producto.getCodigo(), producto.getNombre(), producto.getPrecio()};
            modelo.addRow(filaProducto);
        }
    }
}