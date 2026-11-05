package walletpro.vista;

import java.util.List;
import walletpro.excepciones.OperacionInvalidaException;
import walletpro.modelo.*;
import walletpro.util.Consola;
import walletpro.util.Dinero;

/**
 * ALUMNO 1 - Modulo de Ingresos y Ahorros.
 * Empresas, cargos, empleados, trabajos, sueldos y resumen de ahorro.
 */
public class MenuIngresos extends Menu {

    public MenuIngresos(Billetera billetera) {
        super("INGRESOS Y AHORROS", billetera);
    }

    @Override
    protected String[] getOpciones() {
        return new String[] {
            "Registrar empresa",
            "Registrar cargo",
            "Registrar empleado",
            "Registrar trabajo",
            "Registrar sueldo mensual",
            "Listar trabajos",
            "Consultar ingresos de un empleado",
            "Resumen de ahorro del mes"
        };
    }

    @Override
    protected void ejecutarOpcion(int opcion) {
        switch (opcion) {
            case 1: registrarEmpresa(); break;
            case 2: registrarCargo(); break;
            case 3: registrarEmpleado(); break;
            case 4: registrarTrabajo(); break;
            case 5: registrarSueldo(); break;
            case 6: listarTrabajos(); break;
            case 7: consultarIngresos(); break;
            case 8: resumenAhorro(); break;
        }
    }

    private void registrarEmpresa() {
        System.out.println("\n--- REGISTRAR EMPRESA ---");
        Empresa empresa = new Empresa();
        empresa.registrar();
        try {
            billetera.agregarEmpresa(empresa);
            System.out.println("Empresa registrada: " + empresa);
        } catch (OperacionInvalidaException e) {
            System.out.println("No se pudo registrar: " + e.getMessage());
        }
    }

    private void registrarCargo() {
        System.out.println("\n--- REGISTRAR CARGO ---");
        Cargo cargo = new Cargo();
        cargo.registrar();
        try {
            billetera.agregarCargo(cargo);
            System.out.println("Cargo registrado: " + cargo);
        } catch (OperacionInvalidaException e) {
            System.out.println("No se pudo registrar: " + e.getMessage());
        }
    }

    private void registrarEmpleado() {
        System.out.println("\n--- REGISTRAR EMPLEADO ---");
        if (billetera.getEmpresas().estaVacio() || billetera.getCargos().estaVacio()) {
            System.out.println("Primero registre al menos una empresa y un cargo.");
            return;
        }
        Empleado empleado = new Empleado();
        empleado.registrar();
        empleado.setEmpresa(Consola.seleccionar("Empresas:", billetera.getEmpresas().listar()));
        empleado.setCargo(Consola.seleccionar("Cargos:", billetera.getCargos().listar()));
        try {
            billetera.agregarEmpleado(empleado);
            System.out.println("Empleado registrado.\n" + empleado.resumen());
        } catch (OperacionInvalidaException e) {
            System.out.println("No se pudo registrar: " + e.getMessage());
        }
    }

    private void registrarTrabajo() {
        System.out.println("\n--- REGISTRAR TRABAJO ---");
        if (billetera.getEmpleados().estaVacio()) {
            System.out.println("Primero registre al menos un empleado.");
            return;
        }
        Trabajo trabajo = new Trabajo();
        trabajo.setEmpleado(Consola.seleccionar("Empleados:", billetera.getEmpleados().listar()));
        trabajo.setEmpresa(Consola.seleccionar("Empresas:", billetera.getEmpresas().listar()));
        trabajo.setCargo(Consola.seleccionar("Cargos:", billetera.getCargos().listar()));
        trabajo.registrar();
        try {
            billetera.agregarTrabajo(trabajo);
            System.out.println("Trabajo registrado.\n" + trabajo.resumen());
        } catch (OperacionInvalidaException e) {
            System.out.println("No se pudo registrar: " + e.getMessage());
        }
    }

