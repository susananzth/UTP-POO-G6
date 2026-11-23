package walletpro.vista;

import java.util.ArrayList;
import java.util.Iterator;
import walletpro.dao.BancoDAO;
import walletpro.dao.CuentaDAO;
import walletpro.dao.EmpleadoDAO;
import walletpro.dao.PrestamoDAO;
import walletpro.dao.TipoPrestamoDAO;
import walletpro.modelo.Banco;
import walletpro.modelo.CuentaBancaria;
import walletpro.modelo.Empleado;
import walletpro.modelo.Movimiento;
import walletpro.modelo.Prestamo;
import walletpro.modelo.TipoPrestamo;
import walletpro.util.Consola;
import walletpro.util.Dinero;
import walletpro.util.Paginador;

/**
 * ALUMNO 3 - Modulo de Bancos y Prestamos.
 * Lo llama: MenuPrincipal (opcion 3).
 *
 * Todo cambio de saldo sigue 2 pasos:
 *   1. cuenta.depositar(...) o cuenta.retirar(...)  -> cambia el saldo en el objeto
 *   2. cuentaDAO.guardarMovimiento(cuenta, mov)      -> lo guarda en SQL Server
 */
public class MenuBancos {

    private BancoDAO bancoDAO = new BancoDAO();
    private CuentaDAO cuentaDAO = new CuentaDAO();
    private EmpleadoDAO empleadoDAO = new EmpleadoDAO();
    private TipoPrestamoDAO tipoPrestamoDAO = new TipoPrestamoDAO();
    private PrestamoDAO prestamoDAO = new PrestamoDAO();

    public void mostrar() {
        int opcion;
        do {
            String mensaje = "\n========== BANCOS Y PRESTAMOS ==========";
            mensaje += "\n1. Registrar banco";
            mensaje += "\n2. Registrar cuenta bancaria";
            mensaje += "\n3. Depositar en una cuenta";
            mensaje += "\n4. Retirar de una cuenta";
            mensaje += "\n5. Ver movimientos de una cuenta";
            mensaje += "\n6. Registrar tipo de prestamo";
            mensaje += "\n7. Registrar prestamo";
            mensaje += "\n8. Pagar cuota de un prestamo";
            mensaje += "\n9. Listar prestamos";
            mensaje += "\n0. Volver";
            System.out.println(mensaje);

            opcion = Consola.leerEntero("Seleccione una opcion:", 0, 9);
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
        } while (opcion != 0);
    }

    private void registrarBanco() {
        System.out.println("\n--- REGISTRAR BANCO ---");
        Banco banco = new Banco();
        banco.registrar();
        if (bancoDAO.insertar(banco)) {
            System.out.println("Banco registrado: " + banco);
        }
    }

    // La cuenta se crea con saldo 0. El saldo inicial entra como un deposito (queda en el historial).
    private void registrarCuenta() {
        System.out.println("\n--- REGISTRAR CUENTA BANCARIA ---");
        ArrayList<Banco> bancos = bancoDAO.listar();
        ArrayList<Empleado> empleados = empleadoDAO.listar();
        if (bancos.isEmpty() || empleados.isEmpty()) {
            System.out.println("Primero registre al menos un banco y un empleado.");
            return;
        }
        CuentaBancaria cuenta = new CuentaBancaria();
        cuenta.setBanco(Consola.seleccionar("Bancos:", bancos));
        cuenta.setTitular(Consola.seleccionar("Titular:", empleados));
        cuenta.registrar();
        double saldoInicial = Consola.leerDecimal("Saldo inicial en S/ (0 si empieza vacia):", 0, Consola.MONTO_MAXIMO);

        if (!cuentaDAO.insertar(cuenta)) {
            return;   // por ejemplo, el numero de cuenta ya existe
        }
        if (saldoInicial > 0) {
            Movimiento movimiento = cuenta.depositar(Dinero.redondear(saldoInicial), "Saldo inicial");
            cuentaDAO.guardarMovimiento(cuenta, movimiento);
        }
        System.out.println("Cuenta registrada.\n" + cuenta.resumen());
    }

