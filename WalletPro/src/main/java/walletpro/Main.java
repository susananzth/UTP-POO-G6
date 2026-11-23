package walletpro;

import walletpro.conexion.Conexion;
import walletpro.vista.MenuAcceso;

/**
 * PUNTO DE ENTRADA del programa (NetBeans lo ejecuta con F6).
 *
 * Recorrido del programa:
 *   Main -> MenuAcceso (login) -> MenuPrincipal -> MenuIngresos / MenuGastos / MenuBancos
 *   Cada menu usa las clases del paquete modelo (datos) y del paquete dao (base de datos).
 */
public class Main {

    public static void main(String[] args) {
        String mensaje = "======================================";
        mensaje += "\n        WALLET PRO - BIENVENIDO       ";
        mensaje += "\n  Billetera personal (moneda: soles)  ";
        mensaje += "\n======================================";
        System.out.println(mensaje);

        // Sin base de datos el programa no puede funcionar: se prueba la conexion primero.
        if (Conexion.getConexion() == null) {
            System.out.println("Revise que SQL Server este encendido y que haya ejecutado sql/walletpro.sql.");
            System.out.println("Los pasos estan en docs/BASE_DE_DATOS.md");
            return;
        }
        System.out.println("Conectado a la base de datos WalletPro.");

        MenuAcceso menuAcceso = new MenuAcceso();
        menuAcceso.mostrar();

        Conexion.cerrar();
        System.out.println("Gracias por usar Wallet Pro.");
    }
}
