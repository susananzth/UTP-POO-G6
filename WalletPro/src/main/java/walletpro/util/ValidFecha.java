package walletpro.util;

import java.time.LocalDate;

/**
 * Clase utilitaria de validacion de fechas (formato dd-mm-aaaa).
 * No mantiene estado: solo metodos estaticos. Por eso las demas
 * clases se relacionan con ella como dependencia y no como agregacion.
 */
public class ValidFecha {

    public static boolean validar(String fecha) {
        if (fecha == null || fecha.length() != 10) {
            return false;
        }
        if (fecha.charAt(2) != '-' || fecha.charAt(5) != '-') {
            return false;
        }
        for (int i = 0; i < fecha.length(); i++) {
            if (i == 2 || i == 5) {
                continue;
            }
            if (!Character.isDigit(fecha.charAt(i))) {
                return false;
            }
        }
        int dia = obtenerDia(fecha);
        int mes = obtenerMes(fecha);
        int anio = obtenerAnio(fecha);

        if (anio < 1900 || anio > 2100) {
            return false;
        }
        if (mes < 1 || mes > 12) {
            return false;
        }
        if (dia < 1 || dia > diasDelMes(mes, anio)) {
            return false;
        }
        return true;
    }

    private static int diasDelMes(int mes, int anio) {
        switch (mes) {
            case 2:
                return esBisiesto(anio) ? 29 : 28;
            case 4:
            case 6:
            case 9:
            case 11:
                return 30;
            default:
                return 31;
        }
    }

    public static boolean esBisiesto(int anio) {
        return (anio % 4 == 0 && anio % 100 != 0) || (anio % 400 == 0);
    }

    public static int obtenerDia(String fecha) {
        return Integer.parseInt(fecha.substring(0, 2));
    }

    public static int obtenerMes(String fecha) {
        return Integer.parseInt(fecha.substring(3, 5));
    }

    public static int obtenerAnio(String fecha) {
        return Integer.parseInt(fecha.substring(6, 10));
    }

    /** Indica si una fecha cae en ese mes/anio (se usa en presupuestos y reportes). */
    public static boolean esDelMes(String fecha, int mes, int anio) {
        if (!validar(fecha)) {
            return false;
        }
        return obtenerMes(fecha) == mes && obtenerAnio(fecha) == anio;
    }

    /** Fecha de hoy en formato dd-mm-aaaa (se usa para fechar depositos y retiros). */
    public static String hoy() {
        LocalDate hoy = LocalDate.now();
        return String.format("%02d-%02d-%04d", hoy.getDayOfMonth(), hoy.getMonthValue(), hoy.getYear());
    }

    /** Indica si la fecha es posterior a hoy. */
    public static boolean esFutura(String fecha) {
        LocalDate valor = LocalDate.of(obtenerAnio(fecha), obtenerMes(fecha), obtenerDia(fecha));
        return valor.isAfter(LocalDate.now());
    }
}
