package walletpro.vista;

import java.util.List;
import walletpro.excepciones.OperacionInvalidaException;
import walletpro.modelo.*;
import walletpro.util.Consola;
import walletpro.util.Dinero;
import walletpro.util.ValidFecha;

/**
 * ALUMNO 2 - Modulo de Gastos y Presupuesto.
 *
 * Control del presupuesto al registrar un gasto:
 *   - Si con el gasto se pasa el limite: se avisa cuanto se excede y
 *     se pregunta si igual se quiere registrar (en la vida real el gasto
 *     ya ocurrio, por eso no se prohibe, pero el usuario queda advertido).
 *   - Si con el gasto se llega al 80% o mas: se muestra una alerta.
 *   - Si no hay presupuesto para esa categoria y mes: se informa.
 */
public class MenuGastos extends Menu {

    public MenuGastos(Billetera billetera) {
        super("GASTOS Y PRESUPUESTO", billetera);
    }

    @Override
    protected String[] getOpciones() {
        return new String[] {
            "Registrar categoria de gasto",
            "Registrar gasto",
            "Registrar presupuesto mensual",
            "Modificar monto de un presupuesto",
            "Listar gastos de un mes",
            "Consultar estado de presupuestos"
        };
    }

    @Override
    protected void ejecutarOpcion(int opcion) {
        switch (opcion) {
            case 1: registrarCategoria(); break;
            case 2: registrarGasto(); break;
            case 3: registrarPresupuesto(); break;
            case 4: modificarPresupuesto(); break;
            case 5: listarGastosDelMes(); break;
            case 6: consultarPresupuestos(); break;
        }
    }

    private void registrarCategoria() {
        System.out.println("\n--- REGISTRAR CATEGORIA DE GASTO ---");
        CategoriaGasto categoria = new CategoriaGasto();
        categoria.registrar();
        try {
            billetera.agregarCategoria(categoria);
            System.out.println("Categoria registrada: " + categoria);
        } catch (OperacionInvalidaException e) {
            System.out.println("No se pudo registrar: " + e.getMessage());
        }
    }

    private void registrarGasto() {
        System.out.println("\n--- REGISTRAR GASTO ---");
        if (billetera.getEmpleados().estaVacio() || billetera.getCategorias().estaVacio()) {
            System.out.println("Primero registre al menos un empleado y una categoria.");
            return;
        }
        Empleado persona = Consola.seleccionar("Empleados:", billetera.getEmpleados().listar());
        CategoriaGasto categoria = Consola.seleccionar("Categorias:", billetera.getCategorias().listar());

        Gasto gasto = new Gasto();
        gasto.setPersona(persona);
        gasto.setCategoria(categoria);
        gasto.registrar(billetera.siguienteCodigoGasto());

        if (!revisarPresupuesto(gasto)) {
            System.out.println("Gasto cancelado. No se registro nada.");
            return;
        }

        CuentaBancaria cuentaPago = elegirMedioDePago(persona);
        try {
            billetera.agregarGasto(gasto, cuentaPago);
            String mensaje = "Gasto registrado:\n" + gasto.resumen();
            if (cuentaPago != null) {
                mensaje += "\nNuevo saldo de la cuenta " + cuentaPago.getNumeroCuenta() + ": "
                        + Dinero.formatear(cuentaPago.getSaldo());
            }
            System.out.println(mensaje);
        } catch (OperacionInvalidaException e) {
            System.out.println("No se pudo registrar el gasto: " + e.getMessage());
        }
    }

    /**
     * Compara el gasto con el presupuesto de su categoria y mes.
     * Devuelve false si el usuario decide cancelar el gasto.
     */
    private boolean revisarPresupuesto(Gasto gasto) {
        int mes = ValidFecha.obtenerMes(gasto.getFecha());
        int anio = ValidFecha.obtenerAnio(gasto.getFecha());
        Presupuesto presupuesto = billetera.buscarPresupuesto(gasto.getPersona(), gasto.getCategoria(), mes, anio);
        if (presupuesto == null) {
            System.out.println("Aviso: no hay presupuesto de " + gasto.getCategoria().getNombreCategoria()
                    + " para " + String.format("%02d/%d", mes, anio) + ".");
            return true;
        }

        double gastadoAntes = billetera.calcularGastado(presupuesto);
        double gastadoDespues = Dinero.redondear(gastadoAntes + gasto.getMonto());
        double limite = presupuesto.getMontoPresupuestado();

        if (gastadoDespues > limite) {
            String mensaje = "ATENCION: este gasto EXCEDE su presupuesto de "
                    + presupuesto.getCategoria().getNombreCategoria() + " (" + presupuesto.getPeriodo() + ").";
            mensaje += "\n   Limite:              " + Dinero.formatear(limite);
            mensaje += "\n   Gastado hasta ahora: " + Dinero.formatear(gastadoAntes);
            mensaje += "\n   Con este gasto:      " + Dinero.formatear(gastadoDespues);
            mensaje += "\n   Exceso:              " + Dinero.formatear(gastadoDespues - limite);
            System.out.println(mensaje);
            return Consola.leerSiNo("Desea registrarlo de todos modos?");
        }

        double porcentaje = presupuesto.calcularPorcentajeUsado(gastadoDespues);
        if (porcentaje >= Presupuesto.PORCENTAJE_ALERTA) {
            System.out.printf("ALERTA: con este gasto habra usado el %.1f%% de su presupuesto. Le quedaran %s.%n",
                    porcentaje, Dinero.formatear(presupuesto.calcularDisponible(gastadoDespues)));
        } else {
            System.out.printf("Presupuesto OK: habra usado el %.1f%%. Le quedaran %s.%n",
                    porcentaje, Dinero.formatear(presupuesto.calcularDisponible(gastadoDespues)));
        }
        return true;
    }

