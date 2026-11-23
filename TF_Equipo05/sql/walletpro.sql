/* ==============================================================
   WALLET PRO - Base de datos en SQL Server
   --------------------------------------------------------------
   Como usarlo:
     1. Abrir este archivo en SQL Server Management Studio (SSMS).
     2. Presionar "Ejecutar" (F5).
   Crea la base WalletPro, sus tablas, datos de prueba y el
   usuario "walletpro" con el que se conecta el programa Java.

   OJO: si la base WalletPro ya existe, la BORRA y la crea de
   nuevo (sirve para volver a empezar con los datos de prueba).
   ============================================================== */

USE master;
GO

IF DB_ID('WalletPro') IS NOT NULL
BEGIN
    -- Desconecta a todos (incluido NetBeans) para poder borrarla
    ALTER DATABASE WalletPro SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE WalletPro;
END
GO

CREATE DATABASE WalletPro;
GO

USE WalletPro;
GO

/* ==============================================================
   ACCESO AL SISTEMA  (clase Java: Usuario)
   ============================================================== */
CREATE TABLE usuario (
    id_usuario          INT IDENTITY(1,1) PRIMARY KEY,   -- se numera solo: 1, 2, 3...
    dni                 CHAR(8)      NOT NULL,
    nombres             VARCHAR(50)  NOT NULL,
    apellidos           VARCHAR(50)  NOT NULL,
    fecha_nacimiento    DATE         NOT NULL,
    correo              VARCHAR(100) NOT NULL UNIQUE,    -- UNIQUE: no se repite
    nombre_usuario      VARCHAR(30)  NOT NULL UNIQUE,
    contrasena          VARCHAR(50)  NOT NULL,
    pregunta_seguridad  VARCHAR(100) NOT NULL,
    respuesta_seguridad VARCHAR(100) NOT NULL
);

/* ==============================================================
   ALUMNO 1 - INGRESOS Y AHORROS
   ============================================================== */
CREATE TABLE empresa (                                -- clase Empresa
    ruc          CHAR(11)     PRIMARY KEY,
    razon_social VARCHAR(100) NOT NULL
);

CREATE TABLE cargo (                                  -- clase Cargo
    id_cargo    INT IDENTITY(1,1) PRIMARY KEY,
    nombre      VARCHAR(50)  NOT NULL UNIQUE,
    descripcion VARCHAR(100) NOT NULL
);

CREATE TABLE empleado (                               -- clase Empleado
    dni              CHAR(8)       PRIMARY KEY,
    nombres          VARCHAR(50)   NOT NULL,
    apellidos        VARCHAR(50)   NOT NULL,
    fecha_nacimiento DATE          NOT NULL,
    correo           VARCHAR(100)  NOT NULL,
    sueldo_base      DECIMAL(10,2) NOT NULL CHECK (sueldo_base > 0)
);

CREATE TABLE trabajo (                                -- clase Trabajo
    id_trabajo    INT IDENTITY(1,1) PRIMARY KEY,
    dni           CHAR(8)     NOT NULL REFERENCES empleado(dni),   -- REFERENCES = llave foranea
    ruc           CHAR(11)    NOT NULL REFERENCES empresa(ruc),
    id_cargo      INT         NOT NULL REFERENCES cargo(id_cargo),
    fecha_ingreso DATE        NOT NULL,
    tipo_contrato VARCHAR(20) NOT NULL,
    UNIQUE (dni, ruc, id_cargo)        -- el mismo cargo en la misma empresa no se repite
);

CREATE TABLE sueldo (                                 -- clase Sueldo
    id_sueldo INT IDENTITY(1,1) PRIMARY KEY,
    dni       CHAR(8)       NOT NULL REFERENCES empleado(dni),
    mes       INT           NOT NULL CHECK (mes BETWEEN 1 AND 12),
    anio      INT           NOT NULL,
    monto     DECIMAL(10,2) NOT NULL CHECK (monto > 0),
    UNIQUE (dni, mes, anio)            -- un solo sueldo por mes
);

/* ==============================================================
   ALUMNO 3 - BANCOS Y PRESTAMOS
   (va antes que gasto porque un gasto puede pagarse con una cuenta)
   ============================================================== */
CREATE TABLE banco (                                  -- clase Banco
    codigo CHAR(5)     PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL
);

CREATE TABLE cuenta (                                 -- clase CuentaBancaria
    numero_cuenta VARCHAR(20)   PRIMARY KEY,
    tipo_cuenta   VARCHAR(10)   NOT NULL,
    saldo         DECIMAL(12,2) NOT NULL CHECK (saldo >= 0),   -- nunca negativo
    codigo_banco  CHAR(5)       NOT NULL REFERENCES banco(codigo),
    dni           CHAR(8)       NOT NULL REFERENCES empleado(dni)
);

