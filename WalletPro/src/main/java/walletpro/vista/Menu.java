package walletpro.vista;

import walletpro.modelo.Billetera;
import walletpro.util.Consola;

/**
 * Clase ABSTRACTA con lo que todos los menus hacen igual:
 * mostrar el titulo y las opciones, leer la opcion elegida,
 * ejecutarla y repetir hasta que el usuario elija 0.
 *
 * Cada submenu (MenuIngresos, MenuGastos, MenuBancos...) hereda de
 * aqui y solo define:
 *   - getOpciones(): el texto de sus opciones.
 *   - ejecutarOpcion(): que hace cada opcion.
 * Cuando mostrar() llama a ejecutarOpcion(), Java ejecuta la version
 * del submenu correspondiente: eso es POLIMORFISMO.
 */
public abstract class Menu {

    protected Billetera billetera;
    private String titulo;

    public Menu(String titulo, Billetera billetera) {
        this.titulo = titulo;
        this.billetera = billetera;
    }

    public void mostrar() {
        int opcion;
        do {
            String[] opciones = getOpciones();
            String mensaje = "\n========== " + titulo + " ==========";
            for (int i = 0; i < opciones.length; i++) {
                mensaje += "\n" + (i + 1) + ". " + opciones[i];
            }
            mensaje += "\n0. " + getTextoSalida();
            System.out.println(mensaje);

            opcion = Consola.leerEntero("Seleccione una opcion:", 0, opciones.length);
            if (opcion != 0) {
                ejecutarOpcion(opcion);
            }
        } while (opcion != 0);
    }

    /** Texto de la opcion 0. Cada menu puede cambiarlo (Volver, Salir, Cerrar sesion). */
    protected String getTextoSalida() {
        return "Volver";
    }

    protected abstract String[] getOpciones();

    protected abstract void ejecutarOpcion(int opcion);
}
