package walletpro.interfaces;

import java.util.ArrayList;

/**
 * INTERFAZ GENERICA (semana 13): lo minimo que debe hacer un DAO
 * (clase que habla con una tabla de la base de datos).
 * La T se reemplaza por la clase de la tabla: IDAO<Empresa>, IDAO<Banco>...
 *
 * La implementan: EmpresaDAO, CargoDAO, EmpleadoDAO, TrabajoDAO,
 * CategoriaDAO, PresupuestoDAO, BancoDAO, CuentaDAO, TipoPrestamoDAO, PrestamoDAO.
 */
public interface IDAO<T> {

    // INSERT: guarda el objeto. Devuelve true si se guardo.
    boolean insertar(T objeto);

    // SELECT: devuelve todos los registros de la tabla.
    ArrayList<T> listar();
}
