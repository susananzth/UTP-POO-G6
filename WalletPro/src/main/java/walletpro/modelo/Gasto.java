package walletpro.modelo;

import walletpro.interfaces.*;
import walletpro.util.*;

/**
 * ALUMNO 2 - Registrar Gasto. Hereda de Transaccion (fecha, monto,
 * descripcion) y agrega la categoria, la persona que gasta y si es
 * un gasto ocasional o fijo.
 */
public class Gasto extends Transaccion {

    private CategoriaGasto categoria;
    private boolean esOcasional;
    private boolean montoVariable;
    private Empleado persona;
    /** Cuenta con la que se pago. Si es null, se pago en efectivo. */
    private CuentaBancaria cuentaPago;

    public Gasto() {
        super();
        this.categoria = null;
        this.esOcasional = false;
        this.montoVariable = false;
        this.persona = null;
        this.cuentaPago = null;
    }

    public void registrar(String idTransaccion) {
        registrarDatosTransaccion(idTransaccion);
        this.esOcasional = Consola.leerSiNo("Es un gasto ocasional (no se repite)?");
        if (!esOcasional) {
            this.montoVariable = Consola.leerSiNo("El monto cambia cada mes?");
        }
    }

    public CategoriaGasto getCategoria() { return categoria; }
    public boolean isEsOcasional() { return esOcasional; }
    public boolean isMontoVariable() { return montoVariable; }
    public Empleado getPersona() { return persona; }
    public CuentaBancaria getCuentaPago() { return cuentaPago; }

    public void setCategoria(CategoriaGasto categoria) { this.categoria = categoria; }
    public void setPersona(Empleado persona) { this.persona = persona; }
    public void setCuentaPago(CuentaBancaria cuentaPago) { this.cuentaPago = cuentaPago; }

    public String getMedioPago() {
        return (cuentaPago != null) ? "Cuenta " + cuentaPago.getNumeroCuenta() : "Efectivo";
    }
    public void setEsOcasional(boolean esOcasional) { this.esOcasional = esOcasional; }
    public void setMontoVariable(boolean montoVariable) { this.montoVariable = montoVariable; }

    /** Para precargar datos de prueba sin pasar por el teclado. */
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
        return super.resumen() + " | " + nombreCategoria + " | " + nombrePersona + " | " + getMedioPago();
    }
}
