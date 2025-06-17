package ec.edu.ups.poo.controlador;

import ec.edu.ups.poo.dao.ProductoDAO;
import ec.edu.ups.poo.modelo.Producto;
import ec.edu.ups.poo.view.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ProductoController {

    private  ProductoAnadirView productoAnadirView;
    private  ProductoListaView productoListaView;
    private  ProductoEditar productoEditarView;
    private  ProductoEliminar productoEliminarView;
    private CarritoAnadirView carritoAnadirView;
    private  ProductoDAO productoDAO;

    public ProductoController(ProductoDAO productoDAO,ProductoAnadirView productoAnadirView,ProductoListaView productoListaView,ProductoEditar productoEditarView,ProductoEliminar productoEliminarView,CarritoAnadirView carritoAnadirView) {
        this.productoListaView=productoListaView;
        this.productoEditarView=productoEditarView;
        this.productoAnadirView=productoAnadirView;
        this.productoDAO=productoDAO;
        this.productoEliminarView=productoEliminarView;
        this.carritoAnadirView=carritoAnadirView;
        configurarLitsaEventos();
        configurarAnadirEventos();
        configurarEditarEventos();
        configurarEliminarEventos();
        configurarCarritoEventos();
    }

    private void configurarAnadirEventos() {

        productoAnadirView.getBtnGuardar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guardarProducto();
            }
        });
    }
    private void configurarLitsaEventos() {

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
    }
    private void configurarEditarEventos() {

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
    }

    private void configurarEliminarEventos() {
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
    private void configurarCarritoEventos(){
        carritoAnadirView.getBtnBuscar().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                buscarProductoCarrito();
            }
        });
    }
    private void buscarProductoCarrito(){
        int codigo= Integer.parseInt(carritoAnadirView.getTxtCodigo().getText());
        Producto producto=productoDAO.buscarPorCodigo(codigo);
        if(producto==null){
            carritoAnadirView.mostrarMensaje("Producto no encontrado");
            carritoAnadirView.getTxtNombre().setText("");
            carritoAnadirView.getTxtPrecio().setText("");
        }else{
            carritoAnadirView.getTxtNombre().setText(producto.getNombre());
            carritoAnadirView.getTxtPrecio().setText(String.valueOf(producto.getPrecio()));
        }


    }
    private void mostarProductoEncontrado(Producto producto) {

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

    public ProductoAnadirView getProductoAnadirView() {
        return productoAnadirView;

    }

    public void setProductoAnadirView(ProductoAnadirView productoAnadirView) {
        this.productoAnadirView = productoAnadirView;
        configurarAnadirEventos();
    }

    public ProductoListaView getProductoListaView() {
        return productoListaView;
    }

    public void setProductoListaView(ProductoListaView productoListaView) {
        this.productoListaView = productoListaView;
        configurarLitsaEventos();

    }

    public ProductoEditar getProductoEditarView() {
        return productoEditarView;
    }

    public void setProductoEditarView(ProductoEditar productoEditarView) {
        this.productoEditarView = productoEditarView;
        configurarEditarEventos();
    }

    public ProductoEliminar getProductoEliminarView() {
        return productoEliminarView;
    }

    public void setProductoEliminarView(ProductoEliminar productoEliminarView) {
        this.productoEliminarView = productoEliminarView;
        configurarEliminarEventos();
    }

    public CarritoAnadirView getCarritoAnadirView() {
        return carritoAnadirView;
    }

    public void setCarritoAnadirView(CarritoAnadirView carritoAnadirView) {
        this.carritoAnadirView = carritoAnadirView;
    }
}