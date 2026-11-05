package walletpro.servicio;

import walletpro.datos.Repositorio;
import walletpro.excepciones.OperacionInvalidaException;
import walletpro.modelo.Usuario;
import walletpro.util.ValidRegex;

/**
 * Flujo de recuperacion de contrasena con la PREGUNTA DE SEGURIDAD que
 * el usuario eligio al registrarse.
 *
 * Antes se usaba un codigo fijo ("admin123") igual para todos: quien lo
 * conociera podia cambiar la contrasena de cualquier usuario. Ahora
 * solo el dueno de la cuenta conoce la respuesta.
 */
public class RecuperarContrasena {

    public RecuperarContrasena() {
    }

    public Usuario buscarUsuario(Repositorio<Usuario> usuarios, String nombreUsuario) {
        return usuarios.buscar(u -> u.getNombreUsuario().equalsIgnoreCase(nombreUsuario));
    }

    public void restablecerContrasena(Usuario usuario, String respuesta, String nuevaContrasena)
            throws OperacionInvalidaException {
        if (!usuario.validarRespuestaSeguridad(respuesta)) {
            throw new OperacionInvalidaException("La respuesta de seguridad no es correcta.");
        }
        if (!ValidRegex.esContrasenaValida(nuevaContrasena)) {
            throw new OperacionInvalidaException("La nueva contrasena debe tener al menos 8 caracteres, con letras y numeros.");
        }
        usuario.setContrasena(nuevaContrasena);
    }
}
