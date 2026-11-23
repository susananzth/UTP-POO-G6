package walletpro.modelo;

import walletpro.interfaces.IConsultable;
import walletpro.util.Consola;

/**
 * CLASE ABSTRACTA con los datos comunes de toda persona.
 * No se puede hacer new Persona(): solo sirve para heredar.
 * Clases hijas (HERENCIA): Usuario y Empleado.
 */
public abstract class Persona implements IConsultable {

    // protected: las clases hijas pueden usarlos directamente
    protected String dni;
    protected String nombres;
    protected String apellidos;
    protected String fechaNacimiento;   // dd-mm-aaaa
    protected String correo;

    // Constructor SIN parametros (regla del profesor): deja todo vacio.
    public Persona() {
        dni = "";
        nombres = "";
        apellidos = "";
        fechaNacimiento = "";
        correo = "";
    }

    // Pide por teclado los datos comunes.
    // Lo llaman: Usuario.registrar() y Empleado.registrar()
    protected void registrarDatosPersonales() {
        dni = Consola.leerDigitos("DNI (8 digitos):", 8);
        nombres = Consola.leerTexto("Nombres:");
        apellidos = Consola.leerTexto("Apellidos:");
        fechaNacimiento = Consola.leerFechaPasada("Fecha de nacimiento (dd-mm-aaaa):");
        correo = Consola.leerCorreo("Correo:");
    }

    // METODO ABSTRACTO: cada hija responde distinto (POLIMORFISMO).
    public abstract String getRol();

    public String getNombreCompleto() {
        return nombres + " " + apellidos;
    }

    public String getDni() { return dni; }
    public String getNombres() { return nombres; }
    public String getApellidos() { return apellidos; }
    public String getFechaNacimiento() { return fechaNacimiento; }
    public String getCorreo() { return correo; }

    // Los setters los usan los DAO al leer de la base de datos.
    public void setDni(String dni) { this.dni = dni; }
    public void setNombres(String nombres) { this.nombres = nombres; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public void setFechaNacimiento(String fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    public void setCorreo(String correo) { this.correo = correo; }

    // Como se ve la persona en las listas para elegir (Consola.seleccionar).
    @Override
    public String toString() {
        return getNombreCompleto() + " (DNI " + dni + ")";
    }
}
