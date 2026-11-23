package walletpro.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

/**
 * Metodos que responden true/false: "este dato es valido?".
 * No guarda nada, por eso todos sus metodos son static.
 *
 * Lo usan: Consola (al leer del teclado) y algunas clases del modelo.
 */
public class Validacion {

    // Formato de fecha del programa: dia-mes-anio, ej. 05-11-2026.
    // STRICT: rechaza fechas que no existen, como 31-02-2026.
    private static final DateTimeFormatter FORMATO =
            DateTimeFormatter.ofPattern("dd-MM-uuuu").withResolverStyle(ResolverStyle.STRICT);

    public static boolean esFechaValida(String fecha) {
        try {
            LocalDate dia = LocalDate.parse(fecha, FORMATO);
            return dia.getYear() >= 1900 && dia.getYear() <= 2100;
        } catch (DateTimeParseException e) {
            // parse lanza esta excepcion si el texto no es una fecha correcta
            return false;
        }
    }

    // Supone que la fecha ya fue validada con esFechaValida.
    public static boolean esFechaFutura(String fecha) {
        return LocalDate.parse(fecha, FORMATO).isAfter(LocalDate.now());
    }

    // Fecha de hoy en formato dd-mm-aaaa (se usa para fechar los movimientos).
    public static String hoy() {
        return LocalDate.now().format(FORMATO);
    }

    public static int mesDe(String fecha) {
        return LocalDate.parse(fecha, FORMATO).getMonthValue();
    }

    public static int anioDe(String fecha) {
        return LocalDate.parse(fecha, FORMATO).getYear();
    }

    // matches() compara el texto con una expresion regular.
    // \\d{8} = exactamente 8 digitos.
    public static boolean esSoloDigitos(String texto, int cantidad) {
        return texto.matches("\\d{" + cantidad + "}");
    }

    // algo@dominio.algo (acepta subdominios: alumno@utp.edu.pe)
    public static boolean esCorreoValido(String texto) {
        return texto.matches("[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+");
    }

    // Minimo 8 caracteres, con al menos una letra y un numero.
    public static boolean esContrasenaValida(String texto) {
        return texto.length() >= 8
                && texto.matches(".*[A-Za-z].*")
                && texto.matches(".*\\d.*");
    }

    // Numero de cuenta: solo digitos, entre 6 y 20.
    public static boolean esNumeroCuentaValido(String texto) {
        return texto.matches("\\d{6,20}");
    }
}
