package walletpro.modelo;

import walletpro.util.Consola;

/**
 * Puesto de trabajo (Desarrollador, Soporte...).
 * Tabla: cargo | DAO: CargoDAO | Menu: MenuIngresos (Alumno 1)
 */
public class Cargo {

    private int idCargo;
    private String nombre = "";
    private String descripcion = "";

    // Lo llama: MenuIngresos.registrarCargo()
    public void registrar() {
        nombre = Consola.leerTexto("Nombre del cargo:");
        descripcion = Consola.leerTextoOpcional("Descripcion (Enter para omitir):", "Sin descripcion");
    }

    public int getIdCargo() { return idCargo; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }

    public void setIdCargo(int idCargo) { this.idCargo = idCargo; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    @Override
    public String toString() {
        return nombre;
    }
}
