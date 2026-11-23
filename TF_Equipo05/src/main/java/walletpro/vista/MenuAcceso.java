package walletpro.vista;

import walletpro.dao.UsuarioDAO;
import walletpro.modelo.Usuario;
import walletpro.util.Consola;
import walletpro.util.Validacion;

/**
 * Primer menu: iniciar sesion, registrarse o recuperar la contrasena.
 * Lo llama: Main.main()
 * Llama a: MenuPrincipal (cuando el login es correcto) y UsuarioDAO (tabla usuario).
 */
public class MenuAcceso {

    private static final int MAXIMO_INTENTOS = 3;

    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    public void mostrar() {
        int opcion;
        do {
            String mensaje = "\n========== WALLET PRO - ACCESO ==========";
            mensaje += "\n1. Iniciar sesion";
            mensaje += "\n2. Registrar usuario";
            mensaje += "\n3. Recuperar contrasena";
            mensaje += "\n0. Salir del sistema";
            System.out.println(mensaje);

            opcion = Consola.leerEntero("Seleccione una opcion:", 0, 3);
            switch (opcion) {
                case 1:
                    iniciarSesion();
                    break;
                case 2:
                    registrarUsuario();
                    break;
                case 3:
                    recuperarContrasena();
                    break;
            }
        } while (opcion != 0);
    }

    // Hasta 3 intentos. Si entra, abre el menu principal.
    private void iniciarSesion() {
        Usuario usuario = null;
        int intentos = 0;
        while (usuario == null && intentos < MAXIMO_INTENTOS) {
            String nombreUsuario = Consola.leerTexto("Nombre de usuario:");
            String clave = Consola.leerTexto("Contrasena:");
            intentos++;

            Usuario encontrado = usuarioDAO.buscarPorNombreUsuario(nombreUsuario);
            if (encontrado != null && encontrado.validarContrasena(clave)) {
                usuario = encontrado;
            } else {
                System.out.println("Usuario o contrasena incorrectos. Intento " + intentos + " de " + MAXIMO_INTENTOS + ".");
            }
        }
        if (usuario == null) {
            System.out.println("Demasiados intentos. Puede recuperar su contrasena (opcion 3).");
            return;
        }

        System.out.println("Bienvenido/a, " + usuario.getNombres() + ".");
        MenuPrincipal menuPrincipal = new MenuPrincipal();
        menuPrincipal.setUsuario(usuario);   // setter en lugar de constructor con parametros
        menuPrincipal.mostrar();
        System.out.println("Sesion cerrada.");
    }

    private void registrarUsuario() {
        System.out.println("\n--- REGISTRAR USUARIO ---");
        Usuario usuario = new Usuario();
        usuario.registrar();                 // pide los datos por teclado
        if (usuarioDAO.insertar(usuario)) {  // INSERT en la tabla usuario
            System.out.println("Usuario registrado. Ya puede iniciar sesion como " + usuario.getNombreUsuario() + ".");
        }
    }

    private void recuperarContrasena() {
        System.out.println("\n--- RECUPERAR CONTRASENA ---");
        String nombreUsuario = Consola.leerTexto("Nombre de usuario:");
        Usuario usuario = usuarioDAO.buscarPorNombreUsuario(nombreUsuario);
        if (usuario == null) {
            System.out.println("No existe ese usuario.");
            return;
        }

        System.out.println("Pregunta de seguridad: " + usuario.getPreguntaSeguridad());
        String respuesta = Consola.leerTexto("Respuesta:");
        if (!usuario.validarRespuesta(respuesta)) {
            System.out.println("Respuesta incorrecta.");
            return;
        }

        String nueva = Consola.leerTexto("Nueva contrasena (minimo 8 caracteres, con letras y numeros):");
        if (!Validacion.esContrasenaValida(nueva)) {
            System.out.println("La contrasena no cumple los requisitos. No se cambio.");
            return;
        }
        usuario.setContrasena(nueva);
        if (usuarioDAO.actualizarContrasena(usuario)) {   // UPDATE en la tabla usuario
            System.out.println("Contrasena actualizada.");
        }
    }
}
