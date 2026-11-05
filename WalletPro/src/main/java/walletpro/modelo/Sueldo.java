package walletpro.modelo;

import walletpro.interfaces.*;
import walletpro.util.*;

/**
 * ALUMNO 1 - Registrar Sueldo Mensual. El Empleado se asigna por
 * setter tras la seleccion en el menu (agregacion: Sueldo no es
 * dueno del Empleado).
 */
public class Sueldo implements IValidable, IConsultable {

    private int mes;
    private int anio;
    private double monto;
    private Empleado empleado;

    public Sueldo() {
        this.mes = 1;
        this.anio = 2026;
        this.monto = 0;
        this.empleado = null;
    }

    public void registrar() {
        this.mes = Consola.leerEntero("Mes (1-12):", 1, 12);
        this.anio = Consola.leerEntero("Anio (2000-2100):", 2000, 2100);
        this.monto = Consola.leerMonto("Monto del sueldo (S/):");
    }

    public int getMes() { return mes; }
    public int getAnio() { return anio; }
    public double getMonto() { return monto; }
    public Empleado getEmpleado() { return empleado; }

    public void setEmpleado(Empleado empleado) { this.empleado = empleado; }

    public void setMes(int mes) {
        if (mes >= 1 && mes <= 12) {
            this.mes = mes;
        }
    }

    public void setAnio(int anio) {
        if (anio >= 2000 && anio <= 2100) {
            this.anio = anio;
        }
    }

    public void setMonto(double monto) {
        if (monto > 0) {
            this.monto = Dinero.redondear(monto);
        }
    }

    /** Periodo en formato mm/aaaa, ej. 09/2026 */
    public String getPeriodo() {
        return String.format("%02d/%d", mes, anio);
    }

    @Override
    public boolean validar() {
        return monto > 0 && mes >= 1 && mes <= 12 && empleado != null;
    }

    @Override
    public String resumen() {
        String nombreEmpleado = (empleado != null) ? empleado.getNombreCompleto() : "Sin empleado";
        return String.format("%s | %-25s | %13s", getPeriodo(), nombreEmpleado, Dinero.formatear(monto));
    }
}
