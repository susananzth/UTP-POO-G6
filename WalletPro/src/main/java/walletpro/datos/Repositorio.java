package walletpro.datos;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Clase GENERICA que guarda una lista de objetos de cualquier tipo T.
 *
 * En vez de tener 13 arreglos con su contador (Empleado[] + totalEmpleados,
 * Gasto[] + totalGastos, ...), usamos una sola clase que sirve para todos:
 *   Repositorio<Empleado> empleados = new Repositorio<>();
 *   Repositorio<Gasto> gastos = new Repositorio<>();
 *
 * Por dentro usa un ArrayList (coleccion), que crece solo: ya no hay
 * limite fijo como new Gasto[500].
 *
 * Los metodos buscar/filtrar/existe reciben una condicion escrita con
 * una expresion lambda, por ejemplo:
 *   empleados.buscar(e -> e.getDni().equals("12345678"))
 * Predicate<T> es una interfaz de Java que recibe un T y devuelve true/false.
 */
public class Repositorio<T> {

    private List<T> elementos;

    public Repositorio() {
        this.elementos = new ArrayList<>();
    }

    public void agregar(T elemento) {
        elementos.add(elemento);
    }

    /** Devuelve el elemento de la posicion indicada, contando desde 1 (como se muestra en los menus). */
    public T obtener(int posicion) {
        return elementos.get(posicion - 1);
    }

    public int tamanio() {
        return elementos.size();
    }

    public boolean estaVacio() {
        return elementos.isEmpty();
    }

    /** Copia de la lista: quien la recibe no puede modificar la original. */
    public List<T> listar() {
        return new ArrayList<>(elementos);
    }

    /** Devuelve el primer elemento que cumple la condicion, o null si no hay ninguno. */
    public T buscar(Predicate<T> condicion) {
        return elementos.stream()
                .filter(condicion)
                .findFirst()
                .orElse(null);
    }

    /** Devuelve todos los elementos que cumplen la condicion. */
    public List<T> filtrar(Predicate<T> condicion) {
        return elementos.stream()
                .filter(condicion)
                .collect(Collectors.toList());
    }

    /** Indica si al menos un elemento cumple la condicion (sirve para evitar duplicados). */
    public boolean existe(Predicate<T> condicion) {
        return elementos.stream().anyMatch(condicion);
    }
}
