package walletpro.modelo;

import java.io.Serializable;
import walletpro.interfaces.*;
import walletpro.util.*;

/**
 * Agrupa los gastos (Alimentacion, Transporte, Salud...). Los
 * presupuestos se definen por categoria.
 */
public class CategoriaGasto implements IValidable, Serializable {

    private String nombreCategoria;
    private String descripcion;
    private boolean esFija;

    public CategoriaGasto() {
        this.nombreCategoria = "";
        this.descripcion = "Sin descripcion";
        this.esFija = false;
    }

    public void registrar() {
        this.nombreCategoria = Consola.leerTexto("Nombre de la categoria:");
        this.descripcion = Consola.leerTextoOpcional("Descripcion (opcional, Enter para omitir):", "Sin descripcion");
        this.esFija = Consola.leerSiNo("Es un gasto fijo (se repite cada mes)?");
    }

    public String getNombreCategoria() { return nombreCategoria; }
    public String getDescripcion() { return descripcion; }
    public boolean isEsFija() { return esFija; }

    public void setNombreCategoria(String nombreCategoria) {
        if (ValidRegex.esTextoValido(nombreCategoria)) {
            this.nombreCategoria = nombreCategoria.trim();
        }
    }

    public void setDescripcion(String descripcion) {
        if (ValidRegex.esTextoValido(descripcion)) {
            this.descripcion = descripcion.trim();
        }
    }

    public void setEsFija(boolean esFija) { this.esFija = esFija; }

    @Override
    public boolean validar() {
        return ValidRegex.esTextoValido(nombreCategoria);
    }

    @Override
    public String toString() {
        String tipo = esFija ? "Fija" : "Variable";
        return nombreCategoria + " (" + tipo + ")";
    }
}
