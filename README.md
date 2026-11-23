# WalletPro – Billetera personal (POO, UTP)

Grupo 5 · Sección 45270 · Docente: Espinoza Paredes, Christian Eduardo

| Integrante | Código | Rol | Módulo |
|---|---|---|---|
| Piñero Rodriguez, Susana Nazareth | U25257191 | Delegada | Alumno 1 – Ingresos y Ahorros |
| Moreno Padilla, Keyner Eduardo | U25246100 | Imagen | Alumno 2 – Gastos y Presupuesto |
| Eliott Duran, Carlos Jeremy | U25246351 | PMO | Alumno 3 – Bancos y Préstamos |

Aplicación de consola en Java que registra los ingresos (sueldos), los gastos, los presupuestos mensuales, las cuentas bancarias y los préstamos de una persona. Guarda todo en **SQL Server** y maneja **una sola moneda (soles, S/)**.

## Cómo ejecutarlo

1. Preparar SQL Server y crear la base con `WalletPro/sql/walletpro.sql`. Los pasos están en [docs/BASE_DE_DATOS.md](docs/BASE_DE_DATOS.md).
2. NetBeans → **File → Open Project…** → carpeta `WalletPro`. La primera vez descarga el driver de SQL Server (necesita internet).
3. **Run** (F6). La clase principal es `walletpro.Main`.
4. Usuario de prueba: `Administrador` / `admin1234`.

Requiere **Java 21 o superior**.

## Estructura

```
WalletPro/
├── sql/walletpro.sql        Tablas, datos de prueba y usuario de la base
└── src/main/java/walletpro/
    ├── Main.java            Punto de entrada
    ├── conexion/            Conexion (SQL Server)
    ├── modelo/              Persona, Usuario, Empleado, Transaccion, Gasto, Movimiento,
    │                        CuentaBancaria, Prestamo, Presupuesto, Sueldo, Trabajo...
    ├── dao/                 Un DAO por tabla (INSERT / SELECT / UPDATE / DELETE)
    ├── vista/               MenuAcceso, MenuPrincipal, MenuIngresos, MenuGastos, MenuBancos
    ├── interfaces/          IDAO<T>, IConsultable, OperacionFinanciera
    └── util/                Consola, Validacion, Dinero, Paginador<T>, ComparadorGastoPorMonto
```

Guía para estudiar la sustentación: [docs/GUIA_SUSTENTACION.md](docs/GUIA_SUSTENTACION.md).

## Git desde NetBeans

- **Ver qué cambió:** clic derecho en el proyecto → **Git → Diff**.
- **Historial:** clic derecho → **Git → Show History**.
- **Guardar una versión:** clic derecho → **Git → Commit…**.
- **Subir / traer cambios:** **Git → Remote → Push…** / **Pull…**.
- **Clonar:** **Team → Git → Clone…** con `https://github.com/susananzth/UTP-POO-G6.git`.
