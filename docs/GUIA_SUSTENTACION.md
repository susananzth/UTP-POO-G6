# Guía para la sustentación de WalletPro

Lean primero las secciones 1 a 4, que son de todos. Después, cada uno estudia su módulo (sección 5).

---

## 1. Cómo está organizado el proyecto

```
walletpro/
├── Main.java        Empieza aquí: prueba la conexión y abre el menú de acceso
├── conexion/        Conexion: abre la conexión a SQL Server (como la Coneccion del profesor)
├── modelo/          Las "cosas" del sistema: Empleado, Gasto, CuentaBancaria, Prestamo...
├── dao/             Una clase por tabla: guarda y lee de SQL Server (INSERT, SELECT, UPDATE, DELETE)
├── vista/           Los menús de consola
├── interfaces/      IDAO<T>, IConsultable, OperacionFinanciera
└── util/            Consola (teclado), Validacion, Dinero, Paginador<T>, ComparadorGastoPorMonto
```

**Regla para no perderse:** cada clase empieza con un comentario que dice *quién la usa*, y cada método dice **"Lo llama: …"**.

### Recorrido de una opción (ejemplo: registrar un gasto)

```
Main.main()
 └─ MenuAcceso.mostrar() → iniciarSesion()
     └─ MenuPrincipal.mostrar()  (opción 2)
         └─ MenuGastos.mostrar() → registrarGasto()
             ├─ empleadoDAO.listar()            SELECT de empleados
             ├─ Consola.seleccionar(...)        el usuario elige uno
             ├─ gasto.registrar()               pide fecha, monto, descripción
             ├─ revisarPresupuesto(gasto)       ¿se pasa del presupuesto?
             ├─ cuenta.retirar(...)             si paga con cuenta (devuelve null si no alcanza)
             ├─ gastoDAO.insertar(gasto)        INSERT INTO gasto
             └─ cuentaDAO.guardarMovimiento()   UPDATE saldo + INSERT movimiento
```

**Todas las opciones de "Registrar" siguen los mismos 3 pasos:**

1. `Empresa empresa = new Empresa();` → se crea el objeto vacío.
2. `empresa.registrar();` → se piden los datos por teclado.
3. `empresaDAO.insertar(empresa);` → se guarda en SQL Server.

En NetBeans, **Ctrl + clic** sobre un método los lleva a donde está escrito, y **Alt + F7** (*Find Usages*) muestra desde dónde se llama.

---

## 2. Indicaciones del profesor y dónde se cumplen

| Indicación | Dónde |
|---|---|
| Sin constructores con parámetros | Ninguna clase los tiene. Los datos se asignan con setters o con `registrar()`. `MenuPrincipal` recibe el usuario con `setUsuario(...)`. |
| No usar `nextLine()` | `util/Consola.java`: el `Scanner` usa `useDelimiter("\\R")` y se lee con `next()`, que así lee la línea completa (con espacios). |
| No cerrar el `Scanner` | `Consola` nunca hace `teclado.close()`. Cerrarlo cerraría `System.in` y ya no se podría leer el teclado. |
| Mensajes con `println` (y `printf` con `%n`) | Todos los mensajes. Los `printf` terminan en `%n` (salto de línea). |
| Variable `mensaje` y un solo print | Menús y listados: `String mensaje = ...;` → `mensaje += ...` → `System.out.println(mensaje)`. |
| Usar base de datos | SQL Server: `sql/walletpro.sql` (tablas) + paquete `dao` (SQL desde Java). |
| Una sola moneda | `util/Dinero.java`: todo en soles. |

---

## 3. Temas del curso y dónde están en el código

