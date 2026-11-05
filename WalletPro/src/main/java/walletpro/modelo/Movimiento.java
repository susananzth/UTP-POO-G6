package walletpro.modelo;

import walletpro.interfaces.*;
import walletpro.util.*;

/**
 * Cada deposito, retiro o pago dentro de una CuentaBancaria. Hereda
 * de Transaccion; CuentaBancaria es duena de sus movimientos
 * (composicion): un movimiento no existe fuera de la cuenta donde
 * ocurrio. Lo crea siempre la cuenta, nunca el usuario directamente.
 */
public class Movimiento extends Transaccion {

    public static final String DEPOSITO = "DEPOSITO";
    public static final String RETIRO = "RETIRO";

    private String tipoMovimiento;
    private double saldoResultante;

    public Movimiento() {
        super();
        this.tipoMovimiento = DEPOSITO;
        this.saldoResultante = 0;
    }

    /** Usado por CuentaBancaria al registrar un movimiento ya validado. */
    public void completar(String idTransaccion, String descripcion, double monto,
                          String tipoMovimiento, double saldoResultante) {
        fijarDatosTransaccion(idTransaccion, ValidFecha.hoy(), monto, descripcion);
        this.tipoMovimiento = RETIRO.equals(tipoMovimiento) ? RETIRO : DEPOSITO;
        this.saldoResultante = saldoResultante;
    }

    public String getTipoMovimiento() { return tipoMovimiento; }
    public double getSaldoResultante() { return saldoResultante; }

    public boolean esIngreso() {
        return tipoMovimiento.equals(DEPOSITO);
    }

    @Override
    public String getTipo() {
        return tipoMovimiento;
    }

    @Override
    public String resumen() {
        String signo = esIngreso() ? "+" : "-";
        return String.format("%-6s | %s | %-8s | %14s | Saldo: %13s | %s",
                idTransaccion, fecha, tipoMovimiento, signo + Dinero.formatear(monto),
                Dinero.formatear(saldoResultante), descripcion);
    }
}
