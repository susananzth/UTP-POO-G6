package walletpro.modelo;

import walletpro.util.Consola;

/**
 * Grupo de gastos (Alimentacion, Transporte...). Los presupuestos son por categoria.
 * Tabla: categoria | DAO: CategoriaDAO | Menu: MenuGastos (Alumno 2)
 */
public class CategoriaGasto {

    private int idCategoria;
    private String nombre = "";
    private String descripcion = "";

    // Lo llama: MenuGastos.registrarCategoria()
    public void registrar() {
        nombre = Consola.leerTexto("Nombre de la categoria:");
        descripcion = Consola.leerTextoOpcional("Descripcion (Enter para omitir):", "Sin descripcion");
    }

    public int getIdCategoria() { return idCategoria; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }

    public void setIdCategoria(int idCategoria) { this.idCategoria = idCategoria; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    @Override
    public String toString() {
        return nombre;
    }
}
