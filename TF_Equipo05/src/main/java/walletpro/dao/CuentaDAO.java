package walletpro.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import walletpro.conexion.Conexion;
import walletpro.interfaces.IDAO;
import walletpro.modelo.Banco;
import walletpro.modelo.CuentaBancaria;
import walletpro.modelo.Empleado;
import walletpro.modelo.Movimiento;

/**
 * Tablas cuenta y movimiento. Lo usan: MenuBancos, y tambien MenuIngresos
 * (depositar el sueldo) y MenuGastos (pagar un gasto con la cuenta).
 */
public class CuentaDAO implements IDAO<CuentaBancaria> {

    // El SELECT con JOIN es el mismo para listar() y listarDe(); solo cambia el WHERE.
    private static final String SELECT = "SELECT c.numero_cuenta, c.tipo_cuenta, c.saldo, "
            + "b.codigo, b.nombre AS banco, e.dni, e.nombres, e.apellidos "
            + "FROM cuenta c "
            + "JOIN banco b ON c.codigo_banco = b.codigo "
            + "JOIN empleado e ON c.dni = e.dni ";

    // Lo llama: MenuBancos.registrarCuenta()
    @Override
    public boolean insertar(CuentaBancaria cuenta) {
        String sql = "INSERT INTO cuenta (numero_cuenta, tipo_cuenta, saldo, codigo_banco, dni) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(sql)) {
            ps.setString(1, cuenta.getNumeroCuenta());
            ps.setString(2, cuenta.getTipoCuenta());
            ps.setDouble(3, cuenta.getSaldo());
            ps.setString(4, cuenta.getBanco().getCodigo());
            ps.setString(5, cuenta.getTitular().getDni());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            Conexion.mostrarError(e);
            return false;
        }
    }

    // Todas las cuentas. Lo llama: MenuBancos.elegirCuenta()
    @Override
    public ArrayList<CuentaBancaria> listar() {
        ArrayList<CuentaBancaria> lista = new ArrayList<>();
        String sql = SELECT + "ORDER BY e.nombres";
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(crearCuenta(rs));
            }
        } catch (SQLException e) {
            Conexion.mostrarError(e);
        }
        return lista;
    }

    // Solo las cuentas de un empleado.
    // Lo llaman: MenuIngresos.registrarSueldo(), MenuGastos.registrarGasto(),
    // MenuBancos.registrarPrestamo() y MenuBancos.pagarCuota()
    public ArrayList<CuentaBancaria> listarDe(Empleado empleado) {
        ArrayList<CuentaBancaria> lista = new ArrayList<>();
        String sql = SELECT + "WHERE c.dni = ?";
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(sql)) {
            ps.setString(1, empleado.getDni());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(crearCuenta(rs));
            }
        } catch (SQLException e) {
            Conexion.mostrarError(e);
        }
        return lista;
    }

    /**
     * Guarda en la BD un deposito o retiro que ya se hizo en el objeto:
     *   1. UPDATE del saldo de la cuenta.
     *   2. INSERT del movimiento (queda en el historial).
     * Se llama DESPUES de cuenta.depositar() o cuenta.retirar().
     * Lo llaman: MenuBancos, MenuIngresos.registrarSueldo() y MenuGastos.registrarGasto()
     */
    public boolean guardarMovimiento(CuentaBancaria cuenta, Movimiento movimiento) {
        String sqlSaldo = "UPDATE cuenta SET saldo = ? WHERE numero_cuenta = ?";
        String sqlMovimiento = "INSERT INTO movimiento (numero_cuenta, fecha, tipo, monto, descripcion, saldo_resultante) "
                + "VALUES (?, CONVERT(DATE, ?, 105), ?, ?, ?, ?)";
        try (PreparedStatement psSaldo = Conexion.getConexion().prepareStatement(sqlSaldo);
             PreparedStatement psMov = Conexion.getConexion().prepareStatement(sqlMovimiento)) {
            psSaldo.setDouble(1, cuenta.getSaldo());
            psSaldo.setString(2, cuenta.getNumeroCuenta());
            psSaldo.executeUpdate();

            psMov.setString(1, movimiento.getNumeroCuenta());
            psMov.setString(2, movimiento.getFecha());
            psMov.setString(3, movimiento.getTipo());
            psMov.setDouble(4, movimiento.getMonto());
            psMov.setString(5, movimiento.getDescripcion());
            psMov.setDouble(6, movimiento.getSaldoResultante());
            psMov.executeUpdate();
            return true;
        } catch (SQLException e) {
            Conexion.mostrarError(e);
            return false;
        }
    }

    // Historial de una cuenta, del mas antiguo al mas reciente.
    // Lo llama: MenuBancos.verMovimientos()
    public ArrayList<Movimiento> listarMovimientos(CuentaBancaria cuenta) {
        ArrayList<Movimiento> lista = new ArrayList<>();
        String sql = "SELECT id_movimiento, CONVERT(VARCHAR(10), fecha, 105) AS fecha, tipo, monto, descripcion, "
                + "saldo_resultante FROM movimiento WHERE numero_cuenta = ? ORDER BY id_movimiento";
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(sql)) {
            ps.setString(1, cuenta.getNumeroCuenta());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Movimiento movimiento = new Movimiento();
                movimiento.setId(rs.getInt("id_movimiento"));
                movimiento.setNumeroCuenta(cuenta.getNumeroCuenta());
                movimiento.setFecha(rs.getString("fecha"));
                movimiento.setTipo(rs.getString("tipo"));
                movimiento.setMonto(rs.getDouble("monto"));
                movimiento.setDescripcion(rs.getString("descripcion"));
                movimiento.setSaldoResultante(rs.getDouble("saldo_resultante"));
                lista.add(movimiento);
            }
        } catch (SQLException e) {
            Conexion.mostrarError(e);
        }
        return lista;
    }

    // Arma una CuentaBancaria con la fila actual. Lo usan listar() y listarDe().
    private CuentaBancaria crearCuenta(ResultSet rs) throws SQLException {
        Banco banco = new Banco();
        banco.setCodigo(rs.getString("codigo"));
        banco.setNombre(rs.getString("banco"));

        Empleado titular = new Empleado();
        titular.setDni(rs.getString("dni"));
        titular.setNombres(rs.getString("nombres"));
        titular.setApellidos(rs.getString("apellidos"));

        CuentaBancaria cuenta = new CuentaBancaria();
        cuenta.setNumeroCuenta(rs.getString("numero_cuenta"));
        cuenta.setTipoCuenta(rs.getString("tipo_cuenta"));
        cuenta.setSaldo(rs.getDouble("saldo"));
        cuenta.setBanco(banco);
        cuenta.setTitular(titular);
        return cuenta;
    }
}
