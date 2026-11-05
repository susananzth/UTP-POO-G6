# Guía para la sustentación de WalletPro

Esta guía explica **qué se cambió, por qué, y cómo defenderlo** si el profesor pregunta. Está ordenada para que cada integrante estudie primero lo común y luego su módulo.

---

## 1. Qué indicaciones del profesor se aplicaron y dónde

| Indicación | Dónde está en el código |
|---|---|
| No usar `nextLine()` | `util/Consola.java`: el `Scanner` se crea con `useDelimiter("\\R")` y se lee con `next()` |
| Prompts con `println` (no `print`) | Todos los mensajes al usuario usan `System.out.println` |
| Usar `printf` para dar formato | `MenuGastos`, `MenuBancos` (`System.out.printf(...)`) y `String.format(...)` en tablas |
| No usar `scanner.close()` | `Consola` nunca lo cierra (comentario explicando por qué) |
| Variable `mensaje` y un solo print | Todos los listados: `String mensaje = "";` → `for` con `mensaje += ...` → `System.out.println(mensaje)` |
| Una sola moneda | `util/Dinero.java`: constante `MONEDA = "S/"`, sin tipo de cambio |
| Manejar bien el presupuesto (¿qué pasa si me excedo?) | `MenuGastos.revisarPresupuesto()` y `Presupuesto.calcularEstado()` |
| Más clases si se necesitan, sin sobreingeniería | `Billetera`, `Repositorio<T>`, `Consola`, `Dinero`, `Menu` y sus submenús, `ArchivoBilletera`, `OperacionInvalidaException` |

### ¿Por qué `useDelimiter("\\R")` + `next()`?

`next()` normalmente lee hasta el primer espacio: con "Juan Carlos" solo leería "Juan". Al decirle al `Scanner` que el separador es el **salto de línea** (`\\R`), `next()` devuelve la línea completa, con espacios incluidos. Así cumplimos "no usar `nextLine()`" y además evitamos el problema clásico de `nextInt()` seguido de `nextLine()` (que deja el Enter sin leer).

### ¿Por qué no se cierra el `Scanner`?

El `Scanner` está conectado a `System.in` (el teclado). Si se cierra, también se cierra `System.in`, y ningún otro `Scanner` podría volver a leer en todo el programa. Por eso hay **un solo `Scanner`** en `Consola` y nunca se cierra.

---

## 2. Cómo responde el sistema al exceder el presupuesto

Al registrar un gasto, antes de guardarlo, `MenuGastos.revisarPresupuesto()` busca el presupuesto de **esa persona, esa categoría y ese mes**:

1. **No hay presupuesto:** solo avisa ("no hay presupuesto de Alimentación para 11/2026") y registra el gasto.
2. **Con el gasto se llega al 80 % o más:** muestra **ALERTA** con el porcentaje usado y cuánto queda.
3. **Con el gasto se pasa del límite:** muestra el límite, lo gastado, cómo quedaría y **cuánto se excede**, y pregunta *"¿Desea registrarlo de todos modos?"*. Si dice que no, el gasto se cancela.

**¿Por qué no se prohíbe el gasto?** Porque en la vida real el gasto ya ocurrió (ya pagaste la cena). Una billetera debe registrarlo igual, pero dejando al usuario advertido. Prohibirlo haría que el registro no coincida con la realidad.

La opción **"Consultar estado de presupuestos"** muestra una tabla con límite, gastado, disponible, % de uso y estado: **OK** (< 80 %), **ALERTA** (≥ 80 %) o **EXCEDIDO** (> 100 %).

---

## 3. Decisiones de diseño (las preguntas más probables)

### ¿Por qué hay paquetes?
La rúbrica pide "arquitectura definida mediante paquetes, con alta cohesión". Cada paquete agrupa clases con una sola responsabilidad: `modelo` (datos y reglas), `vista` (menús), `servicio` (acceso), `datos` (guardar y listas), `util` (herramientas), `interfaces` y `excepciones`.

### ¿Qué es `Billetera` y por qué existe?
Es el objeto central: **la billetera del usuario**. Guarda todas las listas (empleados, gastos, cuentas...) y valida las reglas antes de aceptar un dato (que no se repita un DNI, un RUC o un número de cuenta; que no se registre dos veces el sueldo del mismo mes; etc.). Antes todo esto estaba mezclado en `Main` (940 líneas); ahora los menús solo piden datos y muestran resultados.

