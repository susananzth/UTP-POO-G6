package walletpro.vista;

import java.util.ArrayList;
import walletpro.dao.CategoriaDAO;
import walletpro.dao.CuentaDAO;
import walletpro.dao.EmpleadoDAO;
import walletpro.dao.GastoDAO;
import walletpro.dao.PresupuestoDAO;
import walletpro.modelo.CategoriaGasto;
import walletpro.modelo.CuentaBancaria;
import walletpro.modelo.Empleado;
import walletpro.modelo.Gasto;
import walletpro.modelo.Movimiento;
import walletpro.modelo.Presupuesto;
import walletpro.util.ComparadorGastoPorMonto;
import walletpro.util.Consola;
import walletpro.util.Dinero;
import walletpro.util.Validacion;

/**
 * ALUMNO 2 - Modulo de Gastos y Presupuesto.
 * Lo llama: MenuPrincipal (opcion 2).
 *
 * Al registrar un gasto se revisa el presupuesto de esa categoria y mes:
 *   - Si con el gasto se pasa del limite: avisa cuanto se excede y pregunta si igual lo registra.
 *   - Si llega al 80% o mas: muestra una ALERTA.
 *   - Si no hay presupuesto: solo avisa.
 */
public class MenuGastos {

    private EmpleadoDAO empleadoDAO = new EmpleadoDAO();
    private CategoriaDAO categoriaDAO = new CategoriaDAO();
    private GastoDAO gastoDAO = new GastoDAO();
    private PresupuestoDAO presupuestoDAO = new PresupuestoDAO();
    private CuentaDAO cuentaDAO = new CuentaDAO();

    public void mostrar() {
        int opcion;
        do {
            String mensaje = "\n========== GASTOS Y PRESUPUESTO ==========";
            mensaje += "\n1. Registrar categoria de gasto";
            mensaje += "\n2. Registrar gasto";
            mensaje += "\n3. Registrar presupuesto mensual";
            mensaje += "\n4. Modificar monto de un presupuesto";
            mensaje += "\n5. Eliminar un presupuesto";
            mensaje += "\n6. Listar gastos de un mes";
            mensaje += "\n7. Estado de los presupuestos";
            mensaje += "\n0. Volver";
            System.out.println(mensaje);

            opcion = Consola.leerEntero("Seleccione una opcion:", 0, 7);
            switch (opcion) {
                case 1: registrarCategoria(); break;
                case 2: registrarGasto(); break;
                case 3: registrarPresupuesto(); break;
                case 4: modificarPresupuesto(); break;
                case 5: eliminarPresupuesto(); break;
                case 6: listarGastosDelMes(); break;
                case 7: estadoPresupuestos(); break;
            }
        } while (opcion != 0);
    }

    private void registrarCategoria() {
        System.out.println("\n--- REGISTRAR CATEGORIA ---");
        CategoriaGasto categoria = new CategoriaGasto();
        categoria.registrar();
        if (categoriaDAO.insertar(categoria)) {
            System.out.println("Categoria registrada: " + categoria);
        }
    }

    private void registrarGasto() {
        System.out.println("\n--- REGISTRAR GASTO ---");
        ArrayList<Empleado> empleados = empleadoDAO.listar();
        ArrayList<CategoriaGasto> categorias = categoriaDAO.listar();
        if (empleados.isEmpty() || categorias.isEmpty()) {
            System.out.println("Primero registre al menos un empleado y una categoria.");
            return;
        }
        Gasto gasto = new Gasto();
        gasto.setEmpleado(Consola.seleccionar("Empleados:", empleados));
        gasto.setCategoria(Consola.seleccionar("Categorias:", categorias));
        gasto.registrar();

        // 1. Revisar el presupuesto. Si el usuario decide no seguir, se cancela.
        if (!revisarPresupuesto(gasto)) {
            System.out.println("Gasto cancelado.");
            return;
        }

        // 2. Medio de pago: efectivo o una cuenta del mismo empleado.
        CuentaBancaria cuenta = null;
        Movimiento movimiento = null;
        ArrayList<CuentaBancaria> cuentas = cuentaDAO.listarDe(gasto.getEmpleado());
        if (!cuentas.isEmpty() && Consola.leerSiNo("Pagar con una cuenta bancaria? (N = efectivo)")) {
            cuenta = Consola.seleccionar("Cuentas:", cuentas);
            movimiento = cuenta.retirar(gasto.getMonto(), "Gasto: " + gasto.getDescripcion());
            if (movimiento == null) {
                System.out.println("Saldo insuficiente (" + Dinero.formatear(cuenta.getSaldo()) + "). Gasto cancelado.");
                return;
            }
            gasto.setNumeroCuentaPago(cuenta.getNumeroCuenta());
        }

        // 3. Guardar el gasto y, si se pago con cuenta, el nuevo saldo.
        if (gastoDAO.insertar(gasto)) {
            String mensaje = "Gasto registrado: " + gasto.resumen();
            if (movimiento != null && cuentaDAO.guardarMovimiento(cuenta, movimiento)) {
                mensaje += "\nNuevo saldo de la cuenta: " + Dinero.formatear(cuenta.getSaldo());
            }
            System.out.println(mensaje);
        }
    }

