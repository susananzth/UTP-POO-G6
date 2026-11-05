package walletpro.modelo;

import java.io.Serializable;
import walletpro.interfaces.*;
import walletpro.util.*;

/**
 * ALUMNO 1 - Registrar Trabajo. Relaciona a un Empleado con una
 * Empresa y un Cargo ya registrados (agregacion), asignados por
 * setter despues de que el menu muestre las listas.
 */
public class Trabajo implements IValidable, IConsultable, Serializable {

    private static final String[] TIPOS_CONTRATO = {"Planilla", "Independiente", "Contrato", "Practicas"};

    private Empresa empresa;
    private Cargo cargo;
    private String fechaIngreso;
    private String tipoContrato;
    private Empleado empleado;

    public Trabajo() {
        this.empresa = null;
        this.cargo = null;
        this.fechaIngreso = "";
        this.tipoContrato = TIPOS_CONTRATO[0];
        this.empleado = null;
    }

    public void registrar() {
        this.fechaIngreso = Consola.leerFechaPasada("Fecha de ingreso (dd-mm-aaaa):");

        String mensaje = "Tipo de contrato:";
        for (int i = 0; i < TIPOS_CONTRATO.length; i++) {
            mensaje += "\n" + (i + 1) + ". " + TIPOS_CONTRATO[i];
        }
        System.out.println(mensaje);
        int opcion = Consola.leerEntero("Seleccione una opcion:", 1, TIPOS_CONTRATO.length);
        this.tipoContrato = TIPOS_CONTRATO[opcion - 1];
    }

    public Empresa getEmpresa() { return empresa; }
    public Cargo getCargo() { return cargo; }
    public String getFechaIngreso() { return fechaIngreso; }
    public String getTipoContrato() { return tipoContrato; }
    public Empleado getEmpleado() { return empleado; }

    public void setEmpresa(Empresa empresa) { this.empresa = empresa; }
    public void setCargo(Cargo cargo) { this.cargo = cargo; }
    public void setEmpleado(Empleado empleado) { this.empleado = empleado; }

    public void setFechaIngreso(String fechaIngreso) {
        if (ValidFecha.validar(fechaIngreso)) {
            this.fechaIngreso = fechaIngreso;
        }
    }

    public void setTipoContrato(String tipoContrato) {
        for (String tipo : TIPOS_CONTRATO) {
            if (tipo.equalsIgnoreCase(tipoContrato)) {
                this.tipoContrato = tipo;
            }
        }
    }

    @Override
    public boolean validar() {
        return empresa != null && empresa.validar()
                && cargo != null && cargo.validar()
                && empleado != null
                && ValidFecha.validar(fechaIngreso);
    }

    @Override
    public String resumen() {
        String nombreEmpleado = (empleado != null) ? empleado.getNombreCompleto() : "Sin empleado";
        String mensaje = "Datos del trabajo";
        mensaje += "\nEmpleado: " + nombreEmpleado;
        mensaje += "\nEmpresa: " + empresa;
        mensaje += "\nCargo: " + cargo;
        mensaje += "\nIngreso: " + fechaIngreso;
        mensaje += "\nContrato: " + tipoContrato;
        return mensaje;
    }

    @Override
    public String toString() {
        String nombreEmpleado = (empleado != null) ? empleado.getNombreCompleto() : "Sin empleado";
        return nombreEmpleado + " | " + cargo + " en " + empresa + " | " + tipoContrato
                + " desde " + fechaIngreso;
    }
}
