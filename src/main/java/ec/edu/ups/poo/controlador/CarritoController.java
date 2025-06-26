package ec.edu.ups.poo.controlador;

import ec.edu.ups.poo.dao.CarritoDAO;
import ec.edu.ups.poo.dao.ProductoDAO;
import ec.edu.ups.poo.modelo.Carrito;
import ec.edu.ups.poo.modelo.ItemCarrito;
import ec.edu.ups.poo.modelo.Producto;
import ec.edu.ups.poo.modelo.Usuario;
import ec.edu.ups.poo.view.*;

import javax.swing.*;
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
    private Carrito carritoParaEliminar; // Para la vista de Eliminar

    public CarritoController(CarritoDAO carritoDAO, ProductoDAO productoDAO,
                             CarritoAnadirView carritoAnadirView, Usuario usuarioAutenticado,
                             CarritoListarView carritoListarView, CarritoModificarView carritoModificarView,
                             CarritoBuscarView carritoBuscarView, CarritoEliminarView carritoEliminarView) {
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
        configurarEventosListar(); // Nuevo
        configurarEventosBuscar();
        configurarEventosModificar(); // Renombrado
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
        iniciarNuevoCarrito(); // Reinicia el carrito actual para una nueva operación
        actualizarVistaAnadir(); // Limpia la tabla y totales en la vista
        limpiarCamposProductoAnadir(); // Limpia los campos de entrada
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
        // Si el botón btnListar existe en CarritoListarView, se le puede añadir un listener
        // para recargar la lista. Si no, la carga inicial la hace el Main.
        if (carritoListarView.getBtnListar() != null) {
            carritoListarView.getBtnListar().addActionListener(e -> listarTodosLosCarritos());
        }
    }

    public void listarTodosLosCarritos() {
        List<Carrito> carritos = carritoDAO.listarTodos();
        carritoListarView.cargarCarritos(carritos);
    }

    // --- Lógica de CarritoBuscarView ---
    private void configurarEventosBuscar() {
        carritoBuscarView.getBtnBuscarCarrito().addActionListener(e -> buscarCarritoParaVer());
    }

    private void buscarCarritoParaVer() {
        String codigoStr = carritoBuscarView.getTxtBuscarCarrito().getText().trim();

        if (!esNumeroEntero(codigoStr)) {
            carritoBuscarView.mostrarMensaje("El código debe ser un número válido.");
            carritoBuscarView.mostrarCarrito(null); // Limpiar vista si la entrada es inválida
            return;
        }
        int codigo = Integer.parseInt(codigoStr);
        Carrito carrito = carritoDAO.buscarPorCodigo(codigo);

        if (carrito != null && !tienePermiso(carrito)) {
            carritoBuscarView.mostrarMensaje("No tiene permisos para ver este carrito.");
            carritoBuscarView.mostrarCarrito(null); // Limpiar vista si no hay permiso
            return;
        }

        if (carrito == null) {
            carritoBuscarView.mostrarMensaje("No se encontró un carrito con ese código.");
        }
        carritoBuscarView.mostrarCarrito(carrito);
    }

    // --- Lógica de CarritoEliminarView ---
    private void configurarEventosEliminar() {
        carritoEliminarView.getBtnBuscar().addActionListener(e -> buscarCarritoParaEliminar());
        carritoEliminarView.getBtnEliminar().addActionListener(e -> eliminarCarrito());
    }

    private void buscarCarritoParaEliminar() {
        String codigoStr = carritoEliminarView.getTxtEliminarCarrito().getText().trim();

        if (!esNumeroEntero(codigoStr)) {
            carritoEliminarView.mostrarMensaje("El código debe ser un número válido.");
            carritoEliminarView.mostrarCarrito(null); // Limpiar vista si la entrada es inválida
            return;
        }
        int codigo = Integer.parseInt(codigoStr);
        Carrito carrito = carritoDAO.buscarPorCodigo(codigo);

        if (carrito != null && !tienePermiso(carrito)) {
            carritoEliminarView.mostrarMensaje("No tiene permisos para eliminar este carrito.");
            this.carritoParaEliminar = null;
            carritoEliminarView.mostrarCarrito(null); // Limpiar vista si no hay permiso
            return;
        }

        if (carrito == null) {
            carritoEliminarView.mostrarMensaje("No se encontró un carrito con ese código.");
        }

        this.carritoParaEliminar = carrito;
        carritoEliminarView.mostrarCarrito(carrito); // Usar mostrarCarrito para cargar en ambas tablas
    }

    private void eliminarCarrito() {
        if (this.carritoParaEliminar == null) {
            carritoEliminarView.mostrarMensaje("Primero debe buscar un carrito válido para eliminar.");
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(
                carritoEliminarView,
                "¿Está seguro de que desea eliminar permanentemente el carrito " + carritoParaEliminar.getCodigo() + "?",
                "Confirmar Eliminación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirmacion == JOptionPane.YES_OPTION) {
            carritoDAO.eliminar(carritoParaEliminar.getCodigo());
            carritoEliminarView.mostrarMensaje("Carrito eliminado exitosamente.");
            carritoEliminarView.limpiarVista();
            this.carritoParaEliminar = null;
        }
    }

    // --- Lógica de CarritoModificarView ---
    private void configurarEventosModificar() { // Renombrado
        carritoModificarView.getBtnModificar().addActionListener(e -> buscarCarritoParaModificar()); // Usar btnModificar para buscar
    }

    private void buscarCarritoParaModificar() {
        String codigoStr = carritoModificarView.getTxtCodigo().getText().trim(); // Usar txtCodigo
        if (!esNumeroEntero(codigoStr)) {
            carritoModificarView.mostrarError("El código debe ser un número válido.");
            carritoModificarView.cargarCarrito(null); // Limpiar vista si la entrada es inválida
            return;
        }
        int codigo = Integer.parseInt(codigoStr);
        Carrito carrito = carritoDAO.buscarPorCodigo(codigo);

        if (carrito != null && !tienePermiso(carrito)) {
            carritoModificarView.mostrarError("No tiene permisos para modificar este carrito.");
            carritoModificarView.cargarCarrito(null); // Limpiar vista si no hay permiso
            return;
        }

        if (carrito == null) {
            carritoModificarView.mostrarError("No se encontró un carrito con ese código.");
        }
        carritoModificarView.cargarCarrito(carrito);
    }

    // --- Métodos Auxiliares ---
    private boolean esNumeroEntero(String texto) {
        if (texto == null || texto.trim().isEmpty()) return false;
        for (char c : texto.toCharArray()) {
            if (!Character.isDigit(c)) return false;
        }
        return true;
    }

    // Verifica si el usuario autenticado tiene permiso para ver/modificar/eliminar un carrito
    private boolean tienePermiso(Carrito carrito) {
        if (usuarioAutenticado.getRol() == ec.edu.ups.poo.modelo.Rol.ADMINISTRADOR) {
            return true; // El admin puede ver/modificar/eliminar todo
        }
        // Un usuario normal solo puede ver/modificar/eliminar sus propios carritos
        return carrito.getUsuario().getUsername().equals(usuarioAutenticado.getUsername());
    }
}