| Tema (semana) | Dónde | Qué decir |
|---|---|---|
| **Encapsulamiento** | Todas las clases del `modelo` | Atributos `private` y acceso con getters y setters. `Transaccion.setMonto()` valida que sea > 0. El saldo solo cambia con `depositar()`/`retirar()`. |
| **Herencia** | `Usuario` y `Empleado` heredan de `Persona`. `Gasto` y `Movimiento` heredan de `Transaccion`. | Las hijas reutilizan los datos y métodos del padre (`registrarDatosPersonales()`). |
| **Abstracción / clase abstracta** | `Persona`, `Transaccion` | No se puede hacer `new Persona()`. Tienen métodos abstractos: `getRol()` y `getTipo()`. |
| **Polimorfismo** | `getTipo()`: un `Gasto` responde FIJO/OCASIONAL y un `Movimiento` responde DEPOSITO/RETIRO. `resumen()` es distinto en cada clase. | `Consola.mostrarLista()` llama a `resumen()` sin saber de qué clase es el objeto. |
| **Sobrescritura (`@Override`)** | `Gasto.resumen()` usa `super.resumen()` y le agrega datos | — |
| **Interfaces** | `IConsultable`, `OperacionFinanciera`, `IDAO<T>` | Son contratos. `CuentaBancaria` implementa dos interfaces a la vez (herencia múltiple con interfaces). |
| **Relaciones entre clases** | `Trabajo` tiene un `Empleado`, una `Empresa` y un `Cargo` (asociación). `Movimiento` pertenece a una `CuentaBancaria` (composición). Los menús *usan* `Consola` (dependencia). | — |
| **Excepciones** | `Consola.leerEntero()` (`NumberFormatException`), `Validacion.esFechaValida()` (`DateTimeParseException`) y todos los DAO (`SQLException`) | Si el usuario escribe letras donde va un número, el programa no se cae: vuelve a preguntar. |
| **ArrayList** (11) | Todos los `listar()` devuelven `ArrayList<T>` | — |
| **HashMap** (11) | `MenuIngresos.resumenAhorro()` | Clave = nombre de la categoría, valor = total gastado. Se recorre con `entrySet()`, igual que en la diapositiva del profesor. |
| **Comparator** (12) | `util/ComparadorGastoPorMonto` → se usa en `MenuGastos.listarGastosDelMes()` | Igual que `ComparadorCuota` del profesor. |
| **Iterator** (12) | `MenuBancos.pagarCuota()` | `hasNext()` / `next()` / `remove()`: quita los préstamos ya pagados mientras recorre. Con for-each eso da error. |
| **Sequenced Collections** (12) | `MenuBancos.verMovimientos()` → `movimientos.getLast()` | Java 21: obtiene el último elemento sin calcular `size() - 1`. |
| **Clase genérica** (13) | `util/Paginador<T>` | Divide cualquier lista en páginas de 5. Se usa con movimientos. |
| **Interfaz genérica** (13) | `interfaces/IDAO<T>` | `EmpresaDAO implements IDAO<Empresa>`: la `T` se vuelve `Empresa`. |
| **Método genérico / restricción de tipos** (13) | `Consola.seleccionar(<T>)` y `Consola.mostrarLista(<T extends IConsultable>)` | `extends IConsultable` obliga a que la lista sea de objetos que tengan `resumen()`. |
| **JDBC / base de datos** (14) | `conexion/Conexion` + paquete `dao` | Mismo esquema que el `Consumidor` del profesor: `prepareStatement`, `?`, `setString`, `executeUpdate`, `executeQuery`, `ResultSet`. |
| **CRUD** (14) | `PresupuestoDAO`: `insertar`, `listar`/`buscar`, `actualizar`, `eliminar` | En el menú: opciones 3, 4, 5 y 7 de Gastos. |
| **Lambdas y Stream API** (rúbrica) | `MenuIngresos.resumenAhorro()`, `MenuGastos.calcularGastado()`, `MenuBancos.listarPrestamos()` | `filter` (quedarse con algunos), `mapToDouble` (tomar el monto), `sum` (sumar). `(a, b) -> ...` es una lambda que ordena. |

---

## 4. La base de datos

