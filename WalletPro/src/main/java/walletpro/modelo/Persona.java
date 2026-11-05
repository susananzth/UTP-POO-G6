package walletpro.modelo;

import walletpro.interfaces.*;
import walletpro.util.*;

/**
 * Clase abstracta que representa a toda persona del sistema.
 * Usuario y Empleado heredan de aqui porque ambos "son una" persona,
 * pero cada uno agrega un comportamiento distinto (credenciales de
 * acceso vs. datos laborales). No se instancia directamente.
 *
 * Siguiendo el estilo del curso: sin constructor con parametros,
 * registro interactivo por teclado (a traves de la clase Consola)
 * y setters que validan antes de asignar.
 */
public abstract class Persona implements IValidable, IConsultable {

    protected String dni;
    protected String nombres;
    protected String apellidos;
    protected String fechaNacimiento;
    protected String correo;

    public Persona() {
        this.dni = "";
        this.nombres = "";
        this.apellidos = "";
        this.fechaNacimiento = "";
        this.correo = "";
    }

    /**
     * Lee los datos comunes a toda persona. Las subclases la llaman
     * dentro de su propio registrar() antes de pedir sus campos
     * especificos (credenciales o datos laborales).
     */
    protected void registrarDatosPersonales() {
        this.dni = Consola.leerDigitos("DNI (8 digitos):", 8);
        this.nombres = Consola.leerTexto("Nombres:");
        this.apellidos = Consola.leerTexto("Apellidos:");
        this.fechaNacimiento = Consola.leerFechaPasada("Fecha de nacimiento (dd-mm-aaaa):");

        String entrada;
        do {
            entrada = Consola.leerTexto("Correo:");
            if (!ValidRegex.esCorreoValido(entrada)) {
                System.out.println("Correo invalido. Ejemplo: nombre@correo.com");
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
            this.nombres = nombres.trim();
        }
    }

    public void setApellidos(String apellidos) {
        if (ValidRegex.esTextoValido(apellidos)) {
            this.apellidos = apellidos.trim();
        }
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        if (ValidFecha.validar(fechaNacimiento) && !ValidFecha.esFutura(fechaNacimiento)) {
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
                && ValidRegex.esTextoValido(nombres)
                && ValidRegex.esTextoValido(apellidos)
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
        return getNombreCompleto() + " (DNI " + dni + ")";
    }
}
