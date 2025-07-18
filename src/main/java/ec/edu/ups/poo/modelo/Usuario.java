package ec.edu.ups.poo.modelo;

import ec.edu.ups.poo.util.CedulaInvalidaException;
import ec.edu.ups.poo.util.PasswordInvalidaException;
import ec.edu.ups.poo.util.Idioma;

import java.util.HashMap;
import java.util.Map;
import java.io.Serializable;
import java.util.Objects;

public class Usuario implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private String username;
    private String nombre;
    private String apellido;
    private String telefono;
    private int edad;
    private String password;
    private Rol rol;
    private Genero genero;
    private Map<Integer,String> preguntasSeguridad = new HashMap<>();
    private String tipoAlmacenamiento;

    public Usuario() {
    }


    public Usuario(String username, String password, Rol rol, Genero genero, String nombre, String apellido, String telefono, int edad)
            throws CedulaInvalidaException, PasswordInvalidaException {
        this.setUsername(username);
        this.setPassword(password);
        this.rol = rol;
        this.genero = genero;
        this.nombre = nombre;
        this.apellido = apellido;
        this.telefono = telefono;
        this.edad = edad;
        this.tipoAlmacenamiento = "MEMORIA";
    }
    

    public Usuario(String username, String password, Rol rol, Genero genero, String nombre, String apellido, String telefono, int edad, String tipoAlmacenamiento)
            throws CedulaInvalidaException, PasswordInvalidaException {
        this(username, password, rol, genero, nombre, apellido, telefono, edad);
        if (tipoAlmacenamiento != null && !tipoAlmacenamiento.isEmpty()) {
            this.tipoAlmacenamiento = tipoAlmacenamiento;
        }
    }



    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Rol getRol() {
        return rol;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public int getEdad() {
        return edad;
    }

    public Genero getGenero() {
        return genero;
    }

    public Map<Integer, String> getPreguntasSeguridad() {
        return preguntasSeguridad;
    }

    public String getTipoAlmacenamiento() {
        return tipoAlmacenamiento;
    }


    public void setUsername(String username) throws CedulaInvalidaException {
        if (!esCedulaValida(username)) {
            throw new CedulaInvalidaException(Idioma.get("registro.msj.cedulainvalida"));
        }
        this.username = username;
    }

    public void setPassword(String password) throws PasswordInvalidaException {
        if (!esPasswordValido(password)) {
            throw new PasswordInvalidaException(Idioma.get("registro.msj.password.invalido"));
        }
        this.password = password;
    }



    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public void setGenero(Genero genero) {
        this.genero = genero;
    }

    public void setPreguntasSeguridad(Map<Integer, String> preguntasSeguridad) {
        this.preguntasSeguridad = preguntasSeguridad;
    }

    public void setTipoAlmacenamiento(String tipoAlmacenamiento) {
        this.tipoAlmacenamiento = tipoAlmacenamiento;
    }



    private boolean esCedulaValida(String cedula) {
        if (cedula == null || cedula.length() != 10 || !cedula.matches("\\d+")) {
            return false;
        }
        try {
            int provincia = Integer.parseInt(cedula.substring(0, 2));
            if (provincia < 1 || provincia > 24) {
                return false;
            }
            int digitoVerificadorReal = Integer.parseInt(cedula.substring(9, 10));
            int suma = 0;
            for (int i = 0; i < 9; i++) {
                int digito = Integer.parseInt(cedula.substring(i, i + 1));
                int producto = (i % 2 == 0) ? digito * 2 : digito;
                if (producto > 9) {
                    producto -= 9;
                }
                suma += producto;
            }
            int digitoVerificadorCalculado = (suma % 10 == 0) ? 0 : 10 - (suma % 10);
            return digitoVerificadorCalculado == digitoVerificadorReal;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean esPasswordValido(String password) {
        if (password == null || password.length() < 6) {
            return false;
        }
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[@_-]).{6,}$";
        return password.matches(passwordRegex);
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(username, usuario.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }

    @Override
    public String toString() {
        return username;
    }
}