package walletpro.modelo;

import walletpro.interfaces.*;
import walletpro.util.*;

import java.util.Scanner;

/**
 * Clase abstracta que representa a toda persona del sistema.
 * Usuario y Empleado heredan de aqui porque ambos "son una" persona,
 * pero cada uno agrega un comportamiento distinto (credenciales de
 * acceso vs. datos laborales). No se instancia directamente.
 *
 * Siguiendo el estilo del curso: sin constructor con parametros,
 * registro interactivo con Scanner dentro de un do-while, validacion
 * con try-catch y expresiones regulares, y el Scanner se declara una
 * sola vez aqui (protected) para que las subclases lo hereden sin
 * volver a declararlo.
 */
public abstract class Persona implements IValidable, IConsultable {

    protected String dni;
    protected String nombres;
    protected String apellidos;
    protected String fechaNacimiento;
    protected String correo;

    public Persona() {
        this.dni = "00000000";
        this.nombres = "Sin nombre";
        this.apellidos = "Sin apellido";
        this.fechaNacimiento = "01-01-1900";
        this.correo = "sin@correo.com";
    }

    /**
     * Lee los datos comunes a toda persona. Las subclases la llaman
     * dentro de su propio registrar() antes de pedir sus campos
     * especificos (credenciales o datos laborales).
     */
    protected void registrarDatosPersonales(Scanner teclado) {
        String entrada;

        do {
            System.out.print("DNI (8 digitos): ");
            entrada = teclado.nextLine();
            if (!ValidRegex.esSoloDigitos(entrada, 8)) {
                System.out.println("DNI invalido. Debe tener exactamente 8 digitos.");
            }
        } while (!ValidRegex.esSoloDigitos(entrada, 8));
        this.dni = entrada;

        do {
            System.out.print("Nombres: ");
            entrada = teclado.nextLine();
            if (!ValidRegex.esTextoValido(entrada)) {
                System.out.println("Los nombres no pueden estar vacios.");
            }
        } while (!ValidRegex.esTextoValido(entrada));
        this.nombres = entrada;

        do {
            System.out.print("Apellidos: ");
            entrada = teclado.nextLine();
            if (!ValidRegex.esTextoValido(entrada)) {
                System.out.println("Los apellidos no pueden estar vacios.");
            }
        } while (!ValidRegex.esTextoValido(entrada));
        this.apellidos = entrada;

        do {
            System.out.print("Fecha de nacimiento (dd-mm-aaaa): ");
            entrada = teclado.nextLine();
            if (!ValidFecha.validar(entrada)) {
                System.out.println("Fecha invalida. Use el formato dd-mm-aaaa.");
            }
        } while (!ValidFecha.validar(entrada));
        this.fechaNacimiento = entrada;

        do {
            System.out.print("Correo: ");
            entrada = teclado.nextLine();
            if (!ValidRegex.esCorreoValido(entrada)) {
                System.out.println("Correo invalido.");
            }
        } while (!ValidRegex.esCorreoValido(entrada));
        this.correo = entrada;
    }

    public String getDni() { return dni; }
    public String getNombres() { return nombres; }
    public String getApellidos() { return apellidos; }
    public String getFechaNacimiento() { return fechaNacimiento; }
    public String getCorreo() { return correo; }

    public void setDni(String dni) {
        if (ValidRegex.esSoloDigitos(dni, 8)) {
            this.dni = dni;
        }
    }

    public void setNombres(String nombres) {
        if (ValidRegex.esTextoValido(nombres)) {
            this.nombres = nombres;
        }
    }

    public void setApellidos(String apellidos) {
        if (ValidRegex.esTextoValido(apellidos)) {
            this.apellidos = apellidos;
        }
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        if (ValidFecha.validar(fechaNacimiento)) {
            this.fechaNacimiento = fechaNacimiento;
        }
    }

    public void setCorreo(String correo) {
        if (ValidRegex.esCorreoValido(correo)) {
            this.correo = correo;
        }
    }

    public String getNombreCompleto() {
        return nombres + " " + apellidos;
    }

    @Override
    public boolean validar() {
        return ValidRegex.esSoloDigitos(dni, 8)
                && !nombres.equals("Sin nombre")
                && ValidRegex.esCorreoValido(correo);
    }

    @Override
    public String resumen() {
        String mensaje = "Datos de la persona";
        mensaje += "\nNombre: " + getNombreCompleto();
        mensaje += "\nDNI: " + dni;
        mensaje += "\nNacimiento: " + fechaNacimiento;
        mensaje += "\nCorreo: " + correo;
        return mensaje;
    }

    /** Metodo abstracto: cada subclase define su propio rol en el sistema. */
    public abstract String getRol();

    @Override
    public String toString() {
        return "[" + getRol() + "] " + getNombreCompleto();
    }
}
