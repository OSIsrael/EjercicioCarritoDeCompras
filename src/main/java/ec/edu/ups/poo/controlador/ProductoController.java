package ec.edu.ups.poo.controlador;

import ec.edu.ups.poo.dao.ProductoDAO;
import ec.edu.ups.poo.modelo.Carrito;
import ec.edu.ups.poo.modelo.ItemCarrito;
import ec.edu.ups.poo.modelo.Producto;
import ec.edu.ups.poo.util.Idioma;
import ec.edu.ups.poo.view.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.List;

public class ProductoController {

    private ProductoAnadirView productoAnadirView;
    private ProductoListaView productoListaView;
    private ProductoEditar productoEditarView;
    private ProductoEliminar productoEliminarView;
    private CarritoAnadirView carritoAnadirView;
    private ProductoDAO productoDAO;
    public ProductoController(ProductoDAO productoDAO, ProductoAnadirView productoAnadirView, ProductoListaView productoListaView, ProductoEditar productoEditarView, ProductoEliminar productoEliminarView, CarritoAnadirView carritoAnadirView) {
        this.productoDAO = productoDAO;
        this.productoAnadirView = productoAnadirView;
        this.productoListaView = productoListaView;
        this.productoEditarView = productoEditarView;
        this.productoEliminarView = productoEliminarView;
        this.carritoAnadirView = carritoAnadirView;

        configurarLitsaEventos();
        configurarAnadirEventos();
        configurarEditarEventos();
        configurarEliminarEventos();
        configurarCarritoEventos();
    }

    private void configurarAnadirEventos() {
        productoAnadirView.getBtnGuardar().addActionListener(e -> {
            guardarProducto();
        });
    }

    private void configurarLitsaEventos() {
        productoListaView.getBtnBuscar().addActionListener(e -> {
            buscarProducto();
        });
        productoListaView.getBtnListar().addActionListener(e -> {
            listarProductos();
        });
    }

    private void configurarEditarEventos() {
        productoEditarView.getBtnListar1().addActionListener(e -> {
            listarProductosParaEditar();
        });
        productoEditarView.getBtnEditar().addActionListener(e -> {
            editarProducto();
        });
    }

    private void configurarEliminarEventos() {
        productoEliminarView.getBtnListar().addActionListener(e -> {
            listarProductosParaEliminar();
        });
        productoEliminarView.getBtnEliminar().addActionListener(e -> {
            eliminarProducto();
        });
    }

    private void configurarCarritoEventos() {
        carritoAnadirView.getBtnBuscar().addActionListener(e -> {
            buscarProductoCarrito();
        });
    }

    private void buscarProductoCarrito() {
        try {
            String codigoStr = carritoAnadirView.getTxtCodigo().getText().trim();
            if (codigoStr.isEmpty()) {
                carritoAnadirView.mostrarMensaje("carrito.info.noproducto");
                return;
            }

            int codigo;
            try {
                codigo = Integer.parseInt(codigoStr);
            } catch (NumberFormatException e) {
                carritoAnadirView.mostrarMensaje("producto.controller.msj.codigoinvalido");
                return;
            }
            
            Producto producto = productoDAO.buscarPorCodigo(codigo);
            if (producto == null) {
                carritoAnadirView.mostrarMensaje("producto.controller.msj.noencontrado");
                carritoAnadirView.getTxtNombre().setText("");
                carritoAnadirView.getTxtPrecio().setText("");
            } else {
                carritoAnadirView.getTxtNombre().setText(producto.getNombre());
                carritoAnadirView.getTxtPrecio().setText(String.valueOf(producto.getPrecio()));
            }
        } catch (IOException e) {
            carritoAnadirView.mostrarError(Idioma.get("producto.controller.msj.error.archivo") + ": " + e.getMessage());
        }
    }