### ¿Qué es `Repositorio<T>`? (clase genérica)
Antes había 13 arreglos con su contador (`Gasto[] gastos = new Gasto[500]; int totalGastos;`). `Repositorio<T>` es **una sola clase que sirve para cualquier tipo**: la `T` se reemplaza por el tipo real al usarla (`Repositorio<Gasto>`, `Repositorio<Banco>`). Por dentro usa un `ArrayList`, que crece solo (ya no hay límite de 500).

### ¿Dónde están las lambdas y los streams?
- `Repositorio`: `buscar`, `filtrar` y `existe` reciben una **lambda** como condición. Ejemplo en `Billetera`:
  `empleados.existe(e -> e.getDni().equals(empleado.getDni()))` → "¿existe algún empleado con este DNI?".
- `Billetera.totalIngresosDelMes()` y `calcularGastado()` usan **streams**:
  `.filter(...)` se queda con los del mes → `.mapToDouble(g -> g.getMonto())` toma los montos → `.sum()` los suma.
- `Predicate<T>` es una interfaz de Java que recibe un objeto y devuelve `true` o `false`; la lambda es la forma corta de escribirla.

### ¿Dónde están los 4 pilares de la POO?

| Pilar | Ejemplo en el código |
|---|---|
| **Encapsulamiento** | Atributos `private` con setters que validan. `CuentaBancaria` **no tiene `setSaldo`**: el saldo solo cambia con `depositar()`/`retirar()`. `Usuario` no tiene `getContrasena()`: solo se puede comprobar, no leer. |
| **Herencia** | `Usuario` y `Empleado` heredan de `Persona`. `Gasto` y `Movimiento` heredan de `Transaccion`. `MenuIngresos`, `MenuGastos`, etc. heredan de `Menu`. |
| **Abstracción** | `Persona`, `Transaccion` y `Menu` son **clases abstractas**: no se pueden instanciar, solo definen lo común. Las interfaces `IValidable`, `IConsultable` y `OperacionFinanciera` definen "contratos". |
| **Polimorfismo** | `getTipo()` es abstracto en `Transaccion`: un `Gasto` responde "FIJO"/"OCASIONAL" y un `Movimiento` responde "DEPOSITO"/"RETIRO". En `MenuPrincipal`, la variable `Menu submenu` puede ser cualquiera de los tres submenús y `submenu.mostrar()` ejecuta el que corresponde. |

### ¿Por qué la clase abstracta `Menu`?
Todos los menús hacían lo mismo: mostrar opciones, leer la opción, ejecutarla y repetir hasta elegir 0. Eso quedó **una sola vez** en `Menu.mostrar()`. Cada submenú solo dice cuáles son sus opciones (`getOpciones()`) y qué hace cada una (`ejecutarOpcion()`).

### ¿Por qué una excepción propia (`OperacionInvalidaException`)?
Para avisar que **se rompió una regla del negocio** (saldo insuficiente, RUC repetido, cuenta de otra persona...). Es *checked* (extiende de `Exception`), así que Java obliga a atraparla con `try-catch`: ningún error se pasa por alto. Los menús la atrapan y muestran `e.getMessage()`.

Las excepciones de Java también se usan: `NumberFormatException` en `Consola` (cuando escriben letras donde va un número), `IOException` en `ArchivoBilletera` y `NoSuchElementException` en `Main`.

### ¿Cómo se guardan los datos? (persistencia)
Con **serialización** (`ArchivoBilletera`): toda la `Billetera` se escribe en `walletpro.dat` con `ObjectOutputStream` y se lee al abrir el programa con `ObjectInputStream`. Por eso las clases del modelo implementan `Serializable`. Se guarda después de cada operación y al salir. Se usa *try-with-resources*, que cierra el archivo automáticamente.

### ¿Cómo se maneja el dinero?
- Una sola moneda: soles (`Dinero.MONEDA`).
- Todo monto se **redondea a céntimos** (`Dinero.redondear`). Así se evitan errores de `double` como `0.30 - 0.10 = 0.19999...`. No se usó `BigDecimal` para no complicar el código.
- Se rechazan montos negativos, cero, `Infinity` y `NaN` (`Consola.leerMonto`).
- **Trazabilidad:** cada depósito, retiro, pago de gasto, sueldo depositado o cuota de préstamo queda como `Movimiento` con fecha real y el saldo resultante. Opción "Ver movimientos de una cuenta".

