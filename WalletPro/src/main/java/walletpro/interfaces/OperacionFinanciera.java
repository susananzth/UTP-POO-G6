package walletpro.interfaces;

/**
 * Contrato para las clases que ejecutan movimientos de dinero en
 * tiempo real (hoy, solo CuentaBancaria).
 */
public interface OperacionFinanciera {
    boolean ejecutar(double monto);
    boolean validarMonto(double monto);
}
