import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Movimiento de cuenta: puede ser INGRESO o RETIRADA.
 */
public class Movimiento implements Serializable {

    private static final long serialVersionUID = 1L;

    public enum Tipo {
        INGRESO,
        RETIRADA
    }

    private final Tipo tipo;
    private final double cantidad;
    private final LocalDateTime fechaHora;

    public Movimiento(Tipo tipo, double cantidad) {
        this.tipo = tipo;
        this.cantidad = Math.max(0.0, cantidad);
        this.fechaHora = LocalDateTime.now();
    }

    public Tipo getTipo() {
        return tipo;
    }

    public double getCantidad() {
        return cantidad;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    @Override
    public String toString() {
        String f = fechaHora.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return "[" + f + "] " + (tipo == Tipo.INGRESO ? "Ingreso" : "Retirada") + " -> " + String.format("%.2f", cantidad) + " €";
    }
}
