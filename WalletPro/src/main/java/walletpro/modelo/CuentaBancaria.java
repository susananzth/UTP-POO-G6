package walletpro.modelo;

import walletpro.interfaces.IConsultable;
import walletpro.interfaces.OperacionFinanciera;
import walletpro.util.Consola;
import walletpro.util.Dinero;
import walletpro.util.Validacion;

/**
 * Cuenta de un empleado en un banco. Implementa OperacionFinanciera:
 * el saldo solo cambia con depositar() o retirar(), y cada uno crea un Movimiento.
 * Tabla: cuenta | DAO: CuentaDAO | Menu: MenuBancos (Alumno 3)
 *
 * Despues de depositar o retirar, quien llama debe guardar el cambio en la BD:
 *     Movimiento mov = cuenta.retirar(100, "Pago");
 *     if (mov != null) { cuentaDAO.guardarMovimiento(cuenta, mov); }
 */
public class CuentaBancaria implements OperacionFinanciera, IConsultable {

    private static final String[] TIPOS_CUENTA = {"Ahorros", "Corriente"};

    private String numeroCuenta = "";
    private String tipoCuenta = "";
    private double saldo;
    private Banco banco;
    private Empleado titular;

    // El banco y el titular se eligen antes en el menu (setters).
    // Lo llama: MenuBancos.registrarCuenta()
    public void registrar() {
        do {
            numeroCuenta = Consola.leerTexto("Numero de cuenta (6 a 20 digitos):");
            if (!Validacion.esNumeroCuentaValido(numeroCuenta)) {
                System.out.println("Debe tener solo digitos, entre 6 y 20.");
            }
        } while (!Validacion.esNumeroCuentaValido(numeroCuenta));

        String mensaje = "Tipo de cuenta:";
        for (int i = 0; i < TIPOS_CUENTA.length; i++) {
            mensaje += "\n" + (i + 1) + ". " + TIPOS_CUENTA[i];
        }
        System.out.println(mensaje);
        int opcion = Consola.leerEntero("Seleccione una opcion:", 1, TIPOS_CUENTA.length);
        tipoCuenta = TIPOS_CUENTA[opcion - 1];
    }

    // Suma al saldo y devuelve el movimiento creado.
    @Override
    public Movimiento depositar(double monto, String descripcion) {
        saldo = Dinero.redondear(saldo + monto);
        return crearMovimiento(Movimiento.DEPOSITO, monto, descripcion);
    }

    // Resta del saldo. Si no alcanza, NO cambia nada y devuelve null.
    @Override
    public Movimiento retirar(double monto, String descripcion) {
        if (monto > saldo) {
            return null;
        }
        saldo = Dinero.redondear(saldo - monto);
        return crearMovimiento(Movimiento.RETIRO, monto, descripcion);
    }

    // private: solo lo usa esta clase (depositar y retirar).
    private Movimiento crearMovimiento(String tipo, double monto, String descripcion) {
        Movimiento movimiento = new Movimiento();
        movimiento.setNumeroCuenta(numeroCuenta);
        movimiento.setFecha(Validacion.hoy());
        movimiento.setTipo(tipo);
        movimiento.setMonto(monto);
        movimiento.setDescripcion(descripcion);
        movimiento.setSaldoResultante(saldo);
        return movimiento;
    }

    @Override
    public String resumen() {
        String mensaje = "Cuenta " + numeroCuenta + " (" + tipoCuenta + ")";
        mensaje += "\nBanco: " + banco.getNombre();
        mensaje += "\nTitular: " + titular.getNombreCompleto();
        mensaje += "\nSaldo: " + Dinero.formatear(saldo);
        return mensaje;
    }

    @Override
    public String toString() {
        return numeroCuenta + " | " + banco.getNombre() + " | " + titular.getNombreCompleto()
                + " | Saldo: " + Dinero.formatear(saldo);
    }

    public String getNumeroCuenta() { return numeroCuenta; }
    public String getTipoCuenta() { return tipoCuenta; }
    public double getSaldo() { return saldo; }
    public Banco getBanco() { return banco; }
    public Empleado getTitular() { return titular; }

    public void setNumeroCuenta(String numeroCuenta) { this.numeroCuenta = numeroCuenta; }
    public void setTipoCuenta(String tipoCuenta) { this.tipoCuenta = tipoCuenta; }
    public void setBanco(Banco banco) { this.banco = banco; }
    public void setTitular(Empleado titular) { this.titular = titular; }

    // Solo lo usa CuentaDAO al leer la cuenta de la base de datos.
    // En el resto del programa el saldo cambia con depositar() y retirar().
    public void setSaldo(double saldo) { this.saldo = saldo; }
}
