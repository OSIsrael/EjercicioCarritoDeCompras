package ec.edu.ups.poo.dao;

import ec.edu.ups.poo.dao.impl.*;

import java.io.File;

public class DAOFactory {

    private final UsuarioDAO usuarioDAO;
    private final ProductoDAO productoDAO;
    private final CarritoDAO carritoDAO;
    private final PreguntaDAO preguntaDAO;

    public DAOFactory(String tipo, String ruta) {
        switch (tipo.toUpperCase()) {
            case "TEXTO":
                this.usuarioDAO = new UsuarioDAOArchivoTexto(ruta + File.separator + "usuarios.txt");
                this.productoDAO = new ProductoDAOArchivoTexto(ruta + File.separator + "productos.txt");
                this.carritoDAO = new CarritoDAOArchivoTexto(ruta + File.separator + "carritos.txt");
                this.preguntaDAO = new PreguntaDAOArchivoTexto(ruta + File.separator + "preguntas.txt");
                break;
            case "BINARIO":

                this.usuarioDAO = new UsuarioDAOArchivoBinario(ruta + File.separator + "usuarios.dat");
                this.productoDAO = new ProductoDAOArchivoBinario(ruta + File.separator + "productos.dat");
                this.carritoDAO = new CarritoDAOArchivoBinario(ruta + File.separator + "carritos.dat");
                this.preguntaDAO = new PreguntaDAOArchivoBinario(ruta + File.separator + "preguntas.dat");
                break;

            default:
                this.usuarioDAO = new UsuarioDAOMemoria();
                this.productoDAO = new ProductoDAOMemoria();
                this.carritoDAO = new CarritoDAOMemoria();
                this.preguntaDAO = new PreguntaDAOMemoria();
                break;
        }
    }

    public UsuarioDAO getUsuarioDAO() { return this.usuarioDAO; }
    public ProductoDAO getProductoDAO() { return this.productoDAO; }
    public CarritoDAO getCarritoDAO() { return this.carritoDAO; }
    public PreguntaDAO getPreguntaDAO() {
        return this.preguntaDAO;
    }
}