package ec.edu.ups.poo.modelo;

import java.io.Serializable;
import java.util.Objects;

public class Pregunta implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String pregunta;

    public Pregunta(int id, String pregunta) {
        this.id = id;
        this.pregunta = pregunta;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {this.id=id;}

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {this.pregunta=pregunta;}

    @Override
    public String toString() {
        return pregunta;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pregunta pregunta1 = (Pregunta) o;
        return id == pregunta1.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}