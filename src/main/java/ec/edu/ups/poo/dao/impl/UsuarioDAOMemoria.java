package ec.edu.ups.poo.dao.impl;

import ec.edu.ups.poo.dao.UsuarioDAO;
import ec.edu.ups.poo.modelo.Genero;
import ec.edu.ups.poo.modelo.Rol;
import ec.edu.ups.poo.modelo.Usuario;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UsuarioDAOMemoria implements UsuarioDAO {
    private List<Usuario> usuarios;

    public UsuarioDAOMemoria() {
        this.usuarios = new ArrayList<>();
        // Crear usuarios por defecto
        inicializarUsuariosPorDefecto();
    }

    private void inicializarUsuariosPorDefecto() {
        // Crear usuario administrador por defecto
        Usuario admin = new Usuario("admin", "12345", Rol.ADMINISTRADOR, Genero.MASCULINO, "Admin", "Principal", "000000000", 30);
        usuarios.add(admin);

        // Crear usuario normal por defecto
        Usuario usuarioNormal = new Usuario("usuario", "12345", Rol.USUARIO,Genero.FEMININO, "Usuario", "Principal", "099324321", 20);
        usuarios.add(usuarioNormal);

        // Crear algunos usuarios adicionales para pruebas
        Usuario usuario2 = new Usuario("juan", "1234", Rol.USUARIO,Genero.MASCULINO, "Juan", "Perez", "03232432434", 18);
        usuarios.add(usuario2);

        Usuario usuario3 = new Usuario("maria", "1234", Rol.USUARIO,Genero.FEMININO, "Maria", "Perez", "03232432434", 16);
        usuarios.add(usuario3);

        Usuario admin2 = new Usuario("supervisor", "admin", Rol.ADMINISTRADOR,Genero.MASCULINO, "Supervisor", "Principal", "03232432434", 20);
        usuarios.add(admin2);

        System.out.println("Usuarios inicializados: " + usuarios.size());
        imprimirUsuarios();
    }

    @Override
    public Usuario autenticar(String username, String password) {
        if (username == null || password == null) {
            return null;
        }

        for (Usuario usuario : usuarios) {
            if (usuario.getUsername().equals(username) && usuario.getPassword().equals(password)) {
                System.out.println("Autenticación exitosa para: " + username);
                return usuario;
            }
        }
        System.out.println("Autenticación fallida para: " + username);
        return null;
    }

    @Override
    public void crear(Usuario usuario) {
        if (usuario == null) {
            System.out.println("No se puede crear un usuario nulo");
            return;
        }

        if (usuario.getUsername() == null || usuario.getUsername().trim().isEmpty()) {
            System.out.println("No se puede crear un usuario sin nombre de usuario");
            return;
        }

        // Verificar que no exista un usuario con el mismo nombre
        if (buscarPorUsername(usuario.getUsername()) != null) {
            System.out.println("Ya existe un usuario con el nombre: " + usuario.getUsername());
            return;
        }

        usuarios.add(usuario);
        System.out.println("Usuario creado exitosamente: " + usuario.getUsername());
        System.out.println("Total de usuarios: " + usuarios.size());
    }

    @Override
    public Usuario buscarPorUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            return null;
        }

        for (Usuario usuario : usuarios) {
            if (usuario.getUsername().equals(username)) {
                return usuario;
            }
        }
        return null;
    }

    @Override
    public void actualizar(Usuario usuarioActualizado) {
        if (usuarioActualizado == null || usuarioActualizado.getUsername() == null) {
            System.out.println("No se puede actualizar un usuario nulo o sin nombre de usuario");
            return;
        }

        for (int i = 0; i < usuarios.size(); i++) {
            Usuario usuarioExistente = usuarios.get(i);
            if (usuarioExistente.getUsername().equals(usuarioActualizado.getUsername())) {
                usuarios.set(i, usuarioActualizado);
                System.out.println("Usuario actualizado exitosamente: " + usuarioActualizado.getUsername());
                System.out.println("Nueva información: " + usuarioActualizado);
                return;
            }
        }
        System.out.println("Usuario no encontrado para actualizar: " + usuarioActualizado.getUsername());
    }

    @Override
    public void eliminar(String username) {
        if (username == null || username.trim().isEmpty()) {
            System.out.println("No se puede eliminar un usuario sin nombre de usuario");
            return;
        }

        Iterator<Usuario> iterator = usuarios.iterator();
        while (iterator.hasNext()) {
            Usuario usuario = iterator.next();
            if (usuario.getUsername().equals(username)) {
                iterator.remove();
                System.out.println("Usuario eliminado exitosamente: " + username);
                System.out.println("Total de usuarios restantes: " + usuarios.size());
                return;
            }
        }
        System.out.println("Usuario no encontrado para eliminar: " + username);
    }

    @Override
    public List<Usuario> listarTodos() {
        System.out.println("Listando todos los usuarios. Total: " + usuarios.size());
        return new ArrayList<>(usuarios); // Devolver una copia para evitar modificaciones externas
    }

    @Override
    public List<Usuario> listarPorRol(Rol rol) {
        if (rol == null) {
            System.out.println("No se puede listar usuarios con rol nulo");
            return new ArrayList<>();
        }

        List<Usuario> usuariosFiltrados = new ArrayList<>();
        for (Usuario usuario : usuarios) {
            if (usuario.getRol() == rol) {
                usuariosFiltrados.add(usuario);
            }
        }

        System.out.println("Usuarios encontrados con rol " + rol + ": " + usuariosFiltrados.size());
        return usuariosFiltrados;
    }

    // Método auxiliar para debugging
    private void imprimirUsuarios() {
        System.out.println("=== Lista de usuarios ===");
        for (int i = 0; i < usuarios.size(); i++) {
            Usuario usuario = usuarios.get(i);
            System.out.println((i + 1) + ". " + usuario.toString());
        }
        System.out.println("========================");
    }

    // Método para obtener estadísticas
    public void mostrarEstadisticas() {
        System.out.println("=== Estadísticas de usuarios ===");
        System.out.println("Total de usuarios: " + usuarios.size());
        System.out.println("Administradores: " + listarPorRol(Rol.ADMINISTRADOR).size());
        System.out.println("Usuarios normales: " + listarPorRol(Rol.USUARIO).size());
        System.out.println("===============================");
    }
}