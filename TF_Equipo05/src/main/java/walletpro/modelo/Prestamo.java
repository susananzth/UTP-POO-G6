package walletpro.modelo;

import walletpro.interfaces.IConsultable;
import walletpro.util.Consola;
import walletpro.util.Dinero;

/**
 * Prestamo que un banco le da a un empleado, pagado en cuotas.
 * Tabla: prestamo | DAO: PrestamoDAO | Menu: MenuBancos (Alumno 3)
 *
 * Interes simple:
 *   interes total = monto x (tasa / 100) x cuotas
 *   total a pagar = monto + interes total
 *   cuota         = total a pagar / cuotas
 * Ej. S/ 1000 al 2% mensual en 6 cuotas: interes 120, total 1120, cuota 186.67
 */
public class Prestamo implements IConsultable {

    private int idPrestamo;
    private Empleado deudor;
    private Banco banco;
    private TipoPrestamo tipo;
    private double monto;
    private double tasaMensual;      // en %, ej. 2.5
    private int numeroCuotas;
    private int cuotasPagadas;
    private double saldoPendiente;   // lo que falta pagar (empieza en el total a pagar)

    // El deudor, el banco y el tipo se eligen antes en el menu (setters).
    // Lo llama: MenuBancos.registrarPrestamo()
    public void registrar() {
        monto = Consola.leerMonto("Monto prestado (S/):");
        tasaMensual = Consola.leerDecimal("Tasa de interes mensual en % (0 a 20):", 0, 20);
        numeroCuotas = Consola.leerEntero("Numero de cuotas (1 a 120):", 1, 120);
        cuotasPagadas = 0;
        saldoPendiente = calcularTotalAPagar();
    }

    public double calcularInteresTotal() {
        return Dinero.redondear(monto * (tasaMensual / 100) * numeroCuotas);
    }

    public double calcularTotalAPagar() {
        return Dinero.redondear(monto + calcularInteresTotal());
    }

    public double calcularMontoCuota() {
        return Dinero.redondear(calcularTotalAPagar() / numeroCuotas);
    }

    // La ultima cuota paga exactamente lo que falta (para que no sobren centimos).
    public double calcularSiguienteCuota() {
        if (cuotasPagadas == numeroCuotas - 1) {
            return saldoPendiente;
        }
        return Math.min(calcularMontoCuota(), saldoPendiente);
    }

    // Descuenta una cuota pagada. Lo llama: MenuBancos.pagarCuota()
    public void registrarPago(double cuota) {
        saldoPendiente = Dinero.redondear(saldoPendiente - cuota);
        cuotasPagadas++;
    }

    public boolean estaPagado() {
        return saldoPendiente <= 0;
    }

    @Override
    public String resumen() {
        String estado = estaPagado() ? "PAGADO" : "PENDIENTE";
        String mensaje = "Prestamo #" + idPrestamo + " | " + tipo.getNombre() + " | " + banco.getNombre()
                + " | " + deudor.getNombreCompleto();
        mensaje += "\n   Prestado: " + Dinero.formatear(monto) + " | Interes: " + tasaMensual
                + "% mensual = " + Dinero.formatear(calcularInteresTotal());
        mensaje += "\n   Total a pagar: " + Dinero.formatear(calcularTotalAPagar())
                + " | Cuota: " + Dinero.formatear(calcularMontoCuota());
        mensaje += "\n   Cuotas pagadas: " + cuotasPagadas + "/" + numeroCuotas
                + " | Falta pagar: " + Dinero.formatear(saldoPendiente) + " | " + estado;
        return mensaje;
    }

    @Override
    public String toString() {
        return "Prestamo #" + idPrestamo + " | " + deudor.getNombreCompleto() + " | Cuotas "
                + cuotasPagadas + "/" + numeroCuotas + " | Falta: " + Dinero.formatear(saldoPendiente);
    }

    public int getIdPrestamo() { return idPrestamo; }
    public Empleado getDeudor() { return deudor; }
    public Banco getBanco() { return banco; }
    public TipoPrestamo getTipo() { return tipo; }
    public double getMonto() { return monto; }
    public double getTasaMensual() { return tasaMensual; }
    public int getNumeroCuotas() { return numeroCuotas; }
    public int getCuotasPagadas() { return cuotasPagadas; }
    public double getSaldoPendiente() { return saldoPendiente; }

    public void setIdPrestamo(int idPrestamo) { this.idPrestamo = idPrestamo; }
    public void setDeudor(Empleado deudor) { this.deudor = deudor; }
    public void setBanco(Banco banco) { this.banco = banco; }
    public void setTipo(TipoPrestamo tipo) { this.tipo = tipo; }
    public void setMonto(double monto) { this.monto = monto; }
    public void setTasaMensual(double tasaMensual) { this.tasaMensual = tasaMensual; }
    public void setNumeroCuotas(int numeroCuotas) { this.numeroCuotas = numeroCuotas; }
    public void setCuotasPagadas(int cuotasPagadas) { this.cuotasPagadas = cuotasPagadas; }
    public void setSaldoPendiente(double saldoPendiente) { this.saldoPendiente = saldoPendiente; }
}
