package walletpro.modelo;

import walletpro.interfaces.*;
import walletpro.util.*;

/**
 * Usuario extiende de Persona: toda cuenta de acceso pertenece a una
 * persona real, asi que reutiliza dni/nombres/apellidos/fechaNacimiento
 * /correo y solo agrega las credenciales de acceso al sistema.
 */
public class Usuario extends Persona {

    private int idUsuario;
    private String nombreUsuario;
    private String contrasena;
    private String preguntaSeguridad;
    private String respuestaSeguridad;

    public Usuario() {
        super();
        this.idUsuario = 0;
        this.nombreUsuario = "";
        this.contrasena = "";
        this.preguntaSeguridad = "";
        this.respuestaSeguridad = "";
    }

    /**
     * Registra los datos personales (heredado de Persona) y luego
     * las credenciales de acceso.
     */
    public void registrar() {
        registrarDatosPersonales();

        this.nombreUsuario = Consola.leerTexto("Nombre de usuario:");

        String entrada;
        String confirmacion;
        do {
            entrada = Consola.leerTexto("Contrasena (minimo 8 caracteres, con letras y numeros):");
            confirmacion = Consola.leerTexto("Confirmar contrasena:");
            if (!ValidRegex.esContrasenaValida(entrada)) {
                System.out.println("La contrasena debe tener al menos 8 caracteres, con letras y numeros.");
            } else if (!entrada.equals(confirmacion)) {
                System.out.println("Las contrasenas no coinciden.");
            }
        } while (!ValidRegex.esContrasenaValida(entrada) || !entrada.equals(confirmacion));
        this.contrasena = entrada;

        // Se usan para recuperar la contrasena si el usuario la olvida.
        this.preguntaSeguridad = Consola.leerTexto("Pregunta de seguridad (ej. Nombre de mi primera mascota):");
        this.respuestaSeguridad = Consola.leerTexto("Respuesta de seguridad:");
    }

    public int getIdUsuario() { return idUsuario; }
    public String getNombreUsuario() { return nombreUsuario; }
    public String getPreguntaSeguridad() { return preguntaSeguridad; }

    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public void setNombreUsuario(String nombreUsuario) {
        if (ValidRegex.esTextoValido(nombreUsuario)) {
            this.nombreUsuario = nombreUsuario.trim();
        }
    }

    public void setContrasena(String contrasena) {
        if (ValidRegex.esContrasenaValida(contrasena)) {
            this.contrasena = contrasena;
        }
    }

    public void setPreguntaSeguridad(String preguntaSeguridad) {
        if (ValidRegex.esTextoValido(preguntaSeguridad)) {
            this.preguntaSeguridad = preguntaSeguridad.trim();
        }
    }

    public void setRespuestaSeguridad(String respuestaSeguridad) {
        if (ValidRegex.esTextoValido(respuestaSeguridad)) {
            this.respuestaSeguridad = respuestaSeguridad.trim();
        }
    }

    public boolean validarPorNombreUsuario(String nombreUsuario, String contrasena) {
        return this.nombreUsuario.equalsIgnoreCase(nombreUsuario)
                && this.contrasena.equals(contrasena);
    }

    /**
     * Compara la respuesta de seguridad sin distinguir mayusculas ni
     * espacios al inicio/final. La contrasena y la respuesta no tienen
     * getter publico: nadie fuera de esta clase puede leerlas
     * (encapsulamiento), solo comprobarlas.
     */
    public boolean validarRespuestaSeguridad(String respuesta) {
        return respuesta != null && respuestaSeguridad.equalsIgnoreCase(respuesta.trim());
    }

    @Override
    public String getRol() {
        return "Usuario";
    }

    @Override
    public boolean validar() {
        return super.validar()
                && ValidRegex.esTextoValido(nombreUsuario)
                && ValidRegex.esContrasenaValida(contrasena)
                && ValidRegex.esTextoValido(preguntaSeguridad)
                && ValidRegex.esTextoValido(respuestaSeguridad);
    }

    @Override
    public String resumen() {
        String mensaje = "Datos del usuario";
        mensaje += "\nID: " + idUsuario;
        mensaje += "\nUsuario: " + nombreUsuario;
        mensaje += "\nNombre: " + getNombreCompleto();
        mensaje += "\nCorreo: " + correo;
        return mensaje;
    }
}
