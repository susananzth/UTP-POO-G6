package walletpro.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import walletpro.conexion.Conexion;
import walletpro.modelo.Usuario;

/**
 * DAO = clase que habla con UNA tabla de la base de datos. Aqui: tabla usuario.
 * Lo usa: MenuAcceso (registrar, iniciar sesion, recuperar contrasena).
 *
 * Patron de todos los DAO (igual que el Consumidor del profesor, semana 14):
 *   1. Escribir el SQL con ? donde van los datos.
 *   2. prepareStatement(sql) y llenar cada ? con setString / setInt / setDouble.
 *   3. executeUpdate() para INSERT/UPDATE/DELETE, executeQuery() para SELECT.
 * try (...) = try-with-resources: cierra el PreparedStatement solo al terminar.
 * CONVERT(DATE, ?, 105): convierte el texto dd-mm-aaaa a fecha de SQL Server.
 */
public class UsuarioDAO {

    // Lo llama: MenuAcceso.registrarUsuario()
    public boolean insertar(Usuario usuario) {
        String sql = "INSERT INTO usuario (dni, nombres, apellidos, fecha_nacimiento, correo, nombre_usuario, "
                + "contrasena, pregunta_seguridad, respuesta_seguridad) "
                + "VALUES (?, ?, ?, CONVERT(DATE, ?, 105), ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(sql)) {
            ps.setString(1, usuario.getDni());
            ps.setString(2, usuario.getNombres());
            ps.setString(3, usuario.getApellidos());
            ps.setString(4, usuario.getFechaNacimiento());
            ps.setString(5, usuario.getCorreo());
            ps.setString(6, usuario.getNombreUsuario());
            ps.setString(7, usuario.getContrasena());
            ps.setString(8, usuario.getPreguntaSeguridad());
            ps.setString(9, usuario.getRespuestaSeguridad());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            Conexion.mostrarError(e);
            return false;
        }
    }

    // Devuelve el usuario con ese nombre, o null si no existe.
    // Lo llaman: MenuAcceso.iniciarSesion() y MenuAcceso.recuperarContrasena()
    public Usuario buscarPorNombreUsuario(String nombreUsuario) {
        String sql = "SELECT id_usuario, dni, nombres, apellidos, CONVERT(VARCHAR(10), fecha_nacimiento, 105) AS fecha, "
                + "correo, nombre_usuario, contrasena, pregunta_seguridad, respuesta_seguridad "
                + "FROM usuario WHERE nombre_usuario = ?";
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(sql)) {
            ps.setString(1, nombreUsuario);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Usuario usuario = new Usuario();
                usuario.setIdUsuario(rs.getInt("id_usuario"));
                usuario.setDni(rs.getString("dni"));
                usuario.setNombres(rs.getString("nombres"));
                usuario.setApellidos(rs.getString("apellidos"));
                usuario.setFechaNacimiento(rs.getString("fecha"));
                usuario.setCorreo(rs.getString("correo"));
                usuario.setNombreUsuario(rs.getString("nombre_usuario"));
                usuario.setContrasena(rs.getString("contrasena"));
                usuario.setPreguntaSeguridad(rs.getString("pregunta_seguridad"));
                usuario.setRespuestaSeguridad(rs.getString("respuesta_seguridad"));
                return usuario;
            }
        } catch (SQLException e) {
            Conexion.mostrarError(e);
        }
        return null;
    }

    // UPDATE de la contrasena. Lo llama: MenuAcceso.recuperarContrasena()
    public boolean actualizarContrasena(Usuario usuario) {
        String sql = "UPDATE usuario SET contrasena = ? WHERE id_usuario = ?";
        try (PreparedStatement ps = Conexion.getConexion().prepareStatement(sql)) {
            ps.setString(1, usuario.getContrasena());
            ps.setInt(2, usuario.getIdUsuario());
            return ps.executeUpdate() > 0;   // executeUpdate devuelve cuantas filas cambio
        } catch (SQLException e) {
            Conexion.mostrarError(e);
            return false;
        }
    }
}
