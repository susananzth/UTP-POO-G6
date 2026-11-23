package walletpro.modelo;

import walletpro.interfaces.IConsultable;
import walletpro.util.Consola;
import walletpro.util.Dinero;

/**
 * CLASE ABSTRACTA: algo que pasa con dinero en una fecha.
 * Clases hijas (HERENCIA): Gasto y Movimiento.
 */
public abstract class Transaccion implements IConsultable {

    protected int id;
    protected String fecha;        // dd-mm-aaaa
    protected double monto;
    protected String descripcion;

    // Constructor SIN parametros (regla del profesor).
    public Transaccion() {
        id = 0;
        fecha = "";
        monto = 0;
        descripcion = "";
    }

    // Pide por teclado los datos comunes. Lo llama: Gasto.registrar()
    protected void registrarDatosTransaccion() {
        fecha = Consola.leerFecha("Fecha (dd-mm-aaaa):");
        monto = Consola.leerMonto("Monto (S/):");
        descripcion = Consola.leerTexto("Descripcion:");
    }

    // METODO ABSTRACTO: Gasto responde FIJO/OCASIONAL, Movimiento DEPOSITO/RETIRO (POLIMORFISMO).
    public abstract String getTipo();

    // resumen() llama a getTipo(): Java ejecuta la version de la clase hija.
    @Override
    public String resumen() {
        return fecha + " | " + getTipo() + " | " + Dinero.formatear(monto) + " | " + descripcion;
    }

    public int getId() { return id; }
    public String getFecha() { return fecha; }
    public double getMonto() { return monto; }
    public String getDescripcion() { return descripcion; }

    public void setId(int id) { this.id = id; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    // ENCAPSULAMIENTO: el setter valida antes de guardar.
    public void setMonto(double monto) {
        if (monto > 0) {
            this.monto = Dinero.redondear(monto);
        }
    }
}
