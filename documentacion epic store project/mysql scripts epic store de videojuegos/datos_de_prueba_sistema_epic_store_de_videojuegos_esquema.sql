USE tienda_videojuegos;

SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE COMENTARIO;
TRUNCATE TABLE COMPRA;
TRUNCATE TABLE MOVIMIENTO_SALDO;
TRUNCATE TABLE BIBLIOTECA_JUEGO;
TRUNCATE TABLE MIEMBRO_GRUPO;
TRUNCATE TABLE GRUPO_FAMILIAR;
TRUNCATE TABLE BANNER_DESTACADO;
TRUNCATE TABLE VIDEOJUEGO_CATEGORIA;
TRUNCATE TABLE VIDEOJUEGO;
TRUNCATE TABLE USUARIO_EMPRESA;
TRUNCATE TABLE USUARIO;
TRUNCATE TABLE COMISION_EMPRESA;
TRUNCATE TABLE EMPRESA;
TRUNCATE TABLE COMISION_GLOBAL;
TRUNCATE TABLE CATEGORIA;
TRUNCATE TABLE CLASIFICACION_EDAD;
SET FOREIGN_KEY_CHECKS = 1;

-- =========================
-- Catalogos
-- =========================

INSERT INTO CLASIFICACION_EDAD (id_clasificacion, codigo, descripcion, edad_minima) VALUES
(1, 'E', 'Para todos', 0),
(2, 'T', 'Adolescentes (13+)', 13),
(3, 'M', 'Adultos (17+)', 17);

INSERT INTO CATEGORIA (id_categoria, nombre, descripcion) VALUES
(1, 'Acción', 'Juegos de acción y combate'),
(2, 'RPG', 'Rol y progresión de personaje'),
(3, 'Indie', 'Estudios independientes'),
(4, 'Estrategia', 'Gestión y táctica'),
(5, 'Carreras', 'Conducción y velocidad'),
(6, 'Terror', 'Experiencias de suspenso y horror'),
(7, 'Sandbox', 'Mundo abierto y creatividad');

-- =========================
-- Configuracion: comisiones
-- =========================

-- Comision global (historico) por defecto 15%
INSERT INTO COMISION_GLOBAL (id_comision_global, porcentaje, fecha_inicio_vigencia) VALUES
(1, 15.00, '2025-01-01 08:00:00'),
(2, 12.50, '2025-09-01 08:00:00'); -- ejemplo de cambio posterior

-- =========================
-- Empresas y comision especifica
-- =========================

INSERT INTO EMPRESA (id_empresa, nombre, descripcion, pais, correo_contacto, telefono_contacto, comentarios_visibles_global, estado) VALUES
(1, 'Nebula Games', 'Estudio enfocado en aventuras narrativas y mundos abiertos.', 'Guatemala', 'contacto@nebulagames.com', '+502 5555-0101', 'S', 'ACTIVA'),
(2, 'PixelForge Studios', 'Indies de alto impacto, pixel art y mecánicas únicas.', 'México', 'hola@pixelforge.mx', '+52 55 0000 0202', 'S', 'ACTIVA'),
(3, 'IronTide Interactive', 'Acción táctica y estrategia en tiempo real.', 'España', 'info@irontide.es', '+34 91 000 0303', 'S', 'ACTIVA');

-- Comisiones especificas (deben ser <= global vigente del registro asociado)
INSERT INTO COMISION_EMPRESA (id_comision_empresa, id_empresa, id_comision_global, porcentaje_especifico, fecha_inicio_vigencia) VALUES
(1, 2, 1, 10.00, '2025-02-01 10:00:00'), -- PixelForge negociada al 10%
(2, 3, 1, 12.00, '2025-03-15 09:00:00'); -- IronTide al 12%

-- =========================
-- Usuarios (comunes y admin)
-- =========================

