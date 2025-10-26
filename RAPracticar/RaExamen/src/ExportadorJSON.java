import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Exporta los datos de una cuenta bancaria a un archivo JSON.
 * Incluye información del titular y todos los movimientos realizados.
 * Permite interoperabilidad con otras aplicaciones o análisis de datos.
 */
public class ExportadorJSON {

    private static final String DIRECTORIO = "exportaciones_banco"; // Carpeta de exportación

    /**
     * Exporta la cuenta a un archivo JSON.
     *
     * @param cuenta       La cuenta a exportar
     * @param nombreArchivo Nombre base del archivo (sin extensión)
     * @return true si la exportación fue exitosa, false si ocurrió un error
     */
    public static boolean exportar(Cuenta cuenta, String nombreArchivo) {
        // Validación de parámetros
        if (cuenta == null) {
            System.out.println("❌ ERROR: Cuenta nula.");
            return false;
        }

        if (nombreArchivo == null || nombreArchivo.isBlank()) {
            System.out.println("❌ ERROR: Nombre de archivo inválido.");
            return false;
        }

        // Crear directorio si no existe
        File dir = new File(DIRECTORIO);
        if (!dir.exists()) dir.mkdir();

        String rutaCompleta = DIRECTORIO + File.separator + nombreArchivo + ".json";
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaCompleta))) {

            // Inicia el objeto JSON
            writer.write("{\n");

            // Titular de la cuenta
            writer.write("  \"titular\": {\n");
            writer.write("    \"nombre\": \"" + escaparJSON(cuenta.getTitular().getNombre()) + "\",\n");
            writer.write("    \"dni\": \"" + escaparJSON(cuenta.getTitular().getDni()) + "\",\n");
            writer.write("    \"edad\": " + cuenta.getTitular().getEdad() + "\n");
            writer.write("  },\n");

            // Movimientos de la cuenta
            writer.write("  \"movimientos\": [\n");
            List<Movimiento> movimientos = cuenta.getMovimientos();
            for (int i = 0; i < movimientos.size(); i++) {
                Movimiento m = movimientos.get(i);
                writer.write("    {\n");
                writer.write("      \"tipo\": \"" + m.getTipo() + "\",\n");
                writer.write("      \"cantidad\": " + String.format("%.2f", m.getCantidad()) + ",\n");
                writer.write("      \"fechaHora\": \"" + m.getFechaHora().format(formatoFecha) + "\"\n");
                writer.write("    }" + (i < movimientos.size() - 1 ? "," : "") + "\n");
            }
            writer.write("  ]\n");
            writer.write("}\n");

            System.out.println("✅ Exportación JSON completada: " + rutaCompleta);
            return true;

        } catch (IOException e) {
            System.out.println("❌ ERROR al escribir JSON: " + e.getMessage());
            return false;
        }
    }

    /**
     * Escapa caracteres especiales para JSON
     * @param texto Texto original
     * @return Texto escapado
     */
    private static String escaparJSON(String texto) {
        if (texto == null) return "";
        return texto.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n");
    }
}