    private void registrarSueldo() {
        System.out.println("\n--- REGISTRAR SUELDO MENSUAL ---");
        if (billetera.getEmpleados().estaVacio()) {
            System.out.println("Primero registre al menos un empleado.");
            return;
        }
        Empleado empleado = Consola.seleccionar("Empleados:", billetera.getEmpleados().listar());
        Sueldo sueldo = new Sueldo();
        sueldo.setEmpleado(empleado);
        sueldo.registrar();

        // El sueldo puede depositarse en una cuenta del mismo empleado.
        CuentaBancaria cuentaDestino = null;
        List<CuentaBancaria> cuentasEmpleado = billetera.cuentasDe(empleado);
        if (!cuentasEmpleado.isEmpty() && Consola.leerSiNo("Depositar el sueldo en una cuenta bancaria?")) {
            cuentaDestino = Consola.seleccionar("Cuentas de " + empleado.getNombreCompleto() + ":", cuentasEmpleado);
        }
        try {
            billetera.agregarSueldo(sueldo, cuentaDestino);
            String mensaje = "Sueldo registrado: " + sueldo.resumen();
            if (cuentaDestino != null) {
                mensaje += "\nDepositado en la cuenta " + cuentaDestino.getNumeroCuenta()
                        + ". Nuevo saldo: " + Dinero.formatear(cuentaDestino.getSaldo());
            }
            System.out.println(mensaje);
        } catch (OperacionInvalidaException e) {
            System.out.println("No se pudo registrar: " + e.getMessage());
        }
    }

    private void listarTrabajos() {
        System.out.println("\n--- LISTA DE TRABAJOS ---");
        List<Trabajo> trabajos = billetera.getTrabajos().listar();
        if (trabajos.isEmpty()) {
            System.out.println("No hay trabajos registrados.");
            return;
        }
        String mensaje = "Trabajos registrados:";
        for (int i = 0; i < trabajos.size(); i++) {
            mensaje += "\n" + (i + 1) + ". " + trabajos.get(i);
        }
        System.out.println(mensaje);
    }

    private void consultarIngresos() {
        System.out.println("\n--- CONSULTAR INGRESOS ---");
        if (billetera.getEmpleados().estaVacio()) {
            System.out.println("No hay empleados registrados.");
            return;
        }
        Empleado empleado = Consola.seleccionar("Empleados:", billetera.getEmpleados().listar());
        List<Sueldo> sueldos = billetera.sueldosDe(empleado);
        if (sueldos.isEmpty()) {
            System.out.println(empleado.getNombreCompleto() + " no tiene sueldos registrados.");
            return;
        }
        String mensaje = "Sueldos de " + empleado.getNombreCompleto() + ":";
        double total = 0;
        for (int i = 0; i < sueldos.size(); i++) {
            mensaje += "\n" + (i + 1) + ". " + sueldos.get(i).resumen();
            total += sueldos.get(i).getMonto();
        }
        mensaje += "\nTotal de ingresos: " + Dinero.formatear(total);
        mensaje += "\nPromedio mensual: " + Dinero.formatear(total / sueldos.size());
        System.out.println(mensaje);
    }

    /**
     * Ahorro del mes = ingresos (sueldos) - gastos. Muestra tambien en
     * que categorias se fue el dinero.
     */
    private void resumenAhorro() {
        System.out.println("\n--- RESUMEN DE AHORRO DEL MES ---");
        if (billetera.getEmpleados().estaVacio()) {
            System.out.println("No hay empleados registrados.");
            return;
        }
        Empleado empleado = Consola.seleccionar("Empleados:", billetera.getEmpleados().listar());
        int mes = Consola.leerEntero("Mes (1-12):", 1, 12);
        int anio = Consola.leerEntero("Anio (2000-2100):", 2000, 2100);

        double ingresos = billetera.totalIngresosDelMes(empleado, mes, anio);
        double gastos = billetera.totalGastosDelMes(empleado, mes, anio);
        double ahorro = Dinero.redondear(ingresos - gastos);

        String mensaje = String.format("Resumen de %s - %02d/%d", empleado.getNombreCompleto(), mes, anio);
        mensaje += String.format("%n%-22s %15s", "Ingresos (sueldos):", Dinero.formatear(ingresos));
        mensaje += String.format("%n%-22s %15s", "Gastos:", Dinero.formatear(gastos));
        mensaje += String.format("%n%-22s %15s", "Ahorro del mes:", Dinero.formatear(ahorro));
        if (ingresos > 0) {
            mensaje += String.format("%nPorcentaje ahorrado: %.1f%% de los ingresos", ahorro * 100 / ingresos);
        }
        if (ahorro < 0) {
            mensaje += "\nATENCION: este mes gasto mas de lo que gano.";
        }

        mensaje += "\nGastos por categoria:";
        List<CategoriaGasto> categorias = billetera.getCategorias().listar();
        for (int i = 0; i < categorias.size(); i++) {
            double totalCategoria = billetera.totalGastosDelMesPorCategoria(empleado, categorias.get(i), mes, anio);
            if (totalCategoria > 0) {
                mensaje += String.format("%n   %-20s %15s", categorias.get(i).getNombreCategoria(),
                        Dinero.formatear(totalCategoria));
            }
        }
        if (gastos == 0) {
            mensaje += "\n   (sin gastos registrados en este mes)";
        }
        System.out.println(mensaje);
    }
}
