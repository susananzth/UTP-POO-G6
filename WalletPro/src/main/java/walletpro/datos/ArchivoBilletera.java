package walletpro.datos;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import walletpro.modelo.Billetera;

/**
 * PERSISTENCIA por serializacion: guarda la Billetera completa (con
 * todos sus usuarios, gastos, cuentas, prestamos...) en un archivo y
 * la vuelve a leer al abrir el programa. Asi los datos no se pierden
 * al cerrar.
 *
 * Para que un objeto se pueda guardar asi, su clase debe implementar
 * la interfaz Serializable (por eso todas las clases del modelo la tienen).
 *
 * Se usa try-with-resources: try (ObjectOutputStream salida = ...) cierra
 * el archivo automaticamente al terminar, aunque ocurra un error.
 */
public class ArchivoBilletera {

    /** Se crea en la carpeta del proyecto (NetBeans ejecuta desde ahi). */
    public static final String NOMBRE_ARCHIVO = "walletpro.dat";

    public static boolean existe() {
        return new File(NOMBRE_ARCHIVO).exists();
    }

    public static void guardar(Billetera billetera) {
        try (ObjectOutputStream salida = new ObjectOutputStream(new FileOutputStream(NOMBRE_ARCHIVO))) {
            salida.writeObject(billetera);
        } catch (IOException e) {
            System.out.println("No se pudieron guardar los datos: " + e.getMessage());
        }
    }

    /**
     * Lee la billetera guardada. Si el archivo no existe o no se puede
     * leer (por ejemplo, porque cambio el codigo de una clase), devuelve
     * null y el programa empieza con los datos de prueba.
     */
    public static Billetera cargar() {
        if (!existe()) {
            return null;
        }
        try (ObjectInputStream entrada = new ObjectInputStream(new FileInputStream(NOMBRE_ARCHIVO))) {
            return (Billetera) entrada.readObject();
        } catch (IOException | ClassNotFoundException | ClassCastException e) {
            System.out.println("No se pudo leer " + NOMBRE_ARCHIVO + " (" + e.getMessage()
                    + "). Se empezara con los datos de prueba.");
            return null;
        }
    }
}
