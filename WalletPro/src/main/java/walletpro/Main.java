package walletpro;

import walletpro.modelo.*;
import walletpro.servicio.*;

import java.util.Scanner;

public class Main {

    private static Scanner teclado = new Scanner(System.in);

    // Usuarios
    private static Usuario[] usuariosDB = new Usuario[50];
    private static int totalUsuarios = 0;
    private static Usuario usuarioActual = null;

    // Alumno 1 - Ingresos y Ahorros
    private static Empleado[] empleados = new Empleado[30];
    private static int totalEmpleados = 0;

    private static Empresa[] empresas = new Empresa[20];
    private static int totalEmpresas = 0;

    private static Cargo[] cargos = new Cargo[20];
    private static int totalCargos = 0;

    private static Trabajo[] trabajos = new Trabajo[50];
    private static int totalTrabajos = 0;

    private static Sueldo[] sueldos = new Sueldo[200];
    private static int totalSueldos = 0;

    // Alumno 2 - Gastos y Presupuesto
    private static CategoriaGasto[] categorias = new CategoriaGasto[20];
    private static int totalCategorias = 0;

    private static Gasto[] gastos = new Gasto[500];
    private static int totalGastos = 0;

    private static Presupuesto[] presupuestos = new Presupuesto[100];
    private static int totalPresupuestos = 0;

    // Alumno 3 - Bancos y Prestamos
    private static Banco[] bancos = new Banco[20];
    private static int totalBancos = 0;

    private static CuentaBancaria[] cuentas = new CuentaBancaria[50];
    private static int totalCuentas = 0;

    private static TipoPrestamo[] tiposPrestamo = new TipoPrestamo[10];
    private static int totalTiposPrestamo = 0;

    private static Prestamo[] prestamos = new Prestamo[100];
    private static int totalPrestamos = 0;

    private static int contadorTransacciones = 1;

    public static void main(String[] args) {
        cargarDatosDePrueba();

        String mensaje = "======================================";
        mensaje += "\n        WALLET PRO - BIENVENIDO       ";
        mensaje += "\n======================================";
        System.out.println(mensaje);

        menuAutenticacion();

        if (usuarioActual != null) {
            menuPrincipal();
        }

        System.out.println("Gracias por usar Wallet Pro");
    }

