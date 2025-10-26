import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Exporta los datos de una cuenta bancaria a un archivo CSV.
 * Se incluyen los movimientos de la cuenta (tipo, cantidad y fecha/hora).
 * Permite analizar o abrir los datos en hojas de cálculo.
 */
public class ExportadorCSV {

    private static final String SEPARADOR = ";"; // Separador de columnas CSV
    private static final String DIRECTORIO = "exportaciones_banco"; // Carpeta de exportación

    /**
     * Exporta la cuenta bancaria a un archivo CSV.
     *
     * @param cuenta      La cuenta a exportar
     * @param nombreArchivo Nombre base del archivo (sin extensión)
     * @return true si la exportación fue exitosa, false en caso de error
     */
    public static boolean exportar(Cuenta cuenta, String nombreArchivo) {
        // Validación de parámetros
        if (cuenta == null) {
            System.out.println("❌ ERROR: Cuenta nula, no se puede exportar.");
            return false;
        }

        if (nombreArchivo == null || nombreArchivo.isBlank()) {
            System.out.println("❌ ERROR: El nombre del archivo no puede estar vacío.");
            return false;
        }

        // Crear directorio si no existe
        File dir = new File(DIRECTORIO);
        if (!dir.exists()) dir.mkdir();

        String rutaCompleta = DIRECTORIO + File.separator + nombreArchivo + ".csv";

        // Formato de fecha para los movimientos
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaCompleta))) {
            // Escribir encabezado del CSV
            writer.write("Tipo" + SEPARADOR + "Cantidad" + SEPARADOR + "FechaHora");
            writer.newLine();

            // Escribir todos los movimientos de la cuenta
            List<Movimiento> movimientos = cuenta.getMovimientos();
            for (Movimiento m : movimientos) {
                writer.write(
                        m.getTipo() + SEPARADOR +
                                String.format("%.2f", m.getCantidad()) + SEPARADOR +
                                m.getFechaHora().format(formatoFecha)
                );
                writer.newLine();
            }

            System.out.println("✅ Exportación CSV completada: " + rutaCompleta);
            return true;

        } catch (IOException e) {
            System.out.println("❌ ERROR al escribir CSV: " + e.getMessage());
            return false;
        }
    }
}
