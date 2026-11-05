package walletpro;

import java.util.NoSuchElementException;
import walletpro.datos.ArchivoBilletera;
import walletpro.datos.DatosDePrueba;
import walletpro.modelo.Billetera;
import walletpro.vista.MenuAcceso;

/**
 * Punto de entrada de WalletPro.
 *
 * Main ya no contiene la logica: solo prepara la billetera (la lee del
 * archivo o carga datos de prueba la primera vez) y abre el menu de
 * acceso. Cada modulo tiene su propio menu en el paquete vista.
 */
public class Main {

    public static void main(String[] args) {
        String mensaje = "======================================";
        mensaje += "\n        WALLET PRO - BIENVENIDO       ";
        mensaje += "\n  Billetera personal (moneda: soles)  ";
        mensaje += "\n======================================";

        Billetera billetera = ArchivoBilletera.cargar();
        if (billetera == null) {
            billetera = new Billetera();
            DatosDePrueba.cargar(billetera);
            ArchivoBilletera.guardar(billetera);
            mensaje += "\nPrimera ejecucion: se cargaron datos de prueba.";
            mensaje += "\nUsuario: Administrador | Contrasena: admin1234";
        } else {
            mensaje += "\nDatos cargados desde " + ArchivoBilletera.NOMBRE_ARCHIVO + ".";
        }
        System.out.println(mensaje);

        MenuAcceso menuAcceso = new MenuAcceso(billetera);
        try {
            menuAcceso.mostrar();
        } catch (NoSuchElementException e) {
            // Pasa si se cierra la entrada del teclado (por ejemplo, Ctrl+Z / Ctrl+D).
            System.out.println("\nSe cerro la entrada de datos. El programa terminara.");
        }

        ArchivoBilletera.guardar(billetera);
        System.out.println("Datos guardados. Gracias por usar Wallet Pro.");
    }
}
