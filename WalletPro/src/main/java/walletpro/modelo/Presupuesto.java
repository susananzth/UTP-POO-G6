package walletpro.modelo;

import walletpro.interfaces.*;
import walletpro.util.*;

import java.util.Scanner;

/**
 * ALUMNO 2 - Funcionalidad 3 (Registrar Presupuesto), nueva en esta
 * entrega: define un limite de gasto por categoria y por mes para un
 * empleado. CategoriaGasto y Empleado se asignan por setter tras la
 * seleccion en Main (agregacion).
 */
public class Presupuesto implements IValidable, IConsultable {

    private int mes;
    private int anio;
    private double montoPresupuestado;
    private CategoriaGasto categoria;
    private Empleado empleado;

    public Presupuesto() {
        this.mes = 1;
        this.anio = 1900;
        this.montoPresupuestado = 0;
        this.categoria = null;
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
            System.out.print("Monto presupuestado: ");
            try {
                double valor = Double.parseDouble(teclado.nextLine());
                if (valor > 0) {
                    this.montoPresupuestado = valor;
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
        if (anio >= 1900 && anio <= 2100) {
            this.anio = anio;
        }
    }

    public void setMontoPresupuestado(double montoPresupuestado) {
        if (montoPresupuestado > 0) {
            this.montoPresupuestado = montoPresupuestado;
        }
    }

    /** Suma, de un arreglo de gastos, los que caen en este mes/anio/categoria/empleado. */
    public double calcularGastado(Gasto[] gastos, int totalGastos) {
        double total = 0;
        for (int i = 0; i < totalGastos; i++) {
            Gasto g = gastos[i];
            if (g.getCategoria() == categoria && g.getPersona() == empleado
                    && ValidFecha.obtenerMesAnio(g.getFecha(), mes, anio)) {
                total += g.getMonto();
            }
        }
        return total;
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
        String mensaje = "Datos del presupuesto";
        mensaje += "\nMes: " + mes;
        mensaje += "\nAnio: " + anio;
        mensaje += "\nCategoria: " + nombreCategoria;
        mensaje += "\nMonto presupuestado: " + montoPresupuestado;
        mensaje += "\nEmpleado: " + nombreEmpleado;
        return mensaje;
    }
}