    private void guardarProducto() {
        try {

            String codigoStr = productoAnadirView.getTxtCodigo().getText().trim();
            String nombre = productoAnadirView.getTxtNombre().getText().trim();
            String precioStr = productoAnadirView.getTxtPrecio().getText().trim();

            // --- MEJORA: Validación de campos ---
            if (codigoStr.isEmpty() || nombre.isEmpty() || precioStr.isEmpty()) {
                productoAnadirView.mostrarMensaje("producto.controller.msj.camposvacios");
                return;
            }

            int codigo;
            double precio;

            try {
                codigo = Integer.parseInt(codigoStr);
                precio = Double.parseDouble(precioStr);
            } catch (NumberFormatException e) {
                productoAnadirView.mostrarMensaje("producto.controller.msj.valido");
                return;
            }

            if (productoDAO.buscarPorCodigo(codigo) != null) {
                productoAnadirView.mostrarMensaje("producto.controller.msj.codigoexiste");
                return;
            }

            productoDAO.crear(new Producto(codigo, nombre, precio));
            productoAnadirView.mostrarMensaje("producto.controller.msj.guardado");
            productoAnadirView.limpiarCampos();

        } catch (IOException e) {

            productoAnadirView.mostrarError(Idioma.get("producto.controller.msj.error.archivo") + ": " + e.getMessage());
        }
    }

    private void buscarProducto() {
        try {
            String nombre = productoListaView.getTxtBuscar().getText();
            List<Producto> productosEncontrados = productoDAO.buscarPorNombre(nombre);
            productoListaView.cargarDatos(productosEncontrados);
        } catch (IOException e) {
            productoListaView.mostrarError(Idioma.get("producto.controller.msj.error.archivo") + ": " + e.getMessage());
        }
    }

    private void listarProductos() {
        try {
            productoListaView.cargarDatos(productoDAO.listarTodos());
        } catch (IOException e) {
            productoListaView.mostrarError(Idioma.get("producto.controller.msj.error.archivo") + ": " + e.getMessage());
        }
    }

    private void listarProductosParaEditar() {
        try {
            productoEditarView.cargarDatos(productoDAO.listarTodos());
        } catch (IOException e) {
            productoEditarView.mostrarMensaje(Idioma.get("producto.controller.msj.error.archivo") + ": " + e.getMessage());
        }
    }

    private void editarProducto() {
        try {
        int filaSeleccionada = productoEditarView.getTblEditarProducto().getSelectedRow();
        if (filaSeleccionada >= 0) {
            int codigo = (int) productoEditarView.getTblEditarProducto().getValueAt(filaSeleccionada, 0);
            String nuevoNombre = productoEditarView.getTxtNombre().getText();

            if (!nuevoNombre.trim().isEmpty()) {
                Producto producto = productoDAO.buscarPorCodigo(codigo);
                if (producto != null) {
                    producto.setNombre(nuevoNombre);
                    productoDAO.actualizar(producto);
                    productoEditarView.mostrarMensaje("producto.controller.msj.actualizado");
                    productoEditarView.limpiarCampos();
                    listarProductosParaEditar();
                } else {
                    productoEditarView.mostrarMensaje(Idioma.get("producto.controller.msj.noencontrado"));
                }
            } else {
                productoEditarView.mostrarMensaje(Idioma.get("producto.controller.msj.valido"));
            }
        } else {
            productoEditarView.mostrarMensaje(Idioma.get("producto.controller.msj.selectable"));
        }

        } catch (IOException e) {
            productoEditarView.mostrarMensaje(Idioma.get("producto.controller.msj.error.archivo") + ": " + e.getMessage());
        }
    }

    private void listarProductosParaEliminar() {
        try {
            productoEliminarView.cargarDatos(productoDAO.listarTodos());
        } catch (IOException e) {
            productoEliminarView.mostrarMensaje(Idioma.get("producto.controller.msj.error.archivo") + ": " + e.getMessage());
        }
    }


    private void eliminarProducto() {
        int filaSeleccionada = productoEliminarView.getTblEliminarProducto().getSelectedRow();
        if (filaSeleccionada < 0) {
            productoEliminarView.mostrarMensaje("producto.controller.msj.please");
            return;
        }


        int confirmacion = JOptionPane.showConfirmDialog(
                productoEliminarView,
                Idioma.get("producto.controller.msj.seguro"),
                Idioma.get("producto.controller.msj.confirmar"),
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacion == JOptionPane.YES_OPTION) {
            int codigo = (int) productoEliminarView.getTblEliminarProducto().getValueAt(filaSeleccionada, 0);
            try {
                productoDAO.eliminar(codigo);
                productoEliminarView.mostrarMensaje(Idioma.get("producto.controller.msj.eliminado"));
                listarProductosParaEliminar();
            } catch (IOException e) {
                productoEliminarView.mostrarMensaje(Idioma.get("producto.controller.msj.error.archivo") + ": " + e.getMessage());
            }
        }
    }

}