    // ---------- DATOS DE PRUEBA (sin pasar por Scanner) ----------
    private static void cargarDatosDePrueba() {
        Usuario admin = new Usuario();
        admin.setIdUsuario(1);
        admin.setDni("00000000");
        admin.setNombres("Administrador");
        admin.setApellidos("Sistema");
        admin.setFechaNacimiento("01-01-2000");
        admin.setCorreo("admin@walletpro.com");
        admin.setNombreUsuario("Administrador");
        admin.setContrasena("admin1234");
        admin.setPreguntaSeguridad("Color favorito");
        admin.setRespuestaSeguridad("azul");
        usuariosDB[0] = admin;
        totalUsuarios = 1;

        Empresa empresa1 = new Empresa();
        empresa1.setRuc("20123456789");
        empresa1.setRazonSocial("ON EMPRESAS SAC");
        Empresa empresa2 = new Empresa();
        empresa2.setRuc("20234567890");
        empresa2.setRazonSocial("GROUP INFORMATIC SAC");
        empresas[0] = empresa1;
        empresas[1] = empresa2;
        totalEmpresas = 2;

        Cargo cargo1 = new Cargo();
        cargo1.setNombreCargo("Desarrollador");
        cargo1.setDescripcion("Desarrollo de software");
        Cargo cargo2 = new Cargo();
        cargo2.setNombreCargo("Soporte");
        cargo2.setDescripcion("Soporte tecnico");
        cargos[0] = cargo1;
        cargos[1] = cargo2;
        totalCargos = 2;

        Empleado emp1 = new Empleado();
        emp1.setDni("00000001");
        emp1.setNombres("Susana");
        emp1.setApellidos("Pinedo");
        emp1.setFechaNacimiento("04-06-1998");
        emp1.setCorreo("susana@correo.com");
        emp1.setCargo(cargo1);
        emp1.setEmpresa(empresa1);
        emp1.setSueldoBase(3500);

        Empleado emp2 = new Empleado();
        emp2.setDni("00000002");
        emp2.setNombres("Carlos");
        emp2.setApellidos("Eliott");
        emp2.setFechaNacimiento("15-06-2001");
        emp2.setCorreo("carlos@correo.com");
        emp2.setCargo(cargo2);
        emp2.setEmpresa(empresa2);
        emp2.setSueldoBase(2800);

        empleados[0] = emp1;
        empleados[1] = emp2;
        totalEmpleados = 2;

        Trabajo trabajo1 = new Trabajo();
        trabajo1.setEmpresa(empresa1);
        trabajo1.setCargo(cargo1);
        trabajo1.setFechaIngreso("25-06-2024");
        trabajo1.setTipoContrato("Contrato");
        trabajo1.setEmpleado(emp1);

        Trabajo trabajo2 = new Trabajo();
        trabajo2.setEmpresa(empresa2);
        trabajo2.setCargo(cargo2);
        trabajo2.setFechaIngreso("25-06-2023");
        trabajo2.setTipoContrato("Planilla");
        trabajo2.setEmpleado(emp2);

        trabajos[0] = trabajo1;
        trabajos[1] = trabajo2;
        totalTrabajos = 2;

        Sueldo sueldo1 = new Sueldo();
        sueldo1.setMes(9);
        sueldo1.setAnio(2026);
        sueldo1.setMonto(3500);
        sueldo1.setEmpleado(emp1);
        sueldos[0] = sueldo1;
        totalSueldos = 1;

        String[] nombresCategoria = {"Alimentacion", "Transporte", "Salud", "Entretenimiento", "Otros"};
        String[] descripcionesCategoria = {
            "Comida y supermercado", "Pasajes y combustible",
            "Medicinas y consultas", "Ocio y streaming", "Gastos varios"
        };
        boolean[] esFijaCategoria = {true, true, false, false, false};
        for (int i = 0; i < nombresCategoria.length; i++) {
            CategoriaGasto categoria = new CategoriaGasto();
            categoria.setNombreCategoria(nombresCategoria[i]);
            categoria.setDescripcion(descripcionesCategoria[i]);
            categoria.setEsFija(esFijaCategoria[i]);
            categorias[i] = categoria;
        }
        totalCategorias = nombresCategoria.length;

        Banco banco1 = new Banco();
        banco1.setCodigoBanco("00001");
        banco1.setNombreBanco("Interbank");
        Banco banco2 = new Banco();
        banco2.setCodigoBanco("00002");
        banco2.setNombreBanco("BCP");
        bancos[0] = banco1;
        bancos[1] = banco2;
        totalBancos = 2;

        CuentaBancaria cuenta1 = new CuentaBancaria();
        cuenta1.setNumeroCuenta("000120003000");
        cuenta1.setTipoCuenta("Ahorros");
        cuenta1.setSaldoInicial(6000);
        cuenta1.setBanco(banco1);
        cuenta1.setTitular(emp1);
        cuentas[0] = cuenta1;
        totalCuentas = 1;

        Gasto gasto1 = new Gasto();
        gasto1.fijarDatos("G0001", "01-01-2026", 40, "Netflix");
        gasto1.setCategoria(categorias[3]);
        gasto1.setPersona(emp1);
        gasto1.setEsOcasional(true);

        Gasto gasto2 = new Gasto();
        gasto2.fijarDatos("G0002", "01-01-2026", 650, "Compra mensual de comida");
        gasto2.setCategoria(categorias[0]);
        gasto2.setPersona(emp1);
        gasto2.setEsOcasional(false);
        gasto2.setMontoVariable(true);

        gastos[0] = gasto1;
        gastos[1] = gasto2;
        totalGastos = 2;

        TipoPrestamo tipo1 = new TipoPrestamo();
        tipo1.setNombre("Personal");
        tipo1.setDescripcion("Libre disponibilidad");
        TipoPrestamo tipo2 = new TipoPrestamo();
        tipo2.setNombre("Hipotecario");
        tipo2.setDescripcion("Para vivienda");
        tiposPrestamo[0] = tipo1;
        tiposPrestamo[1] = tipo2;
        totalTiposPrestamo = 2;
    }

    // ---------- AUTENTICACION ----------
    private static void menuAutenticacion() {
        int opcion = 0;
        do {
            String mensaje = "\n--- WALLETPRO: MENU DE ACCESO ---";
            mensaje += "\n1. Iniciar Sesion";
            mensaje += "\n2. Registrar Usuario";
            mensaje += "\n3. Recuperar Contrasena";
            mensaje += "\n4. Salir";
            System.out.println(mensaje);
            opcion = leerEntero("Selecciona una opcion: ");

            switch (opcion) {
                case 1: login(); break;
                case 2: registrarUsuario(); break;
                case 3: recuperarContrasena(); break;
                case 4: System.out.println("Saliendo del sistema"); break;
                default: System.out.println("Opcion invalida");
            }
        } while (opcion != 4 && usuarioActual == null);
    }

