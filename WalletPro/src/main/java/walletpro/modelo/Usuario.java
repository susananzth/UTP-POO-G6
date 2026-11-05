package walletpro.modelo;

import walletpro.interfaces.*;
import walletpro.util.*;

import java.util.Scanner;

/**
 * Usuario extiende de Persona: toda cuenta de acceso pertenece a una
 * persona real, asi que reutiliza dni/nombres/apellidos/fechaNacimiento
 * /correo y solo agrega las credenciales de acceso al sistema.
 *
 * Alumno 1 - auth: esta clase, junto con RegistrarUsuario, resuelve
 * la funcionalidad "Registrar Usuario" del modulo de autenticacion.
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
        this.nombreUsuario = "invitado";
        this.contrasena = "temporal1";
        this.preguntaSeguridad = "Sin pregunta";
        this.respuestaSeguridad = "Sin respuesta";
    }

    /**
     * Registra los datos personales (heredado de Persona) y luego
     * las credenciales de acceso, con try-catch no aplica aqui porque
     * son solo cadenas, pero se valida todo con do-while + regex.
     */
    public void registrar(Scanner teclado) {
        registrarDatosPersonales(teclado);

        String entrada;
        do {
            System.out.print("Nombre de usuario: ");
            entrada = teclado.nextLine();
            if (!ValidRegex.esTextoValido(entrada)) {
                System.out.println("El nombre de usuario no puede estar vacio.");
            }
        } while (!ValidRegex.esTextoValido(entrada));
        this.nombreUsuario = entrada;

        String confirmacion;
        do {
            System.out.print("Contrasena (min 8 caracteres, con letras y numeros): ");
            entrada = teclado.nextLine();
            System.out.print("Confirmar contrasena: ");
            confirmacion = teclado.nextLine();
            if (!ValidRegex.esContrasenaValida(entrada)) {
                System.out.println("La contrasena debe tener al menos 8 caracteres, con letras y numeros.");
            } else if (!entrada.equals(confirmacion)) {
                System.out.println("Las contrasenas no coinciden.");
            }
        } while (!ValidRegex.esContrasenaValida(entrada) || !entrada.equals(confirmacion));
        this.contrasena = entrada;

        System.out.print("Pregunta de seguridad: ");
        this.preguntaSeguridad = teclado.nextLine();
        System.out.print("Respuesta de seguridad: ");
        this.respuestaSeguridad = teclado.nextLine();
    }

    public int getIdUsuario() { return idUsuario; }
    public String getNombreUsuario() { return nombreUsuario; }
    public String getContrasena() { return contrasena; }
    public String getPreguntaSeguridad() { return preguntaSeguridad; }
    public String getRespuestaSeguridad() { return respuestaSeguridad; }

    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public void setNombreUsuario(String nombreUsuario) {
        if (ValidRegex.esTextoValido(nombreUsuario)) {
            this.nombreUsuario = nombreUsuario;
        }
    }

    public void setContrasena(String contrasena) {
        if (ValidRegex.esContrasenaValida(contrasena)) {
            this.contrasena = contrasena;
        }
    }

    public void setPreguntaSeguridad(String preguntaSeguridad) {
        if (ValidRegex.esTextoValido(preguntaSeguridad)) {
            this.preguntaSeguridad = preguntaSeguridad;
        }
    }

    public void setRespuestaSeguridad(String respuestaSeguridad) {
        if (ValidRegex.esTextoValido(respuestaSeguridad)) {
            this.respuestaSeguridad = respuestaSeguridad;
        }
    }

    public boolean validarPorNombreUsuario(String nombreUsuario, String contrasena) {
        return this.nombreUsuario.equalsIgnoreCase(nombreUsuario)
                && this.contrasena.equals(contrasena);
    }

    @Override
    public String getRol() {
        return "Usuario";
    }

    @Override
    public boolean validar() {
        return ValidRegex.esTextoValido(nombreUsuario)
                && ValidRegex.esContrasenaValida(contrasena);
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
