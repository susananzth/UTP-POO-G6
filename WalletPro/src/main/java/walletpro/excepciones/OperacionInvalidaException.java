package walletpro.excepciones;

/**
 * Excepcion propia de WalletPro. Se lanza cuando una operacion rompe
 * una regla del negocio: saldo insuficiente, un dato repetido (RUC,
 * DNI, numero de cuenta...), un pago mayor a la deuda, etc.
 *
 * Es "checked" (extiende de Exception): Java obliga a quien llama al
 * metodo a atraparla con try-catch, asi ningun error se pasa por alto.
 * Los menus la atrapan y muestran e.getMessage() al usuario.
 */
public class OperacionInvalidaException extends Exception {

    public OperacionInvalidaException(String mensaje) {
        super(mensaje);
    }
}
