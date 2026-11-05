package walletpro.util;

import java.util.regex.Pattern;

/**
 * Clase utilitaria de validacion con expresiones regulares.
 * No mantiene estado: solo metodos estaticos, igual que ValidFecha.
 */
public class ValidRegex {

    /** usuario@dominio.com, tambien acepta subdominios: usuario@utp.edu.pe */
    private static final Pattern PATRON_CORREO =
            Pattern.compile("^[\\w.+-]+@([A-Za-z0-9-]+\\.)+[A-Za-z]{2,}$");

    /** Minimo 8 caracteres, con al menos una letra y un numero. */
    private static final Pattern PATRON_CONTRASENA =
            Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d).{8,}$");

    /** Numero de cuenta bancaria: solo digitos, entre 6 y 20. */
    private static final Pattern PATRON_NUMERO_CUENTA =
            Pattern.compile("^\\d{6,20}$");

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

    public static boolean esNumeroCuentaValido(String texto) {
        if (texto == null) {
            return false;
        }
        return PATRON_NUMERO_CUENTA.matcher(texto).matches();
    }

    public static boolean esTextoValido(String texto) {
        return texto != null && texto.trim().length() > 0;
    }
}
