package walletpro;

import java.util.Scanner;

/**
 * Clase abstracta que representa un hecho economico con fecha, monto
 * y descripcion. Gasto y Movimiento heredan de aqui porque ambos son,
 * en esencia, una transaccion, pero cada uno calcula su tipo de forma
 * distinta (getTipo() abstracto).
 */
public abstract class Transaccion implements IValidable, IConsultable {

    protected String idTransaccion;
    protected String fecha;
    protected double monto;
    protected String descripcion;

    public Transaccion() {
        this.idTransaccion = "T000";
        this.fecha = "01-01-1900";
        this.monto = 0;
        this.descripcion = "Sin descripcion";
    }

    /**
     * Lee los datos comunes a toda transaccion. Las subclases la
     * llaman dentro de su propio registrar().
     */
    protected void registrarDatosTransaccion(Scanner teclado, String idTransaccion) {
        this.idTransaccion = ValidRegex.esTextoValido(idTransaccion) ? idTransaccion : "T000";

        String entrada;
        do {
            System.out.print("Fecha (dd-mm-aaaa): ");
            entrada = teclado.nextLine();
            if (!ValidFecha.validar(entrada)) {
                System.out.println("Fecha invalida. Use el formato dd-mm-aaaa.");
            }
        } while (!ValidFecha.validar(entrada));
        this.fecha = entrada;

        boolean valido = false;
        do {
            System.out.print("Monto: ");
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

        do {
            System.out.print("Descripcion: ");
            entrada = teclado.nextLine();
            if (!ValidRegex.esTextoValido(entrada)) {
                System.out.println("La descripcion no puede estar vacia.");
            }
        } while (!ValidRegex.esTextoValido(entrada));
        this.descripcion = entrada;
    }

    public String getIdTransaccion() { return idTransaccion; }
    public String getFecha() { return fecha; }
    public double getMonto() { return monto; }
    public String getDescripcion() { return descripcion; }

    /** Para precargar datos de prueba sin pasar por el Scanner. */
    protected void fijarDatosTransaccion(String idTransaccion, String fecha,
                                          double monto, String descripcion) {
        this.idTransaccion = ValidRegex.esTextoValido(idTransaccion) ? idTransaccion : "T000";
        this.fecha = ValidFecha.validar(fecha) ? fecha : "01-01-1900";
        this.monto = (monto > 0) ? monto : 0;
        this.descripcion = ValidRegex.esTextoValido(descripcion) ? descripcion : "Sin descripcion";
    }

    @Override
    public boolean validar() {
        return ValidFecha.validar(fecha)
                && monto > 0
                && !descripcion.equals("Sin descripcion");
    }

    /** Metodo abstracto: cada subclase define su propio tipo de transaccion. */
    public abstract String getTipo();

    public void procesar() {
        String mensaje = "Procesando transaccion " + idTransaccion;
        mensaje += "\nTipo: " + getTipo();
        mensaje += "\nMonto: " + monto;
        System.out.println(mensaje);
    }

    @Override
    public String resumen() {
        String mensaje = "ID: " + idTransaccion;
        mensaje += " | Fecha: " + fecha;
        mensaje += " | Tipo: " + getTipo();
        mensaje += " | Monto: " + monto;
        return mensaje;
    }

    @Override
    public String toString() {
        return resumen();
    }
}