- Script: `TF_Equipo05/sql/walletpro.sql`. Instalación y diccionario de datos: [BASE_DE_DATOS.md](BASE_DE_DATOS.md).
- **Un DAO por tabla.** El menú nunca escribe SQL: le pide al DAO que guarde o traiga los datos.
- **`?` y `PreparedStatement`**: los datos se pasan aparte del SQL. Así nadie puede "inyectar" SQL escribiendo cosas raras (inyección SQL).
- **Duplicados**: no se revisan en Java. La base los rechaza con `PRIMARY KEY` y `UNIQUE` (DNI, RUC, sueldo del mismo mes, presupuesto repetido…), y `Conexion.mostrarError()` muestra "ya existe un registro con esos datos".
- **Fechas**: en Java son texto `dd-mm-aaaa`. `CONVERT(DATE, ?, 105)` las convierte al guardar y `CONVERT(VARCHAR(10), fecha, 105)` al leer (105 es el código del formato dd-mm-aaaa).
- **JOIN**: la tabla `gasto` solo guarda `id_categoria`. Para mostrar el nombre, el `SELECT` hace `JOIN categoria`.
- **Una sola conexión** (`static` en `Conexion`): se abre la primera vez y la usan todos los DAO.

---

## 5. Cada módulo

### Alumno 1: Ingresos y Ahorros (Susana) → `MenuIngresos`
Empresa, Cargo, Empleado, Trabajo, Sueldo y sus DAO. Lo más importante: **`resumenAhorro()`**, con el stream que suma los sueldos y el `HashMap` de gastos por categoría. También `registrarSueldo()`, que puede depositar el sueldo en una cuenta.

### Alumno 2: Gastos y Presupuesto (Keyner) → `MenuGastos`
CategoriaGasto, Gasto, Presupuesto y sus DAO. Lo más importante: **`revisarPresupuesto()`**:
1. No hay presupuesto → solo avisa.
2. Se llega al 80 % o más → **ALERTA**.
3. Se pasa del límite → muestra cuánto se excede y pregunta si igual lo registra.

¿Por qué no se prohíbe el gasto? Porque en la vida real el gasto ya ocurrió: la billetera debe registrarlo, pero dejando al usuario advertido. También: el CRUD completo de presupuesto y el `Comparator`.

### Alumno 3: Bancos y Préstamos (Carlos) → `MenuBancos`
Banco, CuentaBancaria, Movimiento, TipoPrestamo, Prestamo y sus DAO. Lo más importante:
- `cuenta.retirar()` devuelve `null` si no alcanza el saldo.
- `cuentaDAO.guardarMovimiento()` guarda el saldo nuevo y el movimiento.
- El cálculo del préstamo (interés simple): `Prestamo.calcularInteresTotal()`. Con S/ 1000 al 2 % en 6 cuotas, el interés es S/ 120, el total S/ 1120 y la cuota S/ 186.67.
- El `Iterator` en `pagarCuota()` y el `Paginador` en `verMovimientos()`.

---

## 6. Preguntas probables

**¿Por qué no hay constructores con parámetros?** Por indicación del profesor. Los objetos se crean vacíos y se llenan con `registrar()` (teclado) o con setters (cuando los arma el DAO desde la base).

**¿Qué es un DAO?** *Data Access Object*: la clase que habla con una tabla. Separa "qué hace el programa" (menús y modelo) de "cómo se guarda" (SQL). Si mañana cambiamos de base de datos, solo cambia el paquete `dao`.

**¿Por qué el saldo de la cuenta no se cambia con un `setSaldo` desde el menú?** Porque todo cambio de dinero debe quedar en el historial. Por eso solo se cambia con `depositar()`/`retirar()`, que crean un `Movimiento`. `setSaldo` existe solo para que `CuentaDAO` cargue el saldo que viene de la base.

**¿Qué pasa si el usuario escribe letras donde va un número?** `Integer.parseInt` lanza `NumberFormatException`, el `catch` muestra un mensaje y el `do-while` vuelve a preguntar.

**¿Qué pasa si registro dos veces el mismo DNI?** SQL Server rechaza el `INSERT` por la `PRIMARY KEY`, Java recibe un `SQLException` con el código 2627 y muestra "ya existe".

**¿Por qué `useDelimiter("\\R")`?** `next()` normalmente lee hasta el primer espacio ("Juan Carlos" → "Juan"). Si el separador es el salto de línea (`\\R`), `next()` lee la línea completa sin usar `nextLine()`.

**¿Por qué no usaron records?** Un record siempre se crea con un constructor con parámetros (`new Punto(1, 2)`), y eso va contra la indicación del profesor.

**¿Dónde quedó la serialización?** Ya no hace falta: los datos se guardan en SQL Server, que es lo que pidió el profesor.
