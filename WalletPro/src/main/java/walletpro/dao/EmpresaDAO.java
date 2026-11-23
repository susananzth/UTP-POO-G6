package walletpro.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import walletpro.conexion.Conexion;
import walletpro.interfaces.IDAO;
import walletpro.modelo.Empresa;

/**
 * Tabla empresa. Lo usa: MenuIngresos (Alumno 1).
 * Si el RUC ya existe, SQL Server rechaza el INSERT (PRIMARY KEY)
 * y Conexion.mostrarError() avisa "ya existe".
 */
public class EmpresaDAO implements IDAO<Empresa> {

    // Lo llama: MenuIngresos.registrarEmpresa()
    @Override
    public boolean insertar(Empresa empresa) {
        String sql = "INSERT INTO empresa (ruc, razon_social) VALUES (?, ?)";
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(sql)) {
            ps.setString(1, empresa.getRuc());
            ps.setString(2, empresa.getRazonSocial());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            Conexion.mostrarError(e);
            return false;
        }
    }

    // Lo llama: MenuIngresos.registrarTrabajo()
    @Override
    public ArrayList<Empresa> listar() {
        ArrayList<Empresa> lista = new ArrayList<>();
        String sql = "SELECT ruc, razon_social FROM empresa ORDER BY razon_social";
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {   // rs.next() avanza a la siguiente fila
                Empresa empresa = new Empresa();
                empresa.setRuc(rs.getString("ruc"));
                empresa.setRazonSocial(rs.getString("razon_social"));
                lista.add(empresa);
            }
        } catch (SQLException e) {
            Conexion.mostrarError(e);
        }
        return lista;
    }
}
