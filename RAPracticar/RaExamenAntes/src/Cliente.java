import java.io.Serializable;

/**
 * Clase que representa a un cliente del banco.
 * Implementa Serializable para poder guardarse en disco junto con la cuenta.
 */
public class Cliente implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String nombre;
    private final String dni;
    private final int edad;

    public Cliente(String nombre, String dni, int edad) {
        this.nombre = (nombre == null || nombre.isBlank()) ? "Sin nombre" : nombre.trim();
        this.dni = (dni == null || dni.isBlank()) ? "00000000X" : dni.trim();
        this.edad = Math.max(0, edad);
    }

    public String getNombre() {
        return nombre;
    }

    public String getDni() {
        return dni;
    }

    public int getEdad() {
        return edad;
    }

    @Override
    public String toString() {
        return "Cliente{nombre='" + nombre + "', dni='" + dni + "', edad=" + edad + "}";
    }
}
