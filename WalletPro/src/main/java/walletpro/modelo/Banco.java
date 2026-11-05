package walletpro.modelo;

import java.io.Serializable;
import walletpro.interfaces.*;
import walletpro.util.*;

/**
 * Representa la entidad financiera. Se mantiene separada de
 * CuentaBancaria porque un mismo banco puede tener muchas cuentas
 * asociadas en el sistema (agregacion).
 */
public class Banco implements IValidable, Serializable {

    private String codigoBanco;
    private String nombreBanco;

    public Banco() {
        this.codigoBanco = "";
        this.nombreBanco = "";
    }

    public void registrar() {
        this.codigoBanco = Consola.leerDigitos("Codigo del banco (5 digitos):", 5);
        this.nombreBanco = Consola.leerTexto("Nombre del banco:");
    }

    public String getCodigoBanco() { return codigoBanco; }
    public String getNombreBanco() { return nombreBanco; }

    public void setCodigoBanco(String codigoBanco) {
        if (ValidRegex.esSoloDigitos(codigoBanco, 5)) {
            this.codigoBanco = codigoBanco;
        }
    }

    public void setNombreBanco(String nombreBanco) {
        if (ValidRegex.esTextoValido(nombreBanco)) {
            this.nombreBanco = nombreBanco.trim();
        }
    }

    @Override
    public boolean validar() {
        return ValidRegex.esSoloDigitos(codigoBanco, 5) && ValidRegex.esTextoValido(nombreBanco);
    }

    @Override
    public String toString() {
        return nombreBanco + " (codigo " + codigoBanco + ")";
    }
}
