import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Cuenta bancaria con un titular (Cliente) y una lista de movimientos.
 */
public class Cuenta implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Cliente titular;
    private final ArrayList<Movimiento> movimientos;

    public Cuenta(Cliente titular) {
        this.titular = titular;
        this.movimientos = new ArrayList<>();
    }

    public Cliente getTitular() {
        return titular;
    }

    public List<Movimiento> getMovimientos() {
        return Collections.unmodifiableList(movimientos);
    }

    public double getSaldo() {
        double saldo = 0.0;
        for (Movimiento m : movimientos) {
            if (m.getTipo() == Movimiento.Tipo.INGRESO)
                saldo += m.getCantidad();
            else
                saldo -= m.getCantidad();
        }
        return saldo;
    }

    public void ingresar(double cantidad) {
        if (cantidad <= 0) return;
        movimientos.add(new Movimiento(Movimiento.Tipo.INGRESO, cantidad));
    }

    public boolean retirar(double cantidad) {
        if (cantidad <= 0) return false;
        if (getSaldo() < cantidad) return false;
        movimientos.add(new Movimiento(Movimiento.Tipo.RETIRADA, cantidad));
        return true;
    }

    @Override
    public String toString() {
        return "Cuenta{titular=" + titular + ", saldo=" + String.format("%.2f", getSaldo()) + "€}";
    }
}
