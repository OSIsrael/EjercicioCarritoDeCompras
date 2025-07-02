package ec.edu.ups.poo.modelo;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Usuario {
    private String username;
    private String nombre;
    private String apellido;
    private String telefono;
    private int edad;
    private String password;
    private Rol rol;
    private Genero genero;

    private Map<Integer,String> preguntasSeguridad=new HashMap<>();

    public Usuario() {
    }


    public Usuario(String username, String password, Rol rol, Genero genero, String nombre, String apellido, String telefono, int edad) {
        this.username = username;
        this.password = password;
        this.rol = rol;
        this.genero = genero;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.edad = edad;
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public Genero getGenero() {
        return genero;
    }

    public void setGenero(Genero genero) {
        this.genero = genero;
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
    public Map<Integer, String> getPreguntasSeguridad() { return preguntasSeguridad; }
    public void setPreguntasSeguridad(Map<Integer, String> preguntasSeguridad) {
        this.preguntasSeguridad = preguntasSeguridad;
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