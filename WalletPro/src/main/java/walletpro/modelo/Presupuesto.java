package walletpro.modelo;

import java.io.Serializable;
import walletpro.interfaces.*;
import walletpro.util.*;

/**
 * ALUMNO 2 - Registrar Presupuesto: define un limite de gasto por
 * categoria y por mes para un empleado. CategoriaGasto y Empleado se
 * asignan por setter tras la seleccion en el menu (agregacion).
 */
public class Presupuesto implements IValidable, IConsultable, Serializable {

    private int mes;
    private int anio;
    private double montoPresupuestado;
    private CategoriaGasto categoria;
    private Empleado empleado;

    public Presupuesto() {
        this.mes = 1;
        this.anio = 2026;
        this.montoPresupuestado = 0;
        this.categoria = null;
        this.empleado = null;
    }

    public void registrar() {
        this.mes = Consola.leerEntero("Mes (1-12):", 1, 12);
        this.anio = Consola.leerEntero("Anio (2000-2100):", 2000, 2100);
        this.montoPresupuestado = Consola.leerMonto("Monto maximo a gastar en el mes (S/):");
    }

    public int getMes() { return mes; }
    public int getAnio() { return anio; }
    public double getMontoPresupuestado() { return montoPresupuestado; }
    public CategoriaGasto getCategoria() { return categoria; }
    public Empleado getEmpleado() { return empleado; }

    public void setCategoria(CategoriaGasto categoria) { this.categoria = categoria; }
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

    public void setMontoPresupuestado(double montoPresupuestado) {
        if (montoPresupuestado > 0) {
            this.montoPresupuestado = Dinero.redondear(montoPresupuestado);
        }
    }

    /** A partir de este porcentaje de uso se avisa al usuario (80%). */
    public static final double PORCENTAJE_ALERTA = 80;

    /** Lo que aun se puede gastar. Si es negativo, el presupuesto se excedio. */
    public double calcularDisponible(double gastado) {
        return Dinero.redondear(montoPresupuestado - gastado);
    }

    public double calcularPorcentajeUsado(double gastado) {
        return gastado * 100 / montoPresupuestado;
    }

    /** Estado del presupuesto segun lo gastado en el mes. */
    public String calcularEstado(double gastado) {
        if (gastado > montoPresupuestado) {
            return "EXCEDIDO";
        }
        if (calcularPorcentajeUsado(gastado) >= PORCENTAJE_ALERTA) {
            return "ALERTA";
        }
        return "OK";
    }

    public String getPeriodo() {
        return String.format("%02d/%d", mes, anio);
    }

    @Override
    public boolean validar() {
        return montoPresupuestado > 0 && mes >= 1 && mes <= 12
                && categoria != null && empleado != null;
    }

    @Override
    public String resumen() {
        String nombreCategoria = (categoria != null) ? categoria.getNombreCategoria() : "Sin categoria";
        String nombreEmpleado = (empleado != null) ? empleado.getNombreCompleto() : "Sin empleado";
        return getPeriodo() + " | " + nombreCategoria + " | " + nombreEmpleado
                + " | Limite: " + Dinero.formatear(montoPresupuestado);
    }

    @Override
    public String toString() {
        return resumen();
    }
}
