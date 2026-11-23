package walletpro.modelo;

import walletpro.interfaces.IConsultable;
import walletpro.util.Consola;

/**
 * Une a un Empleado con una Empresa y un Cargo (ASOCIACION: guarda
 * referencias a objetos que ya existen).
 * Tabla: trabajo | DAO: TrabajoDAO | Menu: MenuIngresos (Alumno 1)
 */
public class Trabajo implements IConsultable {

    private static final String[] TIPOS_CONTRATO = {"Planilla", "Independiente", "Contrato", "Practicas"};

    private int idTrabajo;
    private Empleado empleado;
    private Empresa empresa;
    private Cargo cargo;
    private String fechaIngreso = "";
    private String tipoContrato = "";

    // El empleado, la empresa y el cargo se eligen antes en el menu (setters).
    // Lo llama: MenuIngresos.registrarTrabajo()
    public void registrar() {
        fechaIngreso = Consola.leerFechaPasada("Fecha de ingreso (dd-mm-aaaa):");

        String mensaje = "Tipo de contrato:";
        for (int i = 0; i < TIPOS_CONTRATO.length; i++) {
            mensaje += "\n" + (i + 1) + ". " + TIPOS_CONTRATO[i];
        }
        System.out.println(mensaje);
        int opcion = Consola.leerEntero("Seleccione una opcion:", 1, TIPOS_CONTRATO.length);
        tipoContrato = TIPOS_CONTRATO[opcion - 1];
    }

    @Override
    public String resumen() {
        return empleado.getNombreCompleto() + " | " + cargo.getNombre() + " en " + empresa.getRazonSocial()
                + " | " + tipoContrato + " desde " + fechaIngreso;
    }

    public int getIdTrabajo() { return idTrabajo; }
    public Empleado getEmpleado() { return empleado; }
    public Empresa getEmpresa() { return empresa; }
    public Cargo getCargo() { return cargo; }
    public String getFechaIngreso() { return fechaIngreso; }
    public String getTipoContrato() { return tipoContrato; }

    public void setIdTrabajo(int idTrabajo) { this.idTrabajo = idTrabajo; }
    public void setEmpleado(Empleado empleado) { this.empleado = empleado; }
    public void setEmpresa(Empresa empresa) { this.empresa = empresa; }
    public void setCargo(Cargo cargo) { this.cargo = cargo; }
    public void setFechaIngreso(String fechaIngreso) { this.fechaIngreso = fechaIngreso; }
    public void setTipoContrato(String tipoContrato) { this.tipoContrato = tipoContrato; }
}