    private static void login() {
        System.out.print("Nombre de usuario: ");
        String nombreUsuario = teclado.nextLine();
        System.out.print("Contrasena: ");
        String contrasena = teclado.nextLine();

        Login login = new Login();
        Usuario usuario = login.iniciarSesion(usuariosDB, totalUsuarios, nombreUsuario, contrasena);

        if (usuario != null) {
            usuarioActual = usuario;
            System.out.println("Bienvenido/a, " + usuario.getNombreUsuario());
        } else {
            System.out.println("Credenciales incorrectas.");
        }
    }

    // ALUMNO 1 - auth: Registrar Usuario
    private static void registrarUsuario() {
        System.out.println("\n--- REGISTRAR USUARIO ---");
        Usuario nuevo = new Usuario();
        nuevo.registrar(teclado);

        RegistrarUsuario registro = new RegistrarUsuario();
        if (registro.registrar(usuariosDB, totalUsuarios, nuevo)) {
            nuevo.setIdUsuario(totalUsuarios + 1);
            usuariosDB[totalUsuarios] = nuevo;
            totalUsuarios++;
            System.out.println("Usuario registrado con exito.");
        }
    }

    private static void recuperarContrasena() {
        System.out.print("Nombre de usuario: ");
        String nombreUsuario = teclado.nextLine();
        System.out.print("Codigo de verificacion: ");
        String codigo = teclado.nextLine();
        System.out.print("Nueva contrasena: ");
        String nueva = teclado.nextLine();

        RecuperarContrasena recuperar = new RecuperarContrasena();
        recuperar.restablecerContrasena(usuariosDB, totalUsuarios, nombreUsuario, codigo, nueva);
    }

    // ---------- MENU PRINCIPAL ----------
    private static void menuPrincipal() {
        int opcion = 0;
        do {
            String mensaje = "\n========== MENU PRINCIPAL ==========";
            mensaje += "\nUsuario: " + usuarioActual.getNombreUsuario();
            mensaje += "\n--- Alumno 1: Ingresos y Ahorros ---";
            mensaje += "\n1. Registrar Empresa";
            mensaje += "\n2. Registrar Cargo";
            mensaje += "\n3. Registrar Empleado";
            mensaje += "\n4. Registrar Trabajo";
            mensaje += "\n5. Registrar Sueldo Mensual";
            mensaje += "\n6. Listar Trabajos";
            mensaje += "\n7. Listar Sueldos (consultar ingresos)";
            mensaje += "\n--- Alumno 2: Gastos y Presupuesto ---";
            mensaje += "\n8. Registrar Categoria de Gasto";
            mensaje += "\n9. Registrar Gasto Mensual";
            mensaje += "\n10. Registrar Presupuesto";
            mensaje += "\n11. Listar Gastos";
            mensaje += "\n12. Consultar Presupuesto";
            mensaje += "\n--- Alumno 3: Bancos y Prestamos ---";
            mensaje += "\n13. Registrar Banco";
            mensaje += "\n14. Registrar Cuenta Bancaria";
            mensaje += "\n15. Depositar en Cuenta";
            mensaje += "\n16. Retirar de Cuenta";
            mensaje += "\n17. Registrar Tipo de Prestamo";
            mensaje += "\n18. Registrar Prestamo";
            mensaje += "\n19. Pagar Cuota de Prestamo";
            mensaje += "\n20. Listar Prestamos";
            mensaje += "\n--- General ---";
            mensaje += "\n21. Cerrar Sesion";
            System.out.println(mensaje);

            opcion = leerEntero("Selecciona una opcion: ");

            switch (opcion) {
                case 1: registrarEmpresa(); break;
                case 2: registrarCargo(); break;
                case 3: registrarEmpleado(); break;
                case 4: registrarTrabajo(); break;
                case 5: registrarSueldo(); break;
                case 6: listarTrabajos(); break;
                case 7: listarSueldos(); break;
                case 8: registrarCategoria(); break;
                case 9: registrarGasto(); break;
                case 10: registrarPresupuesto(); break;
                case 11: listarGastos(); break;
                case 12: consultarPresupuesto(); break;
                case 13: registrarBanco(); break;
                case 14: registrarCuenta(); break;
                case 15: depositarCuenta(); break;
                case 16: retirarCuenta(); break;
                case 17: registrarTipoPrestamo(); break;
                case 18: registrarPrestamo(); break;
                case 19: pagarPrestamo(); break;
                case 20: listarPrestamos(); break;
                case 21:
                    usuarioActual = null;
                    System.out.println("Cerrando sesion");
                    break;
                default:
                    System.out.println("Opcion invalida");
            }
        } while (opcion != 21);
    }

