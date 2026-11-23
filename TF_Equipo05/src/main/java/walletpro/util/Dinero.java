package walletpro.util;

import java.util.Locale;

/**
 * La billetera trabaja con UNA sola moneda: soles (indicacion del profesor).
 *
 * Lo usan: las clases del modelo y los menus, para redondear y mostrar montos.
 */
public class Dinero {

    // Redondea a 2 decimales (centimos). Evita resultados como 0.30 - 0.10 = 0.19999...
    public static double redondear(double monto) {
        return Math.round(monto * 100) / 100.0;
    }

    // Ej. 1250.5 -> "S/ 1,250.50"
    public static String formatear(double monto) {
        return String.format(Locale.US, "S/ %,.2f", monto);
    }
}
