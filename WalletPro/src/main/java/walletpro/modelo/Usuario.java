package walletpro.modelo;

import walletpro.util.Consola;
import walletpro.util.Validacion;

/**
 * Cuenta para entrar al sistema. HEREDA de Persona (dni, nombres...)
 * y agrega las credenciales.
 * Tabla: usuario | DAO: UsuarioDAO | Menu: MenuAcceso
 */
public class Usuario extends Persona {

    private int idUsuario;
    private String nombreUsuario = "";
    private String contrasena = "";
    private String preguntaSeguridad = "";
    private String respuestaSeguridad = "";

    // Lo llama: MenuAcceso.registrarUsuario()
    public void registrar() {
        registrarDatosPersonales();   // metodo heredado de Persona
        nombreUsuario = Consola.leerTexto("Nombre de usuario:");

        String confirmacion;
        do {
            contrasena = Consola.leerTexto("Contrasena (minimo 8 caracteres, con letras y numeros):");
            confirmacion = Consola.leerTexto("Repita la contrasena:");
            if (!Validacion.esContrasenaValida(contrasena)) {
                System.out.println("La contrasena debe tener 8 caracteres o mas, con letras y numeros.");
            } else if (!contrasena.equals(confirmacion)) {
                System.out.println("Las contrasenas no coinciden.");
            }
        } while (!Validacion.esContrasenaValida(contrasena) || !contrasena.equals(confirmacion));

        // Sirven para recuperar la contrasena si la olvida
        preguntaSeguridad = Consola.leerTexto("Pregunta de seguridad (ej. Nombre de mi primera mascota):");
        respuestaSeguridad = Consola.leerTexto("Respuesta:");
    }

    // Lo llama: MenuAcceso.iniciarSesion()
    public boolean validarContrasena(String clave) {
        return contrasena.equals(clave);
    }

    // No distingue mayusculas: "Azul" y "azul" valen igual.
    // Lo llama: MenuAcceso.recuperarContrasena()
    public boolean validarRespuesta(String respuesta) {
        return respuestaSeguridad.equalsIgnoreCase(respuesta.trim());
    }

    // POLIMORFISMO: version de Usuario del metodo abstracto de Persona
    @Override
    public String getRol() {
        return "Usuario del sistema";
    }

    @Override
    public String resumen() {
        String mensaje = "Usuario: " + nombreUsuario;
        mensaje += "\nNombre: " + getNombreCompleto();
        mensaje += "\nCorreo: " + correo;
        mensaje += "\nRol: " + getRol();
        return mensaje;
    }

    public int getIdUsuario() { return idUsuario; }
    public String getNombreUsuario() { return nombreUsuario; }
    public String getContrasena() { return contrasena; }
    public String getPreguntaSeguridad() { return preguntaSeguridad; }
    public String getRespuestaSeguridad() { return respuestaSeguridad; }

    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
    public void setPreguntaSeguridad(String preguntaSeguridad) { this.preguntaSeguridad = preguntaSeguridad; }
    public void setRespuestaSeguridad(String respuestaSeguridad) { this.respuestaSeguridad = respuestaSeguridad; }
}
