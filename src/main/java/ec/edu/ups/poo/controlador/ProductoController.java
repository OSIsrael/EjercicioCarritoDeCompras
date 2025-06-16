package ec.edu.ups.poo.controlador;

import ec.edu.ups.poo.dao.ProductoDAO;
import ec.edu.ups.poo.modelo.Producto;
import ec.edu.ups.poo.view.ProductoAnadirView;
import ec.edu.ups.poo.view.ProductoListaView;
import ec.edu.ups.poo.view.ProductoEditar;
import ec.edu.ups.poo.view.ProductoEliminar;

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


        productoEliminarView.getBtnListar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listarProductosParaEliminar();
            }
        });

        productoEliminarView.getBtnEliminar().addActionListener(new ActionListener() {
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
        productoEditarView.cargarDatos(productos);
    }

    private void editarProducto() {
        int filaSeleccionada = productoEditarView.getTblEditarProducto().getSelectedRow();

        if (filaSeleccionada >= 0) {
            int codigo = (int) productoEditarView.getTblEditarProducto().getValueAt(filaSeleccionada, 0);
            String nuevoNombre = productoEditarView.getTxtNombre().getText();

            if (!nuevoNombre.trim().isEmpty()) {
                Producto productoExistente = productoDAO.buscarPorCodigo(codigo);
                if (productoExistente != null) {
                    productoExistente.setNombre(nuevoNombre);
                    productoDAO.actualizar(productoExistente);
                    productoEditarView.mostrarMensaje("Producto actualizado correctamente");
                    productoEditarView.limpiarCampos();
                    listarProductosParaEditar();
                } else {
                    productoEditarView.mostrarMensaje("Producto no encontrado");
                }
            } else {
                productoEditarView.mostrarMensaje("Ingrese un nombre válido");
            }
        } else {
            productoEditarView.mostrarMensaje("Seleccione un producto de la tabla");
        }
    }

    private void listarProductosParaEliminar() {
        List<Producto> productos = productoDAO.listarTodos();
        productoEliminarView.cargarDatos(productos);
    }

    private void eliminarProducto() {
        int filaSeleccionada = productoEliminarView.getTblEliminarProducto().getSelectedRow();

        if (filaSeleccionada >= 0) {
            int codigo = (int) productoEliminarView.getTblEliminarProducto().getValueAt(filaSeleccionada, 0);
            productoDAO.eliminar(codigo);
            productoEliminarView.mostrarMensaje("Producto eliminado correctamente");
            listarProductosParaEliminar();
        } else {
            productoEliminarView.mostrarMensaje("Seleccione un producto de la tabla");
        }
    }
}