package walletpro.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import walletpro.conexion.Conexion;
import walletpro.interfaces.IDAO;
import walletpro.modelo.TipoPrestamo;

/**
 * Tabla tipo_prestamo. Lo usa: MenuBancos (Alumno 3).
 */
public class TipoPrestamoDAO implements IDAO<TipoPrestamo> {

    // Lo llama: MenuBancos.registrarTipoPrestamo()
    @Override
    public boolean insertar(TipoPrestamo tipo) {
        String sql = "INSERT INTO tipo_prestamo (nombre, descripcion) VALUES (?, ?)";
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(sql)) {
            ps.setString(1, tipo.getNombre());
            ps.setString(2, tipo.getDescripcion());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            Conexion.mostrarError(e);
            return false;
        }
    }

    // Lo llama: MenuBancos.registrarPrestamo()
    @Override
    public ArrayList<TipoPrestamo> listar() {
        ArrayList<TipoPrestamo> lista = new ArrayList<>();
        String sql = "SELECT id_tipo, nombre, descripcion FROM tipo_prestamo ORDER BY nombre";
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                TipoPrestamo tipo = new TipoPrestamo();
                tipo.setIdTipo(rs.getInt("id_tipo"));
                tipo.setNombre(rs.getString("nombre"));
                tipo.setDescripcion(rs.getString("descripcion"));
                lista.add(tipo);
            }
        } catch (SQLException e) {
            Conexion.mostrarError(e);
        }
        return lista;
    }
}
