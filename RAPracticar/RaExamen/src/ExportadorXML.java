import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Exporta los datos de una cuenta bancaria a un archivo XML.
 * Incluye información del titular y todos los movimientos realizados.
 * XML es útil para interoperabilidad con otros sistemas y análisis estructurado.
 */
public class ExportadorXML {

    private static final String DIRECTORIO = "exportaciones_banco"; // Carpeta de exportación
    private static final String IND = "  "; // Indentación para mejorar legibilidad del XML

    /**
     * Exporta la cuenta a XML.
     *
     * @param cuenta        La cuenta a exportar
     * @param nombreArchivo Nombre base del archivo (sin extensión)
     * @return true si la exportación fue exitosa, false en caso de error
     */
    public static boolean exportar(Cuenta cuenta, String nombreArchivo) {
        // Validaciones
        if (cuenta == null) {
            System.out.println("❌ ERROR: Cuenta nula, no se puede exportar.");
            return false;
        }
        if (nombreArchivo == null || nombreArchivo.isBlank()) {
            System.out.println("❌ ERROR: Nombre de archivo inválido.");
            return false;
        }

        // Crear directorio si no existe
        File dir = new File(DIRECTORIO);
        if (!dir.exists()) dir.mkdir();

        String rutaCompleta = DIRECTORIO + File.separator + nombreArchivo + ".xml";
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaCompleta))) {

            // Cabecera XML
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            writer.newLine();
            writer.write("<cuenta>");
            writer.newLine();

            // Datos del titular
            writer.write(IND + "<titular>");
            writer.newLine();
            writer.write(IND + IND + "<nombre>" + escaparXML(cuenta.getTitular().getNombre()) + "</nombre>");
            writer.newLine();
            writer.write(IND + IND + "<dni>" + escaparXML(cuenta.getTitular().getDni()) + "</dni>");
            writer.newLine();
            writer.write(IND + IND + "<edad>" + cuenta.getTitular().getEdad() + "</edad>");
            writer.newLine();
            writer.write(IND + "</titular>");
            writer.newLine();

            // Movimientos de la cuenta
            writer.write(IND + "<movimientos>");
            writer.newLine();
            List<Movimiento> movimientos = cuenta.getMovimientos();
            for (Movimiento m : movimientos) {
                writer.write(IND + IND + "<movimiento tipo=\"" + m.getTipo() + "\">");
                writer.newLine();
                writer.write(IND + IND + IND + "<cantidad>" + String.format("%.2f", m.getCantidad()) + "</cantidad>");
                writer.newLine();
                writer.write(IND + IND + IND + "<fechaHora>" + m.getFechaHora().format(formatoFecha) + "</fechaHora>");
                writer.newLine();
                writer.write(IND + IND + "</movimiento>");
                writer.newLine();
            }
            writer.write(IND + "</movimientos>");
            writer.newLine();

            // Cierre del nodo principal
            writer.write("</cuenta>");
            writer.newLine();

            System.out.println("✅ Exportación XML completada: " + rutaCompleta);
            return true;

        } catch (IOException e) {
            System.out.println("❌ ERROR al escribir XML: " + e.getMessage());
            return false;
        }
    }

    /**
     * Escapa caracteres especiales para XML.
     * @param texto Texto original
     * @return Texto seguro para XML
     */
    private static String escaparXML(String texto) {
        if (texto == null) return "";
        return texto.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }
}
