package ec.edu.ups.poo.modelo;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

public class Carrito {
    private int codigo;
    private GregorianCalendar fechaCreacion;
    private List<ItemCarrito> items;
    public Carrito() {
        items = new ArrayList<>();
        fechaCreacion=new GregorianCalendar();
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public GregorianCalendar getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(GregorianCalendar fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public List<ItemCarrito> getItems() {
        return items;
    }

    public void setItems(List<ItemCarrito> items) {
        this.items = items;
    }

    public void agregarProducto(Producto producto, int cantidad) {
        items.add(new ItemCarrito(producto, cantidad));
    }


    public void eliminarProducto(int codigoProducto) {
        Iterator<ItemCarrito> it = items.iterator();
        while (it.hasNext()) {
            if (it.next().getProducto().getCodigo() == codigoProducto) {
                it.remove();
                break;
            }
        }
    }


    public void vaciarCarrito() {
        items.clear();
    }


    public List<ItemCarrito> obtenerItems() {
        return items;
    }

    public boolean estaVacio() {
        return items.isEmpty();
    }
    public double calcularSubtotal(){
        double subtotal=0;
        for (ItemCarrito item : items) {
            subtotal+=item.getSubtotal();
        }
        return subtotal;
    }
    public double calcularIva(){
        return calcularSubtotal()*0.12;
    }
    public double calcularTotal(){
        return calcularSubtotal()+calcularIva();
    }


}
