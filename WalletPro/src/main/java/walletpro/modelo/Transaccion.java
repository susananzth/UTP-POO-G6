package walletpro.modelo;

import walletpro.interfaces.*;
import walletpro.util.*;

/**
 * Clase abstracta que representa un hecho economico con fecha, monto
 * y descripcion. Gasto y Movimiento heredan de aqui porque ambos son,
 * en esencia, una transaccion, pero cada uno calcula su tipo de forma
 * distinta (getTipo() abstracto = polimorfismo).
 */
public abstract class Transaccion implements IValidable, IConsultable {

    protected String idTransaccion;
    protected String fecha;
    protected double monto;
    protected String descripcion;

    public Transaccion() {
        this.idTransaccion = "";
        this.fecha = "";
        this.monto = 0;
        this.descripcion = "";
    }

    /**
     * Lee los datos comunes a toda transaccion. Las subclases la
     * llaman dentro de su propio registrar().
     */
    protected void registrarDatosTransaccion(String idTransaccion) {
        this.idTransaccion = idTransaccion;
        this.fecha = Consola.leerFecha("Fecha (dd-mm-aaaa):");
        this.monto = Consola.leerMonto("Monto (S/):");
        this.descripcion = Consola.leerTexto("Descripcion:");
    }

    /** Para crear transacciones desde el codigo (datos de prueba, movimientos internos). */
    protected void fijarDatosTransaccion(String idTransaccion, String fecha,
                                          double monto, String descripcion) {
        this.idTransaccion = idTransaccion;
        this.fecha = ValidFecha.validar(fecha) ? fecha : ValidFecha.hoy();
        this.monto = (monto > 0) ? Dinero.redondear(monto) : 0;
        this.descripcion = ValidRegex.esTextoValido(descripcion) ? descripcion.trim() : "Sin descripcion";
    }

    public String getIdTransaccion() { return idTransaccion; }
    public String getFecha() { return fecha; }
    public double getMonto() { return monto; }
    public String getDescripcion() { return descripcion; }

    @Override
    public boolean validar() {
        return ValidRegex.esTextoValido(idTransaccion)
                && ValidFecha.validar(fecha)
                && monto > 0
                && ValidRegex.esTextoValido(descripcion);
    }

    /** Metodo abstracto: cada subclase define su propio tipo de transaccion. */
    public abstract String getTipo();

    @Override
    public String resumen() {
        return String.format("%-6s | %s | %-13s | %13s | %s",
                idTransaccion, fecha, getTipo(), Dinero.formatear(monto), descripcion);
    }

    @Override
    public String toString() {
        return resumen();
    }
}