    // Compara el gasto con su presupuesto. Devuelve false si el usuario decide cancelar.
    private boolean revisarPresupuesto(Gasto gasto) {
        int mes = Validacion.mesDe(gasto.getFecha());
        int anio = Validacion.anioDe(gasto.getFecha());
        Presupuesto presupuesto = presupuestoDAO.buscar(gasto.getEmpleado(), gasto.getCategoria(), mes, anio);
        if (presupuesto == null) {
            System.out.println("Aviso: no hay presupuesto de " + gasto.getCategoria().getNombre()
                    + " para " + String.format("%02d/%d", mes, anio) + ".");
            return true;
        }

        double gastadoAntes = calcularGastado(presupuesto);
        double gastadoDespues = Dinero.redondear(gastadoAntes + gasto.getMonto());
        double limite = presupuesto.getMontoLimite();

        if (gastadoDespues > limite) {
            String mensaje = "ATENCION: este gasto EXCEDE el presupuesto de " + gasto.getCategoria().getNombre();
            mensaje += "\n   Limite:              " + Dinero.formatear(limite);
            mensaje += "\n   Gastado hasta ahora: " + Dinero.formatear(gastadoAntes);
            mensaje += "\n   Con este gasto:      " + Dinero.formatear(gastadoDespues);
            mensaje += "\n   Se excede en:        " + Dinero.formatear(gastadoDespues - limite);
            System.out.println(mensaje);
            return Consola.leerSiNo("Desea registrarlo de todos modos?");
        }

        double porcentaje = presupuesto.calcularPorcentajeUsado(gastadoDespues);
        String disponible = Dinero.formatear(presupuesto.calcularDisponible(gastadoDespues));
        if (porcentaje >= Presupuesto.PORCENTAJE_ALERTA) {
            System.out.printf("ALERTA: con este gasto usara el %.1f%% del presupuesto. Le quedaran %s.%n", porcentaje, disponible);
        } else {
            System.out.printf("Presupuesto OK: usara el %.1f%%. Le quedaran %s.%n", porcentaje, disponible);
        }
        return true;
    }

    // Suma lo gastado en la categoria y el mes de un presupuesto.
    // STREAM: filter deja solo los gastos de esa categoria; mapToDouble + sum los suma.
    private double calcularGastado(Presupuesto presupuesto) {
        ArrayList<Gasto> gastos = gastoDAO.listarDelMes(presupuesto.getEmpleado(), presupuesto.getMes(), presupuesto.getAnio());
        int idCategoria = presupuesto.getCategoria().getIdCategoria();
        return gastos.stream()
                .filter(g -> g.getCategoria().getIdCategoria() == idCategoria)
                .mapToDouble(g -> g.getMonto())
                .sum();
    }