    private void depositar() {
        System.out.println("\n--- DEPOSITAR ---");
        CuentaBancaria cuenta = elegirCuenta(cuentaDAO.listar());
        if (cuenta == null) {
            return;
        }
        double monto = Consola.leerMonto("Monto a depositar (S/):");
        String descripcion = Consola.leerTextoOpcional("Descripcion (Enter para omitir):", "Deposito");
        Movimiento movimiento = cuenta.depositar(monto, descripcion);
        if (cuentaDAO.guardarMovimiento(cuenta, movimiento)) {
            System.out.println("Deposito realizado. Nuevo saldo: " + Dinero.formatear(cuenta.getSaldo()));
        }
    }

    private void retirar() {
        System.out.println("\n--- RETIRAR ---");
        CuentaBancaria cuenta = elegirCuenta(cuentaDAO.listar());
        if (cuenta == null) {
            return;
        }
        System.out.println("Saldo disponible: " + Dinero.formatear(cuenta.getSaldo()));
        double monto = Consola.leerMonto("Monto a retirar (S/):");
        String descripcion = Consola.leerTextoOpcional("Descripcion (Enter para omitir):", "Retiro");
        Movimiento movimiento = cuenta.retirar(monto, descripcion);
        if (movimiento == null) {
            System.out.println("Saldo insuficiente. No se retiro nada.");
            return;
        }
        if (cuentaDAO.guardarMovimiento(cuenta, movimiento)) {
            System.out.println("Retiro realizado. Nuevo saldo: " + Dinero.formatear(cuenta.getSaldo()));
        }
    }

    // Muestra el historial de 5 en 5 con la clase generica Paginador.
    private void verMovimientos() {
        System.out.println("\n--- MOVIMIENTOS DE UNA CUENTA ---");
        CuentaBancaria cuenta = elegirCuenta(cuentaDAO.listar());
        if (cuenta == null) {
            return;
        }
        ArrayList<Movimiento> movimientos = cuentaDAO.listarMovimientos(cuenta);
        if (movimientos.isEmpty()) {
            System.out.println("La cuenta no tiene movimientos.");
            return;
        }
        // SEQUENCED COLLECTIONS (Java 21): getLast() devuelve el ultimo elemento de la lista.
        System.out.println("Ultimo movimiento: " + movimientos.getLast().resumen());

        Paginador<Movimiento> paginador = new Paginador<>();
        paginador.setElementos(movimientos);
        paginador.setTamanioPagina(5);
        int pagina = 1;
        boolean seguir = true;
        while (seguir) {
            String titulo = "Pagina " + pagina + " de " + paginador.getTotalPaginas() + ":";
            Consola.mostrarLista(titulo, paginador.getPagina(pagina));
            pagina++;
            seguir = pagina <= paginador.getTotalPaginas() && Consola.leerSiNo("Ver la siguiente pagina?");
        }
        System.out.println("Saldo actual: " + Dinero.formatear(cuenta.getSaldo()));
    }

    private void registrarTipoPrestamo() {
        System.out.println("\n--- REGISTRAR TIPO DE PRESTAMO ---");
        TipoPrestamo tipo = new TipoPrestamo();
        tipo.registrar();
        if (tipoPrestamoDAO.insertar(tipo)) {
            System.out.println("Tipo de prestamo registrado: " + tipo);
        }
    }

