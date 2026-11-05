package walletpro.modelo;

import walletpro.interfaces.*;
import walletpro.util.*;

/**
 * Empleado extiende de Persona: agrega los datos laborales (cargo,
 * empresa y sueldo base) necesarios para el modulo de Ingresos y
 * Ahorros (Alumno 1).
 */
public class Empleado extends Persona {

    private Cargo cargo;
    private Empresa empresa;
    private double sueldoBase;

    public Empleado() {
        super();
        this.cargo = null;
        this.empresa = null;
        this.sueldoBase = 0;
    }

    /**
     * Registra los datos personales heredados y el sueldo base.
     * Cargo y Empresa se asignan aparte (setCargo/setEmpresa), luego
     * de que el menu muestre la lista y el usuario seleccione una.
     */
    public void registrar() {
        registrarDatosPersonales();
        this.sueldoBase = Consola.leerMonto("Sueldo base mensual (S/):");
    }

    public Cargo getCargo() { return cargo; }
    public Empresa getEmpresa() { return empresa; }
    public double getSueldoBase() { return sueldoBase; }

    public void setCargo(Cargo cargo) { this.cargo = cargo; }
    public void setEmpresa(Empresa empresa) { this.empresa = empresa; }

    public void setSueldoBase(double sueldoBase) {
        if (sueldoBase > 0) {
            this.sueldoBase = Dinero.redondear(sueldoBase);
        }
    }

    public double calcularSueldoAnual() {
        return Dinero.redondear(sueldoBase * 12);
    }

    @Override
    public String getRol() {
        return "Empleado";
    }

    @Override
    public boolean validar() {
        return super.validar() && cargo != null && empresa != null && sueldoBase > 0;
    }

    @Override
    public String resumen() {
        String nombreEmpresa = (empresa != null) ? empresa.getRazonSocial() : "Sin empresa";
        String nombreCargo = (cargo != null) ? cargo.getNombreCargo() : "Sin cargo";
        String mensaje = "Datos del empleado";
        mensaje += "\nNombre: " + getNombreCompleto();
        mensaje += "\nDNI: " + dni;
        mensaje += "\nEmpresa: " + nombreEmpresa;
        mensaje += "\nCargo: " + nombreCargo;
        mensaje += "\nSueldo base: " + Dinero.formatear(sueldoBase);
        return mensaje;
    }
}
