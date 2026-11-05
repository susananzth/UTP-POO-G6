package walletpro.modelo;

import walletpro.interfaces.*;
import walletpro.util.*;

import java.util.Scanner;

/**
 * ALUMNO 3 - Funcionalidad 2 (Registrar Cuenta Bancaria), aplicando
 * lo visto en clase. Implementa OperacionFinanciera porque es la
 * unica clase que ejecuta movimientos de dinero en tiempo real.
 * Es duena de sus Movimiento (composicion: un movimiento no tiene
 * sentido fuera de la cuenta donde ocurrio).
 */
public class CuentaBancaria implements OperacionFinanciera, IValidable, IConsultable {

    private String numeroCuenta;
    private String tipoCuenta;
    private double saldo;
    private Banco banco;
    private Empleado titular;
    private Movimiento[] movimientos;
    private int totalMovimientos;

    public CuentaBancaria() {
        this.numeroCuenta = "000000";
        this.tipoCuenta = "Ahorros";
        this.saldo = 0;
        this.banco = null;
        this.titular = null;
        this.movimientos = new Movimiento[500];
        this.totalMovimientos = 0;
    }

    public void registrar(Scanner teclado) {
        String entrada;

        do {
            System.out.print("Numero de cuenta (minimo 6 digitos): ");
            entrada = teclado.nextLine();
            if (entrada == null || entrada.length() < 6 || !entrada.matches("\\d+")) {
                System.out.println("El numero de cuenta debe tener al menos 6 digitos.");
            }
        } while (entrada == null || entrada.length() < 6 || !entrada.matches("\\d+"));
        this.numeroCuenta = entrada;

        int opcion;
        boolean valido = false;
        do {
            System.out.println("Tipo de cuenta:");
            System.out.println("1. Ahorros");
            System.out.println("2. Corriente");
            try {
                opcion = Integer.parseInt(teclado.nextLine());
                valido = (opcion == 1 || opcion == 2);
                if (!valido) {
                    System.out.println("Opcion fuera de rango.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un numero valido.");
                opcion = -1;
            }
        } while (!valido);
        this.tipoCuenta = (opcion == 2) ? "Corriente" : "Ahorros";

        valido = false;
        do {
            System.out.print("Saldo inicial: ");
            try {
                double valorSaldo = Double.parseDouble(teclado.nextLine());
                if (valorSaldo >= 0) {
                    this.saldo = valorSaldo;
                    valido = true;
                } else {
                    System.out.println("El saldo no puede ser negativo.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un numero valido.");
            }
        } while (!valido);
    }

    public String getNumeroCuenta() { return numeroCuenta; }
    public String getTipoCuenta() { return tipoCuenta; }
    public double getSaldo() { return saldo; }
    public Banco getBanco() { return banco; }
    public Empleado getTitular() { return titular; }
    public int getTotalMovimientos() { return totalMovimientos; }
    public Movimiento getMovimiento(int posicion) { return movimientos[posicion]; }

    public void setBanco(Banco banco) { this.banco = banco; }
    public void setTitular(Empleado titular) { this.titular = titular; }

    public void setNumeroCuenta(String numeroCuenta) {
        if (numeroCuenta != null && numeroCuenta.length() >= 6) {
            this.numeroCuenta = numeroCuenta;
        }
    }

    public void setTipoCuenta(String tipoCuenta) {
        if ("Ahorros".equalsIgnoreCase(tipoCuenta) || "Corriente".equalsIgnoreCase(tipoCuenta)) {
            this.tipoCuenta = tipoCuenta;
        }
    }

    public void setSaldoInicial(double saldo) {
        if (saldo >= 0) {
            this.saldo = saldo;
        }
    }

    public boolean haySaldoSuficiente(double monto) {
        return monto > 0 && monto <= saldo;
    }

    @Override
    public boolean validarMonto(double monto) {
        return monto > 0;
    }

    @Override
    public boolean ejecutar(double monto) {
        if (!validarMonto(monto)) {
            return false;
        }
        saldo += monto;
        return true;
    }

    public boolean depositar(double monto) {
        if (!validarMonto(monto)) {
            System.out.println("Monto invalido para deposito.");
            return false;
        }
        saldo += monto;
        registrarEnHistorial("DEPOSITO", monto, "Deposito en cuenta");
        System.out.println("Deposito realizado. Nuevo saldo: " + saldo);
        return true;
    }

    public boolean retirar(double monto) {
        if (!validarMonto(monto)) {
            System.out.println("Monto invalido para retiro.");
            return false;
        }
        if (!haySaldoSuficiente(monto)) {
            System.out.println("Saldo insuficiente.");
            return false;
        }
        saldo -= monto;
        registrarEnHistorial("RETIRO", monto, "Retiro de cuenta");
        System.out.println("Retiro realizado. Nuevo saldo: " + saldo);
        return true;
    }

    /** Usado por Prestamo.pagarDesdeCuenta para registrar el pago como movimiento. */
    public boolean registrarPago(double monto, String fecha, String idMovimiento, String descripcion) {
        if (!haySaldoSuficiente(monto) || totalMovimientos >= movimientos.length) {
            return false;
        }
        saldo -= monto;
        Movimiento movimiento = new Movimiento();
        movimiento.completar(idMovimiento, fecha, descripcion, monto, "PAGO");
        movimientos[totalMovimientos] = movimiento;
        totalMovimientos++;
        return true;
    }

    private void registrarEnHistorial(String tipo, double monto, String descripcion) {
        if (totalMovimientos >= movimientos.length) {
            return;
        }
        Movimiento movimiento = new Movimiento();
        String id = "M" + String.format("%04d", totalMovimientos + 1);
        movimiento.completar(id, "01-01-2026", descripcion, monto, tipo);
        movimientos[totalMovimientos] = movimiento;
        totalMovimientos++;
    }

    public void listarMovimientos() {
        if (totalMovimientos == 0) {
            System.out.println("No hay movimientos en esta cuenta.");
            return;
        }
        String mensaje = "Movimientos de la cuenta " + numeroCuenta;
        for (int i = 0; i < totalMovimientos; i++) {
            mensaje += "\n" + (i + 1) + ". " + movimientos[i].resumen();
        }
        System.out.println(mensaje);
    }

    @Override
    public boolean validar() {
        return numeroCuenta.length() >= 6 && banco != null && titular != null;
    }

    @Override
    public String resumen() {
        String nombreBanco = (banco != null) ? banco.getNombreBanco() : "Sin banco";
        String nombreTitular = (titular != null) ? titular.getNombreCompleto() : "Sin titular";
        String mensaje = "Datos de la cuenta bancaria";
        mensaje += "\nNumero: " + numeroCuenta;
        mensaje += "\nTipo: " + tipoCuenta;
        mensaje += "\nBanco: " + nombreBanco;
        mensaje += "\nTitular: " + nombreTitular;
        mensaje += "\nSaldo: " + saldo;
        return mensaje;
    }

    @Override
    public String toString() {
        String nombreBanco = (banco != null) ? banco.getNombreBanco() : "Sin banco";
        return "Cuenta " + numeroCuenta + " (" + tipoCuenta + ") | " + nombreBanco
                + " | Saldo: " + saldo;
    }
}