    /** Devuelve la cuenta con la que se paga, o null si se paga en efectivo. */
    private CuentaBancaria elegirMedioDePago(Empleado persona) {
        List<CuentaBancaria> cuentas = billetera.cuentasDe(persona);
        if (cuentas.isEmpty()) {
            System.out.println("Medio de pago: Efectivo (" + persona.getNombres() + " no tiene cuentas registradas).");
            return null;
        }
        System.out.println("Medio de pago:\n1. Efectivo\n2. Cuenta bancaria");
        int opcion = Consola.leerEntero("Seleccione una opcion:", 1, 2);
        if (opcion == 1) {
            return null;
        }
        return Consola.seleccionar("Cuentas de " + persona.getNombreCompleto() + ":", cuentas);
    }

    private void registrarPresupuesto() {
        System.out.println("\n--- REGISTRAR PRESUPUESTO MENSUAL ---");
        if (billetera.getEmpleados().estaVacio() || billetera.getCategorias().estaVacio()) {
            System.out.println("Primero registre al menos un empleado y una categoria.");
            return;
        }
        Presupuesto presupuesto = new Presupuesto();
        presupuesto.setEmpleado(Consola.seleccionar("Empleados:", billetera.getEmpleados().listar()));
        presupuesto.setCategoria(Consola.seleccionar("Categorias:", billetera.getCategorias().listar()));
        presupuesto.registrar();
        try {
            billetera.agregarPresupuesto(presupuesto);
            double gastado = billetera.calcularGastado(presupuesto);
            System.out.println("Presupuesto registrado:\n" + presupuesto.resumen()
                    + "\nYa gastado en ese mes: " + Dinero.formatear(gastado)
                    + " | Estado: " + presupuesto.calcularEstado(gastado));
        } catch (OperacionInvalidaException e) {
            System.out.println("No se pudo registrar: " + e.getMessage());
        }
    }

    private void modificarPresupuesto() {
        System.out.println("\n--- MODIFICAR PRESUPUESTO ---");
        if (billetera.getPresupuestos().estaVacio()) {
            System.out.println("No hay presupuestos registrados.");
            return;
        }
        Presupuesto presupuesto = Consola.seleccionar("Presupuestos:", billetera.getPresupuestos().listar());
        presupuesto.setMontoPresupuestado(Consola.leerMonto("Nuevo monto maximo (S/):"));
        double gastado = billetera.calcularGastado(presupuesto);
        System.out.println("Presupuesto actualizado:\n" + presupuesto.resumen()
                + "\nEstado: " + presupuesto.calcularEstado(gastado));
    }

    private void listarGastosDelMes() {
        System.out.println("\n--- GASTOS DEL MES ---");
        if (billetera.getEmpleados().estaVacio()) {
            System.out.println("No hay empleados registrados.");
            return;
        }
        Empleado persona = Consola.seleccionar("Empleados:", billetera.getEmpleados().listar());
        int mes = Consola.leerEntero("Mes (1-12):", 1, 12);
        int anio = Consola.leerEntero("Anio (2000-2100):", 2000, 2100);

        List<Gasto> gastos = billetera.gastosDelMes(persona, mes, anio);
        if (gastos.isEmpty()) {
            System.out.println("No hay gastos de " + persona.getNombreCompleto()
                    + " en " + String.format("%02d/%d", mes, anio) + ".");
            return;
        }
        String mensaje = "Gastos de " + persona.getNombreCompleto() + " en " + String.format("%02d/%d", mes, anio) + ":";
        double total = 0;
        for (int i = 0; i < gastos.size(); i++) {
            mensaje += "\n" + gastos.get(i).resumen();
            total += gastos.get(i).getMonto();
        }
        mensaje += "\nTotal gastado: " + Dinero.formatear(total);
        System.out.println(mensaje);
    }

    private void consultarPresupuestos() {
        System.out.println("\n--- ESTADO DE PRESUPUESTOS ---");
        List<Presupuesto> presupuestos = billetera.getPresupuestos().listar();
        if (presupuestos.isEmpty()) {
            System.out.println("No hay presupuestos registrados.");
            return;
        }
        String mensaje = String.format("%-7s | %-16s | %-20s | %13s | %13s | %14s | %6s | %s",
                "Periodo", "Categoria", "Empleado", "Limite", "Gastado", "Disponible", "Uso", "Estado");
        for (int i = 0; i < presupuestos.size(); i++) {
            Presupuesto p = presupuestos.get(i);
            double gastado = billetera.calcularGastado(p);
            mensaje += String.format("%n%-7s | %-16s | %-20s | %13s | %13s | %14s | %5.1f%% | %s",
                    p.getPeriodo(), p.getCategoria().getNombreCategoria(), p.getEmpleado().getNombreCompleto(),
                    Dinero.formatear(p.getMontoPresupuestado()), Dinero.formatear(gastado),
                    Dinero.formatear(p.calcularDisponible(gastado)), p.calcularPorcentajeUsado(gastado),
                    p.calcularEstado(gastado));
        }
        mensaje += "\nOK = menos del 80% usado | ALERTA = 80% o mas | EXCEDIDO = se paso del limite";
        System.out.println(mensaje);
    }
}
