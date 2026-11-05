package walletpro.servicio;

import walletpro.modelo.Usuario;
import walletpro.util.ValidRegex;

/**
 * Encapsula el inicio de sesion, separado de Usuario para que la
 * clase de datos no dependa de la logica de acceso. Sin constructor
 * con parametros: recibe el arreglo de usuarios directamente en el
 * metodo, no lo guarda como atributo.
 */
public class Login {

    public Login() {
    }

    public Usuario iniciarSesion(Usuario[] usuariosDB, int totalUsuarios,
                                  String nombreUsuario, String contrasena) {
        for (int i = 0; i < totalUsuarios; i++) {
            if (usuariosDB[i].validarPorNombreUsuario(nombreUsuario, contrasena)) {
                return usuariosDB[i];
            }
        }
        return null;
    }
}
