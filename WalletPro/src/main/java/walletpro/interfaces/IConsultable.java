package walletpro.interfaces;

/**
 * INTERFAZ: toda clase que la implementa sabe describirse en texto.
 * Cada clase escribe su propio resumen() (polimorfismo).
 *
 * La implementan: Persona (Usuario, Empleado), Transaccion (Gasto, Movimiento),
 * Trabajo, Sueldo, Presupuesto, CuentaBancaria y Prestamo.
 * La usa: Consola.mostrarLista(), que llama a resumen() de cada elemento.
 */
public interface IConsultable {

    String resumen();
}