    // ---------- ALUMNO 1: INGRESOS Y AHORROS ----------
    private static void registrarEmpresa() {
        System.out.println("\n--- REGISTRAR EMPRESA ---");
        if (totalEmpresas >= empresas.length) {
            System.out.println("Limite de empresas alcanzado.");
            return;
        }
        Empresa empresa = new Empresa();
        empresa.registrar(teclado);
        if (!empresa.validar()) {
            System.out.println("Datos invalidos.");
            return;
        }
        empresas[totalEmpresas] = empresa;
        totalEmpresas++;
        System.out.println("Empresa registrada: " + empresa);
    }

    private static void registrarCargo() {
        System.out.println("\n--- REGISTRAR CARGO ---");
        if (totalCargos >= cargos.length) {
            System.out.println("Limite de cargos alcanzado.");
            return;
        }
        Cargo cargo = new Cargo();
        cargo.registrar(teclado);
        if (!cargo.validar()) {
            System.out.println("Datos invalidos.");
            return;
        }
        cargos[totalCargos] = cargo;
        totalCargos++;
        System.out.println("Cargo registrado: " + cargo);
    }

    // Funcionalidad 1 de Alumno 1 (contexto, ya existente)
    private static void registrarEmpleado() {
        System.out.println("\n--- REGISTRAR EMPLEADO (Persona) ---");
        if (totalCargos == 0 || totalEmpresas == 0) {
            System.out.println("Primero registre al menos un cargo y una empresa.");
            return;
        }
        if (totalEmpleados >= empleados.length) {
            System.out.println("Limite de empleados alcanzado.");
            return;
        }
        Empleado empleado = new Empleado();
        empleado.registrar(teclado);

        listarEmpresas();
        int posicionEmpresa = leerEntero("Selecciona empresa: ") - 1;
        listarCargos();
        int posicionCargo = leerEntero("Selecciona cargo: ") - 1;

        if (posicionEmpresa < 0 || posicionEmpresa >= totalEmpresas
                || posicionCargo < 0 || posicionCargo >= totalCargos) {
            System.out.println("Seleccion invalida.");
            return;
        }
        empleado.setEmpresa(empresas[posicionEmpresa]);
        empleado.setCargo(cargos[posicionCargo]);

        if (!empleado.validar()) {
            System.out.println("Datos invalidos.");
            return;
        }
        empleados[totalEmpleados] = empleado;
        totalEmpleados++;
        System.out.println(empleado.resumen());
    }

    // Funcionalidad 2 de Alumno 1 (herencia + clase abstracta + interfaces)
    private static void registrarTrabajo() {
        System.out.println("\n--- REGISTRAR TRABAJO ---");
        if (totalEmpleados == 0 || totalEmpresas == 0 || totalCargos == 0) {
            System.out.println("Faltan datos previos (empleado, empresa o cargo).");
            return;
        }
        if (totalTrabajos >= trabajos.length) {
            System.out.println("Limite de trabajos alcanzado.");
            return;
        }
        listarEmpleados();
        int posicionEmpleado = leerEntero("Selecciona empleado: ") - 1;
        listarEmpresas();
        int posicionEmpresa = leerEntero("Selecciona empresa: ") - 1;
        listarCargos();
        int posicionCargo = leerEntero("Selecciona cargo: ") - 1;

        if (posicionEmpleado < 0 || posicionEmpleado >= totalEmpleados
                || posicionEmpresa < 0 || posicionEmpresa >= totalEmpresas
                || posicionCargo < 0 || posicionCargo >= totalCargos) {
            System.out.println("Seleccion invalida.");
            return;
        }

        Trabajo trabajo = new Trabajo();
        trabajo.registrar(teclado);
        trabajo.setEmpresa(empresas[posicionEmpresa]);
        trabajo.setCargo(cargos[posicionCargo]);
        trabajo.setEmpleado(empleados[posicionEmpleado]);

        if (!trabajo.validar()) {
            System.out.println("Datos invalidos.");
            return;
        }
        trabajos[totalTrabajos] = trabajo;
        totalTrabajos++;
        System.out.println(trabajo.resumen());
    }

