package ec.edu.ups.poo.controlador;

import com.sun.source.tree.IfTree;
import ec.edu.ups.poo.dao.CarritoDAO;
import ec.edu.ups.poo.dao.ProductoDAO;
import ec.edu.ups.poo.modelo.Carrito;
import ec.edu.ups.poo.modelo.ItemCarrito;
import ec.edu.ups.poo.modelo.Producto;
import ec.edu.ups.poo.modelo.Usuario;
import ec.edu.ups.poo.modelo.Rol;
import ec.edu.ups.poo.util.Idioma;
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
    private List<Carrito> carritosUltimosMostradosModificar;
    private List<Carrito> carritosUltimosMostradosBuscar;
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
        this.carritosUltimosMostradosModificar = null;
        this.carritosUltimosMostradosBuscar = null;



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
            carritoAnadirView.mostrarMensaje(Idioma.get("carrito.controller.msj.numentero"));
            return;
        }
        int codigo = Integer.parseInt(codigoStr);
        Producto producto = productoDAO.buscarPorCodigo(codigo);

        if (producto == null) {
            carritoAnadirView.mostrarMensaje(Idioma.get("carrito.controller.msj.pnoe"));
            return;
        }

        Object cantidadObj = carritoAnadirView.getCbxCanridad().getSelectedItem();
        if (cantidadObj == null || !esNumeroEntero(cantidadObj.toString())) {
            carritoAnadirView.mostrarMensaje(Idioma.get("carrito.controller.msj.cantsele"));
            return;
        }
        int cantidad = Integer.parseInt(cantidadObj.toString());

        carritoActual.agregarProducto(producto, cantidad);
        actualizarVistaAnadir();
        limpiarCamposProductoAnadir();
    }

    private void guardarCarrito() {
        if (carritoActual.estaVacio()) {
            carritoAnadirView.mostrarMensaje("carrito.controller.msj.carvac");
            return;
        }
        carritoDAO.crear(carritoActual);
        carritoAnadirView.mostrarMensajeConParametros("carrito.anadir.msj.guardado", carritoActual.getCodigo());
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
        carritoBuscarView.getBtnListar().addActionListener(e -> listarCarritosBuscar());
    }

    // Al mostrar la ventana (en Main):
    public void mostrarCarritosUsuarioParaBuscar() {
        List<Carrito> carritos = carritoDAO.listarPorUsuario(usuarioAutenticado);
        carritoBuscarView.cargarCarritosUsuario(carritos);
        this.carritosUltimosMostradosBuscar = carritos;
    }

    // Buscar por código
    private void buscarCarritoParaVer() {
        String codigoStr = carritoBuscarView.getTxtBuscarCarrito().getText().trim();
        if (!esNumeroEntero(codigoStr)) {
            carritoBuscarView.mostrarMensaje(Idioma.get("carrito.controller.msj.numin"));
            carritoBuscarView.mostrarCarrito(null);
            return;
        }
        int codigo = Integer.parseInt(codigoStr);
        Carrito carrito = carritoDAO.buscar(codigo);
        if (carrito == null || !tienePermiso(carrito)) {
            carritoBuscarView.mostrarMensaje(Idioma.get("carrito.controller.msj.noper"));
            carritoBuscarView.mostrarCarrito(null);
            return;
        }
        carritoBuscarView.mostrarCarrito(carrito);
    }

    // Listar todos los carritos (al presionar btnListar)
    private void listarCarritosBuscar() {
        mostrarCarritosUsuarioParaBuscar();
    }

    // --- Lógica de CarritoEliminarView ---
    public void mostrarCarritosUsuarioParaEliminar() {
        List<Carrito> carritos = carritoDAO.listarPorUsuario(usuarioAutenticado);
        carritoEliminarView.cargarCarritosUsuario(carritos);
    }

    // Configuración de eventos
    private void configurarEventosEliminar() {
        carritoEliminarView.getBtnEliminar().addActionListener(e -> eliminarCarritoConfirmado());
        carritoEliminarView.getBtnListar().addActionListener(e -> mostrarCarritosUsuarioParaEliminar());
    }

    private void eliminarCarritoConfirmado() {
        Carrito carrito = carritoEliminarView.getCarritoSeleccionado();
        if (carrito == null) {
            carritoEliminarView.mostrarMensaje(Idioma.get("carrito.controller.msj.selecar"));
            return;
        }
        boolean confirmar = carritoEliminarView.confirmarAccion(Idioma.get("carrito.controller.msj.estaseguro"));
        if (!confirmar) return;

        carritoDAO.eliminar(carrito.getCodigo());
        carritoEliminarView.mostrarMensaje(Idioma.get("carrito.controller.msj.eliminado"));
        mostrarCarritosUsuarioParaEliminar(); // Refresca la lista
    }

    // --- Lógica de CarritoModificarView ---
    private void configurarEventosModificar() {
        carritoModificarView.getBtnBuscar().addActionListener(e -> buscarCarritoParaModificar());
        carritoModificarView.getBtnModificar().addActionListener(e -> guardarCambiosCarrito());
    }

    private void buscarCarritoParaModificar() {
        String codigoStr = carritoModificarView.getTxtCodigo().getText().trim();
        if (!esNumeroEntero(codigoStr)) {
            carritoModificarView.mostrarError(Idioma.get("carrito.controller.msj.numin"));
            carritoModificarView.cargarCarrito(null);
            return;
        }
        int codigo = Integer.parseInt(codigoStr);
        Carrito carrito = carritoDAO.buscar(codigo);
        if (carrito == null || !tienePermiso(carrito)) {
            carritoModificarView.mostrarError(Idioma.get("carrito.controller.msj.noper"));
            carritoModificarView.cargarCarrito(null);
            return;
        }
        carritoModificarView.cargarCarrito(carrito);
    }

    private void guardarCambiosCarrito() {
        int fila = carritoModificarView.getTblModificar().getSelectedRow();
        if (fila == -1) {
            carritoModificarView.mostrarError(Idioma.get("carrito.controller.msj.modificarno"));
            return;
        }
        int codigoCarrito = (int) carritoModificarView.getTblModificar().getValueAt(fila, 0);
        Carrito carrito = carritoDAO.buscar(codigoCarrito);

        if (carrito == null) {
            carritoModificarView.mostrarError(Idioma.get("carrito.controller.mjs.noencontrado"));
            return;
        }
        int nuevaCantidadTotal;
        try {
            nuevaCantidadTotal = Integer.parseInt(carritoModificarView.getTblModificar().getValueAt(fila, 3).toString());
        } catch (NumberFormatException e) {
            carritoModificarView.mostrarError(Idioma.get("carrito.controller.msj.invalida"));
            return;
        }

        int cantidadOriginal = carrito.obtenerItems().stream().mapToInt(ItemCarrito::getCantidad).sum();
        if (cantidadOriginal == 0) {
            carritoModificarView.mostrarError(Idioma.get("carrito.controller.msj.noproductos"));
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(carritoModificarView, Idioma.get("carrito.controller.msj.seguromod"), Idioma.get("carrito.controller.msj.confirmar"), JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        // Repartir la cantidad nueva proporcionalmente entre los items
        List<ItemCarrito> items = carrito.obtenerItems();
        int cantidadAcumulada = 0;
        int totalItems = items.size();

        for (int i = 0; i < totalItems; i++) {
            ItemCarrito item = items.get(i);
            int cantidadAntigua = item.getCantidad();
            int nuevaCantidad;
            if (i < totalItems - 1) {
                nuevaCantidad = (int) Math.round((cantidadAntigua * 1.0 / cantidadOriginal) * nuevaCantidadTotal);
                cantidadAcumulada += nuevaCantidad;
            } else {
                // Al último le asigno el resto para que cuadre exacto el total
                nuevaCantidad = nuevaCantidadTotal - cantidadAcumulada;
            }
            item.setCantidad(nuevaCantidad);
        }

        carritoDAO.actualizar(carrito);
        carritoModificarView.mostrarMensaje(Idioma.get("carrito.controller.msj.exitoso"));
        mostrarCarritosUsuarioParaModificar(); // refresca la lista
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
    public void mostrarCarritosUsuarioParaModificar() {
        List<Carrito> carritos = carritoDAO.listarPorUsuario(usuarioAutenticado);
        carritoModificarView.cargarCarritosUsuario(carritos);
        this.carritosUltimosMostradosModificar = carritos;
    }

}