package walletpro.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import walletpro.conexion.Conexion;
import walletpro.interfaces.IDAO;
import walletpro.modelo.CategoriaGasto;
import walletpro.modelo.Empleado;
import walletpro.modelo.Presupuesto;

/**
 * Tabla presupuesto. Lo usa: MenuGastos (Alumno 2).
 * Aqui esta el CRUD completo: insertar (Create), listar/buscar (Read),
 * actualizar (Update) y eliminar (Delete).
 */
public class PresupuestoDAO implements IDAO<Presupuesto> {

    // El SELECT con JOIN es el mismo para listar() y buscar(); solo cambia el WHERE.
    private static final String SELECT = "SELECT p.id_presupuesto, p.mes, p.anio, p.monto_limite, "
            + "e.dni, e.nombres, e.apellidos, c.id_categoria, c.nombre AS categoria "
            + "FROM presupuesto p "
            + "JOIN empleado e ON p.dni = e.dni "
            + "JOIN categoria c ON p.id_categoria = c.id_categoria ";

    // CREATE. Lo llama: MenuGastos.registrarPresupuesto()
    @Override
    public boolean insertar(Presupuesto presupuesto) {
        String sql = "INSERT INTO presupuesto (dni, id_categoria, mes, anio, monto_limite) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(sql)) {
            ps.setString(1, presupuesto.getEmpleado().getDni());
            ps.setInt(2, presupuesto.getCategoria().getIdCategoria());
            ps.setInt(3, presupuesto.getMes());
            ps.setInt(4, presupuesto.getAnio());
            ps.setDouble(5, presupuesto.getMontoLimite());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            Conexion.mostrarError(e);
            return false;
        }
    }

    // READ: todos. Lo llaman: MenuGastos (modificar, eliminar y estado de presupuestos)
    @Override
    public ArrayList<Presupuesto> listar() {
        ArrayList<Presupuesto> lista = new ArrayList<>();
        String sql = SELECT + "ORDER BY p.anio, p.mes, e.nombres";
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(crearPresupuesto(rs));
            }
        } catch (SQLException e) {
            Conexion.mostrarError(e);
        }
        return lista;
    }

    // READ: uno solo. Devuelve null si no hay presupuesto para ese empleado, categoria y mes.
    // Lo llama: MenuGastos.revisarPresupuesto()
    public Presupuesto buscar(Empleado empleado, CategoriaGasto categoria, int mes, int anio) {
        String sql = SELECT + "WHERE p.dni = ? AND p.id_categoria = ? AND p.mes = ? AND p.anio = ?";
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(sql)) {
            ps.setString(1, empleado.getDni());
            ps.setInt(2, categoria.getIdCategoria());
            ps.setInt(3, mes);
            ps.setInt(4, anio);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return crearPresupuesto(rs);
            }
        } catch (SQLException e) {
            Conexion.mostrarError(e);
        }
        return null;
    }

    // UPDATE del monto limite. Lo llama: MenuGastos.modificarPresupuesto()
    public boolean actualizar(Presupuesto presupuesto) {
        String sql = "UPDATE presupuesto SET monto_limite = ? WHERE id_presupuesto = ?";
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(sql)) {
            ps.setDouble(1, presupuesto.getMontoLimite());
            ps.setInt(2, presupuesto.getIdPresupuesto());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            Conexion.mostrarError(e);
            return false;
        }
    }

    // DELETE. Lo llama: MenuGastos.eliminarPresupuesto()
    public boolean eliminar(Presupuesto presupuesto) {
        String sql = "DELETE FROM presupuesto WHERE id_presupuesto = ?";
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(sql)) {
            ps.setInt(1, presupuesto.getIdPresupuesto());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            Conexion.mostrarError(e);
            return false;
        }
    }

    // Arma un Presupuesto con la fila actual del ResultSet. Lo usan listar() y buscar().
    private Presupuesto crearPresupuesto(ResultSet rs) throws SQLException {
        Empleado empleado = new Empleado();
        empleado.setDni(rs.getString("dni"));
        empleado.setNombres(rs.getString("nombres"));
        empleado.setApellidos(rs.getString("apellidos"));

        CategoriaGasto categoria = new CategoriaGasto();
        categoria.setIdCategoria(rs.getInt("id_categoria"));
        categoria.setNombre(rs.getString("categoria"));

        Presupuesto presupuesto = new Presupuesto();
        presupuesto.setIdPresupuesto(rs.getInt("id_presupuesto"));
        presupuesto.setMes(rs.getInt("mes"));
        presupuesto.setAnio(rs.getInt("anio"));
        presupuesto.setMontoLimite(rs.getDouble("monto_limite"));
        presupuesto.setEmpleado(empleado);
        presupuesto.setCategoria(categoria);
        return presupuesto;
    }
}