    // Funcionalidad 3 de Alumno 1 (nueva en esta entrega)
    private static void registrarSueldo() {
        System.out.println("\n--- REGISTRAR SUELDO MENSUAL ---");
        if (totalEmpleados == 0) {
            System.out.println("Primero registre un empleado.");
            return;
        }
        if (totalSueldos >= sueldos.length) {
            System.out.println("Limite de sueldos alcanzado.");
            return;
        }
        listarEmpleados();
        int posicionEmpleado = leerEntero("Selecciona empleado: ") - 1;
        if (posicionEmpleado < 0 || posicionEmpleado >= totalEmpleados) {
            System.out.println("Empleado invalido.");
            return;
        }

        Sueldo sueldo = new Sueldo();
        sueldo.registrar(teclado);
        sueldo.setEmpleado(empleados[posicionEmpleado]);

        if (!sueldo.validar()) {
            System.out.println("Datos invalidos.");
            return;
        }
        sueldos[totalSueldos] = sueldo;
        totalSueldos++;
        System.out.println(sueldo.resumen());
    }

    private static void listarTrabajos() {
        System.out.println("\n--- LISTA DE TRABAJOS ---");
        if (totalTrabajos == 0) {
            System.out.println("No hay trabajos registrados.");
            return;
        }
        String mensaje = "Trabajos registrados";
        for (int i = 0; i < totalTrabajos; i++) {
            mensaje += "\n" + (i + 1) + ". " + trabajos[i];
        }
        System.out.println(mensaje);
    }

    // Consultar ingresos: suma los sueldos de un empleado
    private static void listarSueldos() {
        System.out.println("\n--- CONSULTAR INGRESOS (sueldos registrados) ---");
        if (totalSueldos == 0) {
            System.out.println("No hay sueldos registrados.");
            return;
        }
        listarEmpleados();
        int posicionEmpleado = leerEntero("Selecciona empleado (0 = todos): ") - 1;

        String mensaje = "Sueldos registrados";
        double totalIngresos = 0;
        for (int i = 0; i < totalSueldos; i++) {
            if (posicionEmpleado < 0 || sueldos[i].getEmpleado() == empleados[posicionEmpleado]) {
                mensaje += "\n" + (i + 1) + ". " + sueldos[i].resumen();
                totalIngresos += sueldos[i].getMonto();
            }
        }
        mensaje += "\nTotal de ingresos consultados: " + totalIngresos;
        System.out.println(mensaje);
    }

    // ---------- ALUMNO 2: GASTOS Y PRESUPUESTO ----------
    // Funcionalidad 2 de Alumno 2 (ahora interactiva, herencia + interfaces)
    private static void registrarCategoria() {
        System.out.println("\n--- REGISTRAR CATEGORIA DE GASTO ---");
        if (totalCategorias >= categorias.length) {
            System.out.println("Limite de categorias alcanzado.");
            return;
        }
        CategoriaGasto categoria = new CategoriaGasto();
        categoria.registrar(teclado);
        if (!categoria.validar()) {
            System.out.println("Datos invalidos.");
            return;
        }
        categorias[totalCategorias] = categoria;
        totalCategorias++;
        System.out.println("Categoria registrada: " + categoria);
    }

    // Funcionalidad 1 de Alumno 2 (contexto, ya existente)
    private static void registrarGasto() {
        System.out.println("\n--- REGISTRAR GASTO MENSUAL ---");
        if (totalEmpleados == 0 || totalCategorias == 0) {
            System.out.println("Faltan empleados o categorias registradas.");
            return;
        }
        if (totalGastos >= gastos.length) {
            System.out.println("Limite de gastos alcanzado.");
            return;
        }
        String idTransaccion = "G" + String.format("%04d", contadorTransacciones);

        listarEmpleados();
        int posicionPersona = leerEntero("Selecciona empleado: ") - 1;
        listarCategorias();
        int posicionCategoria = leerEntero("Selecciona categoria: ") - 1;

        if (posicionPersona < 0 || posicionPersona >= totalEmpleados
                || posicionCategoria < 0 || posicionCategoria >= totalCategorias) {
            System.out.println("Seleccion invalida.");
            return;
        }

        Gasto gasto = new Gasto();
        gasto.registrar(teclado, idTransaccion);
        gasto.setCategoria(categorias[posicionCategoria]);
        gasto.setPersona(empleados[posicionPersona]);

        if (!gasto.validar()) {
            System.out.println("Datos invalidos.");
            return;
        }
        gastos[totalGastos] = gasto;
        totalGastos++;
        contadorTransacciones++;
        gasto.procesar();
        System.out.println(gasto.resumen());
    }

