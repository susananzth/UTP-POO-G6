package walletpro.modelo;

import walletpro.interfaces.*;
import walletpro.util.*;

import java.util.Scanner;

/**
 * Clasifica un Prestamo (Personal, Hipotecario, etc.).
 */
public class TipoPrestamo implements IValidable {

    private String nombre;
    private String descripcion;

    public TipoPrestamo() {
        this.nombre = "General";
        this.descripcion = "Sin descripcion";
    }

    public void registrar(Scanner teclado) {
        String entrada;

        do {
            System.out.print("Nombre del tipo de prestamo: ");
            entrada = teclado.nextLine();
            if (!ValidRegex.esTextoValido(entrada)) {
                System.out.println("El nombre no puede estar vacio.");
            }
        } while (!ValidRegex.esTextoValido(entrada));
        this.nombre = entrada;

        System.out.print("Descripcion: ");
        entrada = teclado.nextLine();
        this.descripcion = ValidRegex.esTextoValido(entrada) ? entrada : "Sin descripcion";
    }

    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }

    public void setNombre(String nombre) {
        if (ValidRegex.esTextoValido(nombre)) {
            this.nombre = nombre;
        }
    }

    public void setDescripcion(String descripcion) {
        if (ValidRegex.esTextoValido(descripcion)) {
            this.descripcion = descripcion;
        }
    }

    @Override
    public boolean validar() {
        return !nombre.equals("General") || !descripcion.equals("Sin descripcion");
    }

    @Override
    public String toString() {
        return nombre;
    }
}
