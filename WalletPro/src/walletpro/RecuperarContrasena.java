package walletpro;

/**
 * Encapsula el flujo de recuperacion de contrasena: busca al Usuario
 * por nombre de usuario y valida un codigo de verificacion fijo
 * antes de permitir actualizar la contrasena. Sin constructor con
 * parametros, igual que Login.
 */
public class RecuperarContrasena {

    private static final String CODIGO_VERIFICACION = "admin123";

    public RecuperarContrasena() {
    }

    private Usuario buscarPorNombreUsuario(Usuario[] usuariosDB, int totalUsuarios,
                                            String nombreUsuario) {
        for (int i = 0; i < totalUsuarios; i++) {
            if (usuariosDB[i].getNombreUsuario().equalsIgnoreCase(nombreUsuario)) {
                return usuariosDB[i];
            }
        }
        return null;
    }

    public boolean restablecerContrasena(Usuario[] usuariosDB, int totalUsuarios,
                                          String nombreUsuario, String codigoVerificacion,
                                          String nuevaContrasena) {
        Usuario usuario = buscarPorNombreUsuario(usuariosDB, totalUsuarios, nombreUsuario);
        if (usuario == null) {
            System.out.println("No existe una cuenta con ese nombre de usuario.");
            return false;
        }
        if (!codigoVerificacion.equals(CODIGO_VERIFICACION)) {
            System.out.println("Codigo de verificacion incorrecto.");
            return false;
        }
        if (!ValidRegex.esContrasenaValida(nuevaContrasena)) {
            System.out.println("La nueva contrasena debe tener al menos 8 caracteres, con letras y numeros.");
            return false;
        }
        usuario.setContrasena(nuevaContrasena);
        System.out.println("Contrasena actualizada con exito.");
        return true;
    }
}
