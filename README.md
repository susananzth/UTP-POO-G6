# WalletPro – Billetera personal (POO, UTP)

Grupo 5 · Sección 45270 · Docente: Espinoza Paredes, Christian Eduardo

| Integrante | Código | Rol |
|---|---|---|
| Eliott Duran, Carlos Jeremy | U25246351 | PMO |
| Moreno Padilla, Keyner Eduardo | U25246100 | Imagen |
| Piñero Rodriguez, Susana Nazareth | U25257191 | Delegada |

Módulos: **Alumno 1** – Ingresos y Ahorros · **Alumno 2** – Gastos y Presupuesto · **Alumno 3** – Bancos y Préstamos.

Aplicación de consola en Java que registra los ingresos (sueldos), los gastos, los presupuestos mensuales, las cuentas bancarias y los préstamos de una persona. Maneja **una sola moneda (soles, S/)**.

## Cómo abrir y ejecutar en NetBeans

1. NetBeans → **File → Open Project…** → elegir la carpeta `WalletPro` (tiene el ícono de proyecto Maven).
2. Clic derecho en el proyecto → **Run** (o F6). La clase principal es `walletpro.Main`.
3. La primera vez se cargan datos de prueba. Usuario: `Administrador`, contraseña: `admin1234`.
4. Los datos se guardan en `WalletPro/walletpro.dat`. Borren ese archivo para empezar de cero con los datos de prueba.

Sin NetBeans, desde la carpeta `WalletPro`:

```bash
javac -d out -encoding UTF-8 $(find src -name "*.java")
```

```bash
java -cp out walletpro.Main
```

## Git desde NetBeans (sí, se puede, como en VS Code)

NetBeans trae Git incorporado; no hay que instalar nada adicional.

- **Ver qué cambió:** los archivos modificados aparecen en azul y los nuevos en verde. Clic derecho en el proyecto → **Git → Diff** para ver los cambios línea por línea.
- **Historial de versiones:** clic derecho → **Git → Show History**. Ahí están todos los commits y pueden ver cómo era cada archivo antes.
- **Guardar una versión (commit):** clic derecho → **Git → Commit…**, escribir qué hicieron y aceptar.
- **Subir a GitHub:** clic derecho → **Git → Remote → Push…**.
- **Traer los cambios de los compañeros:** clic derecho → **Git → Remote → Pull…**.
- Para trabajar los tres a la vez, cada uno puede clonar el repositorio con **Team → Git → Clone…** y la URL `https://github.com/susananzth/UTP-POO-G6.git`.

## Estructura del proyecto

```
WalletPro/src/main/java/walletpro/
├── Main.java            Punto de entrada: carga la billetera y abre el menú de acceso
├── modelo/              Entidades del negocio (Persona, Usuario, Empleado, Gasto, CuentaBancaria, Prestamo...)
│   └── Billetera.java   Guarda todos los datos y aplica las reglas (sin duplicados, saldo suficiente...)
├── vista/               Menús de consola: Menu (abstracta), MenuAcceso, MenuPrincipal,
│                        MenuIngresos (Alumno 1), MenuGastos (Alumno 2), MenuBancos (Alumno 3)
├── servicio/            Login, RegistrarUsuario, RecuperarContrasena
├── datos/               Repositorio<T> (genérico), ArchivoBilletera (persistencia), DatosDePrueba
├── interfaces/          IValidable, IConsultable, OperacionFinanciera
├── excepciones/         OperacionInvalidaException
└── util/                Consola (teclado), Dinero (soles), ValidFecha, ValidRegex
```

La guía para preparar la sustentación está en [docs/GUIA_SUSTENTACION.md](docs/GUIA_SUSTENTACION.md).
