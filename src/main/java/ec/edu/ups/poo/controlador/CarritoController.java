package ec.edu.ups.poo.controlador;

import ec.edu.ups.poo.dao.CarritoDAO;
import ec.edu.ups.poo.dao.ProductoDAO;
import ec.edu.ups.poo.modelo.Carrito;
import ec.edu.ups.poo.modelo.ItemCarrito;
import ec.edu.ups.poo.modelo.Producto;
import ec.edu.ups.poo.modelo.Usuario;
import ec.edu.ups.poo.modelo.Rol;
import ec.edu.ups.poo.view.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class CarritoController {
    // Vistas
    private final CarritoAnadirView carritoAnadirView;
    private final CarritoListarView carritoListarView;
    private final CarritoBuscarView carritoBuscarView;
    private final CarritoModificarView carritoModificarView;
    private final CarritoEliminarView carritoEliminarView;

    // DAOs y Modelos
    private final CarritoDAO carritoDAO;
    private final ProductoDAO productoDAO;
    private final Usuario usuarioAutenticado;
    private Carrito carritoActual; // Para la vista de Añadir
    private Carrito carritoParaModificar; // Para la vista de Modificar
    private Carrito carritoParaEliminar; // Para la vista de Eliminar

    public CarritoController(CarritoDAO carritoDAO, ProductoDAO productoDAO,
                             CarritoAnadirView carritoAnadirView, Usuario usuarioAutenticado,
                             CarritoListarView carritoListarView, CarritoBuscarView carritoBuscarView,
                             CarritoModificarView carritoModificarView, CarritoEliminarView carritoEliminarView) {
        this.carritoDAO = carritoDAO;
        this.productoDAO = productoDAO;
        this.usuarioAutenticado = usuarioAutenticado;

        // Asignación de vistas
        this.carritoAnadirView = carritoAnadirView;
        this.carritoListarView = carritoListarView;
        this.carritoBuscarView = carritoBuscarView;
        this.carritoModificarView = carritoModificarView;
        this.carritoEliminarView = carritoEliminarView;

        iniciarNuevoCarrito(); // Inicializa el carrito para la vista de añadir

        // Configuración de eventos para cada vista
        configurarEventosAnadir();
        configurarEventosListar();
        configurarEventosBuscar();
        configurarEventosModificar();
        configurarEventosEliminar();
    }

    private void iniciarNuevoCarrito() {
        this.carritoActual = new Carrito();
        this.carritoActual.setUsuario(this.usuarioAutenticado);
    }

    // --- Lógica de CarritoAnadirView ---
    private void configurarEventosAnadir() {
        carritoAnadirView.getBtnAnadir().addActionListener(e -> anadirProducto());
        carritoAnadirView.getBtnGuardar().addActionListener(e -> guardarCarrito());
        carritoAnadirView.getBtnLimpiar().addActionListener(e -> limpiarCarritoActual());
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
        if (cantidadObj == null || !esNumeroEntero(cantidadObj.toString())) {
            carritoAnadirView.mostrarMensaje("La cantidad seleccionada no es válida.");
            return;
        }
        int cantidad = Integer.parseInt(cantidadObj.toString());

        carritoActual.agregarProducto(producto, cantidad);
        actualizarVistaAnadir();
        limpiarCamposProductoAnadir();
    }

    private void guardarCarrito() {
        if (carritoActual.estaVacio()) {
            carritoAnadirView.mostrarMensaje("El carrito está vacío.");
            return;
        }
        carritoDAO.crear(carritoActual);
        carritoAnadirView.mostrarMensaje("Carrito guardado exitosamente con código: " + carritoActual.getCodigo());
        limpiarCarritoActual();
    }

    private void limpiarCarritoActual() {
        iniciarNuevoCarrito();
        actualizarVistaAnadir();
        limpiarCamposProductoAnadir();
    }

    private void actualizarVistaAnadir() {
        DefaultTableModel modelo = (DefaultTableModel) carritoAnadirView.getTblMostrar().getModel();
        modelo.setRowCount(0);
        for (ItemCarrito item : carritoActual.obtenerItems()) {
            modelo.addRow(new Object[]{
                    item.getProducto().getCodigo(), item.getProducto().getNombre(),
                    item.getProducto().getPrecio(), item.getCantidad(), item.getSubtotal()
            });
        }
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

    // --- Lógica de CarritoListarView ---
    private void configurarEventosListar() {
        carritoListarView.getBtnListar().addActionListener(e -> listarCarritosDelUsuario());
    }

    public void listarCarritosDelUsuario() {
        List<Carrito> carritos;
        if (usuarioAutenticado.getRol() == Rol.ADMINISTRADOR) {
            carritos = carritoDAO.listarTodos(); // Todos los carritos
        } else {
            carritos = carritoDAO.listarPorUsuario(usuarioAutenticado);
        }
        carritoListarView.cargarCarritos(carritos);
        carritoListarView.mostrarDetallesCarrito(null); // Limpia detalles al cargar lista
    }
    // --- Lógica de CarritoBuscarView ---
    private void configurarEventosBuscar() {
        carritoBuscarView.getBtnBuscarCarrito().addActionListener(e -> buscarCarritoParaVer());
    }

    private void buscarCarritoParaVer() {
        String codigoStr = carritoBuscarView.getTxtBuscarCarrito().getText().trim();
        if (!esNumeroEntero(codigoStr)) {
            carritoBuscarView.mostrarMensaje("El código debe ser un número válido.");
            carritoBuscarView.mostrarCarrito(null);
            return;
        }
        int codigo = Integer.parseInt(codigoStr);
        Carrito carrito = carritoDAO.buscar(codigo);
        if (carrito == null || !tienePermiso(carrito)) {
            carritoBuscarView.mostrarMensaje("No tiene permisos para ver este carrito o no existe.");
            carritoBuscarView.mostrarCarrito(null);
            return;
        }
        carritoBuscarView.mostrarCarrito(carrito);
    }

    // --- Lógica de CarritoEliminarView ---
    private void configurarEventosEliminar() {
        carritoEliminarView.getBtnBuscar().addActionListener(e -> buscarCarritoParaEliminar());
        carritoEliminarView.getBtnEliminar().addActionListener(e -> eliminarCarritoConfirmado());
    }

    private void buscarCarritoParaEliminar() {
        String codigoStr = carritoEliminarView.getTxtEliminarCarrito().getText().trim();
        if (!esNumeroEntero(codigoStr)) {
            carritoEliminarView.mostrarMensaje("El código debe ser un número válido.");
            carritoEliminarView.mostrarCarrito(null);
            this.carritoParaEliminar = null;
            return;
        }
        int codigo = Integer.parseInt(codigoStr);
        Carrito carrito = carritoDAO.buscar(codigo);
        if (carrito == null || !tienePermiso(carrito)) {
            carritoEliminarView.mostrarMensaje("No tiene permisos para eliminar este carrito o no existe.");
            carritoEliminarView.mostrarCarrito(null);
            this.carritoParaEliminar = null;
            return;
        }
        this.carritoParaEliminar = carrito;
        carritoEliminarView.mostrarCarrito(carrito);
    }

    private void eliminarCarritoConfirmado() {
        if (this.carritoParaEliminar == null) {
            carritoEliminarView.mostrarMensaje("Primero debe buscar un carrito válido para eliminar.");
            return;
        }
        // SIN confirmación extra, elimina directo
        carritoDAO.eliminar(carritoParaEliminar.getCodigo());
        carritoEliminarView.mostrarMensaje("Carrito eliminado exitosamente.");
        carritoEliminarView.limpiarVista();
        this.carritoParaEliminar = null;
    }

    // --- Lógica de CarritoModificarView ---
    private void configurarEventosModificar() {
        carritoModificarView.getBtnBuscar().addActionListener(e -> buscarCarritoParaModificar());
        carritoModificarView.getBtnModificar().addActionListener(e -> guardarCambiosCarrito());
    }

    private void buscarCarritoParaModificar() {
        String codigoStr = carritoModificarView.getTxtCodigo().getText().trim();
        if (!esNumeroEntero(codigoStr)) {
            carritoModificarView.mostrarError("El código debe ser un número válido.");
            carritoModificarView.cargarCarrito(null);
            this.carritoParaModificar = null;
            return;
        }
        int codigo = Integer.parseInt(codigoStr);
        Carrito carrito = carritoDAO.buscar(codigo);
        if (carrito == null || !tienePermiso(carrito)) {
            carritoModificarView.mostrarError("No tiene permisos para modificar este carrito o no existe.");
            carritoModificarView.cargarCarrito(null);
            this.carritoParaModificar = null;
            return;
        }
        this.carritoParaModificar = carrito;
        carritoModificarView.cargarCarrito(carrito);
    }

    private void guardarCambiosCarrito() {
        if (this.carritoParaModificar == null) {
            carritoModificarView.mostrarError("Primero debe buscar un carrito válido para modificar.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(carritoModificarView, "¿Está seguro de modificar este carrito?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        // Leer los cambios en la tabla tblModificar (ejemplo, solo cantidad)
        JTable tbl = carritoModificarView.getTblModificar();
        for (int i = 0; i < tbl.getRowCount(); i++) {
            int codigoProd = (int) tbl.getValueAt(i, 0);
            int nuevaCantidad = Integer.parseInt(tbl.getValueAt(i, 2).toString());
            for (ItemCarrito item : carritoParaModificar.obtenerItems()) {
                if (item.getProducto().getCodigo() == codigoProd) {
                    item.setCantidad(nuevaCantidad);
                }
            }
        }

        carritoDAO.actualizar(this.carritoParaModificar);
        carritoModificarView.mostrarMensaje("Carrito modificado exitosamente.");
        carritoModificarView.limpiarVista();
        this.carritoParaModificar = null;
    }

    // --- Métodos Auxiliares ---
    private boolean esNumeroEntero(String texto) {
        if (texto == null || texto.trim().isEmpty()) return false;
        try {
            Integer.parseInt(texto);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Verifica si el usuario autenticado tiene permiso para ver/modificar/eliminar un carrito
    private boolean tienePermiso(Carrito carrito) {
        if (usuarioAutenticado.getRol() == Rol.ADMINISTRADOR) {
            return true;
        }
        return carrito.getUsuario().equals(usuarioAutenticado);
    }
}