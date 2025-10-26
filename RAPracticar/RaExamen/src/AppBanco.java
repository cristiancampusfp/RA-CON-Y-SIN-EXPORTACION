import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.util.Scanner;

/**
 * Clase principal de la aplicación bancaria.
 * Permite gestionar una cuenta con ingresos, retiradas, consulta de saldo y exportación.
 * Implementa un menú por consola y persiste la cuenta en disco.
 */
public class AppBanco {

    // Carpeta donde se almacenarán los datos de la cuenta
    private static final String CARPETA_DATOS = "datos";

    // Nombre del fichero donde se guarda la cuenta serializada
    private static final String FICHERO_CUENTA = "cuenta.dat";

    // Scanner global para leer entradas por consola
    private final Scanner sc = new Scanner(System.in);

    /**
     * Método principal de ejecución de la aplicación.
     * Carga la cuenta si existe, o la crea nueva, y muestra el menú principal.
     */
    public void ejecutar() {
        // Crear carpeta "datos" si no existe
        File dir = new File(CARPETA_DATOS);
        if (!dir.exists() && !dir.mkdirs()) {
            System.out.println("Error: no se pudo crear la carpeta 'datos/'. Finalizando.");
            return;
        }

        // Archivo de la cuenta
        File archivo = new File(dir, FICHERO_CUENTA);

        // Intentar cargar cuenta existente
        Cuenta cuenta = archivo.exists() ? cargarCuenta(archivo) : null;
        if (cuenta == null) {
            System.out.println("No se encontró cuenta válida. Creando nueva...");
            cuenta = crearCuentaPorConsola();
        } else {
            System.out.println("Cuenta cargada desde '" + CARPETA_DATOS + "/" + FICHERO_CUENTA + "'.");
        }

        boolean salir = false;

        // ───────────── Menú principal ─────────────
        while (!salir) {
            System.out.println("\n--- Menú ---");
            System.out.println("1) Ingresar dinero");
            System.out.println("2) Retirar dinero");
            System.out.println("3) Consultar saldo y movimientos");
            System.out.println("4) Exportar cuenta (CSV, XML, JSON)");
            System.out.println("0) Salir y guardar");
            System.out.print("Elige opción: ");

            String opcion = sc.nextLine().trim();

            switch (opcion) {
                case "1" -> { // Ingreso
                    double cantidad = leerDoublePositivo("Cantidad a ingresar: ");
                    cuenta.ingresar(cantidad);
                    System.out.println("Ingreso realizado. Saldo: " + String.format("%.2f", cuenta.getSaldo()) + " €");
                }
                case "2" -> { // Retirada
                    double cantidad = leerDoublePositivo("Cantidad a retirar: ");
                    boolean ok = cuenta.retirar(cantidad);
                    System.out.println(ok
                            ? "Retirada realizada. Saldo: " + String.format("%.2f", cuenta.getSaldo()) + " €"
                            : "Operación no realizada: saldo insuficiente o cantidad inválida.");
                }
                case "3" -> { // Consultar saldo y movimientos
                    System.out.println("\n" + cuenta);
                    if (cuenta.getMovimientos().isEmpty()) {
                        System.out.println("No hay movimientos.");
                    } else {
                        System.out.println("Movimientos:");
                        cuenta.getMovimientos().forEach(m -> System.out.println(" - " + m));
                    }
                }
                case "4" -> exportarCuenta(cuenta); // Exportación a CSV, XML y JSON
                case "0" -> { // Guardar y salir
                    if (guardarCuenta(archivo, cuenta)) {
                        System.out.println("Cuenta guardada en '" + CARPETA_DATOS + "/" + FICHERO_CUENTA + "'.");
                    } else {
                        System.out.println("Aviso: no se pudo guardar la cuenta.");
                    }
                    salir = true;
                }
                default -> System.out.println("Opción no válida.");
            }
        }

        sc.close(); // Cerrar Scanner al salir
    }

    // ───────────── Métodos de creación, carga y guardado ─────────────

