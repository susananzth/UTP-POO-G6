package walletpro.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Abre UNA sola conexion a SQL Server y la comparte con todo el programa.
 * Es la misma idea que la clase Coneccion del profesor (semana 14), pero
 * con SQL Server en lugar de MySQL.
 *
 * Lo usan: Main (para probar la conexion al inicio) y todas las clases
 * del paquete dao (para ejecutar SQL).
 */
public class Conexion {

    // Datos de conexion. El usuario y la clave los crea el script sql/walletpro.sql
    private static final String SERVIDOR = "localhost";
    private static final String PUERTO = "1433";
    private static final String BASE_DATOS = "WalletPro";
    private static final String USUARIO = "walletpro";
    private static final String CLAVE = "WalletPro2026";

    // trustServerCertificate=true: acepta el certificado de prueba que trae SQL Server instalado en la PC
    private static final String CADENA = "jdbc:sqlserver://" + SERVIDOR + ":" + PUERTO
            + ";databaseName=" + BASE_DATOS + ";encrypt=true;trustServerCertificate=true";

    // static: es una sola conexion para todo el programa (se crea la primera vez que se pide)
    private static Connection conexion = null;

    // Devuelve la conexion. Si todavia no existe, la abre.
    // Si no se puede conectar, devuelve null.
    public static Connection getConexion() {
        if (conexion == null) {
            try {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                conexion = DriverManager.getConnection(CADENA, USUARIO, CLAVE);
            } catch (ClassNotFoundException e) {
                System.out.println("Falta el driver de SQL Server (revise el pom.xml): " + e.getMessage());
            } catch (SQLException e) {
                System.out.println("No se pudo conectar a SQL Server: " + e.getMessage());
            }
        }
        return conexion;
    }

    // Lo llama: Main, al salir del programa.
    public static void cerrar() {
        try {
            if (conexion != null) {
                conexion.close();
            }
        } catch (SQLException e) {
            System.out.println("Error al cerrar la conexion: " + e.getMessage());
        }
    }

    // Traduce los errores mas comunes de SQL Server a un mensaje claro.
    // Lo llaman: todas las clases dao, dentro de su catch (SQLException e).
    public static void mostrarError(SQLException e) {
        int codigo = e.getErrorCode();
        if (codigo == 2627 || codigo == 2601) {
            // PRIMARY KEY o UNIQUE repetido
            System.out.println("No se guardo: ya existe un registro con esos datos.");
        } else if (codigo == 547) {
            // No cumple un CHECK o una llave foranea (REFERENCES)
            System.out.println("No se guardo: el dato no cumple una regla de la base de datos.");
        } else {
            System.out.println("Error de base de datos: " + e.getMessage());
        }
    }
}
