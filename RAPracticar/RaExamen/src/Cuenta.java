import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Representa una cuenta bancaria de un cliente.
 * Contiene un titular (Cliente) y una lista de movimientos (ingresos y retiradas).
 * Implementa Serializable para poder guardarse en disco.
 */
public class Cuenta implements Serializable {
    private static final long serialVersionUID = 1L; // Versión para compatibilidad de serialización

    // Titular de la cuenta
    private final Cliente titular;

    // Lista de movimientos de la cuenta (INGRESOS y RETIRADAS)
    private final ArrayList<Movimiento> movimientos;

    /**
     * Constructor de la cuenta.
     * Inicializa la lista de movimientos vacía.
     * @param titular Cliente titular de la cuenta
     */
    public Cuenta(Cliente titular) {
        this.titular = titular;
        this.movimientos = new ArrayList<>();
    }

    // ───────────── Getters ─────────────
    /**
     * Devuelve el titular de la cuenta.
     */
    public Cliente getTitular() { return titular; }

    /**
     * Devuelve una lista inmodificable de los movimientos.
     * Evita que se modifiquen los movimientos desde fuera de la clase.
     */
    public List<Movimiento> getMovimientos() {
        return Collections.unmodifiableList(movimientos);
    }

    /**
     * Calcula el saldo actual de la cuenta.
     * Suma los ingresos y resta las retiradas.
     * @return Saldo actual
     */
    public double getSaldo() {
        double saldo = 0.0;
        for (Movimiento m : movimientos) {
            if (m.getTipo() == Movimiento.Tipo.INGRESO) saldo += m.getCantidad();
            else saldo -= m.getCantidad();
        }
        return saldo;
    }

    /**
     * Realiza un ingreso en la cuenta.
     * Crea un nuevo movimiento de tipo INGRESO y lo añade a la lista.
     * @param cantidad Cantidad a ingresar (debe ser positiva)
     */
    public void ingresar(double cantidad) {
        if (cantidad <= 0) return;
        movimientos.add(new Movimiento(Movimiento.Tipo.INGRESO, cantidad));
    }

    /**
     * Realiza una retirada de la cuenta si hay saldo suficiente.
     * Crea un nuevo movimiento de tipo RETIRADA y lo añade a la lista.
     * @param cantidad Cantidad a retirar
     * @return true si la retirada se realiza, false si no hay suficiente saldo o cantidad inválida
     */
    public boolean retirar(double cantidad) {
        if (cantidad <= 0) return false;
        if (getSaldo() < cantidad) return false;
        movimientos.add(new Movimiento(Movimiento.Tipo.RETIRADA, cantidad));
        return true;
    }

    /**
     * Representación en texto de la cuenta.
     * Incluye el titular y el saldo actual.
     */
    @Override
    public String toString() {
        return "Cuenta{titular=" + titular + ", saldo=" + String.format("%.2f", getSaldo()) + "€}";
    }
}
