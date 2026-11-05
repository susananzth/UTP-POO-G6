package walletpro.vista;

import java.util.List;
import walletpro.excepciones.OperacionInvalidaException;
import walletpro.modelo.*;
import walletpro.util.Consola;
import walletpro.util.Dinero;

/**
 * ALUMNO 3 - Modulo de Bancos y Prestamos.
 * Bancos, cuentas, depositos, retiros, historial y prestamos.
 */
public class MenuBancos extends Menu {

    public MenuBancos(Billetera billetera) {
        super("BANCOS Y PRESTAMOS", billetera);
    }

    @Override
    protected String[] getOpciones() {
        return new String[] {
            "Registrar banco",
            "Registrar cuenta bancaria",
            "Depositar en cuenta",
            "Retirar de cuenta",
            "Ver movimientos de una cuenta",
            "Registrar tipo de prestamo",
            "Registrar prestamo",
            "Pagar cuota de prestamo",
            "Listar prestamos"
        };
    }

    @Override
    protected void ejecutarOpcion(int opcion) {
        switch (opcion) {
            case 1: registrarBanco(); break;
            case 2: registrarCuenta(); break;
            case 3: depositar(); break;
            case 4: retirar(); break;
            case 5: verMovimientos(); break;
            case 6: registrarTipoPrestamo(); break;
            case 7: registrarPrestamo(); break;
            case 8: pagarCuota(); break;
            case 9: listarPrestamos(); break;
        }
    }

    private void registrarBanco() {
        System.out.println("\n--- REGISTRAR BANCO ---");
        Banco banco = new Banco();
        banco.registrar();
        try {
            billetera.agregarBanco(banco);
            System.out.println("Banco registrado: " + banco);
        } catch (OperacionInvalidaException e) {
            System.out.println("No se pudo registrar: " + e.getMessage());
        }
    }

    private void registrarCuenta() {
        System.out.println("\n--- REGISTRAR CUENTA BANCARIA ---");
        if (billetera.getBancos().estaVacio() || billetera.getEmpleados().estaVacio()) {
            System.out.println("Primero registre al menos un banco y un empleado (titular).");
            return;
        }
        CuentaBancaria cuenta = new CuentaBancaria();
        cuenta.setBanco(Consola.seleccionar("Bancos:", billetera.getBancos().listar()));
        cuenta.setTitular(Consola.seleccionar("Titular:", billetera.getEmpleados().listar()));
        cuenta.registrar();
        double saldoInicial = Consola.leerDecimal("Saldo inicial en S/ (0 si la cuenta empieza vacia):",
                0, Consola.MONTO_MAXIMO);
        try {
            billetera.agregarCuenta(cuenta, Dinero.redondear(saldoInicial));
            System.out.println("Cuenta registrada.\n" + cuenta.resumen());
        } catch (OperacionInvalidaException e) {
            System.out.println("No se pudo registrar: " + e.getMessage());
        }
    }

    private CuentaBancaria elegirCuenta() {
        if (billetera.getCuentas().estaVacio()) {
            System.out.println("No hay cuentas registradas.");
            return null;
        }
        return Consola.seleccionar("Cuentas:", billetera.getCuentas().listar());
    }

    private void depositar() {
        System.out.println("\n--- DEPOSITAR EN CUENTA ---");
        CuentaBancaria cuenta = elegirCuenta();
        if (cuenta == null) {
            return;
        }
        double monto = Consola.leerMonto("Monto a depositar (S/):");
        String descripcion = Consola.leerTextoOpcional("Descripcion (opcional, Enter para omitir):", "Deposito");
        try {
            cuenta.depositar(monto, descripcion);
            System.out.printf("Deposito realizado. Nuevo saldo: %s%n", Dinero.formatear(cuenta.getSaldo()));
        } catch (OperacionInvalidaException e) {
            System.out.println("No se pudo depositar: " + e.getMessage());
        }
    }

    private void retirar() {
        System.out.println("\n--- RETIRAR DE CUENTA ---");
        CuentaBancaria cuenta = elegirCuenta();
        if (cuenta == null) {
            return;
        }
        System.out.println("Saldo disponible: " + Dinero.formatear(cuenta.getSaldo()));
        double monto = Consola.leerMonto("Monto a retirar (S/):");
        String descripcion = Consola.leerTextoOpcional("Descripcion (opcional, Enter para omitir):", "Retiro");
        try {
            cuenta.retirar(monto, descripcion);
            System.out.printf("Retiro realizado. Nuevo saldo: %s%n", Dinero.formatear(cuenta.getSaldo()));
        } catch (OperacionInvalidaException e) {
            System.out.println("No se pudo retirar: " + e.getMessage());
        }
    }