CREATE TABLE movimiento (                             -- clase Movimiento
    id_movimiento    INT IDENTITY(1,1) PRIMARY KEY,
    numero_cuenta    VARCHAR(20)   NOT NULL REFERENCES cuenta(numero_cuenta),
    fecha            DATE          NOT NULL,
    tipo             VARCHAR(10)   NOT NULL CHECK (tipo IN ('DEPOSITO', 'RETIRO')),
    monto            DECIMAL(12,2) NOT NULL CHECK (monto > 0),
    descripcion      VARCHAR(100)  NOT NULL,
    saldo_resultante DECIMAL(12,2) NOT NULL
);

CREATE TABLE tipo_prestamo (                          -- clase TipoPrestamo
    id_tipo     INT IDENTITY(1,1) PRIMARY KEY,
    nombre      VARCHAR(50)  NOT NULL UNIQUE,
    descripcion VARCHAR(100) NOT NULL
);

CREATE TABLE prestamo (                               -- clase Prestamo
    id_prestamo     INT IDENTITY(1,1) PRIMARY KEY,
    dni             CHAR(8)       NOT NULL REFERENCES empleado(dni),
    codigo_banco    CHAR(5)       NOT NULL REFERENCES banco(codigo),
    id_tipo         INT           NOT NULL REFERENCES tipo_prestamo(id_tipo),
    monto           DECIMAL(12,2) NOT NULL CHECK (monto > 0),
    tasa_mensual    DECIMAL(5,2)  NOT NULL,       -- en %, ej. 2.50
    numero_cuotas   INT           NOT NULL,
    cuotas_pagadas  INT           NOT NULL,
    saldo_pendiente DECIMAL(12,2) NOT NULL
);

/* ==============================================================
   ALUMNO 2 - GASTOS Y PRESUPUESTO
   ============================================================== */
CREATE TABLE categoria (                              -- clase CategoriaGasto
    id_categoria INT IDENTITY(1,1) PRIMARY KEY,
    nombre       VARCHAR(50)  NOT NULL UNIQUE,
    descripcion  VARCHAR(100) NOT NULL
);

CREATE TABLE gasto (                                  -- clase Gasto
    id_gasto      INT IDENTITY(1,1) PRIMARY KEY,
    dni           CHAR(8)       NOT NULL REFERENCES empleado(dni),
    id_categoria  INT           NOT NULL REFERENCES categoria(id_categoria),
    fecha         DATE          NOT NULL,
    monto         DECIMAL(10,2) NOT NULL CHECK (monto > 0),
    descripcion   VARCHAR(100)  NOT NULL,
    es_ocasional  BIT           NOT NULL,          -- BIT = boolean (1 si, 0 no)
    numero_cuenta VARCHAR(20)   NULL REFERENCES cuenta(numero_cuenta)   -- NULL = pago en efectivo
);

CREATE TABLE presupuesto (                            -- clase Presupuesto
    id_presupuesto INT IDENTITY(1,1) PRIMARY KEY,
    dni            CHAR(8)       NOT NULL REFERENCES empleado(dni),
    id_categoria   INT           NOT NULL REFERENCES categoria(id_categoria),
    mes            INT           NOT NULL CHECK (mes BETWEEN 1 AND 12),
    anio           INT           NOT NULL,
    monto_limite   DECIMAL(10,2) NOT NULL CHECK (monto_limite > 0),
    UNIQUE (dni, id_categoria, mes, anio)   -- un presupuesto por categoria y mes
);
GO

/* ==============================================================
   DATOS DE PRUEBA
   Los sueldos, gastos y presupuestos se crean en el MES ACTUAL
   para que los reportes muestren datos apenas se abre el programa.
   Usuario para entrar: Administrador / admin1234
   ============================================================== */
DECLARE @mes    INT  = MONTH(GETDATE());
DECLARE @anio   INT  = YEAR(GETDATE());
DECLARE @inicio DATE = DATEFROMPARTS(@anio, @mes, 1);    -- dia 1 del mes actual

INSERT INTO usuario (dni, nombres, apellidos, fecha_nacimiento, correo, nombre_usuario,
                     contrasena, pregunta_seguridad, respuesta_seguridad)
VALUES ('00000000', 'Administrador', 'Sistema', '2000-01-01', 'admin@walletpro.com',
        'Administrador', 'admin1234', 'Color favorito', 'azul');

-- Alumno 1
INSERT INTO empresa (ruc, razon_social) VALUES
    ('20123456789', 'ON EMPRESAS SAC'),
    ('20234567890', 'GROUP INFORMATIC SAC');

