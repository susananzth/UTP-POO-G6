package walletpro.modelo;

import java.util.List;
import walletpro.datos.Repositorio;
import walletpro.excepciones.OperacionInvalidaException;
import walletpro.util.ValidFecha;

/**
 * La BILLETERA es el objeto central de la aplicacion: guarda todos los
 * datos (usuarios, empleados, gastos, cuentas, prestamos...) y aplica
 * las reglas del negocio antes de aceptar un dato nuevo:
 *   - que el objeto este completo (validar()),
 *   - que no este repetido (mismo DNI, mismo RUC, mismo numero de cuenta...),
 *   - que el dinero alcance (si se paga con una cuenta).
 *
 * Asi los menus solo piden datos y muestran resultados, y las reglas
 * estan en un solo lugar (alta cohesion, bajo acoplamiento).
 *
 * Las consultas usan streams y lambdas: filter() para quedarse con
 * algunos elementos, mapToDouble() para tomar los montos y sum() para sumarlos.
 */
public class Billetera {

    private Repositorio<Usuario> usuarios;
    private Repositorio<Empresa> empresas;
    private Repositorio<Cargo> cargos;
    private Repositorio<Empleado> empleados;
    private Repositorio<Trabajo> trabajos;
    private Repositorio<Sueldo> sueldos;
    private Repositorio<CategoriaGasto> categorias;
    private Repositorio<Gasto> gastos;
    private Repositorio<Presupuesto> presupuestos;
    private Repositorio<Banco> bancos;
    private Repositorio<CuentaBancaria> cuentas;
    private Repositorio<TipoPrestamo> tiposPrestamo;
    private Repositorio<Prestamo> prestamos;

    /** Contadores para generar codigos unicos: G0001, G0002... / P0001, P0002... */
    private int contadorGastos;
    private int contadorPrestamos;

    public Billetera() {
        this.usuarios = new Repositorio<>();
        this.empresas = new Repositorio<>();
        this.cargos = new Repositorio<>();
        this.empleados = new Repositorio<>();
        this.trabajos = new Repositorio<>();
        this.sueldos = new Repositorio<>();
        this.categorias = new Repositorio<>();
        this.gastos = new Repositorio<>();
        this.presupuestos = new Repositorio<>();
        this.bancos = new Repositorio<>();
        this.cuentas = new Repositorio<>();
        this.tiposPrestamo = new Repositorio<>();
        this.prestamos = new Repositorio<>();
        this.contadorGastos = 0;
        this.contadorPrestamos = 0;
    }

    public Repositorio<Usuario> getUsuarios() { return usuarios; }
    public Repositorio<Empresa> getEmpresas() { return empresas; }
    public Repositorio<Cargo> getCargos() { return cargos; }
    public Repositorio<Empleado> getEmpleados() { return empleados; }
    public Repositorio<Trabajo> getTrabajos() { return trabajos; }
    public Repositorio<Sueldo> getSueldos() { return sueldos; }
    public Repositorio<CategoriaGasto> getCategorias() { return categorias; }
    public Repositorio<Gasto> getGastos() { return gastos; }
    public Repositorio<Presupuesto> getPresupuestos() { return presupuestos; }
    public Repositorio<Banco> getBancos() { return bancos; }
    public Repositorio<CuentaBancaria> getCuentas() { return cuentas; }
    public Repositorio<TipoPrestamo> getTiposPrestamo() { return tiposPrestamo; }
    public Repositorio<Prestamo> getPrestamos() { return prestamos; }

    public String siguienteCodigoGasto() {
        return String.format("G%04d", contadorGastos + 1);
    }

    public String siguienteCodigoPrestamo() {
        return String.format("P%04d", contadorPrestamos + 1);
    }

    // ======================= USUARIOS =======================

    public void agregarUsuario(Usuario nuevo) throws OperacionInvalidaException {
        if (!nuevo.validar()) {
            throw new OperacionInvalidaException("Los datos del usuario estan incompletos.");
        }
        if (usuarios.existe(u -> u.getNombreUsuario().equalsIgnoreCase(nuevo.getNombreUsuario()))) {
            throw new OperacionInvalidaException("El nombre de usuario '" + nuevo.getNombreUsuario() + "' ya existe.");
        }
        if (usuarios.existe(u -> u.getCorreo().equalsIgnoreCase(nuevo.getCorreo()))) {
            throw new OperacionInvalidaException("El correo " + nuevo.getCorreo() + " ya esta registrado.");
        }
        nuevo.setIdUsuario(usuarios.tamanio() + 1);
        usuarios.agregar(nuevo);
    }

    // ============ ALUMNO 1: INGRESOS Y AHORROS ============

    public void agregarEmpresa(Empresa empresa) throws OperacionInvalidaException {
        if (!empresa.validar()) {
            throw new OperacionInvalidaException("Los datos de la empresa estan incompletos.");
        }
        if (empresas.existe(e -> e.getRuc().equals(empresa.getRuc()))) {
            throw new OperacionInvalidaException("Ya existe una empresa con el RUC " + empresa.getRuc() + ".");
        }
        empresas.agregar(empresa);
    }

