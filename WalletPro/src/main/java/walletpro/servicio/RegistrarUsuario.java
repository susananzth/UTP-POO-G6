package walletpro.servicio;

import walletpro.modelo.Usuario;
import walletpro.util.ValidRegex;

/**
 * ALUMNO 1 - auth: resuelve "Registrar Usuario" dentro del menu de
 * acceso. Se separa de Usuario para que la clase de datos no
 * dependa de la logica de registro (igual criterio que Login y
 * RecuperarContrasena). Observacion levantada de la entrega
 * anterior: esta opcion existia en la Semana 3/6 pero se habia
 * perdido del menu de acceso al avanzar hacia el nuevo diseno; aqui
 * se repone.
 */
public class RegistrarUsuario {

    public RegistrarUsuario() {
    }

    /**
     * Valida que el nombre de usuario y el correo del nuevo Usuario
     * no esten repetidos en usuariosDB antes de aceptarlo.
     */
    public boolean registrar(Usuario[] usuariosDB, int totalUsuarios, Usuario nuevo) {
        if (totalUsuarios >= usuariosDB.length) {
            System.out.println("Limite de usuarios alcanzado.");
            return false;
        }
        if (!nuevo.validar()) {
            System.out.println("Los datos del usuario no son validos.");
            return false;
        }
        for (int i = 0; i < totalUsuarios; i++) {
            if (usuariosDB[i].getNombreUsuario().equalsIgnoreCase(nuevo.getNombreUsuario())) {
                System.out.println("El nombre de usuario ya existe.");
                return false;
            }
            if (usuariosDB[i].getCorreo().equalsIgnoreCase(nuevo.getCorreo())) {
                System.out.println("El correo ya esta registrado.");
                return false;
            }
        }
        return true;
    }
}
