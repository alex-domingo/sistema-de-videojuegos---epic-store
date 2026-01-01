CREATE DATABASE tienda_videojuegos;
USE tienda_videojuegos;

-- =========================
-- Catalogos y configuracion
-- =========================

CREATE TABLE CLASIFICACION_EDAD (
  id_clasificacion INT NOT NULL AUTO_INCREMENT,
  codigo VARCHAR(10) NOT NULL,
  descripcion VARCHAR(255) NOT NULL,
  edad_minima INT NOT NULL,
  PRIMARY KEY (id_clasificacion),
  UNIQUE (codigo)
);

CREATE TABLE CATEGORIA (
  id_categoria INT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(120) NOT NULL,
  descripcion VARCHAR(255),
  PRIMARY KEY (id_categoria),
  UNIQUE (nombre)
);

-- Comision global vigente e historico
CREATE TABLE COMISION_GLOBAL (
  id_comision_global INT NOT NULL AUTO_INCREMENT,
  porcentaje DECIMAL(5,2) NOT NULL,
  fecha_inicio_vigencia DATETIME NOT NULL,
  PRIMARY KEY (id_comision_global)
);

CREATE TABLE EMPRESA (
  id_empresa INT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(150) NOT NULL,
  descripcion TEXT,
  pais VARCHAR(80),
  correo_contacto VARCHAR(180),
  telefono_contacto VARCHAR(40),
  comentarios_visibles_global CHAR(1) NOT NULL DEFAULT 'S', -- 'S'/'N'
  estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVA', -- ACTIVA/INACTIVA
  PRIMARY KEY (id_empresa),
  UNIQUE (nombre)
);

-- Comision especifica por empresa (historico)
CREATE TABLE COMISION_EMPRESA (
  id_comision_empresa INT NOT NULL AUTO_INCREMENT,
  id_empresa INT NOT NULL,
  id_comision_global INT NOT NULL,
  porcentaje_especifico DECIMAL(5,2) NOT NULL,
  fecha_inicio_vigencia DATETIME NOT NULL,
  PRIMARY KEY (id_comision_empresa),
  FOREIGN KEY (id_empresa) REFERENCES EMPRESA(id_empresa),
  FOREIGN KEY (id_comision_global) REFERENCES COMISION_GLOBAL(id_comision_global)
);

-- =========================
-- Usuarios y organizacion
-- =========================

CREATE TABLE USUARIO (
  id_usuario INT NOT NULL AUTO_INCREMENT,
  nickname VARCHAR(60) NOT NULL,
  password VARCHAR(255) NOT NULL,
  fecha_nacimiento DATE NOT NULL,
  correo VARCHAR(180) NOT NULL,
  telefono VARCHAR(40),
  pais VARCHAR(80),
  avatar VARCHAR(255),
  saldo_actual DECIMAL(12,2) NOT NULL DEFAULT 0.00,
  biblioteca_publica CHAR(1) NOT NULL DEFAULT 'N', -- 'S'/'N'
  tipo_usuario VARCHAR(20) NOT NULL DEFAULT 'COMUN', -- COMUN/ADMIN
  PRIMARY KEY (id_usuario),
  UNIQUE (nickname),
  UNIQUE (correo)
);

-- Usuarios ligados a una empresa (gestion interna de la empresa)
CREATE TABLE USUARIO_EMPRESA (
  id_usuario_empresa INT NOT NULL AUTO_INCREMENT,
  id_empresa INT NOT NULL,
  nombre VARCHAR(120) NOT NULL,
  correo VARCHAR(180) NOT NULL,
  fecha_nacimiento DATE NOT NULL,
  password VARCHAR(255) NOT NULL,
  PRIMARY KEY (id_usuario_empresa),
  UNIQUE (correo),
  FOREIGN KEY (id_empresa) REFERENCES EMPRESA(id_empresa)
);

-- =========================
-- Videojuegos y relaciones
-- =========================

CREATE TABLE VIDEOJUEGO (
  id_videojuego INT NOT NULL AUTO_INCREMENT,
  id_empresa INT NOT NULL,
  id_clasificacion INT NOT NULL,
  titulo VARCHAR(180) NOT NULL,
  descripcion TEXT,
  precio DECIMAL(12,2) NOT NULL,
  requisitos_minimos TEXT,
  fecha_lanzamiento DATE,
  imagen_portada VARCHAR(255),
  venta_activa CHAR(1) NOT NULL DEFAULT 'S', -- 'S'/'N'
  comentarios_visibles CHAR(1) NOT NULL DEFAULT 'S', -- 'S'/'N'
  PRIMARY KEY (id_videojuego),
  FOREIGN KEY (id_empresa) REFERENCES EMPRESA(id_empresa),
  FOREIGN KEY (id_clasificacion) REFERENCES CLASIFICACION_EDAD(id_clasificacion)
);

CREATE TABLE VIDEOJUEGO_CATEGORIA (
  id_vj_categoria INT NOT NULL AUTO_INCREMENT,
  id_videojuego INT NOT NULL,
  id_categoria INT NOT NULL,
  PRIMARY KEY (id_vj_categoria),
  FOREIGN KEY (id_videojuego) REFERENCES VIDEOJUEGO(id_videojuego),
  FOREIGN KEY (id_categoria) REFERENCES CATEGORIA(id_categoria),
  UNIQUE (id_videojuego, id_categoria)
);

