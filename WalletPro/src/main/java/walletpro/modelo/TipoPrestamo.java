package walletpro.modelo;

import java.io.Serializable;
import walletpro.interfaces.*;
import walletpro.util.*;

/**
 * Clasifica un Prestamo (Personal, Hipotecario, Vehicular, etc.).
 */
public class TipoPrestamo implements IValidable, Serializable {

    private String nombre;
    private String descripcion;

    public TipoPrestamo() {
        this.nombre = "";
        this.descripcion = "Sin descripcion";
    }

    public void registrar() {
        this.nombre = Consola.leerTexto("Nombre del tipo de prestamo:");
        this.descripcion = Consola.leerTextoOpcional("Descripcion (opcional, Enter para omitir):", "Sin descripcion");
    }

    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }

    public void setNombre(String nombre) {
        if (ValidRegex.esTextoValido(nombre)) {
            this.nombre = nombre.trim();
        }
    }

    public void setDescripcion(String descripcion) {
        if (ValidRegex.esTextoValido(descripcion)) {
            this.descripcion = descripcion.trim();
        }
    }

    @Override
    public boolean validar() {
        return ValidRegex.esTextoValido(nombre);
    }

    @Override
    public String toString() {
        return nombre;
    }
}
