package walletpro.vista;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import walletpro.dao.CargoDAO;
import walletpro.dao.CuentaDAO;
import walletpro.dao.EmpleadoDAO;
import walletpro.dao.EmpresaDAO;
import walletpro.dao.GastoDAO;
import walletpro.dao.SueldoDAO;
import walletpro.dao.TrabajoDAO;
import walletpro.modelo.Cargo;
import walletpro.modelo.CuentaBancaria;
import walletpro.modelo.Empleado;
import walletpro.modelo.Empresa;
import walletpro.modelo.Gasto;
import walletpro.modelo.Movimiento;
import walletpro.modelo.Sueldo;
import walletpro.modelo.Trabajo;
import walletpro.util.Consola;
import walletpro.util.Dinero;

/**
 * ALUMNO 1 - Modulo de Ingresos y Ahorros.
 * Lo llama: MenuPrincipal (opcion 1).
 *
 * Patron de cada opcion "Registrar":
 *   1. new Objeto()             -> crear el objeto vacio
 *   2. objeto.registrar()       -> pedir los datos por teclado
 *   3. objetoDAO.insertar(obj)  -> guardarlo en SQL Server
 */
public class MenuIngresos {

    private EmpresaDAO empresaDAO = new EmpresaDAO();
    private CargoDAO cargoDAO = new CargoDAO();
    private EmpleadoDAO empleadoDAO = new EmpleadoDAO();
    private TrabajoDAO trabajoDAO = new TrabajoDAO();
    private SueldoDAO sueldoDAO = new SueldoDAO();
    private CuentaDAO cuentaDAO = new CuentaDAO();
    private GastoDAO gastoDAO = new GastoDAO();

    public void mostrar() {
        int opcion;
        do {
            String mensaje = "\n========== INGRESOS Y AHORROS ==========";
            mensaje += "\n1. Registrar empresa";
            mensaje += "\n2. Registrar cargo";
            mensaje += "\n3. Registrar empleado";
            mensaje += "\n4. Registrar trabajo";
            mensaje += "\n5. Registrar sueldo mensual";
            mensaje += "\n6. Listar trabajos";
            mensaje += "\n7. Consultar ingresos de un empleado";
            mensaje += "\n8. Resumen de ahorro del mes";
            mensaje += "\n0. Volver";
            System.out.println(mensaje);

            opcion = Consola.leerEntero("Seleccione una opcion:", 0, 8);
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
        } while (opcion != 0);
    }

    private void registrarEmpresa() {
        System.out.println("\n--- REGISTRAR EMPRESA ---");
        Empresa empresa = new Empresa();
        empresa.registrar();
        if (empresaDAO.insertar(empresa)) {
            System.out.println("Empresa registrada: " + empresa);
        }
    }

    private void registrarCargo() {
        System.out.println("\n--- REGISTRAR CARGO ---");
        Cargo cargo = new Cargo();
        cargo.registrar();
        if (cargoDAO.insertar(cargo)) {
            System.out.println("Cargo registrado: " + cargo);
        }
    }

    private void registrarEmpleado() {
        System.out.println("\n--- REGISTRAR EMPLEADO ---");
        Empleado empleado = new Empleado();
        empleado.registrar();
        if (empleadoDAO.insertar(empleado)) {
            System.out.println("Empleado registrado.\n" + empleado.resumen());
        }
    }

    private void registrarTrabajo() {
        System.out.println("\n--- REGISTRAR TRABAJO ---");
        ArrayList<Empleado> empleados = empleadoDAO.listar();
        ArrayList<Empresa> empresas = empresaDAO.listar();
        ArrayList<Cargo> cargos = cargoDAO.listar();
        if (empleados.isEmpty() || empresas.isEmpty() || cargos.isEmpty()) {
            System.out.println("Primero registre al menos un empleado, una empresa y un cargo.");
            return;
        }
        Trabajo trabajo = new Trabajo();
        trabajo.setEmpleado(Consola.seleccionar("Empleados:", empleados));
        trabajo.setEmpresa(Consola.seleccionar("Empresas:", empresas));
        trabajo.setCargo(Consola.seleccionar("Cargos:", cargos));
        trabajo.registrar();
        if (trabajoDAO.insertar(trabajo)) {
            System.out.println("Trabajo registrado: " + trabajo.resumen());
        }
    }

    // Registra el sueldo y, si el empleado tiene cuenta, puede depositarlo en ella.
    private void registrarSueldo() {
        System.out.println("\n--- REGISTRAR SUELDO MENSUAL ---");
        Empleado empleado = elegirEmpleado();
        if (empleado == null) {
            return;
        }
        Sueldo sueldo = new Sueldo();
        sueldo.setEmpleado(empleado);
        sueldo.registrar();
        if (!sueldoDAO.insertar(sueldo)) {
            return;   // por ejemplo, ya tenia sueldo ese mes
        }
        System.out.println("Sueldo registrado: " + sueldo.resumen());

        ArrayList<CuentaBancaria> cuentas = cuentaDAO.listarDe(empleado);
        if (!cuentas.isEmpty() && Consola.leerSiNo("Depositar el sueldo en una cuenta?")) {
            CuentaBancaria cuenta = Consola.seleccionar("Cuentas:", cuentas);
            Movimiento movimiento = cuenta.depositar(sueldo.getMonto(), "Sueldo " + sueldo.getPeriodo());
            if (cuentaDAO.guardarMovimiento(cuenta, movimiento)) {
                System.out.println("Depositado. Nuevo saldo: " + Dinero.formatear(cuenta.getSaldo()));
            }
        }
    }

