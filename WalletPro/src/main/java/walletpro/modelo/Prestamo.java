package walletpro.modelo;

import java.io.Serializable;
import walletpro.excepciones.OperacionInvalidaException;
import walletpro.interfaces.*;
import walletpro.util.*;

/**
 * ALUMNO 3 - Registrar Prestamo y pagar sus cuotas.
 *
 * Calculo (interes simple, el que se usa en el curso):
 *   interes total  = monto * (tasa mensual / 100) * numero de cuotas
 *   total a pagar  = monto + interes total
 *   cuota mensual  = total a pagar / numero de cuotas
 * Ejemplo: S/ 1,000 al 10% mensual en 2 cuotas -> interes S/ 200,
 * total S/ 1,200, cuota S/ 600.
 *
 * La deuda (saldoPendiente) arranca en el TOTAL A PAGAR, no en el
 * monto prestado: asi el interes si se cobra.
 */
public class Prestamo implements IValidable, IConsultable, Serializable {

    private String codigoPrestamo;
    private double montoOriginal;
    private double tasaInteresMensual;
    private int numeroCuotas;
    private int cuotasPagadas;
    private double saldoPendiente;
    private TipoPrestamo tipoPrestamo;
    private Banco banco;
    private Empleado deudor;

    public Prestamo() {
        this.codigoPrestamo = "";
        this.montoOriginal = 0;
        this.tasaInteresMensual = 0;
        this.numeroCuotas = 1;
        this.cuotasPagadas = 0;
        this.saldoPendiente = 0;
        this.tipoPrestamo = null;
        this.banco = null;
        this.deudor = null;
    }

    public void registrar(String codigoPrestamo) {
        this.codigoPrestamo = codigoPrestamo;
        this.montoOriginal = Consola.leerMonto("Monto prestado (S/):");
        this.tasaInteresMensual = Consola.leerDecimal("Tasa de interes mensual en % (0 a 20):", 0, 20);
        this.numeroCuotas = Consola.leerEntero("Numero de cuotas mensuales (1 a 120):", 1, 120);
        this.cuotasPagadas = 0;
        this.saldoPendiente = calcularTotalAPagar();
    }

    /** Para precargar datos de prueba sin pasar por el teclado. */
    public void fijarDatos(String codigoPrestamo, double montoOriginal,
                           double tasaInteresMensual, int numeroCuotas) {
        this.codigoPrestamo = codigoPrestamo;
        this.montoOriginal = Dinero.redondear(montoOriginal);
        this.tasaInteresMensual = tasaInteresMensual;
        this.numeroCuotas = numeroCuotas;
        this.cuotasPagadas = 0;
        this.saldoPendiente = calcularTotalAPagar();
    }

    public String getCodigoPrestamo() { return codigoPrestamo; }
    public double getMontoOriginal() { return montoOriginal; }
    public double getTasaInteresMensual() { return tasaInteresMensual; }
    public int getNumeroCuotas() { return numeroCuotas; }
    public int getCuotasPagadas() { return cuotasPagadas; }
    public double getSaldoPendiente() { return saldoPendiente; }
    public TipoPrestamo getTipoPrestamo() { return tipoPrestamo; }
    public Banco getBanco() { return banco; }
    public Empleado getDeudor() { return deudor; }

    public void setTipoPrestamo(TipoPrestamo tipoPrestamo) { this.tipoPrestamo = tipoPrestamo; }
    public void setBanco(Banco banco) { this.banco = banco; }
    public void setDeudor(Empleado deudor) { this.deudor = deudor; }

    public double calcularInteresTotal() {
        return Dinero.redondear(montoOriginal * (tasaInteresMensual / 100) * numeroCuotas);
    }

    public double calcularTotalAPagar() {
        return Dinero.redondear(montoOriginal + calcularInteresTotal());
    }

    public double calcularMontoCuota() {
        return Dinero.redondear(calcularTotalAPagar() / numeroCuotas);
    }

    /**
     * Monto de la siguiente cuota. La ultima cuota paga exactamente lo
     * que falta, asi no quedan centimos sueltos por el redondeo.
     */
    public double calcularSiguienteCuota() {
        if (cuotasPagadas == numeroCuotas - 1) {
            return saldoPendiente;
        }
        return Math.min(calcularMontoCuota(), saldoPendiente);
    }

    /**
     * Paga UNA cuota retirando el dinero de la cuenta indicada. La
     * cuenta debe ser del mismo deudor: nadie paga con cuenta ajena.
     */
    public double pagarCuota(CuentaBancaria cuenta) throws OperacionInvalidaException {
        if (estaPagado()) {
            throw new OperacionInvalidaException("El prestamo " + codigoPrestamo + " ya esta pagado.");
        }
        if (cuenta == null || cuenta.getTitular() != deudor) {
            throw new OperacionInvalidaException("La cuenta debe pertenecer al deudor del prestamo.");
        }
        double cuota = calcularSiguienteCuota();
        // Si no hay saldo, retirar() lanza la excepcion y el prestamo no cambia.
        cuenta.retirar(cuota, "Cuota " + (cuotasPagadas + 1) + "/" + numeroCuotas
                + " del prestamo " + codigoPrestamo);
        saldoPendiente = Dinero.redondear(saldoPendiente - cuota);
        cuotasPagadas++;
        return cuota;
    }

    public boolean estaPagado() {
        return saldoPendiente <= 0;
    }

    @Override
    public boolean validar() {
        return montoOriginal > 0 && deudor != null && banco != null
                && tipoPrestamo != null && numeroCuotas > 0;
    }

    @Override
    public String resumen() {
        String nombreTipo = (tipoPrestamo != null) ? tipoPrestamo.getNombre() : "Sin tipo";
        String nombreBanco = (banco != null) ? banco.getNombreBanco() : "Sin banco";
        String nombreDeudor = (deudor != null) ? deudor.getNombreCompleto() : "Sin deudor";
        String estado = estaPagado() ? "PAGADO" : "PENDIENTE";
        String mensaje = "Prestamo " + codigoPrestamo + " (" + nombreTipo + " - " + nombreBanco + ")";
        mensaje += "\n   Deudor: " + nombreDeudor;
        mensaje += "\n   Monto prestado: " + Dinero.formatear(montoOriginal)
                + " | Interes: " + tasaInteresMensual + "% mensual = " + Dinero.formatear(calcularInteresTotal());
        mensaje += "\n   Total a pagar: " + Dinero.formatear(calcularTotalAPagar())
                + " | Cuota: " + Dinero.formatear(calcularMontoCuota());
        mensaje += "\n   Cuotas pagadas: " + cuotasPagadas + "/" + numeroCuotas
                + " | Saldo pendiente: " + Dinero.formatear(saldoPendiente) + " | " + estado;
        return mensaje;
    }

    @Override
    public String toString() {
        String nombreDeudor = (deudor != null) ? deudor.getNombreCompleto() : "Sin deudor";
        return "Prestamo " + codigoPrestamo + " | " + nombreDeudor + " | Cuotas " + cuotasPagadas + "/"
                + numeroCuotas + " | Saldo pendiente: " + Dinero.formatear(saldoPendiente);
    }
}
