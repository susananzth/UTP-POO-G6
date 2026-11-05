package walletpro;

/**
 * Contrato comun para mostrar el detalle de una entidad de forma
 * homogenea en toda la aplicacion, siempre armando el texto con el
 * patron mensaje += "..." antes de un unico System.out.println.
 */
public interface IConsultable {
    String resumen();
}