    private void listarTrabajos() {
        Consola.mostrarLista("\n--- LISTA DE TRABAJOS ---", trabajoDAO.listar());
    }

    private void consultarIngresos() {
        System.out.println("\n--- CONSULTAR INGRESOS ---");
        Empleado empleado = elegirEmpleado();
        if (empleado == null) {
            return;
        }
        ArrayList<Sueldo> sueldos = sueldoDAO.listarDe(empleado);
        if (sueldos.isEmpty()) {
            System.out.println(empleado.getNombreCompleto() + " no tiene sueldos registrados.");
            return;
        }
        String mensaje = "Sueldos de " + empleado.getNombreCompleto() + ":";
        double total = 0;
        for (Sueldo sueldo : sueldos) {
            mensaje += "\n" + sueldo.resumen();
            total += sueldo.getMonto();
        }
        mensaje += "\nTotal: " + Dinero.formatear(total);
        mensaje += "\nPromedio mensual: " + Dinero.formatear(total / sueldos.size());
        System.out.println(mensaje);
    }

    /**
     * Ahorro del mes = sueldos del mes - gastos del mes.
     * Aqui se usan:
     *  - STREAM y LAMBDA: para sumar los sueldos del mes.
     *  - HASHMAP (semana 11): para sumar los gastos de cada categoria.
     */
    private void resumenAhorro() {
        System.out.println("\n--- RESUMEN DE AHORRO DEL MES ---");
        Empleado empleado = elegirEmpleado();
        if (empleado == null) {
            return;
        }
        int mes = Consola.leerEntero("Mes (1-12):", 1, 12);
        int anio = Consola.leerEntero("Anio (2000-2100):", 2000, 2100);

        // STREAM: filter se queda con los sueldos de ese mes, mapToDouble toma el monto, sum los suma.
        // s -> ... es una LAMBDA: una funcion corta que recibe un sueldo s.
        double ingresos = sueldoDAO.listarDe(empleado).stream()
                .filter(s -> s.getMes() == mes && s.getAnio() == anio)
                .mapToDouble(s -> s.getMonto())
                .sum();

        ArrayList<Gasto> gastos = gastoDAO.listarDelMes(empleado, mes, anio);
        double totalGastos = gastos.stream().mapToDouble(g -> g.getMonto()).sum();
        double ahorro = Dinero.redondear(ingresos - totalGastos);

        // HASHMAP: clave = nombre de la categoria, valor = total gastado en ella.
        HashMap<String, Double> porCategoria = new HashMap<>();
        for (Gasto gasto : gastos) {
            String categoria = gasto.getCategoria().getNombre();
            if (porCategoria.containsKey(categoria)) {
                porCategoria.put(categoria, porCategoria.get(categoria) + gasto.getMonto());
            } else {
                porCategoria.put(categoria, gasto.getMonto());
            }
        }

        String mensaje = String.format("Resumen de %s - %02d/%d", empleado.getNombreCompleto(), mes, anio);
        mensaje += "\nIngresos (sueldos): " + Dinero.formatear(ingresos);
        mensaje += "\nGastos:             " + Dinero.formatear(totalGastos);
        mensaje += "\nAhorro del mes:     " + Dinero.formatear(ahorro);
        if (ingresos > 0) {
            mensaje += String.format("\nAhorro: %.1f%% de los ingresos", ahorro * 100 / ingresos);
        }
        if (ahorro < 0) {
            mensaje += "\nATENCION: este mes gasto mas de lo que gano.";
        }
        mensaje += "\nGastos por categoria:";
        if (porCategoria.isEmpty()) {
            mensaje += "\n   (sin gastos en este mes)";
        }
        // Recorrer un HashMap: entrySet() da cada par clave -> valor
        for (Map.Entry<String, Double> entrada : porCategoria.entrySet()) {
            mensaje += "\n   " + entrada.getKey() + ": " + Dinero.formatear(entrada.getValue());
        }
        System.out.println(mensaje);
    }

    // Muestra los empleados y devuelve el elegido (null si no hay ninguno).
    private Empleado elegirEmpleado() {
        ArrayList<Empleado> empleados = empleadoDAO.listar();
        if (empleados.isEmpty()) {
            System.out.println("Primero registre al menos un empleado.");
            return null;
        }
        return Consola.seleccionar("Empleados:", empleados);
    }
}
