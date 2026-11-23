package walletpro.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import walletpro.conexion.Conexion;
import walletpro.interfaces.IDAO;
import walletpro.modelo.Cargo;
import walletpro.modelo.Empleado;
import walletpro.modelo.Empresa;
import walletpro.modelo.Trabajo;

/**
 * Tabla trabajo. Lo usa: MenuIngresos (Alumno 1).
 * La tabla solo guarda el dni, el ruc y el id_cargo (llaves foraneas).
 * Para mostrar los nombres, listar() hace JOIN con empleado, empresa y cargo.
 */
public class TrabajoDAO implements IDAO<Trabajo> {

    // Lo llama: MenuIngresos.registrarTrabajo()
    @Override
    public boolean insertar(Trabajo trabajo) {
        String sql = "INSERT INTO trabajo (dni, ruc, id_cargo, fecha_ingreso, tipo_contrato) "
                + "VALUES (?, ?, ?, CONVERT(DATE, ?, 105), ?)";
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(sql)) {
            ps.setString(1, trabajo.getEmpleado().getDni());
            ps.setString(2, trabajo.getEmpresa().getRuc());
            ps.setInt(3, trabajo.getCargo().getIdCargo());
            ps.setString(4, trabajo.getFechaIngreso());
            ps.setString(5, trabajo.getTipoContrato());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            Conexion.mostrarError(e);
            return false;
        }
    }

    // Lo llama: MenuIngresos.listarTrabajos()
    @Override
    public ArrayList<Trabajo> listar() {
        ArrayList<Trabajo> lista = new ArrayList<>();
        String sql = "SELECT t.id_trabajo, CONVERT(VARCHAR(10), t.fecha_ingreso, 105) AS fecha, t.tipo_contrato, "
                + "e.dni, e.nombres, e.apellidos, em.ruc, em.razon_social, c.id_cargo, c.nombre AS cargo "
                + "FROM trabajo t "
                + "JOIN empleado e ON t.dni = e.dni "
                + "JOIN empresa em ON t.ruc = em.ruc "
                + "JOIN cargo c ON t.id_cargo = c.id_cargo "
                + "ORDER BY e.nombres";
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Empleado empleado = new Empleado();
                empleado.setDni(rs.getString("dni"));
                empleado.setNombres(rs.getString("nombres"));
                empleado.setApellidos(rs.getString("apellidos"));

                Empresa empresa = new Empresa();
                empresa.setRuc(rs.getString("ruc"));
                empresa.setRazonSocial(rs.getString("razon_social"));

                Cargo cargo = new Cargo();
                cargo.setIdCargo(rs.getInt("id_cargo"));
                cargo.setNombre(rs.getString("cargo"));

                Trabajo trabajo = new Trabajo();
                trabajo.setIdTrabajo(rs.getInt("id_trabajo"));
                trabajo.setFechaIngreso(rs.getString("fecha"));
                trabajo.setTipoContrato(rs.getString("tipo_contrato"));
                trabajo.setEmpleado(empleado);
                trabajo.setEmpresa(empresa);
                trabajo.setCargo(cargo);
                lista.add(trabajo);
            }
        } catch (SQLException e) {
            Conexion.mostrarError(e);
        }
        return lista;
    }
}
