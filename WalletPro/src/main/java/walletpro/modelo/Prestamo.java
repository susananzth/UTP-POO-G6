package walletpro.modelo;

import walletpro.interfaces.*;
import walletpro.util.*;

import java.util.Scanner;

/**
 * ALUMNO 3 - Funcionalidad 3 (Registrar Prestamo), nueva en esta
 * entrega. TipoPrestamo, Banco, Empleado (deudor) y CuentaBancaria
 * se asignan por setter tras la seleccion en Main (agregacion).
 */
public class Prestamo implements IValidable, IConsultable {

    private String codigoPrestamo;
    private double montoOriginal;
    private double saldoPendiente;
    private double tasaInteresMensual;
    private int numeroCuotas;
    private TipoPrestamo tipoPrestamo;
    private Banco banco;
    private Empleado deudor;
    private CuentaBancaria cuentaAsociada;

    public Prestamo() {
        this.codigoPrestamo = "P000";
        this.montoOriginal = 0;
        this.saldoPendiente = 0;
        this.tasaInteresMensual = 0;
        this.numeroCuotas = 1;
        this.tipoPrestamo = null;
        this.banco = null;
        this.deudor = null;
        this.cuentaAsociada = null;
    }

    public void registrar(Scanner teclado, String codigoPrestamo) {
        this.codigoPrestamo = ValidRegex.esTextoValido(codigoPrestamo) ? codigoPrestamo : "P000";

        boolean valido = false;
        do {
            System.out.print("Monto original del prestamo: ");
            try {
                double valor = Double.parseDouble(teclado.nextLine());
                if (valor > 0) {
                    this.montoOriginal = valor;
                    this.saldoPendiente = valor;
                    valido = true;
                } else {
                    System.out.println("El monto debe ser mayor a 0.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un numero valido.");
            }
        } while (!valido);

        valido = false;
        do {
            System.out.print("Tasa de interes mensual (%): ");
            try {
                double valor = Double.parseDouble(teclado.nextLine());
                if (valor >= 0) {
                    this.tasaInteresMensual = valor;
                    valido = true;
                } else {
                    System.out.println("La tasa no puede ser negativa.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un numero valido.");
            }
        } while (!valido);

        valido = false;
        do {
            System.out.print("Numero de cuotas: ");
            try {
                int valor = Integer.parseInt(teclado.nextLine());
                if (valor > 0) {
                    this.numeroCuotas = valor;
                    valido = true;
                } else {
                    System.out.println("El numero de cuotas debe ser mayor a 0.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un numero entero valido.");
            }
        } while (!valido);
    }

    public String getCodigoPrestamo() { return codigoPrestamo; }
    public double getMontoOriginal() { return montoOriginal; }
    public double getSaldoPendiente() { return saldoPendiente; }
    public double getTasaInteresMensual() { return tasaInteresMensual; }
    public int getNumeroCuotas() { return numeroCuotas; }
    public TipoPrestamo getTipoPrestamo() { return tipoPrestamo; }
    public Banco getBanco() { return banco; }
    public Empleado getDeudor() { return deudor; }
    public CuentaBancaria getCuentaAsociada() { return cuentaAsociada; }

    public void setTipoPrestamo(TipoPrestamo tipoPrestamo) { this.tipoPrestamo = tipoPrestamo; }
    public void setBanco(Banco banco) { this.banco = banco; }
    public void setDeudor(Empleado deudor) { this.deudor = deudor; }
    public void setCuentaAsociada(CuentaBancaria cuentaAsociada) { this.cuentaAsociada = cuentaAsociada; }

    public void setCodigoPrestamo(String codigoPrestamo) {
        if (ValidRegex.esTextoValido(codigoPrestamo)) {
            this.codigoPrestamo = codigoPrestamo;
        }
    }

    public void setMontoOriginal(double montoOriginal) {
        if (montoOriginal > 0) {
            this.montoOriginal = montoOriginal;
            this.saldoPendiente = montoOriginal;
        }
    }

    public void setTasaInteresMensual(double tasaInteresMensual) {
        if (tasaInteresMensual >= 0) {
            this.tasaInteresMensual = tasaInteresMensual;
        }
    }

    public void setNumeroCuotas(int numeroCuotas) {
        if (numeroCuotas > 0) {
            this.numeroCuotas = numeroCuotas;
        }
    }

    public double calcularMontoCuota() {
        if (numeroCuotas <= 0) {
            return 0;
        }
        double totalConInteres = montoOriginal * (1 + (tasaInteresMensual / 100) * numeroCuotas);
        return totalConInteres / numeroCuotas;
    }

    public boolean pagarDesdeCuenta(double montoAPagar, String fecha, String idMovimiento) {
        if (montoAPagar <= 0 || montoAPagar > saldoPendiente) {
            return false;
        }
        if (cuentaAsociada == null) {
            return false;
        }
        String descripcion = "Pago de prestamo " + codigoPrestamo;
        if (!cuentaAsociada.registrarPago(montoAPagar, fecha, idMovimiento, descripcion)) {
            return false;
        }
        saldoPendiente -= montoAPagar;
        return true;
    }

    public boolean estaPagado() {
        return saldoPendiente <= 0.01;
    }

    @Override
    public boolean validar() {
        return montoOriginal > 0 && deudor != null && banco != null
                && tipoPrestamo != null && numeroCuotas > 0;
    }

    @Override
    public String resumen() {
        String nombreTipo = (tipoPrestamo != null) ? tipoPrestamo.getNombre() : "General";
        String estado = estaPagado() ? "PAGADO" : "PENDIENTE";
        String nombreDeudor = (deudor != null) ? deudor.getNombreCompleto() : "Sin deudor";
        String mensaje = "Datos del prestamo";
        mensaje += "\nCodigo: " + codigoPrestamo;
        mensaje += "\nTipo: " + nombreTipo;
        mensaje += "\nDeudor: " + nombreDeudor;
        mensaje += "\nMonto original: " + montoOriginal;
        mensaje += "\nCuota mensual: " + String.format("%.2f", calcularMontoCuota());
        mensaje += "\nSaldo pendiente: " + saldoPendiente;
        mensaje += "\nCuotas: " + numeroCuotas;
        mensaje += "\nEstado: " + estado;
        return mensaje;
    }

    @Override
    public String toString() {
        return "Prestamo " + codigoPrestamo + " | Saldo: " + saldoPendiente;
    }
}