    private void registrarPrestamo() {
        System.out.println("\n--- REGISTRAR PRESTAMO ---");
        ArrayList<TipoPrestamo> tipos = tipoPrestamoDAO.listar();
        ArrayList<Banco> bancos = bancoDAO.listar();
        ArrayList<Empleado> empleados = empleadoDAO.listar();
        if (tipos.isEmpty() || bancos.isEmpty() || empleados.isEmpty()) {
            System.out.println("Primero registre al menos un tipo de prestamo, un banco y un empleado.");
            return;
        }
        Prestamo prestamo = new Prestamo();
        prestamo.setTipo(Consola.seleccionar("Tipos de prestamo:", tipos));
        prestamo.setBanco(Consola.seleccionar("Bancos:", bancos));
        prestamo.setDeudor(Consola.seleccionar("Deudor:", empleados));
        prestamo.registrar();
        if (!prestamoDAO.insertar(prestamo)) {
            return;
        }
        System.out.println("Prestamo registrado. Total a pagar: " + Dinero.formatear(prestamo.calcularTotalAPagar())
                + " en " + prestamo.getNumeroCuotas() + " cuotas de " + Dinero.formatear(prestamo.calcularMontoCuota()));

        // Opcional: el banco deposita el dinero prestado en una cuenta del deudor.
        ArrayList<CuentaBancaria> cuentas = cuentaDAO.listarDe(prestamo.getDeudor());
        if (!cuentas.isEmpty() && Consola.leerSiNo("Depositar el dinero prestado en una cuenta del deudor?")) {
            CuentaBancaria cuenta = Consola.seleccionar("Cuentas:", cuentas);
            Movimiento movimiento = cuenta.depositar(prestamo.getMonto(), "Desembolso de prestamo");
            if (cuentaDAO.guardarMovimiento(cuenta, movimiento)) {
                System.out.println("Depositado. Nuevo saldo: " + Dinero.formatear(cuenta.getSaldo()));
            }
        }
    }

    private void pagarCuota() {
        System.out.println("\n--- PAGAR CUOTA ---");
        // ITERATOR (semana 12): recorre la lista y QUITA los prestamos ya pagados.
        // Con for-each no se puede borrar mientras se recorre; con iterador.remove() si.
        ArrayList<Prestamo> pendientes = prestamoDAO.listar();
        Iterator<Prestamo> iterador = pendientes.iterator();
        while (iterador.hasNext()) {
            if (iterador.next().estaPagado()) {
                iterador.remove();
            }
        }
        if (pendientes.isEmpty()) {
            System.out.println("No hay prestamos pendientes.");
            return;
        }
        Prestamo prestamo = Consola.seleccionar("Prestamos pendientes:", pendientes);

        ArrayList<CuentaBancaria> cuentas = cuentaDAO.listarDe(prestamo.getDeudor());
        CuentaBancaria cuenta = elegirCuenta(cuentas);
        if (cuenta == null) {
            return;
        }
        double cuota = prestamo.calcularSiguienteCuota();
        System.out.println("Cuota a pagar: " + Dinero.formatear(cuota));
        String descripcion = "Cuota " + (prestamo.getCuotasPagadas() + 1) + "/" + prestamo.getNumeroCuotas()
                + " del prestamo #" + prestamo.getIdPrestamo();

        Movimiento movimiento = cuenta.retirar(cuota, descripcion);
        if (movimiento == null) {
            System.out.println("Saldo insuficiente (" + Dinero.formatear(cuenta.getSaldo()) + "). No se pago.");
            return;
        }
        prestamo.registrarPago(cuota);
        if (cuentaDAO.guardarMovimiento(cuenta, movimiento) && prestamoDAO.actualizarPago(prestamo)) {
            String mensaje = "Pago realizado. Nuevo saldo de la cuenta: " + Dinero.formatear(cuenta.getSaldo());
            mensaje += "\n" + prestamo.resumen();
            if (prestamo.estaPagado()) {
                mensaje += "\nFelicidades, el prestamo quedo pagado.";
            }
            System.out.println(mensaje);
        }
    }

    private void listarPrestamos() {
        ArrayList<Prestamo> prestamos = prestamoDAO.listar();
        // LAMBDA como Comparator: ordena del que mas debe al que menos debe.
        prestamos.sort((a, b) -> Double.compare(b.getSaldoPendiente(), a.getSaldoPendiente()));
        Consola.mostrarLista("\n--- PRESTAMOS (del que mas debe al que menos) ---", prestamos);

        double deudaTotal = prestamos.stream().mapToDouble(p -> p.getSaldoPendiente()).sum();
        System.out.println("Deuda total pendiente: " + Dinero.formatear(deudaTotal));
    }

    // Muestra las cuentas y devuelve la elegida (null si no hay ninguna).
    private CuentaBancaria elegirCuenta(ArrayList<CuentaBancaria> cuentas) {
        if (cuentas.isEmpty()) {
            System.out.println("No hay cuentas registradas para esta operacion.");
            return null;
        }
        return Consola.seleccionar("Cuentas:", cuentas);
    }
}
