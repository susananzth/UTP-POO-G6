package walletpro.modelo;

import java.io.Serializable;
import walletpro.interfaces.*;
import walletpro.util.*;

/**
 * Representa el puesto que ocupa un Empleado dentro de su Empresa.
 * Un mismo cargo (ej. "Desarrollador") puede repetirse en varios
 * empleados, por eso se mantiene como clase propia.
 */
public class Cargo implements IValidable, Serializable {

    private String nombreCargo;
    private String descripcion;

    public Cargo() {
        this.nombreCargo = "";
        this.descripcion = "Sin descripcion";
    }

    public void registrar() {
        this.nombreCargo = Consola.leerTexto("Nombre del cargo:");
        this.descripcion = Consola.leerTextoOpcional("Descripcion (opcional, Enter para omitir):", "Sin descripcion");
    }

    public String getNombreCargo() { return nombreCargo; }
    public String getDescripcion() { return descripcion; }

    public void setNombreCargo(String nombreCargo) {
        if (ValidRegex.esTextoValido(nombreCargo)) {
            this.nombreCargo = nombreCargo.trim();
        }
    }

    public void setDescripcion(String descripcion) {
        if (ValidRegex.esTextoValido(descripcion)) {
            this.descripcion = descripcion.trim();
        }
    }

    @Override
    public boolean validar() {
        return ValidRegex.esTextoValido(nombreCargo);
    }

    @Override
    public String toString() {
        return nombreCargo;
    }
}