    private void verMovimientos() {
        System.out.println("\n--- MOVIMIENTOS DE UNA CUENTA ---");
        CuentaBancaria cuenta = elegirCuenta();
        if (cuenta != null) {
            System.out.println(cuenta.historial());
        }
    }

    private void registrarTipoPrestamo() {
        System.out.println("\n--- REGISTRAR TIPO DE PRESTAMO ---");
        TipoPrestamo tipo = new TipoPrestamo();
        tipo.registrar();
        try {
            billetera.agregarTipoPrestamo(tipo);
            System.out.println("Tipo de prestamo registrado: " + tipo);
        } catch (OperacionInvalidaException e) {
            System.out.println("No se pudo registrar: " + e.getMessage());
        }
    }

    private void registrarPrestamo() {
        System.out.println("\n--- REGISTRAR PRESTAMO ---");
        if (billetera.getEmpleados().estaVacio() || billetera.getBancos().estaVacio()
                || billetera.getTiposPrestamo().estaVacio()) {
            System.out.println("Primero registre al menos un empleado, un banco y un tipo de prestamo.");
            return;
        }
        Prestamo prestamo = new Prestamo();
        prestamo.setTipoPrestamo(Consola.seleccionar("Tipos de prestamo:", billetera.getTiposPrestamo().listar()));
        prestamo.setBanco(Consola.seleccionar("Bancos:", billetera.getBancos().listar()));
        prestamo.setDeudor(Consola.seleccionar("Deudor:", billetera.getEmpleados().listar()));
        prestamo.registrar(billetera.siguienteCodigoPrestamo());

        // Opcional: el banco deposita el dinero prestado en una cuenta del deudor.
        CuentaBancaria cuentaDesembolso = null;
        List<CuentaBancaria> cuentasDeudor = billetera.cuentasDe(prestamo.getDeudor());
        if (!cuentasDeudor.isEmpty() && Consola.leerSiNo("Depositar el dinero prestado en una cuenta del deudor?")) {
            cuentaDesembolso = Consola.seleccionar("Cuentas del deudor:", cuentasDeudor);
        }
        try {
            billetera.agregarPrestamo(prestamo, cuentaDesembolso);
            System.out.println("Prestamo registrado:\n" + prestamo.resumen());
        } catch (OperacionInvalidaException e) {
            System.out.println("No se pudo registrar: " + e.getMessage());
        }
    }

    private void pagarCuota() {
        System.out.println("\n--- PAGAR CUOTA DE PRESTAMO ---");
        List<Prestamo> pendientes = billetera.prestamosPendientes();
        if (pendientes.isEmpty()) {
            System.out.println("No hay prestamos pendientes de pago.");
            return;
        }
        Prestamo prestamo = Consola.seleccionar("Prestamos pendientes:", pendientes);
        List<CuentaBancaria> cuentasDeudor = billetera.cuentasDe(prestamo.getDeudor());
        if (cuentasDeudor.isEmpty()) {
            System.out.println(prestamo.getDeudor().getNombreCompleto()
                    + " no tiene cuentas bancarias. Registre una para poder pagar.");
            return;
        }
        System.out.println("Cuota a pagar: " + Dinero.formatear(prestamo.calcularSiguienteCuota()));
        CuentaBancaria cuenta = Consola.seleccionar("Pagar desde la cuenta:", cuentasDeudor);
        try {
            double pagado = prestamo.pagarCuota(cuenta);
            String mensaje = "Pago realizado: " + Dinero.formatear(pagado);
            mensaje += "\nNuevo saldo de la cuenta: " + Dinero.formatear(cuenta.getSaldo());
            mensaje += "\n" + prestamo.resumen();
            if (prestamo.estaPagado()) {
                mensaje += "\nFelicidades, el prestamo quedo totalmente pagado.";
            }
            System.out.println(mensaje);
        } catch (OperacionInvalidaException e) {
            System.out.println("No se pudo pagar: " + e.getMessage());
        }
    }

    private void listarPrestamos() {
        System.out.println("\n--- LISTA DE PRESTAMOS ---");
        List<Prestamo> prestamos = billetera.getPrestamos().listar();
        if (prestamos.isEmpty()) {
            System.out.println("No hay prestamos registrados.");
            return;
        }
        String mensaje = "Prestamos registrados:";
        double deudaTotal = 0;
        for (int i = 0; i < prestamos.size(); i++) {
            mensaje += "\n" + (i + 1) + ". " + prestamos.get(i).resumen();
            deudaTotal += prestamos.get(i).getSaldoPendiente();
        }
        mensaje += "\nDeuda total pendiente: " + Dinero.formatear(deudaTotal);
        System.out.println(mensaje);
    }
}
