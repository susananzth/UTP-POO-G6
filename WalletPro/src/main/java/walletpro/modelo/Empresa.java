package walletpro.modelo;

import walletpro.interfaces.*;
import walletpro.util.*;

import java.util.Scanner;

/**
 * Representa a la empresa empleadora de un Empleado. Se mantiene
 * separada de Empleado porque una misma empresa puede dar empleo a
 * muchas personas dentro del sistema (agregacion).
 */
public class Empresa implements IValidable {

    private String ruc;
    private String razonSocial;

    public Empresa() {
        this.ruc = "00000000000";
        this.razonSocial = "Sin razon social";
    }

    public void registrar(Scanner teclado) {
        String entrada;

        do {
            System.out.print("RUC (11 digitos): ");
            entrada = teclado.nextLine();
            if (!ValidRegex.esSoloDigitos(entrada, 11)) {
                System.out.println("RUC invalido. Debe tener exactamente 11 digitos.");
            }
        } while (!ValidRegex.esSoloDigitos(entrada, 11));
        this.ruc = entrada;

        do {
            System.out.print("Razon social: ");
            entrada = teclado.nextLine();
            if (!ValidRegex.esTextoValido(entrada)) {
                System.out.println("La razon social no puede estar vacia.");
            }
        } while (!ValidRegex.esTextoValido(entrada));
        this.razonSocial = entrada;
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
            this.razonSocial = razonSocial;
        }
    }

    @Override
    public boolean validar() {
        return ValidRegex.esSoloDigitos(ruc, 11) && !razonSocial.startsWith("Sin");
    }

    @Override
    public String toString() {
        return razonSocial + " (RUC: " + ruc + ")";
    }
}
