package walletpro;

import java.util.NoSuchElementException;
import walletpro.datos.DatosDePrueba;
import walletpro.modelo.Billetera;
import walletpro.vista.MenuAcceso;

/**
 * Punto de entrada de WalletPro.
 *
 * Main ya no contiene la logica: solo prepara la billetera y abre el
 * menu de acceso. Cada modulo tiene su propio menu en el paquete vista.
 */
public class Main {

    public static void main(String[] args) {
        Billetera billetera = new Billetera();
        DatosDePrueba.cargar(billetera);

        String mensaje = "======================================";
        mensaje += "\n        WALLET PRO - BIENVENIDO       ";
        mensaje += "\n  Billetera personal (moneda: soles)  ";
        mensaje += "\n======================================";
        System.out.println(mensaje);

        MenuAcceso menuAcceso = new MenuAcceso(billetera);
        try {
            menuAcceso.mostrar();
        } catch (NoSuchElementException e) {
            // Pasa si se cierra la entrada del teclado (por ejemplo, Ctrl+Z / Ctrl+D).
            System.out.println("\nSe cerro la entrada de datos. El programa terminara.");
        }

        System.out.println("Gracias por usar Wallet Pro.");
    }
}
