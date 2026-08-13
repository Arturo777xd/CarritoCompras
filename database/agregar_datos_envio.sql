-- Ejecuta este script una sola vez en la base ProyectoCarrito.
-- Agrega los datos que se guardarán para los envíos de cada usuario.
ALTER TABLE usuarios ADD COLUMN IF NOT EXISTS nombre_completo VARCHAR(150);
ALTER TABLE usuarios ADD COLUMN IF NOT EXISTS telefono VARCHAR(20);
ALTER TABLE usuarios ADD COLUMN IF NOT EXISTS direccion VARCHAR(200);
ALTER TABLE usuarios ADD COLUMN IF NOT EXISTS colonia VARCHAR(100);
ALTER TABLE usuarios ADD COLUMN IF NOT EXISTS ciudad VARCHAR(100);
ALTER TABLE usuarios ADD COLUMN IF NOT EXISTS estado VARCHAR(100);
ALTER TABLE usuarios ADD COLUMN IF NOT EXISTS codigo_postal VARCHAR(10);
ALTER TABLE usuarios ADD COLUMN IF NOT EXISTS referencias VARCHAR(200);
