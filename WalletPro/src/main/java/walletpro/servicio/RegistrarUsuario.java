package walletpro.servicio;

import walletpro.excepciones.OperacionInvalidaException;
import walletpro.modelo.Billetera;
import walletpro.modelo.Usuario;

/**
 * Resuelve "Registrar Usuario" dentro del menu de acceso. Se separa de
 * Usuario para que la clase de datos no dependa de la logica de
 * registro (mismo criterio que Login y RecuperarContrasena).
 */
public class RegistrarUsuario {

    public RegistrarUsuario() {
    }

    /**
     * Pide los datos por teclado y guarda el usuario en la billetera.
     * Si el nombre de usuario o el correo ya existen, la billetera
     * lanza OperacionInvalidaException y el usuario no se guarda.
     */
    public Usuario registrar(Billetera billetera) throws OperacionInvalidaException {
        Usuario nuevo = new Usuario();
        nuevo.registrar();
        billetera.agregarUsuario(nuevo);
        return nuevo;
    }
}