    /**
     * Crea una nueva cuenta pidiendo los datos al usuario.
     * @return Cuenta creada
     */
    private Cuenta crearCuentaPorConsola() {
        System.out.print("Nombre del cliente: ");
        String nombre = sc.nextLine();
        System.out.print("DNI/NIF del cliente: ");
        String dni = sc.nextLine();
        int edad = leerEnteroNoNegativo("Edad del cliente: ");
        Cliente cliente = new Cliente(nombre, dni, edad);
        Cuenta cuenta = new Cuenta(cliente);
        System.out.println("Cuenta creada para: " + cliente);
        return cuenta;
    }

    /**
     * Carga la cuenta desde disco usando serialización.
     * Maneja errores de lectura y formato.
     * @param archivo Archivo donde está la cuenta
     * @return Cuenta cargada o null si hubo error
     */
    private Cuenta cargarCuenta(File archivo) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            Object obj = ois.readObject();
            return (Cuenta) obj;
        } catch (IOException | ClassNotFoundException | ClassCastException e) {
            System.out.println("Error al cargar cuenta: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Guarda la cuenta en disco usando serialización.
     * @param archivo Archivo donde se guardará
     * @param cuenta Cuenta a guardar
     * @return true si se guardó correctamente, false si hubo error
     */
    private boolean guardarCuenta(File archivo, Cuenta cuenta) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo))) {
            oos.writeObject(cuenta);
            oos.flush();
            return true;
        } catch (IOException e) {
            System.out.println("Error al guardar la cuenta: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // ───────────── Métodos de lectura segura ─────────────

    /**
     * Lee un número double positivo desde consola, controlando errores.
     */
    private double leerDoublePositivo(String prompt) {
        while (true) {
            System.out.print(prompt);
            String linea = sc.nextLine().trim().replace(",", ".");
            try {
                double valor = Double.parseDouble(linea);
                if (valor > 0) return valor;
                System.out.println("Introduce una cantidad positiva.");
            } catch (NumberFormatException e) {
                System.out.println("Formato no válido. Ejemplo: 1234.56");
            }
        }
    }

    /**
     * Lee un número entero no negativo desde consola, controlando errores.
     */
    private int leerEnteroNoNegativo(String prompt) {
        while (true) {
            System.out.print(prompt);
            String linea = sc.nextLine().trim();
            try {
                int valor = Integer.parseInt(linea);
                if (valor >= 0) return valor;
                System.out.println("Introduce un número entero no negativo.");
            } catch (NumberFormatException e) {
                System.out.println("Formato no válido. Ejemplo: 30");
            }
        }
    }

    // ───────────── Exportación ─────────────

    /**
     * Muestra un submenú para exportar la cuenta a CSV, XML o JSON.
     * Permite elegir múltiples formatos combinados.
     */
    private void exportarCuenta(Cuenta cuenta) {
        System.out.print("Nombre base para los archivos de exportación: ");
        String nombreArchivo = sc.nextLine().trim();
        if (nombreArchivo.isEmpty()) {
            System.out.println("❌ Nombre inválido, operación cancelada.");
            return;
        }

        System.out.println("Elige los formatos de exportación (puedes combinar, separados por coma):");
        System.out.println("1) CSV");
        System.out.println("2) XML");
        System.out.println("3) JSON");
        System.out.print("Opciones (ejemplo: 1,3): ");
        String opciones = sc.nextLine().trim();

        if (opciones.isEmpty()) {
            System.out.println("❌ No se seleccionó ningún formato. Operación cancelada.");
            return;
        }

        String[] formatos = opciones.split(",");
        boolean exportado = false;

        for (String f : formatos) {
            switch (f.trim()) {
                case "1" -> {
                    if (ExportadorCSV.exportar(cuenta, nombreArchivo)) {
                        System.out.println("✅ CSV exportado correctamente.");
                        exportado = true;
                    }
                }
                case "2" -> {
                    if (ExportadorXML.exportar(cuenta, nombreArchivo)) {
                        System.out.println("✅ XML exportado correctamente.");
                        exportado = true;
                    }
                }
                case "3" -> {
                    if (ExportadorJSON.exportar(cuenta, nombreArchivo)) {
                        System.out.println("✅ JSON exportado correctamente.");
                        exportado = true;
                    }
                }
                default -> System.out.println("❌ Opción desconocida: " + f);
            }
        }

        if (exportado) {
            System.out.println("Exportación completada.");
        } else {
            System.out.println("❌ No se exportó ningún archivo.");
        }
    }
}
