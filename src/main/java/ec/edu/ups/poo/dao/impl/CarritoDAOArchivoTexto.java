package ec.edu.ups.poo.dao.impl;

import ec.edu.ups.poo.dao.CarritoDAO;
import ec.edu.ups.poo.modelo.*;

import java.io.*;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CarritoDAOArchivoTexto implements CarritoDAO {
    private final String path;
    private int nextCodigo = 1;


    private static final String SEPARADOR_CARRITO = "%%CARRITO%%";
    private static final String SEPARADOR_CAMPO_USUARIO = ";";
    private static final String SEPARADOR_CAMPO_PRODUCTO = ";";
    private static final String SEPARADOR_MAPA_ENTRADA = "\\|";
    private static final String SEPARADOR_MAPA_KV = ":";
    private static final String SEPARADOR_ITEM_LISTA = "%%ITEM%%";
    private static final String SEPARADOR_ITEM_FIELD = ";;";

    public CarritoDAOArchivoTexto(String path) {
        this.path = path;
    }



    private String usuarioToString(Usuario usuario) {

        StringBuilder sb = new StringBuilder();
        sb.append(usuario.getUsername()).append(SEPARADOR_CAMPO_USUARIO);
        sb.append(usuario.getPassword()).append(SEPARADOR_CAMPO_USUARIO);
        sb.append(usuario.getRol()).append(SEPARADOR_CAMPO_USUARIO);
        sb.append(usuario.getGenero()).append(SEPARADOR_CAMPO_USUARIO);
        sb.append(usuario.getNombre()).append(SEPARADOR_CAMPO_USUARIO);
        sb.append(usuario.getApellido()).append(SEPARADOR_CAMPO_USUARIO);
        sb.append(usuario.getTelefono()).append(SEPARADOR_CAMPO_USUARIO);
        sb.append(usuario.getEdad()).append(SEPARADOR_CAMPO_USUARIO);
        sb.append(usuario.getTipoAlmacenamiento()).append(SEPARADOR_CAMPO_USUARIO);


        String preguntasStr = usuario.getPreguntasSeguridad().entrySet().stream()
                .map(entry -> entry.getKey() + SEPARADOR_MAPA_KV + entry.getValue())
                .collect(Collectors.joining(SEPARADOR_MAPA_ENTRADA.replace("\\", "")));
        sb.append(preguntasStr);

        return sb.toString();
    }

    private Usuario stringToUsuario(String linea) {
        String[] campos = linea.split(SEPARADOR_CAMPO_USUARIO, 11);
        if (campos.length < 9) return null;
        try {
            Usuario usuario = new Usuario(campos[0], campos[1], Rol.valueOf(campos[2]), Genero.valueOf(campos[3]),
                    campos[4], campos[5], campos[6], Integer.parseInt(campos[7]), campos[8]);
            

            if (campos.length > 9 && !campos[9].isEmpty()) {
                Map<Integer, String> preguntas = new HashMap<>();
                String[] pares = campos[9].split(SEPARADOR_MAPA_ENTRADA);
                for (String par : pares) {
                    String[] kv = par.split(SEPARADOR_MAPA_KV);
                    if (kv.length == 2) {
                        preguntas.put(Integer.parseInt(kv[0]), kv[1]);
                    }
                }
                usuario.setPreguntasSeguridad(preguntas);
            }
            return usuario;
        } catch (Exception e) {

            System.err.println("Error al parsear usuario desde CarritoDAO: " + linea + ". Error: " + e.getMessage());
            return null;
        }
    }

    private String productoToString(Producto producto) {
        return producto.getCodigo() + SEPARADOR_CAMPO_PRODUCTO + producto.getNombre() + SEPARADOR_CAMPO_PRODUCTO + producto.getPrecio();
    }

    private Producto stringToProducto(String linea) {
        String[] campos = linea.split(SEPARADOR_CAMPO_PRODUCTO);
        if (campos.length == 3) {
            try {
                return new Producto(Integer.parseInt(campos[0]), campos[1], Double.parseDouble(campos[2]));
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    private String itemToString(ItemCarrito item) {
        return productoToString(item.getProducto()) + SEPARADOR_ITEM_FIELD + item.getCantidad();
    }

    private ItemCarrito stringToItem(String linea) {
        String[] campos = linea.split(SEPARADOR_ITEM_FIELD);
        if (campos.length == 2) {
            Producto p = stringToProducto(campos[0]);
            try {
                int cantidad = Integer.parseInt(campos[1]);
                if (p != null) {
                    return new ItemCarrito(p, cantidad);
                }
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }



    private List<Carrito> cargarCarritos() throws IOException {
        List<Carrito> carritos = new ArrayList<>();
        File archivo = new File(path);
        if (!archivo.exists()) {
            return carritos;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partesCarrito = linea.split(SEPARADOR_CARRITO, 4);
                if (partesCarrito.length >= 3) {
                    try {
                        int codigo = Integer.parseInt(partesCarrito[0]);
                        long fechaMillis = Long.parseLong(partesCarrito[1]);
                        Usuario usuario = stringToUsuario(partesCarrito[2]);

                        List<ItemCarrito> items = new ArrayList<>();
                        if (partesCarrito.length == 4 && !partesCarrito[3].isEmpty()) {
                            String[] itemsStr = partesCarrito[3].split(SEPARADOR_ITEM_LISTA);
                            for (String itemStr : itemsStr) {
                                ItemCarrito item = stringToItem(itemStr);
                                if (item != null) items.add(item);
                            }
                        }

                        if (usuario != null) {
                            Carrito carrito = new Carrito();
                            carrito.setCodigo(codigo);
                            GregorianCalendar fecha = new GregorianCalendar();
                            fecha.setTimeInMillis(fechaMillis);
                            carrito.setFechaCreacion(fecha);
                            carrito.setUsuario(usuario);
                            carrito.setItems(items);
                            carritos.add(carrito);
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Error de formato al cargar carrito (se ignora línea): " + linea);
                    }
                }
            }
        }
        if (!carritos.isEmpty()) {
            nextCodigo = carritos.stream().mapToInt(Carrito::getCodigo).max().orElse(0) + 1;
        }
        return carritos;
    }

    private void guardarCarritos(List<Carrito> carritos) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            for (Carrito c : carritos) {
                if (c.getUsuario() == null || c.getFechaCreacion() == null) continue;

                String usuarioStr = usuarioToString(c.getUsuario());
                String itemsStr = c.getItems().stream()
                        .map(this::itemToString)
                        .collect(Collectors.joining(SEPARADOR_ITEM_LISTA));

                String linea = String.join(SEPARADOR_CARRITO,
                        String.valueOf(c.getCodigo()),
                        String.valueOf(c.getFechaCreacion().getTimeInMillis()),
                        usuarioStr,
                        itemsStr);
                bw.write(linea);
                bw.newLine();
            }
        }
    }



    @Override
    public void crear(Carrito carrito) throws IOException {
        List<Carrito> carritos = cargarCarritos();
        if (carrito.getCodigo() == 0) {
            carrito.setCodigo(nextCodigo++);
        }
        carritos.add(carrito);
        guardarCarritos(carritos);
    }

    @Override
    public void eliminar(int codigo) throws IOException {
        List<Carrito> carritos = cargarCarritos();
        carritos.removeIf(c -> c.getCodigo() == codigo);
        guardarCarritos(carritos);
    }

    @Override
    public void actualizar(Carrito carrito) throws IOException {
        List<Carrito> carritos = cargarCarritos();
        for (int i = 0; i < carritos.size(); i++) {
            if (carritos.get(i).getCodigo() == carrito.getCodigo()) {
                carritos.set(i, carrito);
                guardarCarritos(carritos);
                return;
            }
        }
    }

    @Override
    public Carrito buscar(int codigo) throws IOException {
        return cargarCarritos().stream().filter(c -> c.getCodigo() == codigo).findFirst().orElse(null);
    }

    @Override
    public List<Carrito> listarTodos() throws IOException {
        return cargarCarritos();
    }

    @Override
    public List<Carrito> listarPorUsuario(Usuario usuario) throws IOException {
        return cargarCarritos().stream()
                .filter(c -> c.getUsuario() != null && c.getUsuario().equals(usuario))
                .collect(Collectors.toList());
    }
}

