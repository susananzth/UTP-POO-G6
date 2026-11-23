package walletpro.interfaces;

import walletpro.modelo.Movimiento;

/**
 * INTERFAZ para lo que mueve dinero. La implementa: CuentaBancaria.
 * Cada operacion devuelve el Movimiento que se genero, para guardarlo
 * despues en la base de datos con CuentaDAO.guardarMovimiento().
 */
public interface OperacionFinanciera {

    Movimiento depositar(double monto, String descripcion);

    // Devuelve null si no hay saldo suficiente.
    Movimiento retirar(double monto, String descripcion);
}
