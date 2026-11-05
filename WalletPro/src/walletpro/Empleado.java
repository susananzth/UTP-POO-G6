package walletpro;

import java.util.Scanner;

/**
 * Empleado extiende de Persona: agrega los datos laborales (cargo,
 * empresa y sueldo base) necesarios para el modulo de Ingresos y
 * Ahorros (Alumno 1).
 */
public class Empleado extends Persona {

    private Cargo cargo;
    private Empresa empresa;
    private double sueldoBase;

    public Empleado() {
        super();
        this.cargo = null;
        this.empresa = null;
        this.sueldoBase = 0;
    }

    /**
     * Registra los datos personales heredados y el sueldo base.
     * Cargo y Empresa se asignan aparte (setCargo/setEmpresa), luego
     * de que Main muestre la lista y el usuario seleccione una.
     */
    public void registrar(Scanner teclado) {
        registrarDatosPersonales(teclado);

        boolean valido = false;
        do {
            System.out.print("Sueldo base: ");
            try {
                double valor = Double.parseDouble(teclado.nextLine());
                if (valor > 0) {
                    this.sueldoBase = valor;
                    valido = true;
                } else {
                    System.out.println("El sueldo base debe ser mayor a 0.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un numero valido.");
            }
        } while (!valido);
    }

    public Cargo getCargo() { return cargo; }
    public Empresa getEmpresa() { return empresa; }
    public double getSueldoBase() { return sueldoBase; }

    public void setCargo(Cargo cargo) { this.cargo = cargo; }
    public void setEmpresa(Empresa empresa) { this.empresa = empresa; }

    public void setSueldoBase(double sueldoBase) {
        if (sueldoBase >= 0) {
            this.sueldoBase = sueldoBase;
        }
    }

    public double calcularSueldoAnual() {
        return sueldoBase * 12;
    }

    @Override
    public String getRol() {
        return "Empleado";
    }

    @Override
    public boolean validar() {
        return super.validar() && cargo != null && empresa != null && sueldoBase > 0;
    }

    @Override
    public String resumen() {
        String nombreEmpresa = (empresa != null) ? empresa.getRazonSocial() : "Sin empresa";
        String nombreCargo = (cargo != null) ? cargo.getNombreCargo() : "Sin cargo";
        String mensaje = "Datos del empleado";
        mensaje += "\nNombre: " + getNombreCompleto();
        mensaje += "\nDNI: " + dni;
        mensaje += "\nEmpresa: " + nombreEmpresa;
        mensaje += "\nCargo: " + nombreCargo;
        mensaje += "\nSueldo base: " + sueldoBase;
        return mensaje;
    }
}
