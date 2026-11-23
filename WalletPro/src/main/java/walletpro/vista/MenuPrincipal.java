package walletpro.vista;

import walletpro.modelo.Usuario;
import walletpro.util.Consola;

/**
 * Menu principal: un modulo por integrante del equipo.
 * Lo llama: MenuAcceso.iniciarSesion()
 * Llama a: MenuIngresos (Alumno 1), MenuGastos (Alumno 2), MenuBancos (Alumno 3).
 */
public class MenuPrincipal {

    private Usuario usuario;   // quien inicio sesion

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void mostrar() {
        int opcion;
        do {
            String mensaje = "\n========== MENU PRINCIPAL ==========";
            mensaje += "\nUsuario: " + usuario.getNombreUsuario() + " (" + usuario.getRol() + ")";
            mensaje += "\n1. Ingresos y Ahorros (empresas, empleados, trabajos, sueldos)";
            mensaje += "\n2. Gastos y Presupuesto (categorias, gastos, presupuestos)";
            mensaje += "\n3. Bancos y Prestamos (cuentas, depositos, retiros, prestamos)";
            mensaje += "\n4. Ver mis datos";
            mensaje += "\n0. Cerrar sesion";
            System.out.println(mensaje);

            opcion = Consola.leerEntero("Seleccione una opcion:", 0, 4);
            switch (opcion) {
                case 1:
                    MenuIngresos menuIngresos = new MenuIngresos();
                    menuIngresos.mostrar();
                    break;
                case 2:
                    MenuGastos menuGastos = new MenuGastos();
                    menuGastos.mostrar();
                    break;
                case 3:
                    MenuBancos menuBancos = new MenuBancos();
                    menuBancos.mostrar();
                    break;
                case 4:
                    System.out.println(usuario.resumen());
                    break;
            }
        } while (opcion != 0);
    }
}