INSERT INTO USUARIO (id_usuario, nickname, password, fecha_nacimiento, correo, telefono, pais, avatar, saldo_actual, biblioteca_publica, tipo_usuario) VALUES
(1, 'admin', 'admin123', '1995-05-10', 'admin@store.com', '+502 5555-0000', 'Guatemala', 'avatar_admin.png', 0.00, 'N', 'ADMIN'),
(2, 'alex', 'alex123', '2000-02-20', 'alex@gmail.com', '+502 5555-1111', 'Guatemala', 'avatar_alex.png', 350.00, 'S', 'COMUN'),
(3, 'maria', 'maria123', '2002-11-05', 'maria@gmail.com', '+502 5555-2222', 'Guatemala', 'avatar_maria.png', 120.00, 'S', 'COMUN'),
(4, 'diego', 'diego123', '1998-08-12', 'diego@gmail.com', '+502 5555-3333', 'Guatemala', 'avatar_diego.png', 50.00, 'N', 'COMUN'),
(5, 'sofia', 'sofia123', '2012-06-01', 'sofia@gmail.com', '+502 5555-4444', 'Guatemala', 'avatar_sofia.png', 80.00, 'N', 'COMUN'),
(6, 'carlos', 'carlos123', '1999-09-09', 'carlos@gmail.com', '+502 5555-5555', 'México', 'avatar_carlos.png', 200.00, 'N', 'COMUN'),
(7, 'ana', 'ana123', '2001-03-03', 'ana@gmail.com', '+502 5555-6666', 'España', 'avatar_ana.png', 65.00, 'S', 'COMUN'),
(77, 'ludvin', 'ludvin123', '2000-07-09', 'ludvin@gmail.com', '+502 3658-9885', 'Guatemala', 'avatar_ludvin.png', 2000.00, 'S', 'COMUN'),
(88, 'sergio', 'sergio123', '2017-10-10', 'sergio@gmail.com', '+502 8956-1245', 'Guatemala', 'avatar_sergio.png', 1500.00, 'S', 'COMUN');

-- =========================
-- Usuarios de empresa (internos)
-- =========================

INSERT INTO USUARIO_EMPRESA (id_usuario_empresa, id_empresa, nombre, correo, fecha_nacimiento, password) VALUES
(1, 1, 'Luis Mejía', 'luis@nebulagames.com', '1990-01-15', 'luis123'),
(2, 1, 'Karla Pérez', 'karla@nebulagames.com', '1994-07-22', 'karla123'),
(3, 2, 'Jorge Luna', 'jorge@pixelforge.mx', '1988-09-30', 'jorge123'),
(4, 3, 'Marta Ríos', 'marta@irontide.es', '1991-12-01', 'marta123');

-- =========================
-- Videojuegos
-- =========================

INSERT INTO VIDEOJUEGO (id_videojuego, id_empresa, id_clasificacion, titulo, descripcion, precio, requisitos_minimos, fecha_lanzamiento, imagen_portada, venta_activa, comentarios_visibles) VALUES
(1, 1, 2, 'Aurora Drift', 'Aventura narrativa con exploración en mundo abierto.', 149.99, 'CPU i5, 8GB RAM, GPU 2GB, 20GB libres', '2024-10-10', 'aurora_drift.png', 'S', 'S'),
(2, 1, 3, 'Nocturne Siege', 'Acción intensa con combate táctico. Contenido adulto.', 199.99, 'CPU i7, 16GB RAM, GPU 4GB, 40GB libres', '2025-02-14', 'nocturne_siege.png', 'S', 'S'),
(3, 2, 1, 'Pixel Pioneers', 'Indie relajante de construcción y exploración.', 79.99, 'CPU i3, 4GB RAM, GPU integrada, 5GB libres', '2023-06-01', 'pixel_pioneers.png', 'S', 'S'),
(4, 2, 2, 'Circuit Legends', 'Carreras arcade con modo historia.', 99.99, 'CPU i5, 8GB RAM, GPU 2GB, 10GB libres', '2024-03-20', 'circuit_legends.png', 'S', 'S'),
(5, 3, 2, 'Iron Atlas', 'Estrategia en tiempo real con gestión de recursos.', 129.99, 'CPU i5, 8GB RAM, GPU 2GB, 15GB libres', '2022-11-11', 'iron_atlas.png', 'S', 'S'),
(6, 3, 1, 'Garden Sandbox', 'Sandbox creativo para todas las edades.', 49.99, 'CPU i3, 4GB RAM, GPU integrada, 2GB libres', '2021-05-05', 'garden_sandbox.png', 'S', 'S'),
(7, 1, 2, 'Echoes of Rain', 'RPG con decisiones y progresión profunda.', 159.99, 'CPU i5, 8GB RAM, GPU 2GB, 25GB libres', '2025-06-30', 'echoes_of_rain.png', 'N', 'S'); -- venta desactivada (prueba)

-- =========================
-- Categorias por videojuego
-- =========================

