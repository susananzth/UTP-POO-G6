package walletpro.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import walletpro.conexion.Conexion;
import walletpro.interfaces.IDAO;
import walletpro.modelo.Cargo;

/**
 * Tabla cargo. Lo usa: MenuIngresos (Alumno 1).
 * id_cargo lo genera SQL Server solo (IDENTITY), por eso no va en el INSERT.
 */
public class CargoDAO implements IDAO<Cargo> {

    // Lo llama: MenuIngresos.registrarCargo()
    @Override
    public boolean insertar(Cargo cargo) {
        String sql = "INSERT INTO cargo (nombre, descripcion) VALUES (?, ?)";
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(sql)) {
            ps.setString(1, cargo.getNombre());
            ps.setString(2, cargo.getDescripcion());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            Conexion.mostrarError(e);
            return false;
        }
    }

    // Lo llama: MenuIngresos.registrarTrabajo()
    @Override
    public ArrayList<Cargo> listar() {
        ArrayList<Cargo> lista = new ArrayList<>();
        String sql = "SELECT id_cargo, nombre, descripcion FROM cargo ORDER BY nombre";
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Cargo cargo = new Cargo();
                cargo.setIdCargo(rs.getInt("id_cargo"));
                cargo.setNombre(rs.getString("nombre"));
                cargo.setDescripcion(rs.getString("descripcion"));
                lista.add(cargo);
            }
        } catch (SQLException e) {
            Conexion.mostrarError(e);
        }
        return lista;
    }
}
