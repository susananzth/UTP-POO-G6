package walletpro;

import java.util.Scanner;

/**
 * ALUMNO 2 - Funcionalidad 1 (Registrar Gasto Mensual), ya existente
 * desde la entrega anterior y ahora adaptada a la herencia de
 * Transaccion y al estilo sin constructor con parametros.
 */
public class Gasto extends Transaccion {

    private CategoriaGasto categoria;
    private boolean esOcasional;
    private boolean montoVariable;
    private Empleado persona;

    public Gasto() {
        super();
        this.categoria = null;
        this.esOcasional = false;
        this.montoVariable = false;
        this.persona = null;
    }

    public void registrar(Scanner teclado, String idTransaccion) {
        registrarDatosTransaccion(teclado, idTransaccion);

        String respuesta;
        do {
            System.out.print("Es un gasto ocasional? (S/N): ");
            respuesta = teclado.nextLine();
            if (!respuesta.equalsIgnoreCase("S") && !respuesta.equalsIgnoreCase("N")) {
                System.out.println("Responda con S o N.");
            }
        } while (!respuesta.equalsIgnoreCase("S") && !respuesta.equalsIgnoreCase("N"));
        this.esOcasional = respuesta.equalsIgnoreCase("S");

        if (!esOcasional) {
            do {
                System.out.print("El monto es variable cada mes? (S/N): ");
                respuesta = teclado.nextLine();
                if (!respuesta.equalsIgnoreCase("S") && !respuesta.equalsIgnoreCase("N")) {
                    System.out.println("Responda con S o N.");
                }
            } while (!respuesta.equalsIgnoreCase("S") && !respuesta.equalsIgnoreCase("N"));
            this.montoVariable = respuesta.equalsIgnoreCase("S");
        }
    }

    public CategoriaGasto getCategoria() { return categoria; }
    public boolean isEsOcasional() { return esOcasional; }
    public boolean isMontoVariable() { return montoVariable; }
    public Empleado getPersona() { return persona; }

    public void setCategoria(CategoriaGasto categoria) { this.categoria = categoria; }
    public void setPersona(Empleado persona) { this.persona = persona; }
    public void setEsOcasional(boolean esOcasional) { this.esOcasional = esOcasional; }
    public void setMontoVariable(boolean montoVariable) { this.montoVariable = montoVariable; }

    /** Para precargar datos de prueba sin pasar por el Scanner. */
    public void fijarDatos(String idTransaccion, String fecha, double monto, String descripcion) {
        fijarDatosTransaccion(idTransaccion, fecha, monto, descripcion);
    }

    @Override
    public String getTipo() {
        if (esOcasional) {
            return "OCASIONAL";
        }
        if (montoVariable) {
            return "FIJO-VARIABLE";
        }
        return "FIJO";
    }

    @Override
    public boolean validar() {
        return super.validar() && categoria != null && persona != null;
    }

    @Override
    public String resumen() {
        String nombreCategoria = (categoria != null) ? categoria.getNombreCategoria() : "Sin categoria";
        String nombrePersona = (persona != null) ? persona.getNombreCompleto() : "Sin persona";
        String mensaje = super.resumen();
        mensaje += "\nCategoria: " + nombreCategoria;
        mensaje += "\nPersona: " + nombrePersona;
        return mensaje;
    }
}
