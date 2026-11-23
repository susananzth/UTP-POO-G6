package walletpro.util;

import java.util.ArrayList;

/**
 * CLASE GENERICA (semana 13): divide una lista de CUALQUIER tipo en paginas.
 * La T se reemplaza por el tipo real al usarla: Paginador<Movimiento>.
 *
 * Lo usa: MenuBancos.verMovimientos() para mostrar los movimientos de 5 en 5.
 */
public class Paginador<T> {

    private ArrayList<T> elementos = new ArrayList<>();
    private int tamanioPagina = 5;

    public void setElementos(ArrayList<T> elementos) {
        this.elementos = elementos;
    }

    public void setTamanioPagina(int tamanioPagina) {
        if (tamanioPagina > 0) {
            this.tamanioPagina = tamanioPagina;
        }
    }

    // Ej. 12 elementos de 5 en 5 = 3 paginas (5 + 5 + 2).
    public int getTotalPaginas() {
        if (elementos.isEmpty()) {
            return 1;
        }
        return (elementos.size() + tamanioPagina - 1) / tamanioPagina;
    }

    // Devuelve los elementos de la pagina indicada (la primera es la 1).
    public ArrayList<T> getPagina(int numero) {
        ArrayList<T> pagina = new ArrayList<>();
        int inicio = (numero - 1) * tamanioPagina;
        int fin = Math.min(inicio + tamanioPagina, elementos.size());
        for (int i = inicio; i < fin; i++) {
            pagina.add(elementos.get(i));
        }
        return pagina;
    }
}
