package walletpro.modelo;

import walletpro.util.Consola;

/**
 * Un gasto de un empleado. HEREDA de Transaccion (fecha, monto, descripcion).
 * Tabla: gasto | DAO: GastoDAO | Menu: MenuGastos (Alumno 2)
 */
public class Gasto extends Transaccion {

    private Empleado empleado;
    private CategoriaGasto categoria;
    private boolean esOcasional;
    private String numeroCuentaPago;   // null = se pago en efectivo

    // El empleado y la categoria se eligen antes en el menu (setters).
    // Lo llama: MenuGastos.registrarGasto()
    public void registrar() {
        registrarDatosTransaccion();   // metodo heredado de Transaccion
        esOcasional = Consola.leerSiNo("Es un gasto ocasional (no se repite cada mes)?");
    }

    // POLIMORFISMO: version de Gasto del metodo abstracto de Transaccion
    @Override
    public String getTipo() {
        if (esOcasional) {
            return "OCASIONAL";
        }
        return "FIJO";
    }

    public String getMedioPago() {
        if (numeroCuentaPago == null) {
            return "Efectivo";
        }
        return "Cuenta " + numeroCuentaPago;
    }

    // SOBRESCRITURA: usa el resumen del padre (super) y le agrega datos.
    @Override
    public String resumen() {
        return super.resumen() + " | " + categoria.getNombre() + " | " + getMedioPago();
    }

    public Empleado getEmpleado() { return empleado; }
    public CategoriaGasto getCategoria() { return categoria; }
    public boolean isEsOcasional() { return esOcasional; }
    public String getNumeroCuentaPago() { return numeroCuentaPago; }

    public void setEmpleado(Empleado empleado) { this.empleado = empleado; }
    public void setCategoria(CategoriaGasto categoria) { this.categoria = categoria; }
    public void setEsOcasional(boolean esOcasional) { this.esOcasional = esOcasional; }
    public void setNumeroCuentaPago(String numeroCuentaPago) { this.numeroCuentaPago = numeroCuentaPago; }
}
