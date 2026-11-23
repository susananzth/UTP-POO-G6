package walletpro.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import walletpro.conexion.Conexion;
import walletpro.modelo.CategoriaGasto;
import walletpro.modelo.Empleado;
import walletpro.modelo.Gasto;

/**
 * Tabla gasto. Lo usan: MenuGastos (Alumno 2) y MenuIngresos.resumenAhorro().
 */
public class GastoDAO {

    // Lo llama: MenuGastos.registrarGasto()
    public boolean insertar(Gasto gasto) {
        String sql = "INSERT INTO gasto (dni, id_categoria, fecha, monto, descripcion, es_ocasional, numero_cuenta) "
                + "VALUES (?, ?, CONVERT(DATE, ?, 105), ?, ?, ?, ?)";
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(sql)) {
            ps.setString(1, gasto.getEmpleado().getDni());
            ps.setInt(2, gasto.getCategoria().getIdCategoria());
            ps.setString(3, gasto.getFecha());
            ps.setDouble(4, gasto.getMonto());
            ps.setString(5, gasto.getDescripcion());
            ps.setBoolean(6, gasto.isEsOcasional());
            ps.setString(7, gasto.getNumeroCuentaPago());   // si es null, se guarda NULL (efectivo)
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            Conexion.mostrarError(e);
            return false;
        }
    }

    // Gastos de un empleado en un mes. MONTH() y YEAR() sacan el mes y el anio de la fecha.
    // Lo llaman: MenuGastos (listar, presupuestos) y MenuIngresos.resumenAhorro()
    public ArrayList<Gasto> listarDelMes(Empleado empleado, int mes, int anio) {
        ArrayList<Gasto> lista = new ArrayList<>();
        String sql = "SELECT g.id_gasto, CONVERT(VARCHAR(10), g.fecha, 105) AS fecha, g.monto, g.descripcion, "
                + "g.es_ocasional, g.numero_cuenta, c.id_categoria, c.nombre AS categoria "
                + "FROM gasto g JOIN categoria c ON g.id_categoria = c.id_categoria "
                + "WHERE g.dni = ? AND MONTH(g.fecha) = ? AND YEAR(g.fecha) = ? "
                + "ORDER BY g.fecha";
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(sql)) {
            ps.setString(1, empleado.getDni());
            ps.setInt(2, mes);
            ps.setInt(3, anio);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                CategoriaGasto categoria = new CategoriaGasto();
                categoria.setIdCategoria(rs.getInt("id_categoria"));
                categoria.setNombre(rs.getString("categoria"));

                Gasto gasto = new Gasto();
                gasto.setId(rs.getInt("id_gasto"));
                gasto.setFecha(rs.getString("fecha"));
                gasto.setMonto(rs.getDouble("monto"));
                gasto.setDescripcion(rs.getString("descripcion"));
                gasto.setEsOcasional(rs.getBoolean("es_ocasional"));
                gasto.setNumeroCuentaPago(rs.getString("numero_cuenta"));
                gasto.setCategoria(categoria);
                gasto.setEmpleado(empleado);
                lista.add(gasto);
            }
        } catch (SQLException e) {
            Conexion.mostrarError(e);
        }
        return lista;
    }
}
