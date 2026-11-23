package walletpro.modelo;

import walletpro.util.Consola;
import walletpro.util.Dinero;

/**
 * Persona que trabaja, cobra sueldo, gasta y tiene cuentas.
 * HEREDA de Persona y agrega el sueldo base.
 * Tabla: empleado | DAO: EmpleadoDAO | Menu: MenuIngresos (Alumno 1)
 */
public class Empleado extends Persona {

    private double sueldoBase;

    // Lo llama: MenuIngresos.registrarEmpleado()
    public void registrar() {
        registrarDatosPersonales();   // metodo heredado de Persona
        sueldoBase = Consola.leerMonto("Sueldo base mensual (S/):");
    }

    public double calcularSueldoAnual() {
        return Dinero.redondear(sueldoBase * 12);
    }

    // POLIMORFISMO: version de Empleado del metodo abstracto de Persona
    @Override
    public String getRol() {
        return "Empleado";
    }

    @Override
    public String resumen() {
        String mensaje = "Empleado: " + getNombreCompleto() + " (DNI " + dni + ")";
        mensaje += "\nCorreo: " + correo;
        mensaje += "\nSueldo base: " + Dinero.formatear(sueldoBase)
                + " | Anual: " + Dinero.formatear(calcularSueldoAnual());
        return mensaje;
    }

    public double getSueldoBase() { return sueldoBase; }

    public void setSueldoBase(double sueldoBase) { this.sueldoBase = sueldoBase; }
}
