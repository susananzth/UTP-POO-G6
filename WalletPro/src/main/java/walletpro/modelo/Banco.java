package walletpro.modelo;

import walletpro.util.Consola;

/**
 * Entidad financiera (Interbank, BCP...).
 * Tabla: banco | DAO: BancoDAO | Menu: MenuBancos (Alumno 3)
 */
public class Banco {

    private String codigo = "";
    private String nombre = "";

    // Lo llama: MenuBancos.registrarBanco()
    public void registrar() {
        codigo = Consola.leerDigitos("Codigo del banco (5 digitos):", 5);
        nombre = Consola.leerTexto("Nombre del banco:");
    }

    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }

    public void setCodigo(String codigo) { this.codigo = codigo; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    @Override
    public String toString() {
        return nombre + " (codigo " + codigo + ")";
    }
}