INSERT INTO VIDEOJUEGO_CATEGORIA (id_vj_categoria, id_videojuego, id_categoria) VALUES
(1, 1, 7), -- Aurora Drift -> Sandbox (mundo abierto)
(2, 1, 2), -- Aurora Drift -> RPG
(3, 2, 1), -- Nocturne Siege -> Acción
(4, 2, 6), -- Nocturne Siege -> Terror
(5, 3, 3), -- Pixel Pioneers -> Indie
(6, 3, 7), -- Pixel Pioneers -> Sandbox
(7, 4, 5), -- Circuit Legends -> Carreras
(8, 4, 1), -- Circuit Legends -> Acción
(9, 5, 4), -- Iron Atlas -> Estrategia
(10, 6, 7), -- Garden Sandbox -> Sandbox
(11, 7, 2); -- Echoes of Rain -> RPG

-- =========================
-- Banner principal (destacados)
-- =========================

INSERT INTO BANNER_DESTACADO (id_banner, id_videojuego, posicion, activo, fecha_inicio_vigencia, fecha_fin_vigencia) VALUES
(1, 1, 1, 'S', '2025-11-01 00:00:00', NULL),
(2, 3, 2, 'S', '2025-11-01 00:00:00', NULL),
(3, 5, 3, 'S', '2025-11-01 00:00:00', NULL);

-- =========================
-- Grupos familiares
-- =========================

INSERT INTO GRUPO_FAMILIAR (id_grupo, nombre_grupo, fecha_creacion, estado) VALUES
(1, 'Familia Gamma', '2025-10-01 12:00:00', 'ACTIVO');

-- Maximo 6 miembros
INSERT INTO MIEMBRO_GRUPO (id_miembro_grupo, id_grupo, id_usuario, rol) VALUES
(1, 1, 2, 'DUENIO'),
(2, 1, 3, 'MIEMBRO'),
(3, 1, 4, 'MIEMBRO'),
(4, 1, 5, 'MIEMBRO'),
(5, 1, 6, 'MIEMBRO'),
(6, 1, 7, 'MIEMBRO');

-- =========================
-- Biblioteca (propio y prestado) y estados de instalacion
-- =========================

-- Juegos propios (comprados por cuenta propia)
INSERT INTO BIBLIOTECA_JUEGO (id_biblioteca_juego, id_usuario, id_videojuego, id_grupo_origen, tipo_propiedad, estado_instalacion, fecha_ultimo_cambio_estado) VALUES
(1, 2, 1, NULL, 'PROPIO', 'INSTALADO', '2025-12-01 18:00:00'),
(2, 2, 3, NULL, 'PROPIO', 'NO_INSTALADO', '2025-11-20 10:00:00'),
(3, 3, 3, NULL, 'PROPIO', 'INSTALADO', '2025-12-02 14:00:00'),
(4, 4, 5, NULL, 'PROPIO', 'NO_INSTALADO', '2025-10-10 09:30:00'),
(5, 6, 4, NULL, 'PROPIO', 'INSTALADO', '2025-12-03 20:00:00');

-- Juegos prestados por grupo (id_grupo_origen = 1)
INSERT INTO BIBLIOTECA_JUEGO (id_biblioteca_juego, id_usuario, id_videojuego, id_grupo_origen, tipo_propiedad, estado_instalacion, fecha_ultimo_cambio_estado) VALUES
(6, 3, 1, 1, 'PRESTADO', 'NO_INSTALADO', '2025-12-05 08:00:00'),
(7, 4, 1, 1, 'PRESTADO', 'INSTALADO', '2025-12-06 19:15:00'),
(8, 5, 6, 1, 'PRESTADO', 'INSTALADO', '2025-12-07 17:45:00'),
(9, 7, 5, 1, 'PRESTADO', 'NO_INSTALADO', '2025-12-06 09:10:00');

-- =========================
-- Movimientos de saldo (recargas, compras)
-- =========================

INSERT INTO MOVIMIENTO_SALDO (id_movimiento, id_usuario, tipo, fecha, monto, saldo_resultante, descripcion) VALUES
(1, 2, 'RECARGA', '2025-11-15 09:00:00', 300.00, 300.00, 'Recarga inicial'),
(2, 2, 'RECARGA', '2025-12-01 10:00:00', 200.00, 500.00, 'Recarga adicional'),
(3, 3, 'RECARGA', '2025-11-20 11:30:00', 150.00, 150.00, 'Recarga'),
(4, 6, 'RECARGA', '2025-11-25 16:00:00', 250.00, 250.00, 'Recarga'),
(5, 2, 'COMPRA',  '2025-12-01 18:05:00', -149.99, 350.01, 'Compra Aurora Drift'),
(6, 3, 'COMPRA',  '2025-12-02 14:10:00', -79.99, 70.01,  'Compra Pixel Pioneers'),
(7, 6, 'COMPRA',  '2025-12-03 20:05:00', -99.99, 150.01, 'Compra Circuit Legends');