    // Funcionalidad 3 de Alumno 2 (nueva en esta entrega)
    private static void registrarPresupuesto() {
        System.out.println("\n--- REGISTRAR PRESUPUESTO ---");
        if (totalEmpleados == 0 || totalCategorias == 0) {
            System.out.println("Faltan empleados o categorias registradas.");
            return;
        }
        if (totalPresupuestos >= presupuestos.length) {
            System.out.println("Limite de presupuestos alcanzado.");
            return;
        }
        listarEmpleados();
        int posicionPersona = leerEntero("Selecciona empleado: ") - 1;
        listarCategorias();
        int posicionCategoria = leerEntero("Selecciona categoria: ") - 1;

        if (posicionPersona < 0 || posicionPersona >= totalEmpleados
                || posicionCategoria < 0 || posicionCategoria >= totalCategorias) {
            System.out.println("Seleccion invalida.");
            return;
        }

        Presupuesto presupuesto = new Presupuesto();
        presupuesto.registrar(teclado);
        presupuesto.setCategoria(categorias[posicionCategoria]);
        presupuesto.setEmpleado(empleados[posicionPersona]);

        if (!presupuesto.validar()) {
            System.out.println("Datos invalidos.");
            return;
        }
        presupuestos[totalPresupuestos] = presupuesto;
        totalPresupuestos++;
        System.out.println(presupuesto.resumen());
    }

    private static void listarGastos() {
        System.out.println("\n--- LISTA DE GASTOS ---");
        if (totalGastos == 0) {
            System.out.println("No hay gastos registrados.");
            return;
        }
        String mensaje = "Gastos registrados";
        for (int i = 0; i < totalGastos; i++) {
            mensaje += "\n" + (i + 1) + ". " + gastos[i].resumen();
        }
        System.out.println(mensaje);
    }

    private static void consultarPresupuesto() {
        System.out.println("\n--- CONSULTAR PRESUPUESTO ---");
        if (totalPresupuestos == 0) {
            System.out.println("No hay presupuestos registrados.");
            return;
        }
        String mensaje = "Presupuestos registrados";
        for (int i = 0; i < totalPresupuestos; i++) {
            Presupuesto p = presupuestos[i];
            double gastado = p.calcularGastado(gastos, totalGastos);
            double disponible = p.getMontoPresupuestado() - gastado;
            mensaje += "\n" + (i + 1) + ". " + p.resumen();
            mensaje += "\n   Gastado: " + gastado + " | Disponible: " + disponible;
            if (disponible < 0) {
                mensaje += " (PRESUPUESTO SUPERADO)";
            }
        }
        System.out.println(mensaje);
    }

    // ---------- ALUMNO 3: BANCOS Y PRESTAMOS ----------
    private static void registrarBanco() {
        System.out.println("\n--- REGISTRAR BANCO ---");
        if (totalBancos >= bancos.length) {
            System.out.println("Limite de bancos alcanzado.");
            return;
        }
        Banco banco = new Banco();
        banco.registrar(teclado);
        if (!banco.validar()) {
            System.out.println("Datos invalidos.");
            return;
        }
        bancos[totalBancos] = banco;
        totalBancos++;
        System.out.println("Banco registrado: " + banco);
    }

    // Funcionalidad 2 de Alumno 3 (herencia + interfaces, OperacionFinanciera)
    private static void registrarCuenta() {
        System.out.println("\n--- REGISTRAR CUENTA BANCARIA ---");
        if (totalBancos == 0 || totalEmpleados == 0) {
            System.out.println("Faltan bancos o empleados registrados.");
            return;
        }
        if (totalCuentas >= cuentas.length) {
            System.out.println("Limite de cuentas alcanzado.");
            return;
        }
        listarBancos();
        int posicionBanco = leerEntero("Selecciona banco: ") - 1;
        listarEmpleados();
        int posicionTitular = leerEntero("Selecciona titular: ") - 1;

        if (posicionBanco < 0 || posicionBanco >= totalBancos
                || posicionTitular < 0 || posicionTitular >= totalEmpleados) {
            System.out.println("Seleccion invalida.");
            return;
        }

        CuentaBancaria cuenta = new CuentaBancaria();
        cuenta.registrar(teclado);
        cuenta.setBanco(bancos[posicionBanco]);
        cuenta.setTitular(empleados[posicionTitular]);

        if (!cuenta.validar()) {
            System.out.println("Datos invalidos.");
            return;
        }
        cuentas[totalCuentas] = cuenta;
        totalCuentas++;
        System.out.println(cuenta.resumen());
    }

