package walletpro;

import java.util.Scanner;

/**
 * Representa el puesto que ocupa un Empleado dentro de su Empresa.
 * Un mismo cargo (ej. "Desarrollador") puede repetirse en varios
 * empleados, por eso se mantiene como clase propia.
 */
public class Cargo implements IValidable {

    private String nombreCargo;
    private String descripcion;

    public Cargo() {
        this.nombreCargo = "Sin cargo";
        this.descripcion = "Sin descripcion";
    }

    public void registrar(Scanner teclado) {
        String entrada;

        do {
            System.out.print("Nombre del cargo: ");
            entrada = teclado.nextLine();
            if (!ValidRegex.esTextoValido(entrada)) {
                System.out.println("El nombre del cargo no puede estar vacio.");
            }
        } while (!ValidRegex.esTextoValido(entrada));
        this.nombreCargo = entrada;

        System.out.print("Descripcion: ");
        entrada = teclado.nextLine();
        this.descripcion = ValidRegex.esTextoValido(entrada) ? entrada : "Sin descripcion";
    }

    public String getNombreCargo() { return nombreCargo; }
    public String getDescripcion() { return descripcion; }

    public void setNombreCargo(String nombreCargo) {
        if (ValidRegex.esTextoValido(nombreCargo)) {
            this.nombreCargo = nombreCargo;
        }
    }

    public void setDescripcion(String descripcion) {
        if (ValidRegex.esTextoValido(descripcion)) {
            this.descripcion = descripcion;
        }
    }

    @Override
    public boolean validar() {
        return !nombreCargo.startsWith("Sin");
    }

    @Override
    public String toString() {
        return nombreCargo;
    }
}
