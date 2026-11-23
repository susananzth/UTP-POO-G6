package walletpro.modelo;

import walletpro.util.Dinero;

/**
 * Deposito o retiro en una cuenta. HEREDA de Transaccion.
 * No se pide por teclado: lo crea CuentaBancaria en depositar() o retirar().
 * Tabla: movimiento | Se guarda con: CuentaDAO.guardarMovimiento()
 */
public class Movimiento extends Transaccion {

    public static final String DEPOSITO = "DEPOSITO";
    public static final String RETIRO = "RETIRO";

    private String numeroCuenta = "";
    private String tipo = DEPOSITO;
    private double saldoResultante;   // como quedo el saldo despues del movimiento

    // POLIMORFISMO: version de Movimiento del metodo abstracto de Transaccion
    @Override
    public String getTipo() {
        return tipo;
    }

    // SOBRESCRITURA: muestra + o - y el saldo que quedo.
    @Override
    public String resumen() {
        String signo = tipo.equals(DEPOSITO) ? "+" : "-";
        return fecha + " | " + tipo + " | " + signo + Dinero.formatear(monto)
                + " | Saldo: " + Dinero.formatear(saldoResultante) + " | " + descripcion;
    }

    public String getNumeroCuenta() { return numeroCuenta; }
    public double getSaldoResultante() { return saldoResultante; }

    public void setNumeroCuenta(String numeroCuenta) { this.numeroCuenta = numeroCuenta; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public void setSaldoResultante(double saldoResultante) { this.saldoResultante = saldoResultante; }
}