    private static void depositarCuenta() {
        System.out.println("\n--- DEPOSITAR EN CUENTA ---");
        if (totalCuentas == 0) {
            System.out.println("No hay cuentas registradas.");
            return;
        }
        listarCuentas();
        int posicionCuenta = leerEntero("Selecciona cuenta: ") - 1;
        if (posicionCuenta < 0 || posicionCuenta >= totalCuentas) {
            System.out.println("Seleccion invalida.");
            return;
        }
        double monto = leerDouble("Monto a depositar: ");
        cuentas[posicionCuenta].depositar(monto);
    }

    private static void retirarCuenta() {
        System.out.println("\n--- RETIRAR DE CUENTA ---");
        if (totalCuentas == 0) {
            System.out.println("No hay cuentas registradas.");
            return;
        }
        listarCuentas();
        int posicionCuenta = leerEntero("Selecciona cuenta: ") - 1;
        if (posicionCuenta < 0 || posicionCuenta >= totalCuentas) {
            System.out.println("Seleccion invalida.");
            return;
        }
        double monto = leerDouble("Monto a retirar: ");
        cuentas[posicionCuenta].retirar(monto);
    }

    private static void registrarTipoPrestamo() {
        System.out.println("\n--- REGISTRAR TIPO DE PRESTAMO ---");
        if (totalTiposPrestamo >= tiposPrestamo.length) {
            System.out.println("Limite de tipos de prestamo alcanzado.");
            return;
        }
        TipoPrestamo tipo = new TipoPrestamo();
        tipo.registrar(teclado);
        if (!tipo.validar()) {
            System.out.println("Datos invalidos.");
            return;
        }
        tiposPrestamo[totalTiposPrestamo] = tipo;
        totalTiposPrestamo++;
        System.out.println("Tipo de prestamo registrado: " + tipo);
    }

    // Funcionalidad 3 de Alumno 3 (nueva en esta entrega)
    private static void registrarPrestamo() {
        System.out.println("\n--- REGISTRAR PRESTAMO ---");
        if (totalEmpleados == 0 || totalBancos == 0 || totalTiposPrestamo == 0) {
            System.out.println("Faltan datos previos (empleado, banco o tipo de prestamo).");
            return;
        }
        if (totalPrestamos >= prestamos.length) {
            System.out.println("Limite de prestamos alcanzado.");
            return;
        }
        String codigo = "P" + String.format("%04d", totalPrestamos + 1);

        listarTiposPrestamo();
        int posicionTipo = leerEntero("Selecciona tipo de prestamo: ") - 1;
        listarBancos();
        int posicionBanco = leerEntero("Selecciona banco: ") - 1;
        listarEmpleados();
        int posicionDeudor = leerEntero("Selecciona deudor: ") - 1;

        if (posicionTipo < 0 || posicionTipo >= totalTiposPrestamo
                || posicionBanco < 0 || posicionBanco >= totalBancos
                || posicionDeudor < 0 || posicionDeudor >= totalEmpleados) {
            System.out.println("Seleccion invalida.");
            return;
        }

        Prestamo prestamo = new Prestamo();
        prestamo.registrar(teclado, codigo);
        prestamo.setTipoPrestamo(tiposPrestamo[posicionTipo]);
        prestamo.setBanco(bancos[posicionBanco]);
        prestamo.setDeudor(empleados[posicionDeudor]);

        if (totalCuentas > 0) {
            System.out.print("Asociar una cuenta bancaria para el pago automatico? (S/N): ");
            if (teclado.nextLine().equalsIgnoreCase("S")) {
                listarCuentas();
                int posicionCuenta = leerEntero("Selecciona cuenta: ") - 1;
                if (posicionCuenta >= 0 && posicionCuenta < totalCuentas) {
                    prestamo.setCuentaAsociada(cuentas[posicionCuenta]);
                }
            }
        }

        if (!prestamo.validar()) {
            System.out.println("Datos invalidos.");
            return;
        }
        prestamos[totalPrestamos] = prestamo;
        totalPrestamos++;
        System.out.println(prestamo.resumen());
    }

