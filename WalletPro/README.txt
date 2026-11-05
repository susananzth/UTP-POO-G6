WalletPro - Sistema de Billetera Personal
Grupo 5 - Seccion 45270 - Segunda Entrega (Checkpoint)
Docente: Espinoza Paredes, Christian Eduardo

Integrantes:
- Eliott Duran, Carlos Jeremy (U25246351) - PMO
- Moreno Padilla, Keyner Eduardo (U25246100) - Imagen
- Piñero Rodriguez, Susana Nazareth (U25257191) - Delegada

COMPILACION Y EJECUCION
------------------------
Desde la carpeta "src":
    javac -d out -encoding UTF-8 walletpro/*.java
    java -cp out walletpro.Main

El codigo fue compilado y verificado sin errores antes de la entrega.

ESTRUCTURA
----------
walletpro/
  Interfaces: IValidable, IConsultable, OperacionFinanciera
  Clases abstractas: Persona, Transaccion
  Herencia: Usuario extends Persona ; Empleado extends Persona
  Alumno 1 (Trabajo y Sueldo): Trabajo, Sueldo, Empleado, Empresa, Cargo
  Alumno 2 (Categorias y Presupuesto): Gasto, CategoriaGasto, Presupuesto, Transaccion
  Alumno 3 (Bancos y Prestamos): Banco, CuentaBancaria, Movimiento, Prestamo, TipoPrestamo
  Comunes: ValidRegex, ValidFecha, Login, RecuperarContrasena, RegistrarUsuario, Main
