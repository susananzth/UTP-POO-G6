package walletpro.datos;

import java.time.LocalDate;
import walletpro.excepciones.OperacionInvalidaException;
import walletpro.modelo.*;

/**
 * Carga datos de ejemplo la PRIMERA vez que se ejecuta el programa,
 * para poder probarlo y hacer la demostracion sin registrar todo a mano.
 * Se crean sin pasar por el teclado (con setters) y se agregan con los
 * mismos metodos de la Billetera, asi pasan por las mismas validaciones.
 *
 * Los gastos, sueldos y presupuestos se crean en el MES ACTUAL para que
 * los reportes muestren informacion apenas se abre el programa.
 *
 * Usuario de prueba: Administrador / admin1234
 */
public class DatosDePrueba {

    public static void cargar(Billetera billetera) {
        LocalDate hoy = LocalDate.now();
        int mes = hoy.getMonthValue();
        int anio = hoy.getYear();
        String inicioDeMes = String.format("01-%02d-%d", mes, anio);
        String quinceDelMes = String.format("15-%02d-%d", mes, anio);

        try {
            Usuario admin = new Usuario();
            admin.setDni("00000000");
            admin.setNombres("Administrador");
            admin.setApellidos("Sistema");
            admin.setFechaNacimiento("01-01-2000");
            admin.setCorreo("admin@walletpro.com");
            admin.setNombreUsuario("Administrador");
            admin.setContrasena("admin1234");
            admin.setPreguntaSeguridad("Color favorito");
            admin.setRespuestaSeguridad("azul");
            billetera.agregarUsuario(admin);

            // ---------- Alumno 1 ----------
            Empresa empresa1 = new Empresa();
            empresa1.setRuc("20123456789");
            empresa1.setRazonSocial("ON EMPRESAS SAC");
            billetera.agregarEmpresa(empresa1);

            Empresa empresa2 = new Empresa();
            empresa2.setRuc("20234567890");
            empresa2.setRazonSocial("GROUP INFORMATIC SAC");
            billetera.agregarEmpresa(empresa2);

            Cargo cargo1 = new Cargo();
            cargo1.setNombreCargo("Desarrollador");
            cargo1.setDescripcion("Desarrollo de software");
            billetera.agregarCargo(cargo1);

            Cargo cargo2 = new Cargo();
            cargo2.setNombreCargo("Soporte");
            cargo2.setDescripcion("Soporte tecnico");
            billetera.agregarCargo(cargo2);

            Empleado emp1 = new Empleado();
            emp1.setDni("00000001");
            emp1.setNombres("Susana");
            emp1.setApellidos("Pinero");
            emp1.setFechaNacimiento("04-06-1998");
            emp1.setCorreo("susana@correo.com");
            emp1.setCargo(cargo1);
            emp1.setEmpresa(empresa1);
            emp1.setSueldoBase(3500);
            billetera.agregarEmpleado(emp1);

            Empleado emp2 = new Empleado();
            emp2.setDni("00000002");
            emp2.setNombres("Carlos");
            emp2.setApellidos("Eliott");
            emp2.setFechaNacimiento("15-06-2001");
            emp2.setCorreo("carlos@correo.com");
            emp2.setCargo(cargo2);
            emp2.setEmpresa(empresa2);
            emp2.setSueldoBase(2800);
            billetera.agregarEmpleado(emp2);

            Trabajo trabajo1 = new Trabajo();
            trabajo1.setEmpleado(emp1);
            trabajo1.setEmpresa(empresa1);
            trabajo1.setCargo(cargo1);
            trabajo1.setFechaIngreso("25-06-2024");
            trabajo1.setTipoContrato("Contrato");
            billetera.agregarTrabajo(trabajo1);

            Trabajo trabajo2 = new Trabajo();
            trabajo2.setEmpleado(emp2);
            trabajo2.setEmpresa(empresa2);
            trabajo2.setCargo(cargo2);
            trabajo2.setFechaIngreso("25-06-2023");
            trabajo2.setTipoContrato("Planilla");
            billetera.agregarTrabajo(trabajo2);

            // ---------- Alumno 3 (antes que los sueldos, para depositarlos en la cuenta) ----------
            Banco banco1 = new Banco();
            banco1.setCodigoBanco("00001");
            banco1.setNombreBanco("Interbank");
            billetera.agregarBanco(banco1);

            Banco banco2 = new Banco();
            banco2.setCodigoBanco("00002");
            banco2.setNombreBanco("BCP");
            billetera.agregarBanco(banco2);

            CuentaBancaria cuenta1 = new CuentaBancaria();
            cuenta1.setNumeroCuenta("000120003000");
            cuenta1.setTipoCuenta("Ahorros");
            cuenta1.setBanco(banco1);
            cuenta1.setTitular(emp1);
            billetera.agregarCuenta(cuenta1, 2500);

            TipoPrestamo tipo1 = new TipoPrestamo();
            tipo1.setNombre("Personal");
            tipo1.setDescripcion("Libre disponibilidad");
            billetera.agregarTipoPrestamo(tipo1);

            TipoPrestamo tipo2 = new TipoPrestamo();
            tipo2.setNombre("Hipotecario");
            tipo2.setDescripcion("Para vivienda");
            billetera.agregarTipoPrestamo(tipo2);

            Sueldo sueldo1 = new Sueldo();
            sueldo1.setEmpleado(emp1);
            sueldo1.setMes(mes);
            sueldo1.setAnio(anio);
            sueldo1.setMonto(3500);
            billetera.agregarSueldo(sueldo1, cuenta1);

            // ---------- Alumno 2 ----------
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
                billetera.agregarCategoria(categoria);
            }
            CategoriaGasto alimentacion = billetera.getCategorias().obtener(1);
            CategoriaGasto entretenimiento = billetera.getCategorias().obtener(4);

            Presupuesto presupuesto1 = new Presupuesto();
            presupuesto1.setEmpleado(emp1);
            presupuesto1.setCategoria(alimentacion);
            presupuesto1.setMes(mes);
            presupuesto1.setAnio(anio);
            presupuesto1.setMontoPresupuestado(800);
            billetera.agregarPresupuesto(presupuesto1);

            Presupuesto presupuesto2 = new Presupuesto();
            presupuesto2.setEmpleado(emp1);
            presupuesto2.setCategoria(entretenimiento);
            presupuesto2.setMes(mes);
            presupuesto2.setAnio(anio);
            presupuesto2.setMontoPresupuestado(100);
            billetera.agregarPresupuesto(presupuesto2);

            Gasto gasto1 = new Gasto();
            gasto1.fijarDatos(billetera.siguienteCodigoGasto(), inicioDeMes, 40, "Netflix");
            gasto1.setCategoria(entretenimiento);
            gasto1.setPersona(emp1);
            gasto1.setEsOcasional(false);
            billetera.agregarGasto(gasto1, cuenta1);

            Gasto gasto2 = new Gasto();
            gasto2.fijarDatos(billetera.siguienteCodigoGasto(), quinceDelMes, 650, "Compra mensual de comida");
            gasto2.setCategoria(alimentacion);
            gasto2.setPersona(emp1);
            gasto2.setMontoVariable(true);
            billetera.agregarGasto(gasto2, null);

        } catch (OperacionInvalidaException e) {
            // Solo pasaria si los datos de prueba estuvieran mal escritos en el codigo.
            System.out.println("Error al cargar los datos de prueba: " + e.getMessage());
        }
    }
}
