package walletpro.modelo;

import java.io.Serializable;
import walletpro.interfaces.*;
import walletpro.util.*;

/**
 * Representa a la empresa empleadora de un Empleado. Se mantiene
 * separada de Empleado porque una misma empresa puede dar empleo a
 * muchas personas dentro del sistema (agregacion).
 */
public class Empresa implements IValidable, Serializable {

    private String ruc;
    private String razonSocial;

    public Empresa() {
        this.ruc = "";
        this.razonSocial = "";
    }

    public void registrar() {
        this.ruc = Consola.leerDigitos("RUC (11 digitos):", 11);
        this.razonSocial = Consola.leerTexto("Razon social:");
    }

    public String getRuc() { return ruc; }
    public String getRazonSocial() { return razonSocial; }

    public void setRuc(String ruc) {
        if (ValidRegex.esSoloDigitos(ruc, 11)) {
            this.ruc = ruc;
        }
    }

    public void setRazonSocial(String razonSocial) {
        if (ValidRegex.esTextoValido(razonSocial)) {
            this.razonSocial = razonSocial.trim();
        }
    }

    @Override
    public boolean validar() {
        return ValidRegex.esSoloDigitos(ruc, 11) && ValidRegex.esTextoValido(razonSocial);
    }

    @Override
    public String toString() {
        return razonSocial + " (RUC: " + ruc + ")";
    }
}
