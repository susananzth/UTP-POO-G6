package walletpro.util;

import java.util.List;
import java.util.Scanner;

/**
 * Centraliza TODA la lectura del teclado de la aplicacion.
 *
 * Indicaciones del profesor que se aplican aqui:
 * - No se usa nextLine(): el Scanner se configura con useDelimiter("\\R")
 *   (\\R = salto de linea), asi next() devuelve la linea completa, incluso
 *   si tiene espacios ("Juan Carlos"), y se evita el clasico problema de
 *   mezclar nextInt() con nextLine().
 * - Los textos que se muestran al usuario usan println (no print).
 * - El Scanner NO se cierra (no hay teclado.close()): cerrarlo cerraria
 *   System.in y ya no se podria volver a leer en todo el programa.
 *
 * Antes cada clase repetia el mismo do-while + try-catch; ahora todas
 * llaman a estos metodos, asi la validacion es la misma en todo el sistema.
 */
public class Consola {

    private static final Scanner teclado = new Scanner(System.in).useDelimiter("\\R");

    /** Monto maximo aceptado en una sola operacion (evita valores absurdos). */
    public static final double MONTO_MAXIMO = 1000000000;

    /** Lee un texto que no puede quedar vacio. */
    public static String leerTexto(String mensaje) {
        String entrada;
        do {
            System.out.println(mensaje);
            entrada = teclado.next().trim();
            if (entrada.isEmpty()) {
                System.out.println("Este dato es obligatorio, no puede quedar vacio.");
            }
        } while (entrada.isEmpty());
        return entrada;
    }

    /** Lee un texto opcional: si el usuario no escribe nada, devuelve el valor por defecto. */
    public static String leerTextoOpcional(String mensaje, String porDefecto) {
        System.out.println(mensaje);
        String entrada = teclado.next().trim();
        if (entrada.isEmpty()) {
            return porDefecto;
        }
        return entrada;
    }

    /** Lee un texto formado solo por digitos, con la cantidad exacta indicada (DNI, RUC...). */
    public static String leerDigitos(String mensaje, int cantidad) {
        String entrada;
        do {
            entrada = leerTexto(mensaje);
            if (!ValidRegex.esSoloDigitos(entrada, cantidad)) {
                System.out.println("Debe tener exactamente " + cantidad + " digitos.");
            }
        } while (!ValidRegex.esSoloDigitos(entrada, cantidad));
        return entrada;
    }

    /** Lee un numero entero dentro del rango [minimo, maximo]. */
    public static int leerEntero(String mensaje, int minimo, int maximo) {
        int valor = 0;
        boolean valido = false;
        do {
            System.out.println(mensaje);
            try {
                valor = Integer.parseInt(teclado.next().trim());
                if (valor >= minimo && valor <= maximo) {
                    valido = true;
                } else {
                    System.out.println("El valor debe estar entre " + minimo + " y " + maximo + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un numero entero valido.");
            }
        } while (!valido);
        return valor;
    }

    /**
     * Lee un decimal dentro del rango [minimo, maximo]. Se rechazan
     * "Infinity" y "NaN", que Double.parseDouble acepta como numeros.
     */
    public static double leerDecimal(String mensaje, double minimo, double maximo) {
        double valor = 0;
        boolean valido = false;
        do {
            System.out.println(mensaje);
            try {
                valor = Double.parseDouble(teclado.next().trim());
                if (Double.isNaN(valor) || Double.isInfinite(valor)) {
                    System.out.println("Ingrese un numero valido.");
                } else if (valor < minimo || valor > maximo) {
                    System.out.println("El valor debe estar entre " + minimo + " y " + maximo + ".");
                } else {
                    valido = true;
                }
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un numero valido (use punto para los decimales).");
            }
        } while (!valido);
        return valor;
    }

    /** Lee un monto de dinero en soles: mayor a 0 y redondeado a centimos. */
    public static double leerMonto(String mensaje) {
        double monto;
        do {
            monto = Dinero.redondear(leerDecimal(mensaje, 0, MONTO_MAXIMO));
            if (monto <= 0) {
                System.out.println("El monto debe ser mayor a S/ 0.00");
            }
        } while (monto <= 0);
        return monto;
    }

    /** Lee una fecha valida en formato dd-mm-aaaa. */
    public static String leerFecha(String mensaje) {
        String entrada;
        do {
            entrada = leerTexto(mensaje);
            if (!ValidFecha.validar(entrada)) {
                System.out.println("Fecha invalida. Use el formato dd-mm-aaaa (ej. 05-11-2026).");
            }
        } while (!ValidFecha.validar(entrada));
        return entrada;
    }

    /** Lee una fecha que no puede ser posterior a hoy (nacimiento, ingreso a un trabajo...). */
    public static String leerFechaPasada(String mensaje) {
        String entrada;
        do {
            entrada = leerFecha(mensaje);
            if (ValidFecha.esFutura(entrada)) {
                System.out.println("La fecha no puede ser posterior a hoy (" + ValidFecha.hoy() + ").");
            }
        } while (ValidFecha.esFutura(entrada));
        return entrada;
    }

    /**
     * Metodo GENERICO <T>: sirve para elegir de cualquier lista (empleados,
     * bancos, cuentas...). Muestra las opciones numeradas y devuelve el
     * elemento elegido. Si la lista esta vacia devuelve null.
     */
    public static <T> T seleccionar(String titulo, List<T> opciones) {
        if (opciones.isEmpty()) {
            return null;
        }
        String mensaje = titulo;
        for (int i = 0; i < opciones.size(); i++) {
            mensaje += "\n" + (i + 1) + ". " + opciones.get(i);
        }
        System.out.println(mensaje);
        int posicion = leerEntero("Seleccione una opcion (1-" + opciones.size() + "):", 1, opciones.size());
        return opciones.get(posicion - 1);
    }

    /** Lee una respuesta S/N y la devuelve como boolean. */
    public static boolean leerSiNo(String mensaje) {
        String entrada;
        do {
            entrada = leerTexto(mensaje + " (S/N)");
            if (!entrada.equalsIgnoreCase("S") && !entrada.equalsIgnoreCase("N")) {
                System.out.println("Responda con S o N.");
            }
        } while (!entrada.equalsIgnoreCase("S") && !entrada.equalsIgnoreCase("N"));
        return entrada.equalsIgnoreCase("S");
    }
}
