package ec.edu.ups.poo.modelo;

import java.util.Objects;

public class Usuario {
    private String username;
    private String password;
    private Rol rol;

    public Usuario() {
    }

    public Usuario(String username, String password, Rol rol) {
        this.username = username;
        this.password = password;
        this.rol = rol;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    /**
     * Compara dos objetos Usuario basándose en su 'username'.
     * Es crucial para que las búsquedas y comparaciones funcionen correctamente.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(username, usuario.username); // Comparamos por username
    }

    /**
     * Genera un código hash basado en el 'username'.
     * Debe ser consistente con equals().
     */
    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    /**
     * Se modifica para devolver solo el username, lo cual es más útil
     * para mostrar en componentes de la interfaz de usuario (como etiquetas o tablas).
     */
    @Override
    public String toString() {
        return username;
    }
}