-- Base de datos para Carrito Compras.
-- Ejecutar en PostgreSQL después de crear una base llamada ProyectoCarrito.

CREATE TABLE IF NOT EXISTS usuarios (
    id_usuario SERIAL PRIMARY KEY,
    nombre_usuario VARCHAR(100),
    correo VARCHAR(100),
    contra_usuario VARCHAR(100),
    nombre_completo VARCHAR(150),
    telefono VARCHAR(20),
    direccion VARCHAR(200),
    colonia VARCHAR(100),
    ciudad VARCHAR(100),
    estado VARCHAR(100),
    codigo_postal VARCHAR(10),
    referencias VARCHAR(200)
);

CREATE TABLE IF NOT EXISTS marcas (
    id_marca SERIAL PRIMARY KEY,
    nombre_marca VARCHAR(100),
    descripcion VARCHAR(200)
);

CREATE TABLE IF NOT EXISTS productos (
    id_productos SERIAL PRIMARY KEY,
    nombre VARCHAR(100),
    precio NUMERIC(10,2),
    marca VARCHAR(100),
    carcateristica VARCHAR(200),
    stock INT,
    id_usuario_vendedor INT REFERENCES usuarios(id_usuario),
    id_marca INT REFERENCES marcas(id_marca)
);

CREATE TABLE IF NOT EXISTS compras (
    id_compra SERIAL PRIMARY KEY,
    id_comprador INT REFERENCES usuarios(id_usuario),
    fecha_compra TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total NUMERIC(10,2)
);

CREATE TABLE IF NOT EXISTS detalle_compras (
    id_detalle SERIAL PRIMARY KEY,
    id_compra INT REFERENCES compras(id_compra),
    id_producto INT REFERENCES productos(id_productos),
    cantidad INT,
    precio_unitario NUMERIC(10,2)
);

CREATE TABLE IF NOT EXISTS carrito (
    id_carrito SERIAL PRIMARY KEY,
    id_usuario INT REFERENCES usuarios(id_usuario),
    id_producto INT REFERENCES productos(id_productos),
    cantidad INT DEFAULT 1,
    CONSTRAINT unique_usuario_producto UNIQUE(id_usuario, id_producto)
);
