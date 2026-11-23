package walletpro.modelo;

import walletpro.interfaces.IConsultable;
import walletpro.util.Consola;
import walletpro.util.Dinero;

/**
 * Limite de gasto de un empleado, para una categoria, en un mes.
 * Tabla: presupuesto | DAO: PresupuestoDAO | Menu: MenuGastos (Alumno 2)
 *
 * Lo "gastado" no se guarda aqui: el menu lo calcula sumando los gastos
 * del mes (MenuGastos.calcularGastado) y lo pasa a estos metodos.
 */
public class Presupuesto implements IConsultable {

    // Desde este porcentaje de uso se muestra una ALERTA
    public static final double PORCENTAJE_ALERTA = 80;

    private int idPresupuesto;
    private Empleado empleado;
    private CategoriaGasto categoria;
    private int mes;
    private int anio;
    private double montoLimite;

    // El empleado y la categoria se eligen antes en el menu (setters).
    // Lo llama: MenuGastos.registrarPresupuesto()
    public void registrar() {
        mes = Consola.leerEntero("Mes (1-12):", 1, 12);
        anio = Consola.leerEntero("Anio (2000-2100):", 2000, 2100);
        montoLimite = Consola.leerMonto("Monto maximo a gastar en el mes (S/):");
    }

    // Lo que aun se puede gastar. Si es negativo, se excedio.
    public double calcularDisponible(double gastado) {
        return Dinero.redondear(montoLimite - gastado);
    }

    public double calcularPorcentajeUsado(double gastado) {
        return gastado * 100 / montoLimite;
    }

    // OK (menos de 80%), ALERTA (80% o mas) o EXCEDIDO (se paso del limite)
    public String calcularEstado(double gastado) {
        if (gastado > montoLimite) {
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
    public String resumen() {
        return getPeriodo() + " | " + categoria.getNombre() + " | " + empleado.getNombreCompleto()
                + " | Limite: " + Dinero.formatear(montoLimite);
    }

    @Override
    public String toString() {
        return resumen();
    }

    public int getIdPresupuesto() { return idPresupuesto; }
    public Empleado getEmpleado() { return empleado; }
    public CategoriaGasto getCategoria() { return categoria; }
    public int getMes() { return mes; }
    public int getAnio() { return anio; }
    public double getMontoLimite() { return montoLimite; }

    public void setIdPresupuesto(int idPresupuesto) { this.idPresupuesto = idPresupuesto; }
    public void setEmpleado(Empleado empleado) { this.empleado = empleado; }
    public void setCategoria(CategoriaGasto categoria) { this.categoria = categoria; }
    public void setMes(int mes) { this.mes = mes; }
    public void setAnio(int anio) { this.anio = anio; }
    public void setMontoLimite(double montoLimite) { this.montoLimite = montoLimite; }
}
