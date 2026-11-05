package walletpro.modelo;

import java.util.ArrayList;
import java.util.List;
import walletpro.excepciones.OperacionInvalidaException;
import walletpro.interfaces.*;
import walletpro.util.*;

/**
 * ALUMNO 3 - Registrar Cuenta Bancaria. Implementa OperacionFinanciera
 * porque es la unica clase que mueve dinero. Es duena de sus
 * Movimiento (composicion: un movimiento no tiene sentido fuera de la
 * cuenta donde ocurrio).
 *
 * El saldo NO tiene setter: solo cambia con depositar() o retirar(),
 * y cada cambio queda guardado como Movimiento (trazabilidad).
 */
public class CuentaBancaria implements OperacionFinanciera, IValidable, IConsultable {

    private static final String[] TIPOS_CUENTA = {"Ahorros", "Corriente"};

    private String numeroCuenta;
    private String tipoCuenta;
    private double saldo;
    private Banco banco;
    private Empleado titular;
    private List<Movimiento> movimientos;

    public CuentaBancaria() {
        this.numeroCuenta = "";
        this.tipoCuenta = TIPOS_CUENTA[0];
        this.saldo = 0;
        this.banco = null;
        this.titular = null;
        this.movimientos = new ArrayList<>();
    }

    /**
     * Pide numero y tipo de cuenta. El saldo inicial lo pide el menu
     * y se registra como un deposito, para que quede en el historial.
     */
    public void registrar() {
        String entrada;
        do {
            entrada = Consola.leerTexto("Numero de cuenta (6 a 20 digitos):");
            if (!ValidRegex.esNumeroCuentaValido(entrada)) {
                System.out.println("El numero de cuenta debe tener solo digitos (entre 6 y 20).");
            }
        } while (!ValidRegex.esNumeroCuentaValido(entrada));
        this.numeroCuenta = entrada;

        String mensaje = "Tipo de cuenta:";
        for (int i = 0; i < TIPOS_CUENTA.length; i++) {
            mensaje += "\n" + (i + 1) + ". " + TIPOS_CUENTA[i];
        }
        System.out.println(mensaje);
        int opcion = Consola.leerEntero("Seleccione una opcion:", 1, TIPOS_CUENTA.length);
        this.tipoCuenta = TIPOS_CUENTA[opcion - 1];
    }

    public String getNumeroCuenta() { return numeroCuenta; }
    public String getTipoCuenta() { return tipoCuenta; }
    public double getSaldo() { return saldo; }
    public Banco getBanco() { return banco; }
    public Empleado getTitular() { return titular; }
    public List<Movimiento> getMovimientos() { return movimientos; }

    public void setBanco(Banco banco) { this.banco = banco; }
    public void setTitular(Empleado titular) { this.titular = titular; }

    public void setNumeroCuenta(String numeroCuenta) {
        if (ValidRegex.esNumeroCuentaValido(numeroCuenta)) {
            this.numeroCuenta = numeroCuenta;
        }
    }

    public void setTipoCuenta(String tipoCuenta) {
        for (String tipo : TIPOS_CUENTA) {
            if (tipo.equalsIgnoreCase(tipoCuenta)) {
                this.tipoCuenta = tipo;
            }
        }
    }

    public boolean haySaldoSuficiente(double monto) {
        return monto > 0 && Dinero.redondear(monto) <= saldo;
    }

    @Override
    public boolean validarMonto(double monto) {
        return monto > 0 && monto <= Consola.MONTO_MAXIMO
                && !Double.isNaN(monto) && !Double.isInfinite(monto);
    }

    @Override
    public void depositar(double monto, String descripcion) throws OperacionInvalidaException {
        if (!validarMonto(monto)) {
            throw new OperacionInvalidaException("Monto invalido para deposito.");
        }
        saldo = Dinero.redondear(saldo + monto);
        registrarMovimiento(Movimiento.DEPOSITO, monto, descripcion);
    }

    @Override
    public void retirar(double monto, String descripcion) throws OperacionInvalidaException {
        if (!validarMonto(monto)) {
            throw new OperacionInvalidaException("Monto invalido para retiro.");
        }
        if (!haySaldoSuficiente(monto)) {
            throw new OperacionInvalidaException("Saldo insuficiente en la cuenta " + numeroCuenta
                    + ". Saldo disponible: " + Dinero.formatear(saldo));
        }
        saldo = Dinero.redondear(saldo - monto);
        registrarMovimiento(Movimiento.RETIRO, monto, descripcion);
    }

    /** Cada cuenta numera sus propios movimientos: M0001, M0002, ... */
    private void registrarMovimiento(String tipo, double monto, String descripcion) {
        Movimiento movimiento = new Movimiento();
        String id = String.format("M%04d", movimientos.size() + 1);
        movimiento.completar(id, descripcion, monto, tipo, saldo);
        movimientos.add(movimiento);
    }

    /** Arma el historial de movimientos en un solo texto (patron mensaje +=). */
    public String historial() {
        String mensaje = "Movimientos de la cuenta " + numeroCuenta + " (" + titularTexto() + ")";
        if (movimientos.isEmpty()) {
            mensaje += "\nNo hay movimientos en esta cuenta.";
        }
        for (int i = 0; i < movimientos.size(); i++) {
            mensaje += "\n" + movimientos.get(i).resumen();
        }
        mensaje += "\nSaldo actual: " + Dinero.formatear(saldo);
        return mensaje;
    }

    private String titularTexto() {
        return (titular != null) ? titular.getNombreCompleto() : "Sin titular";
    }

    @Override
    public boolean validar() {
        return ValidRegex.esNumeroCuentaValido(numeroCuenta) && banco != null && titular != null;
    }

    @Override
    public String resumen() {
        String nombreBanco = (banco != null) ? banco.getNombreBanco() : "Sin banco";
        String mensaje = "Datos de la cuenta bancaria";
        mensaje += "\nNumero: " + numeroCuenta;
        mensaje += "\nTipo: " + tipoCuenta;
        mensaje += "\nBanco: " + nombreBanco;
        mensaje += "\nTitular: " + titularTexto();
        mensaje += "\nSaldo: " + Dinero.formatear(saldo);
        return mensaje;
    }

    @Override
    public String toString() {
        String nombreBanco = (banco != null) ? banco.getNombreBanco() : "Sin banco";
        return "Cuenta " + numeroCuenta + " (" + tipoCuenta + ") | " + nombreBanco
                + " | " + titularTexto() + " | Saldo: " + Dinero.formatear(saldo);
    }
}
