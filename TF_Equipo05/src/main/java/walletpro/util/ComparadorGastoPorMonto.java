package walletpro.util;

import java.util.Comparator;
import walletpro.modelo.Gasto;

/**
 * COMPARATOR (semana 12): criterio para ordenar gastos del MAYOR al MENOR monto.
 * Mismo estilo que ComparadorCuota del profesor.
 *
 * Lo usa: MenuGastos.listarGastosDelMes() -> gastos.sort(new ComparadorGastoPorMonto());
 */
public class ComparadorGastoPorMonto implements Comparator<Gasto> {

    // Devuelve negativo si g1 va antes, 0 si son iguales, positivo si g1 va despues.
    // Se compara g2 con g1 (al reves) para que el mayor quede primero.
    @Override
    public int compare(Gasto g1, Gasto g2) {
        return Double.compare(g2.getMonto(), g1.getMonto());
    }
}
