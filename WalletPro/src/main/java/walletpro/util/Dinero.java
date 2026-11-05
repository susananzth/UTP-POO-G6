package walletpro.util;

import java.util.Locale;

/**
 * Manejo de montos de dinero. Por indicacion del profesor la billetera
 * trabaja con UNA sola moneda (soles), asi no hay tipo de cambio.
 *
 * Los montos se guardan como double, pero siempre redondeados a 2
 * decimales (centimos). Asi se evitan errores como 0.30 - 0.10 = 0.19999...
 * sin tener que usar clases mas complejas como BigDecimal.
 */
public class Dinero {

    public static final String MONEDA = "S/";

    /** Redondea un monto a centimos (2 decimales). */
    public static double redondear(double monto) {
        return Math.round(monto * 100) / 100.0;
    }

    /** Devuelve el monto listo para mostrar, ej. "S/ 1,250.50". */
    public static String formatear(double monto) {
        return String.format(Locale.US, "%s %,.2f", MONEDA, monto);
    }
}
