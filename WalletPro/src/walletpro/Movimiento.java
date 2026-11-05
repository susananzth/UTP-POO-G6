package walletpro;

import java.util.Scanner;

/**
 * Cada deposito, retiro o pago dentro de una CuentaBancaria. Hereda
 * de Transaccion; CuentaBancaria es duena de sus movimientos
 * (composicion): un movimiento no existe fuera de la cuenta donde
 * ocurrio. Se crea internamente (no por el usuario directamente),
 * por eso conserva un constructor de apoyo ademas del no-arg.
 */
public class Movimiento extends Transaccion {

    private String tipoMovimiento;

    public Movimiento() {
        super();
        this.tipoMovimiento = "DEPOSITO";
    }

    public void registrar(Scanner teclado, String idTransaccion, String tipoMovimiento) {
        registrarDatosTransaccion(teclado, idTransaccion);
        setTipoMovimiento(tipoMovimiento);
    }

    /** Usado internamente por CuentaBancaria/Prestamo al registrar un movimiento ya validado. */
    public void completar(String idTransaccion, String fecha, String descripcion,
                           double monto, String tipoMovimiento) {
        this.idTransaccion = ValidRegex.esTextoValido(idTransaccion) ? idTransaccion : "T000";
        this.fecha = ValidFecha.validar(fecha) ? fecha : "01-01-1900";
        this.monto = (monto > 0) ? monto : 0;
        this.descripcion = ValidRegex.esTextoValido(descripcion) ? descripcion : "Sin descripcion";
        setTipoMovimiento(tipoMovimiento);
    }

    private void setTipoMovimiento(String tipoMovimiento) {
        if (tipoMovimiento != null && tipoValido(tipoMovimiento)) {
            this.tipoMovimiento = tipoMovimiento.toUpperCase();
        } else {
            this.tipoMovimiento = "DEPOSITO";
        }
    }

    private boolean tipoValido(String tipo) {
        String t = tipo.trim().toUpperCase();
        return t.equals("DEPOSITO") || t.equals("RETIRO")
                || t.equals("PAGO") || t.equals("COBRO");
    }

    public String getTipoMovimiento() { return tipoMovimiento; }

    public boolean esIngreso() {
        return tipoMovimiento.equals("DEPOSITO") || tipoMovimiento.equals("COBRO");
    }

    @Override
    public String getTipo() {
        return tipoMovimiento;
    }
}
