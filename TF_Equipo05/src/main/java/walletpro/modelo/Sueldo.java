package walletpro.modelo;

import walletpro.interfaces.IConsultable;
import walletpro.util.Consola;
import walletpro.util.Dinero;

/**
 * Sueldo que cobro un empleado en un mes.
 * Tabla: sueldo | DAO: SueldoDAO | Menu: MenuIngresos (Alumno 1)
 */
public class Sueldo implements IConsultable {

    private int idSueldo;
    private Empleado empleado;
    private int mes;
    private int anio;
    private double monto;

    // El empleado se elige antes en el menu (setEmpleado).
    // Lo llama: MenuIngresos.registrarSueldo()
    public void registrar() {
        mes = Consola.leerEntero("Mes (1-12):", 1, 12);
        anio = Consola.leerEntero("Anio (2000-2100):", 2000, 2100);
        monto = Consola.leerMonto("Monto del sueldo (S/):");
    }

    // Ej. "09/2026"
    public String getPeriodo() {
        return String.format("%02d/%d", mes, anio);
    }

    @Override
    public String resumen() {
        return getPeriodo() + " | " + Dinero.formatear(monto);
    }

    public int getIdSueldo() { return idSueldo; }
    public Empleado getEmpleado() { return empleado; }
    public int getMes() { return mes; }
    public int getAnio() { return anio; }
    public double getMonto() { return monto; }

    public void setIdSueldo(int idSueldo) { this.idSueldo = idSueldo; }
    public void setEmpleado(Empleado empleado) { this.empleado = empleado; }
    public void setMes(int mes) { this.mes = mes; }
    public void setAnio(int anio) { this.anio = anio; }
    public void setMonto(double monto) { this.monto = monto; }
}
