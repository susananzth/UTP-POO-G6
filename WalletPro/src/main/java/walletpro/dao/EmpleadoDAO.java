package walletpro.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import walletpro.conexion.Conexion;
import walletpro.interfaces.IDAO;
import walletpro.modelo.Empleado;

/**
 * Tabla empleado. Lo usan: MenuIngresos, MenuGastos y MenuBancos
 * (todos necesitan elegir un empleado de la lista).
 */
public class EmpleadoDAO implements IDAO<Empleado> {

    // Lo llama: MenuIngresos.registrarEmpleado()
    @Override
    public boolean insertar(Empleado empleado) {
        String sql = "INSERT INTO empleado (dni, nombres, apellidos, fecha_nacimiento, correo, sueldo_base) "
                + "VALUES (?, ?, ?, CONVERT(DATE, ?, 105), ?, ?)";
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(sql)) {
            ps.setString(1, empleado.getDni());
            ps.setString(2, empleado.getNombres());
            ps.setString(3, empleado.getApellidos());
            ps.setString(4, empleado.getFechaNacimiento());
            ps.setString(5, empleado.getCorreo());
            ps.setDouble(6, empleado.getSueldoBase());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            Conexion.mostrarError(e);
            return false;
        }
    }

    // Lo llaman: los tres menus de modulo, antes de Consola.seleccionar("Empleados:", ...)
    @Override
    public ArrayList<Empleado> listar() {
        ArrayList<Empleado> lista = new ArrayList<>();
        String sql = "SELECT dni, nombres, apellidos, CONVERT(VARCHAR(10), fecha_nacimiento, 105) AS fecha, "
                + "correo, sueldo_base FROM empleado ORDER BY nombres";
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Empleado empleado = new Empleado();
                empleado.setDni(rs.getString("dni"));
                empleado.setNombres(rs.getString("nombres"));
                empleado.setApellidos(rs.getString("apellidos"));
                empleado.setFechaNacimiento(rs.getString("fecha"));
                empleado.setCorreo(rs.getString("correo"));
                empleado.setSueldoBase(rs.getDouble("sueldo_base"));
                lista.add(empleado);
            }
        } catch (SQLException e) {
            Conexion.mostrarError(e);
        }
        return lista;
    }
}
