package walletpro.interfaces;

import walletpro.excepciones.OperacionInvalidaException;

/**
 * Contrato para las clases que mueven dinero (hoy, CuentaBancaria).
 * Toda entrada o salida de dinero debe pasar por depositar() o
 * retirar(), que dejan registro en el historial: no existe otra forma
 * de cambiar un saldo "por debajo".
 */
public interface OperacionFinanciera {

    void depositar(double monto, String descripcion) throws OperacionInvalidaException;

    void retirar(double monto, String descripcion) throws OperacionInvalidaException;

    boolean validarMonto(double monto);
}
