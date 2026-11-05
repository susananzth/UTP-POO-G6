package walletpro.util;

import java.util.regex.Pattern;

/**
 * Clase utilitaria de validacion con expresiones regulares.
 * No mantiene estado: solo metodos estaticos, igual que ValidFecha.
 * Centraliza los patrones que antes se repetian como
 * correo.contains("@") en varias clases distintas.
 */
public class ValidRegex {

    private static final Pattern PATRON_CORREO =
            Pattern.compile("^[\\w.+-]+@[\\w-]+\\.[A-Za-z]{2,}$");

    private static final Pattern PATRON_CONTRASENA =
            Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d).{8,}$");

    private static final Pattern PATRON_SOLO_LETRAS =
            Pattern.compile("^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$");

    public static boolean esSoloDigitos(String texto, int longitud) {
        if (texto == null) {
            return false;
        }
        return texto.matches("\\d{" + longitud + "}");
    }

    public static boolean esCorreoValido(String texto) {
        if (texto == null) {
            return false;
        }
        return PATRON_CORREO.matcher(texto).matches();
    }

    public static boolean esContrasenaValida(String texto) {
        if (texto == null) {
            return false;
        }
        return PATRON_CONTRASENA.matcher(texto).matches();
    }

    public static boolean esSoloLetras(String texto) {
        if (texto == null) {
            return false;
        }
        return PATRON_SOLO_LETRAS.matcher(texto.trim()).matches();
    }

    public static boolean esTextoValido(String texto) {
        return texto != null && texto.trim().length() > 0;
    }
}
