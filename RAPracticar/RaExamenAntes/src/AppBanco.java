import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Scanner;

/**
 * Clase que encapsula la lógica de la aplicación.
 */
public class AppBanco {

    private static final String CARPETA_DATOS = "datos";
    private static final String FICHERO_CUENTA = "cuenta.dat";

    private final Scanner sc = new Scanner(System.in);

    public void ejecutar() {
        File dir = new File(CARPETA_DATOS);
        if (!dir.exists() && !dir.mkdirs()) {
            System.out.println("Error: no se pudo crear la carpeta 'datos/'. Finalizando.");
            return;
        }

        File archivo = new File(dir, FICHERO_CUENTA);
        Cuenta cuenta = archivo.exists() ? cargarCuenta(archivo) : null;

        if (cuenta == null) {
            System.out.println("No se encontró cuenta válida. Creando nueva...");
            cuenta = crearCuentaPorConsola();
        } else {
            System.out.println("Cuenta cargada desde '" + CARPETA_DATOS + "/" + FICHERO_CUENTA + "'.");
        }

        boolean salir = false;
        while (!salir) {
            System.out.println("\n--- Menú ---");
            System.out.println("1) Ingresar dinero");
            System.out.println("2) Retirar dinero");
            System.out.println("3) Consultar saldo y movimientos");
            System.out.println("0) Salir y guardar");
            System.out.print("Elige opción: ");
            String opcion = sc.nextLine().trim();

            switch (opcion) {
                case "1" -> {
                    double cantidad = leerDoublePositivo("Cantidad a ingresar: ");
                    cuenta.ingresar(cantidad);
                    System.out.println("Ingreso realizado. Saldo: " + String.format("%.2f", cuenta.getSaldo()) + " €");
                }
                case "2" -> {
                    double cantidad = leerDoublePositivo("Cantidad a retirar: ");
                    boolean ok = cuenta.retirar(cantidad);
                    System.out.println(ok
                            ? "Retirada realizada. Saldo: " + String.format("%.2f", cuenta.getSaldo()) + " €"
                            : "Operación no realizada: saldo insuficiente o cantidad inválida.");
                }
                case "3" -> {
                    System.out.println("\n" + cuenta);
                    if (cuenta.getMovimientos().isEmpty()) {
                        System.out.println("No hay movimientos.");
                    } else {
                        System.out.println("Movimientos:");
                        cuenta.getMovimientos().forEach(m -> System.out.println(" - " + m));
                    }
                }
                case "0" -> {
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

        sc.close();
    }

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

    private Cuenta cargarCuenta(File archivo) {
        System.out.println("Intentando leer: " + archivo.getPath()
                + " (existe=" + archivo.exists()
                + ", tamaño=" + (archivo.exists() ? archivo.length() + " bytes" : "n/a") + ")");
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(archivo))) {
            Object obj = ois.readObject();
            return (Cuenta) obj;
        } catch (FileNotFoundException e) {
            System.out.println("Fichero no encontrado: " + archivo.getPath());
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error de lectura: " + e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException | ClassCastException e) {
            System.out.println("Formato de datos o clase incompatible: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    private boolean guardarCuenta(File archivo, Cuenta cuenta) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(archivo))) {
            oos.writeObject(cuenta);
            oos.flush();
            return true;
        } catch (FileNotFoundException e) {
            System.out.println("No se puede abrir/crear: " + archivo.getPath());
        } catch (IOException e) {
            System.out.println("Error de escritura: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

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
}
