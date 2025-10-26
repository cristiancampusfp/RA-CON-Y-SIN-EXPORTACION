import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Representa un movimiento en una cuenta bancaria.
 * Puede ser un INGRESO o una RETIRADA.
 * Implementa Serializable para poder guardarse junto con la cuenta.
 */
public class Movimiento implements Serializable {
    private static final long serialVersionUID = 1L; // Para compatibilidad en serialización

    /**
     * Tipo de movimiento: INGRESO o RETIRADA
     */
    public enum Tipo { INGRESO, RETIRADA }

    // Tipo de movimiento
    private final Tipo tipo;

    // Cantidad asociada al movimiento (positiva)
    private final double cantidad;

    // Fecha y hora en que se realiza el movimiento
    private final LocalDateTime fechaHora;

    /**
     * Constructor de movimiento.
     * Registra el tipo, la cantidad y la fecha/hora actual.
     * @param tipo Tipo de movimiento (INGRESO o RETIRADA)
     * @param cantidad Cantidad del movimiento (si es negativa se ajusta a 0)
     */
    public Movimiento(Tipo tipo, double cantidad) {
        this.tipo = tipo;
        this.cantidad = Math.max(0.0, cantidad); // Asegura que no haya cantidades negativas
        this.fechaHora = LocalDateTime.now(); // Fecha/hora del momento de creación
    }

    // ───────────── Getters ─────────────
    public Tipo getTipo() { return tipo; }
    public double getCantidad() { return cantidad; }
    public LocalDateTime getFechaHora() { return fechaHora; }

    /**
     * Representación en texto del movimiento.
     * Incluye fecha/hora, tipo y cantidad formateada.
     */
    @Override
    public String toString() {
        String f = fechaHora.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return "[" + f + "] " + (tipo == Tipo.INGRESO ? "Ingreso" : "Retirada")
                + " -> " + String.format("%.2f", cantidad) + " €";
    }
}
