package walletpro;

import java.util.Scanner;

/**
 * ALUMNO 2 - Funcionalidad 2 (Registrar Categoria de Gasto),
 * aplicando lo visto en clase: en la entrega anterior esta clase se
 * precargaba automaticamente (cargarDatosDePrueba); ahora se levanta
 * esa observacion y se vuelve totalmente interactiva, con registro
 * por Scanner y validacion con regex.
 */
public class CategoriaGasto implements IValidable {

    private String nombreCategoria;
    private String descripcion;
    private boolean esFija;

    public CategoriaGasto() {
        this.nombreCategoria = "Otros";
        this.descripcion = "Sin descripcion";
        this.esFija = false;
    }

    public void registrar(Scanner teclado) {
        String entrada;

        do {
            System.out.print("Nombre de la categoria: ");
            entrada = teclado.nextLine();
            if (!ValidRegex.esTextoValido(entrada)) {
                System.out.println("El nombre de la categoria no puede estar vacio.");
            }
        } while (!ValidRegex.esTextoValido(entrada));
        this.nombreCategoria = entrada;

        System.out.print("Descripcion: ");
        entrada = teclado.nextLine();
        this.descripcion = ValidRegex.esTextoValido(entrada) ? entrada : "Sin descripcion";

        String respuesta;
        do {
            System.out.print("Es un gasto fijo (recurrente)? (S/N): ");
            respuesta = teclado.nextLine();
            if (!respuesta.equalsIgnoreCase("S") && !respuesta.equalsIgnoreCase("N")) {
                System.out.println("Responda con S o N.");
            }
        } while (!respuesta.equalsIgnoreCase("S") && !respuesta.equalsIgnoreCase("N"));
        this.esFija = respuesta.equalsIgnoreCase("S");
    }

    public String getNombreCategoria() { return nombreCategoria; }
    public String getDescripcion() { return descripcion; }
    public boolean isEsFija() { return esFija; }

    public void setNombreCategoria(String nombreCategoria) {
        if (ValidRegex.esTextoValido(nombreCategoria)) {
            this.nombreCategoria = nombreCategoria;
        }
    }

    public void setDescripcion(String descripcion) {
        if (ValidRegex.esTextoValido(descripcion)) {
            this.descripcion = descripcion;
        }
    }

    public void setEsFija(boolean esFija) { this.esFija = esFija; }

    @Override
    public boolean validar() {
        return ValidRegex.esTextoValido(nombreCategoria);
    }

    @Override
    public String toString() {
        String tipo = esFija ? "Fija" : "Ocasional";
        return nombreCategoria + " (" + tipo + ")";
    }
}
