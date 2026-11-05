package walletpro.modelo;

import walletpro.interfaces.*;
import walletpro.util.*;

import java.util.Scanner;

/**
 * ALUMNO 1 - Funcionalidad 3 (Registrar Sueldo Mensual), nueva en
 * esta entrega. El Empleado se asigna por setter tras la seleccion
 * en Main (agregacion: Sueldo no es dueno del Empleado).
 */
public class Sueldo implements IValidable, IConsultable {

    private int mes;
    private int anio;
    private double monto;
    private Empleado empleado;

    public Sueldo() {
        this.mes = 1;
        this.anio = 1900;
        this.monto = 0;
        this.empleado = null;
    }

    public void registrar(Scanner teclado) {
        boolean valido = false;
        do {
            System.out.print("Mes (1-12): ");
            try {
                int valor = Integer.parseInt(teclado.nextLine());
                if (valor >= 1 && valor <= 12) {
                    this.mes = valor;
                    valido = true;
                } else {
                    System.out.println("El mes debe estar entre 1 y 12.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un numero entero valido.");
            }
        } while (!valido);

        valido = false;
        do {
            System.out.print("Anio (1900-2100): ");
            try {
                int valor = Integer.parseInt(teclado.nextLine());
                if (valor >= 1900 && valor <= 2100) {
                    this.anio = valor;
                    valido = true;
                } else {
                    System.out.println("El anio debe estar entre 1900 y 2100.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un numero entero valido.");
            }
        } while (!valido);

        valido = false;
        do {
            System.out.print("Monto del sueldo: ");
            try {
                double valor = Double.parseDouble(teclado.nextLine());
                if (valor > 0) {
                    this.monto = valor;
                    valido = true;
                } else {
                    System.out.println("El monto debe ser mayor a 0.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un numero valido.");
            }
        } while (!valido);
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
        if (anio >= 1900 && anio <= 2100) {
            this.anio = anio;
        }
    }

    public void setMonto(double monto) {
        if (monto > 0) {
            this.monto = monto;
        }
    }

    @Override
    public boolean validar() {
        return monto > 0 && mes >= 1 && mes <= 12 && empleado != null;
    }

    @Override
    public String resumen() {
        String nombreEmpleado = (empleado != null) ? empleado.getNombreCompleto() : "Sin empleado";
        String mensaje = "Datos del sueldo";
        mensaje += "\nMes: " + mes;
        mensaje += "\nAnio: " + anio;
        mensaje += "\nMonto: " + monto;
        mensaje += "\nEmpleado: " + nombreEmpleado;
        return mensaje;
    }
}