    private static void pagarPrestamo() {
        System.out.println("\n--- PAGAR CUOTA DE PRESTAMO ---");
        if (totalPrestamos == 0) {
            System.out.println("No hay prestamos registrados.");
            return;
        }
        listarPrestamos();
        int posicion = leerEntero("Selecciona prestamo: ") - 1;
        if (posicion < 0 || posicion >= totalPrestamos) {
            System.out.println("Seleccion invalida.");
            return;
        }
        double monto = leerDouble("Monto a pagar: ");
        System.out.print("Fecha del pago (dd-mm-aaaa): ");
        String fecha = teclado.nextLine();

        String idMovimiento = "M" + String.format("%04d", contadorTransacciones);
        if (prestamos[posicion].pagarDesdeCuenta(monto, fecha, idMovimiento)) {
            contadorTransacciones++;
            System.out.println("Pago realizado correctamente.");
            System.out.println(prestamos[posicion].resumen());
        } else {
            System.out.println("Pago rechazado (monto invalido, fecha invalida, "
                    + "falta cuenta asociada o saldo insuficiente).");
        }
    }

    private static void listarPrestamos() {
        System.out.println("\n--- LISTA DE PRESTAMOS ---");
        if (totalPrestamos == 0) {
            System.out.println("No hay prestamos registrados.");
            return;
        }
        String mensaje = "Prestamos registrados";
        for (int i = 0; i < totalPrestamos; i++) {
            mensaje += "\n" + (i + 1) + ". " + prestamos[i].resumen();
        }
        System.out.println(mensaje);
    }

    // ---------- LISTADOS AUXILIARES ----------
    private static void listarEmpleados() {
        String mensaje = "Empleados";
        for (int i = 0; i < totalEmpleados; i++) {
            mensaje += "\n" + (i + 1) + ". " + empleados[i].getNombreCompleto();
        }
        System.out.println(mensaje);
    }

    private static void listarEmpresas() {
        String mensaje = "Empresas";
        for (int i = 0; i < totalEmpresas; i++) {
            mensaje += "\n" + (i + 1) + ". " + empresas[i];
        }
        System.out.println(mensaje);
    }

    private static void listarCargos() {
        String mensaje = "Cargos";
        for (int i = 0; i < totalCargos; i++) {
            mensaje += "\n" + (i + 1) + ". " + cargos[i];
        }
        System.out.println(mensaje);
    }

    private static void listarCategorias() {
        String mensaje = "Categorias";
        for (int i = 0; i < totalCategorias; i++) {
            mensaje += "\n" + (i + 1) + ". " + categorias[i];
        }
        System.out.println(mensaje);
    }

    private static void listarBancos() {
        String mensaje = "Bancos";
        for (int i = 0; i < totalBancos; i++) {
            mensaje += "\n" + (i + 1) + ". " + bancos[i];
        }
        System.out.println(mensaje);
    }

    private static void listarCuentas() {
        String mensaje = "Cuentas";
        for (int i = 0; i < totalCuentas; i++) {
            mensaje += "\n" + (i + 1) + ". " + cuentas[i];
        }
        System.out.println(mensaje);
    }

    private static void listarTiposPrestamo() {
        String mensaje = "Tipos de prestamo";
        for (int i = 0; i < totalTiposPrestamo; i++) {
            mensaje += "\n" + (i + 1) + ". " + tiposPrestamo[i];
        }
        System.out.println(mensaje);
    }

    // ---------- UTILITARIOS (try-catch para enteros y decimales) ----------
    private static int leerEntero(String mensaje) {
        boolean valido = false;
        int valor = 0;
        do {
            try {
                System.out.print(mensaje);
                valor = Integer.parseInt(teclado.nextLine());
                valido = true;
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un numero entero valido.");
            }
        } while (!valido);
        return valor;
    }

    private static double leerDouble(String mensaje) {
        boolean valido = false;
        double valor = 0;
        do {
            try {
                System.out.print(mensaje);
                valor = Double.parseDouble(teclado.nextLine());
                valido = true;
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un numero decimal valido.");
            }
        } while (!valido);
        return valor;
    }
}