-- Ajustamos saldo_actual para que coincida con los movimientos (segun los ejemplos anteriores)
UPDATE USUARIO SET saldo_actual = 350.01 WHERE id_usuario = 2;
UPDATE USUARIO SET saldo_actual = 70.01  WHERE id_usuario = 3;
UPDATE USUARIO SET saldo_actual = 50.00  WHERE id_usuario = 4;
UPDATE USUARIO SET saldo_actual = 80.00  WHERE id_usuario = 5;
UPDATE USUARIO SET saldo_actual = 150.01 WHERE id_usuario = 6;
UPDATE USUARIO SET saldo_actual = 65.00  WHERE id_usuario = 7;

-- =========================
-- Compras (fecha_compra manual para simular historico)
-- =========================
-- Comision global usada en estas compras: 15% (para simplificar el set de prueba)
-- Aurora Drift: 149.99 -> comision 22.50 (aprox), empresa 127.49
-- Pixel Pioneers: 79.99 -> comision 12.00, empresa 67.99
-- Circuit Legends: 99.99 -> comision 15.00, empresa 84.99

INSERT INTO COMPRA (id_compra, id_usuario, id_videojuego, fecha_compra, precio_unitario, porcentaje_comision, monto_comision, monto_empresa) VALUES
(1, 2, 1, '2025-12-01', 149.99, 15.00, 22.50, 127.49),
(2, 3, 3, '2025-12-02', 79.99,  15.00, 12.00, 67.99),
(3, 6, 4, '2025-12-03', 99.99,  15.00, 15.00, 84.99);

-- =========================
-- Comentarios (hilos) + calificaciones
-- =========================

-- Comentario raiz de Alex sobre Aurora Drift
INSERT INTO COMENTARIO (id_comentario, id_usuario, id_videojuego, id_comentario_padre, fecha, texto, calificacion, visible) VALUES
(1, 2, 1, NULL, '2025-12-02 09:00:00', 'Muy buen mundo abierto, historia sólida y exploración divertida.', 5, 'S');

-- Respuesta de Maria al comentario de Alex (hilo)
INSERT INTO COMENTARIO (id_comentario, id_usuario, id_videojuego, id_comentario_padre, fecha, texto, calificacion, visible) VALUES
(2, 3, 1, 1, '2025-12-02 10:15:00', 'Totalmente de acuerdo, el soundtrack está excelente.', 5, 'S');

-- Comentario de Maria sobre Pixel Pioneers
INSERT INTO COMENTARIO (id_comentario, id_usuario, id_videojuego, id_comentario_padre, fecha, texto, calificacion, visible) VALUES
(3, 3, 3, NULL, '2025-12-03 08:20:00', 'Relajante y creativo. Perfecto para sesiones cortas.', 4, 'S');

-- Respuesta de Alex a Pixel Pioneers
INSERT INTO COMENTARIO (id_comentario, id_usuario, id_videojuego, id_comentario_padre, fecha, texto, calificacion, visible) VALUES
(4, 2, 3, 3, '2025-12-03 09:05:00', 'Sí, es ideal para desconectarse. Buen trabajo del estudio.', 4, 'S');

-- Comentario de Carlos sobre Circuit Legends (con visibilidad normal)
INSERT INTO COMENTARIO (id_comentario, id_usuario, id_videojuego, id_comentario_padre, fecha, texto, calificacion, visible) VALUES
(5, 6, 4, NULL, '2025-12-04 21:10:00', 'Arcade divertido, aunque el online podría mejorar.', 4, 'S');

-- Un comentario oculto (prueba de moderacion a nivel comentario)
INSERT INTO COMENTARIO (id_comentario, id_usuario, id_videojuego, id_comentario_padre, fecha, texto, calificacion, visible) VALUES
(6, 6, 4, NULL, '2025-12-05 09:00:00', 'Texto oculto por moderación (prueba).', 3, 'N');
