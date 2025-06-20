package ec.edu.ups.poo.controlador;

import ec.edu.ups.poo.dao.CarritoDAO;
import ec.edu.ups.poo.dao.ProductoDAO;
import ec.edu.ups.poo.modelo.Carrito;
import ec.edu.ups.poo.modelo.ItemCarrito;
import ec.edu.ups.poo.modelo.Producto;
import ec.edu.ups.poo.view.CarritoAnadirView;

import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class CarritoController {
    private final CarritoDAO carritoDAO;
    private final CarritoAnadirView carritoAnadirView;
    private final ProductoDAO productoDAO;
    private Carrito carrito;
    public CarritoController(CarritoDAO carritoDAO,ProductoDAO productoDAO, CarritoAnadirView carritoAnadirView) {
        this.carritoDAO = carritoDAO;
        this.carritoAnadirView = carritoAnadirView;
        this.productoDAO = productoDAO;
        this.carrito = new Carrito();
        configurarEventosEnVistas();
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
    }
    private void anadirProducto() {
        int codigo= Integer.parseInt(carritoAnadirView.getTxtCodigo().getText());
        Producto producto=productoDAO.buscarPorCodigo(codigo);
        int cantidad=Integer.parseInt(carritoAnadirView.getCbxCanridad().getSelectedItem().toString());
        carrito.agregarProducto(producto,cantidad);
        cargarProductos();
        mostrarTotales();
    }
    private void cargarProductos(){
        List<ItemCarrito> items=carrito.obtenerItems();
        DefaultTableModel modelo=(DefaultTableModel) carritoAnadirView.getTblMostrar().getModel();
        modelo.setRowCount(0);
        for (ItemCarrito item : items) {
            String subtotal=String.valueOf(item.getProducto().getPrecio()*item.getCantidad());
            modelo.addRow(new Object[]{item.getProducto().getCodigo(),
                                       item.getProducto().getNombre(),
                                       item.getProducto().getPrecio(),
                                       item.getCantidad(),
                                       subtotal});

        }
    }
    private void mostrarTotales(){
        String total=String.valueOf(carrito.calcularTotal());
        String subtotal=String.valueOf(carrito.calcularSubtotal());
        String iva=String.valueOf(carrito.calcularIva());
        carritoAnadirView.getTxtTotal().setText(total);
        carritoAnadirView.getTxtSubtotal().setText(subtotal);
        carritoAnadirView.getTxtIva().setText(iva);

    }
    private void guardarCarrito(){
        carritoDAO.crear(carrito);
        carritoAnadirView.mostrarMensaje("Carrito guardado correctamente");
    }


}
