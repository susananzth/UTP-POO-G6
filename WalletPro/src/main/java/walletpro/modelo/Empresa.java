package walletpro.modelo;

import walletpro.util.Consola;

/**
 * Empresa donde trabaja un empleado.
 * Tabla: empresa | DAO: EmpresaDAO | Menu: MenuIngresos (Alumno 1)
 */
public class Empresa {

    private String ruc = "";
    private String razonSocial = "";

    // Lo llama: MenuIngresos.registrarEmpresa()
    public void registrar() {
        ruc = Consola.leerDigitos("RUC (11 digitos):", 11);
        razonSocial = Consola.leerTexto("Razon social:");
    }

    public String getRuc() { return ruc; }
    public String getRazonSocial() { return razonSocial; }

    public void setRuc(String ruc) { this.ruc = ruc; }
    public void setRazonSocial(String razonSocial) { this.razonSocial = razonSocial; }

    @Override
    public String toString() {
        return razonSocial + " (RUC " + ruc + ")";
    }
}
