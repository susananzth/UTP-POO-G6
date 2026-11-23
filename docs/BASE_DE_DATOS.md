# Base de datos WalletPro (SQL Server)

Guía para dejar SQL Server listo y conectar el programa. Hay que hacerla **una vez en cada computadora**.

## 1. Instalar

1. **SQL Server Express** (gratis): microsoft.com/es-es/sql-server/sql-server-downloads → Express → *Descargar ahora*. Al instalar, elijan **Básica**. Al final aparece el nombre de la instancia (normalmente `SQLEXPRESS`).
2. **SQL Server Management Studio (SSMS)**: es el programa para ver las tablas y ejecutar SQL. **Ojo:** SSMS no es el servidor; hay que instalar los dos.

## 2. Activar el usuario y la contraseña de SQL Server ("autenticación mixta")

La instalación básica solo deja entrar con el usuario de Windows. Java necesita entrar con usuario y clave:

1. Abran SSMS y conéctense con *Autenticación de Windows* (no pide usuario ni clave).
   - Nombre del servidor: `localhost\SQLEXPRESS` (Express) o `localhost` (Developer).
   - Marquen **Certificado de servidor de confianza** (*Trust server certificate*).
2. Clic derecho en el servidor (arriba a la izquierda) → **Properties** → **Security**.
3. Marquen **SQL Server and Windows Authentication mode** → **OK**.
4. Clic derecho en el servidor → **Restart** (para que tome el cambio).

## 3. Activar TCP/IP en el puerto 1433

Java se conecta por red (TCP/IP), y viene **apagado** por defecto:

1. Abran **SQL Server Configuration Manager**. Si no aparece en el menú Inicio, presionen `Win + R` y escriban `SQLServerManager17.msc` (SQL Server 2025) o `SQLServerManager16.msc` (SQL Server 2022).
2. **SQL Server Network Configuration** → **Protocols for SQLEXPRESS** (o *MSSQLSERVER*).
3. Clic derecho en **TCP/IP** → **Enable**.
4. Doble clic en **TCP/IP** → pestaña **IP Addresses** → bajen hasta **IPAll**:
   - *TCP Dynamic Ports*: déjenlo **vacío**.
   - *TCP Port*: escriban **1433**.
5. **SQL Server Services** → clic derecho en **SQL Server (SQLEXPRESS)** → **Restart**.

## 4. Crear la base de datos

1. En SSMS: **File → Open → File…** → abran `WalletPro/sql/walletpro.sql`.
2. Presionen **Execute** (F5). Al final debe mostrar la tabla `empleado` con 2 filas.

El script crea la base `WalletPro`, las 14 tablas, datos de prueba y el usuario `walletpro` (clave `WalletPro2026`) con el que entra Java. **Si lo vuelven a ejecutar, borra todo y empieza de cero** con los datos de prueba.

## 5. Probar

1. *(Opcional)* En SSMS: **Connect** → *SQL Server Authentication* → usuario `walletpro`, clave `WalletPro2026`. Si entra, Java también podrá.
2. En NetBeans: abran el proyecto `WalletPro` y presionen **F6**. Debe decir `Conectado a la base de datos WalletPro.`
3. Entren con `Administrador` / `admin1234`.

## Errores comunes

| Mensaje | Solución |
|---|---|
| `No se pudo realizar la conexión TCP/IP al host localhost, puerto 1433` | SQL Server está apagado o el TCP/IP no está activado (paso 3). |
| En SSMS: `error 26` o `error 40` al conectar | El servidor no está instalado (solo SSMS) o el nombre del servidor es incorrecto. En `Win + R` → `services.msc` debe aparecer **SQL Server (SQLEXPRESS)** en ejecución. |
| `Login failed for user 'walletpro'` | Falta la autenticación mixta (paso 2) o no se ejecutó el script (paso 4). |
| `Cannot open database "WalletPro"` | No se ejecutó el script (paso 4). |
| `Falta el driver de SQL Server` | NetBeans no descargó el driver: clic derecho en el proyecto → **Build** con internet. |

---

## Diccionario de datos (resumen para el informe)

**PK** = llave primaria (no se repite) · **FK** = llave foránea (debe existir en la otra tabla) · **UQ** = único.

| Tabla | Clase Java | PK | FK | Reglas |
|---|---|---|---|---|
| usuario | Usuario | id_usuario (autonumérico) | — | UQ correo, UQ nombre_usuario |
| empresa | Empresa | ruc | — | — |
| cargo | Cargo | id_cargo | — | UQ nombre |
| empleado | Empleado | dni | — | sueldo_base > 0 |
| trabajo | Trabajo | id_trabajo | dni → empleado, ruc → empresa, id_cargo → cargo | UQ (dni, ruc, id_cargo) |
| sueldo | Sueldo | id_sueldo | dni → empleado | UQ (dni, mes, anio), monto > 0 |
| banco | Banco | codigo | — | — |
| cuenta | CuentaBancaria | numero_cuenta | codigo_banco → banco, dni → empleado | saldo >= 0 |
| movimiento | Movimiento | id_movimiento | numero_cuenta → cuenta | tipo = DEPOSITO o RETIRO |
| tipo_prestamo | TipoPrestamo | id_tipo | — | UQ nombre |
| prestamo | Prestamo | id_prestamo | dni → empleado, codigo_banco → banco, id_tipo → tipo_prestamo | monto > 0 |
| categoria | CategoriaGasto | id_categoria | — | UQ nombre |
| gasto | Gasto | id_gasto | dni → empleado, id_categoria → categoria, numero_cuenta → cuenta (NULL = efectivo) | monto > 0 |
| presupuesto | Presupuesto | id_presupuesto | dni → empleado, id_categoria → categoria | UQ (dni, id_categoria, mes, anio) |

Los tipos de cada columna están en `WalletPro/sql/walletpro.sql`. Para el **diagrama físico** que pide el informe: en SSMS, clic derecho en **Database Diagrams** → **New Database Diagram** → seleccionen todas las tablas.
