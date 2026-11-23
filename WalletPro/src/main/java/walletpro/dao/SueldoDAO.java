package walletpro.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import walletpro.conexion.Conexion;
import walletpro.modelo.Empleado;
import walletpro.modelo.Sueldo;

/**
 * Tabla sueldo. Lo usa: MenuIngresos (Alumno 1).
 * La tabla tiene UNIQUE (dni, mes, anio): no deja registrar dos sueldos del mismo mes.
 */
public class SueldoDAO {

    // Lo llama: MenuIngresos.registrarSueldo()
    public boolean insertar(Sueldo sueldo) {
        String sql = "INSERT INTO sueldo (dni, mes, anio, monto) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(sql)) {
            ps.setString(1, sueldo.getEmpleado().getDni());
            ps.setInt(2, sueldo.getMes());
            ps.setInt(3, sueldo.getAnio());
            ps.setDouble(4, sueldo.getMonto());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            Conexion.mostrarError(e);
            return false;
        }
    }

    // Todos los sueldos de un empleado, del mas antiguo al mas reciente.
    // Lo llaman: MenuIngresos.consultarIngresos() y MenuIngresos.resumenAhorro()
    public ArrayList<Sueldo> listarDe(Empleado empleado) {
        ArrayList<Sueldo> lista = new ArrayList<>();
        String sql = "SELECT id_sueldo, mes, anio, monto FROM sueldo WHERE dni = ? ORDER BY anio, mes";
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(sql)) {
            ps.setString(1, empleado.getDni());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Sueldo sueldo = new Sueldo();
                sueldo.setIdSueldo(rs.getInt("id_sueldo"));
                sueldo.setEmpleado(empleado);
                sueldo.setMes(rs.getInt("mes"));
                sueldo.setAnio(rs.getInt("anio"));
                sueldo.setMonto(rs.getDouble("monto"));
                lista.add(sueldo);
            }
        } catch (SQLException e) {
            Conexion.mostrarError(e);
        }
        return lista;
    }
}
