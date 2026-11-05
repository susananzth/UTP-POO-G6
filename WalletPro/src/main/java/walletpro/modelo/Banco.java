package walletpro.modelo;

import walletpro.interfaces.*;
import walletpro.util.*;

import java.util.Scanner;

/**
 * Representa la entidad financiera. Se mantiene separada de
 * CuentaBancaria porque un mismo banco puede tener muchas cuentas
 * asociadas en el sistema (agregacion).
 */
public class Banco implements IValidable {

    private String codigoBanco;
    private String nombreBanco;

    public Banco() {
        this.codigoBanco = "00000";
        this.nombreBanco = "Sin nombre";
    }

    public void registrar(Scanner teclado) {
        String entrada;

        do {
            System.out.print("Codigo del banco (5 digitos): ");
            entrada = teclado.nextLine();
            if (!ValidRegex.esSoloDigitos(entrada, 5)) {
                System.out.println("El codigo debe tener exactamente 5 digitos.");
            }
        } while (!ValidRegex.esSoloDigitos(entrada, 5));
        this.codigoBanco = entrada;

        do {
            System.out.print("Nombre del banco: ");
            entrada = teclado.nextLine();
            if (!ValidRegex.esTextoValido(entrada)) {
                System.out.println("El nombre del banco no puede estar vacio.");
            }
        } while (!ValidRegex.esTextoValido(entrada));
        this.nombreBanco = entrada;
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
            this.nombreBanco = nombreBanco;
        }
    }

    @Override
    public boolean validar() {
        return ValidRegex.esSoloDigitos(codigoBanco, 5) && !nombreBanco.startsWith("Sin");
    }

    @Override
    public String toString() {
        return "Banco " + codigoBanco + " | " + nombreBanco;
    }
}
