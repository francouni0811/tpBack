-- =========================
-- CLIENTES
-- =========================
INSERT INTO clientes (nombre, telefono, email) VALUES
('Juan Pérez',        3515551111, 'juan.perez@example.com'),
('María López',       3515552222, 'maria.lopez@example.com'),
('Constructora Andina SA', 3515553333, 'contacto@andina.com'),
('Logística Centro SRL',   3515554444, 'info@logisticacentro.com'),
('Supermercados El Ahorro',3515555555, 'compras@elahorro.com');

-- IDs generados (asumiendo serial):
-- 1: Juan Pérez
-- 2: María López
-- 3: Constructora Andina SA
-- 4: Logística Centro SRL
-- 5: Supermercados El Ahorro

-- =========================
-- TRANSPORTISTAS
-- =========================
INSERT INTO transportistas (nombre, telefono) VALUES
('Transporte Norte SA', 3516001000),
('Logística Sur SRL',   3516002000),
('Camiones del Centro', 3516003000);

-- IDs:
-- 1: Transporte Norte SA
-- 2: Logística Sur SRL
-- 3: Camiones del Centro

-- =========================
-- CAMIONES
-- =========================
INSERT INTO camiones (
  id_transportista,
  patente,
  telefono,
  capacidadKg,
  volumen_max,
  estado,
  consumoXKm,
  costo_base_trasladoXKm
) VALUES
(1, 'AA123BB', 3517001001, 12000.00,  30.000, 'Disponible',   0.320, 250.00),
(1, 'AA234CC', 3517001002,  8000.00,  20.000, 'Disponible',   0.280, 220.00),
(2, 'AB345DD', 3517002001, 16000.00,  40.000, 'Mantenimiento',0.350, 270.00),
(3, 'AC456EE', 3517003001, 10000.00,  25.000, 'Trasladando',  0.300, 240.00);

-- =========================
-- DEPOSITOS
-- =========================
-- Direcciones con: provincia, ciudad, calle y número
INSERT INTO depositos (
  nombre,
  direccion_txt,
  longitud,
  latitud,
  costo_estadia_hora
) VALUES
-- 1) Córdoba
('Depósito Córdoba Centro',
 'Córdoba, Córdoba Capital, Av. Colón 1234',
 -64.181000, -31.417000, 1500.00),

-- 2) Buenos Aires
('Depósito Buenos Aires',
 'Buenos Aires, CABA, Av. Corrientes 450',
 -58.384000, -34.603700, 2200.00),

-- 3) Rosario
('Depósito Rosario',
 'Santa Fe, Rosario, Bv. Oroño 1500',
 -60.660000, -32.950000, 1700.00),

-- 4) Mendoza
('Depósito Mendoza',
 'Mendoza, Ciudad de Mendoza, Av. San Martín 850',
 -68.845000, -32.890000, 1800.00),

-- 5) Salta
('Depósito Salta',
 'Salta, Ciudad de Salta, Av. Belgrano 900',
 -65.410000, -24.787000, 1600.00),

-- 6) Ushuaia
('Depósito Ushuaia',
 'Tierra del Fuego, Ushuaia, Av. Maipú 120',
 -68.309000, -54.801000, 2500.00);


-- =========================
-- CONTENEDORES
-- =========================
INSERT INTO contenedores (
  id_cliente,
  pesoKg,
  volumen,
  estado
) VALUES
(1,  800.0,  8.500, 'Disponible'),
(2, 1200.0, 12.000, 'Disponible'),
(3, 3500.0, 28.000, 'Disponible'),
(4, 5000.0, 40.000, 'Disponible'),
(5,  600.0,  5.000, 'Disponible');

-- IDs contenedores:
-- 1: Cliente 1
-- 2: Cliente 2
-- 3: Cliente 3
-- 4: Cliente 4
-- 5: Cliente 5

-- =========================
-- TARIFAS
-- =========================
INSERT INTO tarifas (
  descripcion,
  vol_min,
  vol_max,
  costo_base_km_xvol,
  valor_combustible,
  activa
) VALUES
('Tarifa Volumen Chico',   0.000, 10.000,  5.5000, 1.2000, TRUE),
('Tarifa Volumen Mediano',10.000, 30.000,  4.8000, 1.1500, TRUE),
('Tarifa Volumen Grande', 30.000,100.000,  4.2000, 1.1000, TRUE);

-- IDs tarifas:
-- 1: Chico
-- 2: Mediano
-- 3: Grande

-- =========================
-- SOLICITUDES
-- =========================
-- IMPORTANTE:
--  - No se cargan costo_estimado, tiempo_estimado_hs, costo_final ni tiempo_final_hs
--  - Direcciones con provincia, ciudad, calle y número
--  - Se deja que fechaSolicitud use el DEFAULT NOW()
INSERT INTO solicitudes (
  id_contenedor,
  id_cliente,
  id_tarifa,
  estado,
  origen_direccion,
  origen_latitud,
  origen_longitud,
  destino_direccion,
  destino_latitud,
  destino_longitud
) VALUES
-- Solicitud 1: Córdoba -> Rosario
(1, 1, 1,
 'BORRADOR',
 'Córdoba, Córdoba Capital, Av. Colón 1234',
 -31.417000, -64.181000,
 'Santa Fe, Rosario, Bv. Oroño 1500',
 -32.950000, -60.660000),

-- Solicitud 2: CABA -> Córdoba
(2, 2, 2,
 'BORRADOR',
 'Buenos Aires, CABA, Av. Corrientes 450',
 -34.603700, -58.384000,
 'Córdoba, Córdoba Capital, Bv. San Juan 800',
 -31.417500, -64.191000),

-- Solicitud 3: Mendoza -> Córdoba
(3, 3, 2,
 'BORRADOR',
 'Mendoza, Ciudad de Mendoza, Av. San Martín 850',
 -32.890000, -68.845000,
 'Córdoba, Córdoba Capital, Av. Vélez Sarsfield 500',
 -31.420500, -64.188000),

-- Solicitud 4: Salta -> Buenos Aires
(4, 4, 3,
 'BORRADOR',
 'Salta, Ciudad de Salta, Av. Belgrano 900',
 -24.787000, -65.410000,
 'Buenos Aires, CABA, Av. Paseo Colón 700',
 -34.614000, -58.370000),

-- Solicitud 5: Ushuaia -> Mendoza
(5, 5, 1,
 'BORRADOR',
 'Tierra del Fuego, Ushuaia, Av. Maipú 120',
 -54.801000, -68.309000,
 'Mendoza, Ciudad de Mendoza, Av. Las Heras 300',
 -32.889000, -68.842000);
