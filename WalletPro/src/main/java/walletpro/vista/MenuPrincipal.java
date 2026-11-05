package walletpro.vista;

import walletpro.modelo.Billetera;
import walletpro.modelo.Usuario;

/**
 * Menu principal: lleva a los tres modulos del sistema, uno por
 * integrante del equipo. Elegir 0 cierra la sesion y vuelve al menu
 * de acceso (no termina el programa).
 */
public class MenuPrincipal extends Menu {

    private Usuario usuarioActual;

    public MenuPrincipal(Billetera billetera, Usuario usuarioActual) {
        super("MENU PRINCIPAL", billetera);
        this.usuarioActual = usuarioActual;
    }

    @Override
    protected String[] getOpciones() {
        return new String[] {
            "Ingresos y Ahorros (empresas, empleados, trabajos, sueldos)",
            "Gastos y Presupuesto (categorias, gastos, presupuestos)",
            "Bancos y Prestamos (cuentas, depositos, retiros, prestamos)"
        };
    }

    @Override
    protected String getTextoSalida() {
        return "Cerrar sesion (" + usuarioActual.getNombreUsuario() + ")";
    }

    @Override
    protected void ejecutarOpcion(int opcion) {
        Menu submenu = null;
        switch (opcion) {
            case 1: submenu = new MenuIngresos(billetera); break;
            case 2: submenu = new MenuGastos(billetera); break;
            case 3: submenu = new MenuBancos(billetera); break;
        }
        // Polimorfismo: la variable es de tipo Menu, pero se ejecuta el submenu elegido.
        submenu.mostrar();
    }
}
