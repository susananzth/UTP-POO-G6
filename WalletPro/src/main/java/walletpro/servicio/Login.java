package walletpro.servicio;

import walletpro.datos.Repositorio;
import walletpro.modelo.Usuario;

/**
 * Encapsula el inicio de sesion, separado de Usuario para que la
 * clase de datos no dependa de la logica de acceso.
 */
public class Login {

    public static final int MAXIMO_INTENTOS = 3;

    public Login() {
    }

    /** Devuelve el usuario si las credenciales son correctas, o null si no. */
    public Usuario iniciarSesion(Repositorio<Usuario> usuarios, String nombreUsuario, String contrasena) {
        return usuarios.buscar(u -> u.validarPorNombreUsuario(nombreUsuario, contrasena));
    }
}
