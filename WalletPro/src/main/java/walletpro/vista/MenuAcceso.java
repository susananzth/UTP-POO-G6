package walletpro.vista;

import walletpro.excepciones.OperacionInvalidaException;
import walletpro.modelo.Billetera;
import walletpro.modelo.Usuario;
import walletpro.servicio.Login;
import walletpro.servicio.RecuperarContrasena;
import walletpro.servicio.RegistrarUsuario;
import walletpro.util.Consola;

/**
 * Menu de acceso: es la puerta de entrada al sistema (login
 * obligatorio). Al cerrar sesion en el menu principal se vuelve aqui,
 * y el programa termina solo cuando el usuario elige "Salir".
 */
public class MenuAcceso extends Menu {

    public MenuAcceso(Billetera billetera) {
        super("WALLET PRO - ACCESO", billetera);
    }

    @Override
    protected String[] getOpciones() {
        return new String[] {
            "Iniciar sesion",
            "Registrar usuario",
            "Recuperar contrasena"
        };
    }

    @Override
    protected String getTextoSalida() {
        return "Salir del sistema";
    }

    @Override
    protected void ejecutarOpcion(int opcion) {
        switch (opcion) {
            case 1: iniciarSesion(); break;
            case 2: registrarUsuario(); break;
            case 3: recuperarContrasena(); break;
        }
    }

    private void iniciarSesion() {
        Login login = new Login();
        Usuario usuario = null;
        int intentos = 0;
        while (usuario == null && intentos < Login.MAXIMO_INTENTOS) {
            String nombreUsuario = Consola.leerTexto("Nombre de usuario:");
            String contrasena = Consola.leerTexto("Contrasena:");
            usuario = login.iniciarSesion(billetera.getUsuarios(), nombreUsuario, contrasena);
            intentos++;
            if (usuario == null) {
                System.out.println("Credenciales incorrectas. Intento " + intentos + " de " + Login.MAXIMO_INTENTOS + ".");
            }
        }
        if (usuario == null) {
            System.out.println("Demasiados intentos fallidos. Puede recuperar su contrasena desde el menu.");
            return;
        }
        System.out.println("Bienvenido/a, " + usuario.getNombres() + ".");
        MenuPrincipal menuPrincipal = new MenuPrincipal(billetera, usuario);
        menuPrincipal.mostrar();
        System.out.println("Sesion cerrada. Hasta pronto, " + usuario.getNombres() + ".");
    }

    private void registrarUsuario() {
        System.out.println("\n--- REGISTRAR USUARIO ---");
        try {
            RegistrarUsuario registro = new RegistrarUsuario();
            Usuario nuevo = registro.registrar(billetera);
            System.out.println("Usuario registrado con exito. Ya puede iniciar sesion como " + nuevo.getNombreUsuario() + ".");
        } catch (OperacionInvalidaException e) {
            System.out.println("No se pudo registrar: " + e.getMessage());
        }
    }

    private void recuperarContrasena() {
        System.out.println("\n--- RECUPERAR CONTRASENA ---");
        RecuperarContrasena recuperar = new RecuperarContrasena();
        String nombreUsuario = Consola.leerTexto("Nombre de usuario:");
        Usuario usuario = recuperar.buscarUsuario(billetera.getUsuarios(), nombreUsuario);
        if (usuario == null) {
            System.out.println("No existe una cuenta con ese nombre de usuario.");
            return;
        }
        System.out.println("Pregunta de seguridad: " + usuario.getPreguntaSeguridad());
        String respuesta = Consola.leerTexto("Respuesta:");
        String nueva = Consola.leerTexto("Nueva contrasena (minimo 8 caracteres, con letras y numeros):");
        try {
            recuperar.restablecerContrasena(usuario, respuesta, nueva);
            System.out.println("Contrasena actualizada con exito.");
        } catch (OperacionInvalidaException e) {
            System.out.println("No se pudo cambiar la contrasena: " + e.getMessage());
        }
    }
}
