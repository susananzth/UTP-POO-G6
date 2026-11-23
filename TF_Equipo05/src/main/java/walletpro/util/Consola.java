package walletpro.util;

import java.util.ArrayList;
import java.util.Scanner;
import walletpro.interfaces.IConsultable;

/**
 * TODA la lectura del teclado esta aqui. Cada metodo repite la pregunta
 * hasta que el dato sea valido.
 * Ejemplo de uso: String nombre = Consola.leerTexto("Nombres:");
 *
 * Reglas del profesor:
 * - No se usa nextLine(): con useDelimiter("\\R") el metodo next() lee la
 *   linea completa hasta el Enter (con espacios, ej. "Juan Carlos").
 * - Los mensajes se muestran con println.
 * - El Scanner nunca se cierra: cerrarlo cerraria System.in (el teclado).
 *
 * Lo usan: las clases del modelo (en registrar()) y los menus.
 */
public class Consola {

    // \\R significa "salto de linea": cada Enter separa un dato del siguiente
    private static Scanner teclado = new Scanner(System.in).useDelimiter("\\R");

    public static final double MONTO_MAXIMO = 1000000;

    // Texto obligatorio (no puede quedar vacio).
    public static String leerTexto(String pregunta) {
        String texto;
        do {
            System.out.println(pregunta);
            texto = teclado.next().trim();
            if (texto.isEmpty()) {
                System.out.println("Este dato es obligatorio.");
            }
        } while (texto.isEmpty());
        return texto;
    }

    // Texto opcional: si solo presiona Enter, devuelve el valor por defecto.
    public static String leerTextoOpcional(String pregunta, String porDefecto) {
        System.out.println(pregunta);
        String texto = teclado.next().trim();
        if (texto.isEmpty()) {
            return porDefecto;
        }
        return texto;
    }

    // Solo digitos y con la cantidad exacta (DNI = 8, RUC = 11, codigo de banco = 5).
    public static String leerDigitos(String pregunta, int cantidad) {
        String texto;
        do {
            texto = leerTexto(pregunta);
            if (!Validacion.esSoloDigitos(texto, cantidad)) {
                System.out.println("Debe tener exactamente " + cantidad + " digitos.");
            }
        } while (!Validacion.esSoloDigitos(texto, cantidad));
        return texto;
    }

    public static String leerCorreo(String pregunta) {
        String texto;
        do {
            texto = leerTexto(pregunta);
            if (!Validacion.esCorreoValido(texto)) {
                System.out.println("Correo invalido. Ejemplo: nombre@correo.com");
            }
        } while (!Validacion.esCorreoValido(texto));
        return texto;
    }

    // Numero entero entre minimo y maximo.
    // try-catch: si escribe letras, Integer.parseInt lanza NumberFormatException.
    public static int leerEntero(String pregunta, int minimo, int maximo) {
        int valor = 0;
        boolean valido = false;
        do {
            System.out.println(pregunta);
            try {
                valor = Integer.parseInt(teclado.next().trim());
                if (valor >= minimo && valor <= maximo) {
                    valido = true;
                } else {
                    System.out.println("Debe estar entre " + minimo + " y " + maximo + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un numero entero.");
            }
        } while (!valido);
        return valor;
    }

    // Numero con decimales entre minimo y maximo (use punto: 10.50).
    public static double leerDecimal(String pregunta, double minimo, double maximo) {
        double valor = 0;
        boolean valido = false;
        do {
            System.out.println(pregunta);
            try {
                valor = Double.parseDouble(teclado.next().trim());
                if (valor >= minimo && valor <= maximo) {
                    valido = true;
                } else {
                    System.out.println("Debe estar entre " + minimo + " y " + maximo + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un numero (use punto para los decimales).");
            }
        } while (!valido);
        return valor;
    }

    // Monto en soles: mayor a 0 y redondeado a 2 decimales.
    public static double leerMonto(String pregunta) {
        double monto;
        do {
            monto = Dinero.redondear(leerDecimal(pregunta, 0, MONTO_MAXIMO));
            if (monto <= 0) {
                System.out.println("El monto debe ser mayor a 0.");
            }
        } while (monto <= 0);
        return monto;
    }

    // Fecha valida con formato dd-mm-aaaa.
    public static String leerFecha(String pregunta) {
        String texto;
        do {
            texto = leerTexto(pregunta);
            if (!Validacion.esFechaValida(texto)) {
                System.out.println("Fecha invalida. Use dd-mm-aaaa, ej. 05-11-2026.");
            }
        } while (!Validacion.esFechaValida(texto));
        return texto;
    }

    // Fecha que no puede ser posterior a hoy (nacimiento, ingreso a un trabajo).
    public static String leerFechaPasada(String pregunta) {
        String texto;
        do {
            texto = leerFecha(pregunta);
            if (Validacion.esFechaFutura(texto)) {
                System.out.println("La fecha no puede ser posterior a hoy.");
            }
        } while (Validacion.esFechaFutura(texto));
        return texto;
    }

    // Respuesta S o N. Devuelve true si responde S.
    public static boolean leerSiNo(String pregunta) {
        String texto;
        do {
            texto = leerTexto(pregunta + " (S/N)");
        } while (!texto.equalsIgnoreCase("S") && !texto.equalsIgnoreCase("N"));
        return texto.equalsIgnoreCase("S");
    }

    /**
     * METODO GENERICO <T>: sirve para elegir de cualquier lista
     * (empleados, bancos, cuentas...). Muestra la lista numerada
     * (usa el toString() de cada objeto) y devuelve el elegido.
     * Ejemplo: Empleado e = Consola.seleccionar("Empleados:", empleados);
     */
    public static <T> T seleccionar(String titulo, ArrayList<T> lista) {
        String mensaje = titulo;
        for (int i = 0; i < lista.size(); i++) {
            mensaje += "\n" + (i + 1) + ". " + lista.get(i);
        }
        System.out.println(mensaje);
        int posicion = leerEntero("Seleccione una opcion (1-" + lista.size() + "):", 1, lista.size());
        return lista.get(posicion - 1);
    }

    /**
     * GENERICO CON RESTRICCION <T extends IConsultable>: solo acepta listas
     * de clases que implementan IConsultable, porque necesita llamar a
     * resumen(). Cada clase tiene su propio resumen() (polimorfismo).
     */
    public static <T extends IConsultable> void mostrarLista(String titulo, ArrayList<T> lista) {
        String mensaje = titulo;
        if (lista.isEmpty()) {
            mensaje += "\n(no hay registros)";
        }
        for (T elemento : lista) {
            mensaje += "\n" + elemento.resumen();
        }
        System.out.println(mensaje);
    }
}
