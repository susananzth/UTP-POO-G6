package walletpro.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import walletpro.conexion.Conexion;
import walletpro.interfaces.IDAO;
import walletpro.modelo.Banco;

/**
 * Tabla banco. Lo usa: MenuBancos (Alumno 3).
 */
public class BancoDAO implements IDAO<Banco> {

    // Lo llama: MenuBancos.registrarBanco()
    @Override
    public boolean insertar(Banco banco) {
        String sql = "INSERT INTO banco (codigo, nombre) VALUES (?, ?)";
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(sql)) {
            ps.setString(1, banco.getCodigo());
            ps.setString(2, banco.getNombre());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            Conexion.mostrarError(e);
            return false;
        }
    }

    // Lo llaman: MenuBancos.registrarCuenta() y MenuBancos.registrarPrestamo()
    @Override
    public ArrayList<Banco> listar() {
        ArrayList<Banco> lista = new ArrayList<>();
        String sql = "SELECT codigo, nombre FROM banco ORDER BY nombre";
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Banco banco = new Banco();
                banco.setCodigo(rs.getString("codigo"));
                banco.setNombre(rs.getString("nombre"));
                lista.add(banco);
            }
        } catch (SQLException e) {
            Conexion.mostrarError(e);
        }
        return lista;
    }
}