### ¿Cómo se calcula el préstamo?
Interés simple: `interés = monto × (tasa/100) × cuotas`; `total = monto + interés`; `cuota = total / cuotas`.
Ejemplo: S/ 1,000 al 10 % mensual en 2 cuotas → interés S/ 200 → total S/ 1,200 → cuota S/ 600.
La deuda arranca en el **total a pagar** (antes arrancaba en el monto prestado y el interés nunca se cobraba). Cada cuota se paga **desde una cuenta del mismo deudor**; si no hay saldo, no se paga y la deuda no cambia. La última cuota paga exactamente lo que falta, para que no queden céntimos sueltos.

---

## 4. Errores que tenía la versión anterior y se corrigieron

Basado en la revisión que compartió Eduardo:

- Recuperar contraseña con un código fijo (`admin123`) igual para todos → ahora con **pregunta de seguridad**.
- Préstamo que no cobraba intereses → corregido.
- Se podía pagar un préstamo con la cuenta de otra persona → ahora solo con cuentas del deudor.
- Pagos con fecha inválida que igual descontaban dinero → las fechas se validan siempre.
- Consultar sueldos con un número fuera de rango cerraba el programa → `Consola.leerEntero` valida el rango.
- Cerrar sesión terminaba el programa → ahora vuelve al menú de acceso.
- Depósitos con fecha fija 01-01-2026 → ahora con la fecha real.
- Límite de 500 movimientos → ahora `ArrayList`, sin límite.
- Códigos de gastos repetidos → contador único en `Billetera`.
- Cuentas, sueldos, presupuestos, categorías, RUC y DNI duplicados → se rechazan.
- Se aceptaba `Infinity` como monto → se rechaza.
- Errores de redondeo con `double` → redondeo a céntimos.
- Pregunta/respuesta de seguridad vacías y fechas de nacimiento futuras → se validan.
- Correos con subdominio (`@utp.edu.pe`) rechazados → expresión regular corregida.
- Nombres que empezaban con "Sin" (ej. "Sinergia SAC") rechazados → corregido.
- Métodos públicos que cambiaban el saldo sin dejar rastro (`ejecutar`, `setSaldoInicial`) → eliminados.
- No había forma de ver el historial de una cuenta → nueva opción.
- Los datos se perdían al cerrar → persistencia en archivo.

### Lo que se dejó fuera a propósito (por si lo preguntan)

- **Varios usuarios con datos separados:** el alcance del proyecto es la billetera de una persona; todos los usuarios ven la misma billetera.
- **Contraseñas cifradas:** se guardan tal cual; cifrarlas (hash) queda fuera del alcance del curso.
- **Base de datos (MySQL):** la rúbrica acepta "archivos planos para serialización"; se eligió eso por simplicidad. Si el profesor exige base de datos, solo hay que cambiar `ArchivoBilletera`.
- **Varias monedas:** el profesor indicó trabajar con una sola.

---

## 5. Qué estudiar según el módulo

**Todos:** `Main`, `Menu`, `MenuAcceso`, `MenuPrincipal`, `Billetera`, `Repositorio`, `Consola`, `Dinero`, `ArchivoBilletera`, `OperacionInvalidaException`, `Persona`, `Transaccion`.

**Alumno 1 – Ingresos y Ahorros:** `MenuIngresos`, `Empresa`, `Cargo`, `Empleado`, `Trabajo`, `Sueldo`. Clave: el resumen de ahorro (ingresos − gastos del mes) y cómo `Billetera.totalIngresosDelMes()` usa un stream.

**Alumno 2 – Gastos y Presupuesto:** `MenuGastos`, `CategoriaGasto`, `Gasto`, `Presupuesto`. Clave: `revisarPresupuesto()` (OK / ALERTA / EXCEDIDO), `Billetera.calcularGastado()` y el pago con cuenta o efectivo.

**Alumno 3 – Bancos y Préstamos:** `MenuBancos`, `Banco`, `CuentaBancaria`, `Movimiento`, `TipoPrestamo`, `Prestamo`, `OperacionFinanciera`. Clave: por qué el saldo no tiene setter, el historial de movimientos y el cálculo y pago de cuotas.

---

## 6. Pendiente para el equipo

- **Actualizar el diagrama de clases (draw.io)** con las clases nuevas: `Billetera`, `Repositorio<T>`, `Menu` y sus submenús, `Consola`, `Dinero`, `ArchivoBilletera`, `DatosDePrueba`, `OperacionInvalidaException`. Recuerden **no** incluir `Main` en el diagrama (lo pidió el profesor) ni la clase `Moneda`.
- La consigna pide que el proyecto se llame `TF_EquipoNN` (ej. `TF_Equipo05`). Confirmen el número y renombren la carpeta `WalletPro` y el `artifactId` del `pom.xml` antes de entregar.