INSERT INTO cargo (nombre, descripcion) VALUES
    ('Desarrollador', 'Desarrollo de software'),      -- id_cargo = 1
    ('Soporte', 'Soporte tecnico');                    -- id_cargo = 2

INSERT INTO empleado (dni, nombres, apellidos, fecha_nacimiento, correo, sueldo_base) VALUES
    ('00000001', 'Susana', 'Pinero', '1998-06-04', 'susana@correo.com', 3500),
    ('00000002', 'Carlos', 'Eliott', '2001-06-15', 'carlos@correo.com', 2800);

INSERT INTO trabajo (dni, ruc, id_cargo, fecha_ingreso, tipo_contrato) VALUES
    ('00000001', '20123456789', 1, '2024-06-25', 'Contrato'),
    ('00000002', '20234567890', 2, '2023-06-25', 'Planilla');

INSERT INTO sueldo (dni, mes, anio, monto) VALUES ('00000001', @mes, @anio, 3500);

-- Alumno 3
INSERT INTO banco (codigo, nombre) VALUES ('00001', 'Interbank'), ('00002', 'BCP');

-- Saldo de Susana = 2500 (inicial) + 3500 (sueldo) - 40 (Netflix) = 5960
INSERT INTO cuenta (numero_cuenta, tipo_cuenta, saldo, codigo_banco, dni) VALUES
    ('000120003000', 'Ahorros', 5960, '00001', '00000001'),
    ('000220004000', 'Ahorros', 1500, '00002', '00000002');

INSERT INTO movimiento (numero_cuenta, fecha, tipo, monto, descripcion, saldo_resultante) VALUES
    ('000120003000', @inicio, 'DEPOSITO', 2500, 'Saldo inicial', 2500),
    ('000120003000', @inicio, 'DEPOSITO', 3500, 'Sueldo del mes', 6000),
    ('000120003000', @inicio, 'RETIRO',     40, 'Gasto: Netflix', 5960),
    ('000220004000', @inicio, 'DEPOSITO', 1500, 'Saldo inicial', 1500);

INSERT INTO tipo_prestamo (nombre, descripcion) VALUES
    ('Personal', 'Libre disponibilidad'),              -- id_tipo = 1
    ('Hipotecario', 'Para vivienda');                  -- id_tipo = 2

-- 1000 al 2% mensual en 6 cuotas: interes 120, total a pagar 1120
INSERT INTO prestamo (dni, codigo_banco, id_tipo, monto, tasa_mensual, numero_cuotas,
                      cuotas_pagadas, saldo_pendiente)
VALUES ('00000002', '00002', 1, 1000, 2, 6, 0, 1120);

-- Alumno 2
INSERT INTO categoria (nombre, descripcion) VALUES
    ('Alimentacion', 'Comida y supermercado'),         -- id_categoria = 1
    ('Transporte', 'Pasajes y combustible'),           -- 2
    ('Salud', 'Medicinas y consultas'),                -- 3
    ('Entretenimiento', 'Ocio y streaming'),           -- 4
    ('Otros', 'Gastos varios');                        -- 5

INSERT INTO gasto (dni, id_categoria, fecha, monto, descripcion, es_ocasional, numero_cuenta) VALUES
    ('00000001', 4, @inicio, 40, 'Netflix', 0, '000120003000'),
    ('00000001', 1, DATEADD(DAY, 14, @inicio), 650, 'Compra mensual de comida', 0, NULL);

-- Alimentacion: 650 de 800 = 81% (ALERTA) | Entretenimiento: 40 de 100 = 40% (OK)
INSERT INTO presupuesto (dni, id_categoria, mes, anio, monto_limite) VALUES
    ('00000001', 1, @mes, @anio, 800),
    ('00000001', 4, @mes, @anio, 100);
GO

/* ==============================================================
   USUARIO DE SQL SERVER QUE USA JAVA
   Asi los tres integrantes tienen el mismo usuario y clave, y no
   hay que cambiar Conexion.java en cada computadora.
   Requiere que SQL Server tenga activada la "autenticacion mixta"
   (ver docs/BASE_DE_DATOS.md).
   ============================================================== */
USE master;
GO
IF NOT EXISTS (SELECT * FROM sys.server_principals WHERE name = 'walletpro')
    CREATE LOGIN walletpro WITH PASSWORD = 'WalletPro2026', CHECK_POLICY = OFF;
GO
USE WalletPro;
GO
CREATE USER walletpro FOR LOGIN walletpro;
ALTER ROLE db_owner ADD MEMBER walletpro;
GO

-- Prueba rapida: deberia mostrar los dos empleados
SELECT * FROM empleado;
