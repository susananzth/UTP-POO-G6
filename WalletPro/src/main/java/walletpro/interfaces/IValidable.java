package walletpro.interfaces;

/**
 * Contrato que deben cumplir todas las clases que registran datos
 * desde el teclado: toda entidad relevante del sistema debe poder
 * validarse a si misma antes de ser guardada en su arreglo.
 */
public interface IValidable {
    boolean validar();
}
