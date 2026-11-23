package walletpro.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import walletpro.conexion.Conexion;
import walletpro.interfaces.IDAO;
import walletpro.modelo.CategoriaGasto;

/**
 * Tabla categoria. Lo usan: MenuGastos (Alumno 2) y MenuIngresos.resumenAhorro().
 */
public class CategoriaDAO implements IDAO<CategoriaGasto> {

    // Lo llama: MenuGastos.registrarCategoria()
    @Override
    public boolean insertar(CategoriaGasto categoria) {
        String sql = "INSERT INTO categoria (nombre, descripcion) VALUES (?, ?)";
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(sql)) {
            ps.setString(1, categoria.getNombre());
            ps.setString(2, categoria.getDescripcion());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            Conexion.mostrarError(e);
            return false;
        }
    }

    // Lo llaman: MenuGastos.registrarGasto() y MenuGastos.registrarPresupuesto()
    @Override
    public ArrayList<CategoriaGasto> listar() {
        ArrayList<CategoriaGasto> lista = new ArrayList<>();
        String sql = "SELECT id_categoria, nombre, descripcion FROM categoria ORDER BY nombre";
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                CategoriaGasto categoria = new CategoriaGasto();
                categoria.setIdCategoria(rs.getInt("id_categoria"));
                categoria.setNombre(rs.getString("nombre"));
                categoria.setDescripcion(rs.getString("descripcion"));
                lista.add(categoria);
            }
        } catch (SQLException e) {
            Conexion.mostrarError(e);
        }
        return lista;
    }
}