    // CREATE de presupuesto
    private void registrarPresupuesto() {
        System.out.println("\n--- REGISTRAR PRESUPUESTO ---");
        ArrayList<Empleado> empleados = empleadoDAO.listar();
        ArrayList<CategoriaGasto> categorias = categoriaDAO.listar();
        if (empleados.isEmpty() || categorias.isEmpty()) {
            System.out.println("Primero registre al menos un empleado y una categoria.");
            return;
        }
        Presupuesto presupuesto = new Presupuesto();
        presupuesto.setEmpleado(Consola.seleccionar("Empleados:", empleados));
        presupuesto.setCategoria(Consola.seleccionar("Categorias:", categorias));
        presupuesto.registrar();
        if (presupuestoDAO.insertar(presupuesto)) {
            double gastado = calcularGastado(presupuesto);
            System.out.println("Presupuesto registrado: " + presupuesto.resumen()
                    + "\nYa gastado ese mes: " + Dinero.formatear(gastado)
                    + " | Estado: " + presupuesto.calcularEstado(gastado));
        }
    }

    // UPDATE de presupuesto
    private void modificarPresupuesto() {
        System.out.println("\n--- MODIFICAR PRESUPUESTO ---");
        Presupuesto presupuesto = elegirPresupuesto();
        if (presupuesto == null) {
            return;
        }
        presupuesto.setMontoLimite(Consola.leerMonto("Nuevo monto maximo (S/):"));
        if (presupuestoDAO.actualizar(presupuesto)) {
            double gastado = calcularGastado(presupuesto);
            System.out.println("Presupuesto actualizado: " + presupuesto.resumen()
                    + "\nEstado: " + presupuesto.calcularEstado(gastado));
        }
    }

    // DELETE de presupuesto
    private void eliminarPresupuesto() {
        System.out.println("\n--- ELIMINAR PRESUPUESTO ---");
        Presupuesto presupuesto = elegirPresupuesto();
        if (presupuesto == null) {
            return;
        }
        if (Consola.leerSiNo("Seguro que desea eliminar " + presupuesto.resumen() + "?")
                && presupuestoDAO.eliminar(presupuesto)) {
            System.out.println("Presupuesto eliminado.");
        }
    }

    // Gastos del mes ordenados del mayor al menor monto (COMPARATOR, semana 12).
    private void listarGastosDelMes() {
        System.out.println("\n--- GASTOS DEL MES ---");
        ArrayList<Empleado> empleados = empleadoDAO.listar();
        if (empleados.isEmpty()) {
            System.out.println("No hay empleados registrados.");
            return;
        }
        Empleado empleado = Consola.seleccionar("Empleados:", empleados);
        int mes = Consola.leerEntero("Mes (1-12):", 1, 12);
        int anio = Consola.leerEntero("Anio (2000-2100):", 2000, 2100);

        ArrayList<Gasto> gastos = gastoDAO.listarDelMes(empleado, mes, anio);
        gastos.sort(new ComparadorGastoPorMonto());

        String titulo = String.format("Gastos de %s en %02d/%d (de mayor a menor):", empleado.getNombreCompleto(), mes, anio);
        Consola.mostrarLista(titulo, gastos);
        double total = gastos.stream().mapToDouble(g -> g.getMonto()).sum();
        System.out.println("Total gastado: " + Dinero.formatear(total));
    }

    // READ: tabla con el estado de cada presupuesto.
    private void estadoPresupuestos() {
        System.out.println("\n--- ESTADO DE LOS PRESUPUESTOS ---");
        ArrayList<Presupuesto> presupuestos = presupuestoDAO.listar();
        if (presupuestos.isEmpty()) {
            System.out.println("No hay presupuestos registrados.");
            return;
        }
        String mensaje = "";
        for (Presupuesto p : presupuestos) {
            double gastado = calcularGastado(p);
            mensaje += "\n" + p.resumen();
            mensaje += String.format("\n   Gastado: %s | Disponible: %s | Uso: %.1f%% | %s",
                    Dinero.formatear(gastado), Dinero.formatear(p.calcularDisponible(gastado)),
                    p.calcularPorcentajeUsado(gastado), p.calcularEstado(gastado));
        }
        mensaje += "\nOK = menos del 80% | ALERTA = 80% o mas | EXCEDIDO = se paso del limite";
        System.out.println(mensaje);
    }

    // Muestra los presupuestos y devuelve el elegido (null si no hay ninguno).
    private Presupuesto elegirPresupuesto() {
        ArrayList<Presupuesto> presupuestos = presupuestoDAO.listar();
        if (presupuestos.isEmpty()) {
            System.out.println("No hay presupuestos registrados.");
            return null;
        }
        return Consola.seleccionar("Presupuestos:", presupuestos);
    }
}
