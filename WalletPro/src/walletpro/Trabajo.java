package walletpro;

import java.util.Scanner;

/**
 * ALUMNO 1 - Funcionalidad 2 (Registrar Trabajo), aplicando lo visto
 * en clase: sin constructor con parametros, registro con Scanner +
 * try-catch/regex, relaciones con Empresa/Cargo/Empleado ya creados
 * (agregacion) asignadas por setter despues de que Main muestre las
 * listas y el usuario seleccione.
 */
public class Trabajo implements IValidable, IConsultable {

    private Empresa empresa;
    private Cargo cargo;
    private String fechaIngreso;
    private String tipoContrato;
    private Empleado empleado;

    public Trabajo() {
        this.empresa = null;
        this.cargo = null;
        this.fechaIngreso = "01-01-1900";
        this.tipoContrato = "Independiente";
        this.empleado = null;
    }

    public void registrar(Scanner teclado) {
        String entrada;

        do {
            System.out.print("Fecha de ingreso (dd-mm-aaaa): ");
            entrada = teclado.nextLine();
            if (!ValidFecha.validar(entrada)) {
                System.out.println("Fecha invalida. Use el formato dd-mm-aaaa.");
            }
        } while (!ValidFecha.validar(entrada));
        this.fechaIngreso = entrada;

        int opcion;
        boolean valido = false;
        do {
            System.out.println("Tipo de contrato:");
            System.out.println("1. Planilla");
            System.out.println("2. Independiente");
            System.out.println("3. Contrato");
            System.out.println("4. Practicas");
            try {
                opcion = Integer.parseInt(teclado.nextLine());
                if (opcion >= 1 && opcion <= 4) {
                    valido = true;
                } else {
                    System.out.println("Opcion fuera de rango.");
                    opcion = -1;
                }
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un numero valido.");
                opcion = -1;
            }
        } while (!valido);

        switch (opcion) {
            case 1: this.tipoContrato = "Planilla"; break;
            case 2: this.tipoContrato = "Independiente"; break;
            case 3: this.tipoContrato = "Contrato"; break;
            case 4: this.tipoContrato = "Practicas"; break;
        }
    }

    public Empresa getEmpresa() { return empresa; }
    public Cargo getCargo() { return cargo; }
    public String getFechaIngreso() { return fechaIngreso; }
    public String getTipoContrato() { return tipoContrato; }
    public Empleado getEmpleado() { return empleado; }

    public void setEmpresa(Empresa empresa) { this.empresa = empresa; }
    public void setCargo(Cargo cargo) { this.cargo = cargo; }
    public void setEmpleado(Empleado empleado) { this.empleado = empleado; }

    public void setFechaIngreso(String fechaIngreso) {
        if (ValidFecha.validar(fechaIngreso)) {
            this.fechaIngreso = fechaIngreso;
        }
    }

    public void setTipoContrato(String tipoContrato) {
        if (ValidRegex.esTextoValido(tipoContrato)) {
            this.tipoContrato = tipoContrato;
        }
    }

    @Override
    public boolean validar() {
        return empresa != null && empresa.validar()
                && cargo != null && cargo.validar()
                && empleado != null
                && ValidFecha.validar(fechaIngreso);
    }

    @Override
    public String resumen() {
        String nombreEmpleado = (empleado != null) ? empleado.getNombreCompleto() : "Sin empleado";
        String mensaje = "Datos del trabajo";
        mensaje += "\nEmpleado: " + nombreEmpleado;
        mensaje += "\nEmpresa: " + empresa;
        mensaje += "\nCargo: " + cargo;
        mensaje += "\nIngreso: " + fechaIngreso;
        mensaje += "\nContrato: " + tipoContrato;
        return mensaje;
    }

    @Override
    public String toString() {
        String nombreEmpleado = (empleado != null) ? empleado.getNombreCompleto() : "Sin empleado";
        return nombreEmpleado + " | " + cargo + " en " + empresa;
    }
}
