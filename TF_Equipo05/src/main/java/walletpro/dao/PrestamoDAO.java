package walletpro.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import walletpro.conexion.Conexion;
import walletpro.interfaces.IDAO;
import walletpro.modelo.Banco;
import walletpro.modelo.Empleado;
import walletpro.modelo.Prestamo;
import walletpro.modelo.TipoPrestamo;

/**
 * Tabla prestamo. Lo usa: MenuBancos (Alumno 3).
 */
public class PrestamoDAO implements IDAO<Prestamo> {

    // Lo llama: MenuBancos.registrarPrestamo()
    @Override
    public boolean insertar(Prestamo prestamo) {
        String sql = "INSERT INTO prestamo (dni, codigo_banco, id_tipo, monto, tasa_mensual, numero_cuotas, "
                + "cuotas_pagadas, saldo_pendiente) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(sql)) {
            ps.setString(1, prestamo.getDeudor().getDni());
            ps.setString(2, prestamo.getBanco().getCodigo());
            ps.setInt(3, prestamo.getTipo().getIdTipo());
            ps.setDouble(4, prestamo.getMonto());
            ps.setDouble(5, prestamo.getTasaMensual());
            ps.setInt(6, prestamo.getNumeroCuotas());
            ps.setInt(7, prestamo.getCuotasPagadas());
            ps.setDouble(8, prestamo.getSaldoPendiente());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            Conexion.mostrarError(e);
            return false;
        }
    }

    // Lo llaman: MenuBancos.pagarCuota() y MenuBancos.listarPrestamos()
    @Override
    public ArrayList<Prestamo> listar() {
        ArrayList<Prestamo> lista = new ArrayList<>();
        String sql = "SELECT p.id_prestamo, p.monto, p.tasa_mensual, p.numero_cuotas, p.cuotas_pagadas, "
                + "p.saldo_pendiente, e.dni, e.nombres, e.apellidos, b.codigo, b.nombre AS banco, "
                + "t.id_tipo, t.nombre AS tipo "
                + "FROM prestamo p "
                + "JOIN empleado e ON p.dni = e.dni "
                + "JOIN banco b ON p.codigo_banco = b.codigo "
                + "JOIN tipo_prestamo t ON p.id_tipo = t.id_tipo "
                + "ORDER BY p.id_prestamo";
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Empleado deudor = new Empleado();
                deudor.setDni(rs.getString("dni"));
                deudor.setNombres(rs.getString("nombres"));
                deudor.setApellidos(rs.getString("apellidos"));

                Banco banco = new Banco();
                banco.setCodigo(rs.getString("codigo"));
                banco.setNombre(rs.getString("banco"));

                TipoPrestamo tipo = new TipoPrestamo();
                tipo.setIdTipo(rs.getInt("id_tipo"));
                tipo.setNombre(rs.getString("tipo"));

                Prestamo prestamo = new Prestamo();
                prestamo.setIdPrestamo(rs.getInt("id_prestamo"));
                prestamo.setMonto(rs.getDouble("monto"));
                prestamo.setTasaMensual(rs.getDouble("tasa_mensual"));
                prestamo.setNumeroCuotas(rs.getInt("numero_cuotas"));
                prestamo.setCuotasPagadas(rs.getInt("cuotas_pagadas"));
                prestamo.setSaldoPendiente(rs.getDouble("saldo_pendiente"));
                prestamo.setDeudor(deudor);
                prestamo.setBanco(banco);
                prestamo.setTipo(tipo);
                lista.add(prestamo);
            }
        } catch (SQLException e) {
            Conexion.mostrarError(e);
        }
        return lista;
    }

    // UPDATE despues de pagar una cuota. Lo llama: MenuBancos.pagarCuota()
    public boolean actualizarPago(Prestamo prestamo) {
        String sql = "UPDATE prestamo SET cuotas_pagadas = ?, saldo_pendiente = ? WHERE id_prestamo = ?";
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(sql)) {
            ps.setInt(1, prestamo.getCuotasPagadas());
            ps.setDouble(2, prestamo.getSaldoPendiente());
            ps.setInt(3, prestamo.getIdPrestamo());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            Conexion.mostrarError(e);
            return false;
        }
    }
}