    public void agregarCargo(Cargo cargo) throws OperacionInvalidaException {
        if (!cargo.validar()) {
            throw new OperacionInvalidaException("El nombre del cargo es obligatorio.");
        }
        if (cargos.existe(c -> c.getNombreCargo().equalsIgnoreCase(cargo.getNombreCargo()))) {
            throw new OperacionInvalidaException("El cargo '" + cargo.getNombreCargo() + "' ya existe.");
        }
        cargos.agregar(cargo);
    }

    public void agregarEmpleado(Empleado empleado) throws OperacionInvalidaException {
        if (!empleado.validar()) {
            throw new OperacionInvalidaException("Los datos del empleado estan incompletos.");
        }
        if (empleados.existe(e -> e.getDni().equals(empleado.getDni()))) {
            throw new OperacionInvalidaException("Ya existe un empleado con el DNI " + empleado.getDni() + ".");
        }
        empleados.agregar(empleado);
    }

    public void agregarTrabajo(Trabajo trabajo) throws OperacionInvalidaException {
        if (!trabajo.validar()) {
            throw new OperacionInvalidaException("Los datos del trabajo estan incompletos.");
        }
        boolean repetido = trabajos.existe(t -> t.getEmpleado() == trabajo.getEmpleado()
                && t.getEmpresa() == trabajo.getEmpresa()
                && t.getCargo() == trabajo.getCargo());
        if (repetido) {
            throw new OperacionInvalidaException("Ese empleado ya tiene registrado ese cargo en esa empresa.");
        }
        trabajos.agregar(trabajo);
    }

    /**
     * Registra el sueldo del mes. Si se indica una cuenta, el sueldo se
     * deposita en ella (queda como movimiento de la cuenta).
     */
    public void agregarSueldo(Sueldo sueldo, CuentaBancaria cuentaDestino) throws OperacionInvalidaException {
        if (!sueldo.validar()) {
            throw new OperacionInvalidaException("Los datos del sueldo estan incompletos.");
        }
        boolean repetido = sueldos.existe(s -> s.getEmpleado() == sueldo.getEmpleado()
                && s.getMes() == sueldo.getMes() && s.getAnio() == sueldo.getAnio());
        if (repetido) {
            throw new OperacionInvalidaException("Ya se registro el sueldo de " + sueldo.getPeriodo()
                    + " para " + sueldo.getEmpleado().getNombreCompleto() + ".");
        }
        if (cuentaDestino != null) {
            cuentaDestino.depositar(sueldo.getMonto(), "Sueldo " + sueldo.getPeriodo());
        }
        sueldos.agregar(sueldo);
    }

    public List<Sueldo> sueldosDe(Empleado empleado) {
        return sueldos.filtrar(s -> s.getEmpleado() == empleado);
    }

    /** Suma de los sueldos de un empleado en un mes (stream: filter + mapToDouble + sum). */
    public double totalIngresosDelMes(Empleado empleado, int mes, int anio) {
        return sueldos.listar().stream()
                .filter(s -> s.getEmpleado() == empleado && s.getMes() == mes && s.getAnio() == anio)
                .mapToDouble(s -> s.getMonto())
                .sum();
    }

    // ============ ALUMNO 2: GASTOS Y PRESUPUESTO ============

    public void agregarCategoria(CategoriaGasto categoria) throws OperacionInvalidaException {
        if (!categoria.validar()) {
            throw new OperacionInvalidaException("El nombre de la categoria es obligatorio.");
        }
        if (categorias.existe(c -> c.getNombreCategoria().equalsIgnoreCase(categoria.getNombreCategoria()))) {
            throw new OperacionInvalidaException("La categoria '" + categoria.getNombreCategoria() + "' ya existe.");
        }
        categorias.agregar(categoria);
    }

    /**
     * Registra un gasto. Si se paga con una cuenta, primero se retira el
     * dinero: si no alcanza, retirar() lanza la excepcion y el gasto NO
     * se registra (no queda un gasto "pagado" sin dinero).
     */
    public void agregarGasto(Gasto gasto, CuentaBancaria cuentaPago) throws OperacionInvalidaException {
        if (!gasto.validar()) {
            throw new OperacionInvalidaException("Los datos del gasto estan incompletos.");
        }
        if (cuentaPago != null) {
            if (cuentaPago.getTitular() != gasto.getPersona()) {
                throw new OperacionInvalidaException("Solo se puede pagar con una cuenta de la misma persona.");
            }
            cuentaPago.retirar(gasto.getMonto(), "Gasto " + gasto.getIdTransaccion() + ": " + gasto.getDescripcion());
            gasto.setCuentaPago(cuentaPago);
        }
        gastos.agregar(gasto);
        contadorGastos++;
    }

