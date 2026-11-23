package walletpro.modelo;

import walletpro.util.Consola;

/**
 * Clase de prestamo (Personal, Hipotecario, Vehicular...).
 * Tabla: tipo_prestamo | DAO: TipoPrestamoDAO | Menu: MenuBancos (Alumno 3)
 */
public class TipoPrestamo {

    private int idTipo;
    private String nombre = "";
    private String descripcion = "";

    // Lo llama: MenuBancos.registrarTipoPrestamo()
    public void registrar() {
        nombre = Consola.leerTexto("Nombre del tipo de prestamo:");
        descripcion = Consola.leerTextoOpcional("Descripcion (Enter para omitir):", "Sin descripcion");
    }

    public int getIdTipo() { return idTipo; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }

    public void setIdTipo(int idTipo) { this.idTipo = idTipo; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    @Override
    public String toString() {
        return nombre;
    }
}