-- Banner principal / destacados (gestionado manualmente)
CREATE TABLE BANNER_DESTACADO (
  id_banner INT NOT NULL AUTO_INCREMENT,
  id_videojuego INT NOT NULL,
  posicion INT NOT NULL,
  activo CHAR(1) NOT NULL DEFAULT 'S', -- 'S'/'N'
  fecha_inicio_vigencia DATETIME,
  fecha_fin_vigencia DATETIME,
  PRIMARY KEY (id_banner),
  FOREIGN KEY (id_videojuego) REFERENCES VIDEOJUEGO(id_videojuego),
  UNIQUE (posicion)
);

-- =========================
-- Compras, saldo y biblioteca
-- =========================

-- Registro de movimientos del saldo (recargas, compras, ajustes)
CREATE TABLE MOVIMIENTO_SALDO (
  id_movimiento INT NOT NULL AUTO_INCREMENT,
  id_usuario INT NOT NULL,
  tipo VARCHAR(20) NOT NULL, -- RECARGA/COMPRA/AJUSTE
  fecha DATETIME NOT NULL,
  monto DECIMAL(12,2) NOT NULL,
  saldo_resultante DECIMAL(12,2) NOT NULL,
  descripcion VARCHAR(255),
  PRIMARY KEY (id_movimiento),
  FOREIGN KEY (id_usuario) REFERENCES USUARIO(id_usuario)
);

-- Compra por videojuego
CREATE TABLE COMPRA (
  id_compra INT NOT NULL AUTO_INCREMENT,
  id_usuario INT NOT NULL,
  id_videojuego INT NOT NULL,
  fecha_compra DATE NOT NULL,
  precio_unitario DECIMAL(12,2) NOT NULL,
  porcentaje_comision DECIMAL(5,2) NOT NULL,
  monto_comision DECIMAL(12,2) NOT NULL,
  monto_empresa DECIMAL(12,2) NOT NULL,
  PRIMARY KEY (id_compra),
  FOREIGN KEY (id_usuario) REFERENCES USUARIO(id_usuario),
  FOREIGN KEY (id_videojuego) REFERENCES VIDEOJUEGO(id_videojuego)
);

-- Biblioteca del usuario: juegos propios o prestados
CREATE TABLE BIBLIOTECA_JUEGO (
  id_biblioteca_juego INT NOT NULL AUTO_INCREMENT,
  id_usuario INT NOT NULL,
  id_videojuego INT NOT NULL,
  id_grupo_origen INT,
  tipo_propiedad VARCHAR(15) NOT NULL, -- PROPIO/PRESTADO
  estado_instalacion VARCHAR(15) NOT NULL, -- INSTALADO/NO_INSTALADO
  fecha_ultimo_cambio_estado DATETIME,
  PRIMARY KEY (id_biblioteca_juego),
  FOREIGN KEY (id_usuario) REFERENCES USUARIO(id_usuario),
  FOREIGN KEY (id_videojuego) REFERENCES VIDEOJUEGO(id_videojuego)
);

-- =========================
-- Comentarios, calificaciones e hilos
-- =========================

CREATE TABLE COMENTARIO (
  id_comentario INT NOT NULL AUTO_INCREMENT,
  id_usuario INT NOT NULL,
  id_videojuego INT NOT NULL,
  id_comentario_padre INT,
  fecha DATETIME NOT NULL,
  texto TEXT,
  calificacion INT NOT NULL, -- 1 a 5
  visible CHAR(1) NOT NULL DEFAULT 'S', -- 'S'/'N'
  PRIMARY KEY (id_comentario),
  FOREIGN KEY (id_usuario) REFERENCES USUARIO(id_usuario),
  FOREIGN KEY (id_videojuego) REFERENCES VIDEOJUEGO(id_videojuego),
  FOREIGN KEY (id_comentario_padre) REFERENCES COMENTARIO(id_comentario)
);

-- =========================
-- Grupos familiares (biblioteca familiar)
-- =========================

CREATE TABLE GRUPO_FAMILIAR (
  id_grupo INT NOT NULL AUTO_INCREMENT,
  nombre_grupo VARCHAR(120) NOT NULL,
  fecha_creacion DATETIME NOT NULL,
  estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVO', -- ACTIVO/INACTIVO
  PRIMARY KEY (id_grupo)
);

CREATE TABLE MIEMBRO_GRUPO (
  id_miembro_grupo INT NOT NULL AUTO_INCREMENT,
  id_grupo INT NOT NULL,
  id_usuario INT NOT NULL,
  rol VARCHAR(20) NOT NULL, -- DUENIO/MIEMBRO
  PRIMARY KEY (id_miembro_grupo),
  FOREIGN KEY (id_grupo) REFERENCES GRUPO_FAMILIAR(id_grupo),
  FOREIGN KEY (id_usuario) REFERENCES USUARIO(id_usuario),
  UNIQUE (id_grupo, id_usuario)
);

ALTER TABLE BIBLIOTECA_JUEGO
  ADD FOREIGN KEY (id_grupo_origen) REFERENCES GRUPO_FAMILIAR(id_grupo);
  
-- Comision global por defecto (15%)
INSERT INTO COMISION_GLOBAL (porcentaje, fecha_inicio_vigencia) VALUES
(15.00, NOW());