    public void agregarPresupuesto(Presupuesto presupuesto) throws OperacionInvalidaException {
        if (!presupuesto.validar()) {
            throw new OperacionInvalidaException("Los datos del presupuesto estan incompletos.");
        }
        Presupuesto existente = buscarPresupuesto(presupuesto.getEmpleado(), presupuesto.getCategoria(),
                presupuesto.getMes(), presupuesto.getAnio());
        if (existente != null) {
            throw new OperacionInvalidaException("Ya existe un presupuesto de "
                    + presupuesto.getCategoria().getNombreCategoria() + " para " + presupuesto.getPeriodo()
                    + ". Use la opcion de modificar presupuesto.");
        }
        presupuestos.agregar(presupuesto);
    }

    public Presupuesto buscarPresupuesto(Empleado empleado, CategoriaGasto categoria, int mes, int anio) {
        return presupuestos.buscar(p -> p.getEmpleado() == empleado && p.getCategoria() == categoria
                && p.getMes() == mes && p.getAnio() == anio);
    }

    /** Cuanto se gasto en la categoria y mes de un presupuesto. */
    public double calcularGastado(Presupuesto presupuesto) {
        return gastos.listar().stream()
                .filter(g -> g.getPersona() == presupuesto.getEmpleado()
                        && g.getCategoria() == presupuesto.getCategoria()
                        && ValidFecha.esDelMes(g.getFecha(), presupuesto.getMes(), presupuesto.getAnio()))
                .mapToDouble(g -> g.getMonto())
                .sum();
    }

    public List<Gasto> gastosDelMes(Empleado empleado, int mes, int anio) {
        return gastos.filtrar(g -> g.getPersona() == empleado
                && ValidFecha.esDelMes(g.getFecha(), mes, anio));
    }

    public double totalGastosDelMes(Empleado empleado, int mes, int anio) {
        return gastosDelMes(empleado, mes, anio).stream()
                .mapToDouble(g -> g.getMonto())
                .sum();
    }

    public double totalGastosDelMesPorCategoria(Empleado empleado, CategoriaGasto categoria, int mes, int anio) {
        return gastosDelMes(empleado, mes, anio).stream()
                .filter(g -> g.getCategoria() == categoria)
                .mapToDouble(g -> g.getMonto())
                .sum();
    }

    // ============ ALUMNO 3: BANCOS Y PRESTAMOS ============

    public void agregarBanco(Banco banco) throws OperacionInvalidaException {
        if (!banco.validar()) {
            throw new OperacionInvalidaException("Los datos del banco estan incompletos.");
        }
        if (bancos.existe(b -> b.getCodigoBanco().equals(banco.getCodigoBanco()))) {
            throw new OperacionInvalidaException("Ya existe un banco con el codigo " + banco.getCodigoBanco() + ".");
        }
        bancos.agregar(banco);
    }

    /** Registra la cuenta y, si hay saldo inicial, lo deposita (queda en el historial). */
    public void agregarCuenta(CuentaBancaria cuenta, double saldoInicial) throws OperacionInvalidaException {
        if (!cuenta.validar()) {
            throw new OperacionInvalidaException("Los datos de la cuenta estan incompletos.");
        }
        if (cuentas.existe(c -> c.getNumeroCuenta().equals(cuenta.getNumeroCuenta()))) {
            throw new OperacionInvalidaException("Ya existe una cuenta con el numero " + cuenta.getNumeroCuenta() + ".");
        }
        if (saldoInicial > 0) {
            cuenta.depositar(saldoInicial, "Saldo inicial");
        }
        cuentas.agregar(cuenta);
    }

    public List<CuentaBancaria> cuentasDe(Empleado empleado) {
        return cuentas.filtrar(c -> c.getTitular() == empleado);
    }

    public void agregarTipoPrestamo(TipoPrestamo tipo) throws OperacionInvalidaException {
        if (!tipo.validar()) {
            throw new OperacionInvalidaException("El nombre del tipo de prestamo es obligatorio.");
        }
        if (tiposPrestamo.existe(t -> t.getNombre().equalsIgnoreCase(tipo.getNombre()))) {
            throw new OperacionInvalidaException("El tipo de prestamo '" + tipo.getNombre() + "' ya existe.");
        }
        tiposPrestamo.agregar(tipo);
    }

    /** Registra el prestamo y, si se indica una cuenta, deposita en ella el dinero prestado. */
    public void agregarPrestamo(Prestamo prestamo, CuentaBancaria cuentaDesembolso) throws OperacionInvalidaException {
        if (!prestamo.validar()) {
            throw new OperacionInvalidaException("Los datos del prestamo estan incompletos.");
        }
        if (cuentaDesembolso != null) {
            if (cuentaDesembolso.getTitular() != prestamo.getDeudor()) {
                throw new OperacionInvalidaException("El dinero solo se puede depositar en una cuenta del deudor.");
            }
            cuentaDesembolso.depositar(prestamo.getMontoOriginal(),
                    "Desembolso del prestamo " + prestamo.getCodigoPrestamo());
        }
        prestamos.agregar(prestamo);
        contadorPrestamos++;
    }

    public List<Prestamo> prestamosPendientes() {
        return prestamos.filtrar(p -> !p.estaPagado());
    }
}
