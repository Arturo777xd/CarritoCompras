# Carrito Compras

Aplicación de escritorio para la gestión de productos, compras, ventas y carrito de compras. Fue desarrollada con Java Swing en Apache NetBeans y utiliza PostgreSQL para persistir los datos.

Repositorio: <https://github.com/Arturo777xd/CarritoCompras>

## Funcionalidades principales

- Registro e inicio de sesión de usuarios.
- Consulta de productos por categoría.
- Publicación de productos para venta.
- Carrito de compras con eliminación de productos.
- Registro de compras y descuento de stock.
- Captura de datos de envío.
- Factura PDF con código de barras al confirmar el pago.
- Historial de compras realizadas y productos vendidos.

## Tecnologías

- Java 25.
- Java Swing.
- Apache NetBeans.
- PostgreSQL.
- JDBC PostgreSQL.

## Buenas prácticas aplicadas

- **Responsabilidad única:** las pantallas se concentran en su flujo visual; `VentanaEnvio` administra los datos de envío y `FacturaPDF` únicamente genera comprobantes PDF.
- **Encapsulamiento:** las variables de estado y componentes se declaran privados.
- **Métodos pequeños:** las operaciones de compra se dividen en cargar carrito, eliminar producto, calcular total, registrar compra y abrir factura.
- **Manejo de recursos:** se usan `try-with-resources` en operaciones JDBC y de archivos para cerrar recursos correctamente.
- **SQL parametrizado:** las consultas que reciben datos usan `PreparedStatement`, reduciendo errores de formato y riesgos de inyección SQL.
- **Transacciones:** el registro de compra, descuento de stock, detalle de compra y limpieza del carrito se realizan como una sola transacción.

## Base de datos

1. Crear una base local llamada `ProyectoCarrito`.
2. Ejecutar `database/esquema_completo.sql` para crear las tablas.
3. Ejecutar `database/agregar_datos_envio.sql` si se parte de una instalación anterior.
4. Configurar las credenciales locales de PostgreSQL en `src/carritocompras/CarritoCompras.java`.

## Ejecución

1. Abrir la carpeta del proyecto con Apache NetBeans.
2. Agregar el controlador JDBC de PostgreSQL a **Libraries**.
3. Ejecutar el proyecto con **Run Project** o `F6`.

## Prueba unitaria incluida

El archivo `test/carritocompras/Ventanas/FacturaPDFTest.java` prueba la generación de la factura de manera aislada, sin requerir PostgreSQL. Verifica que:

- Se cree el archivo.
- El archivo tenga encabezado PDF.
- Aparezcan título, producto, total y dirección de envío.

El archivo `test/carritocompras/ValidacionLoginTest.java` comprueba los mensajes de validación cuando faltan el usuario, la contraseña o ambos campos del inicio de sesión.

Para ejecutarla desde consola, después de compilar las clases del proyecto:

```text
java -cp build/classes;build/test/classes carritocompras.Ventanas.FacturaPDFTest
```

En Windows el separador del classpath es `;`.

## Versiones

El control de versiones se mantiene con Git y GitHub. Algunos hitos del historial son:

1. Avance de aplicación.
2. Cambios en la consulta de productos por categoría.
3. Función para agregar y mostrar productos en el carrito.
4. Versión completa y terminada.

El historial completo se puede consultar con `git log --oneline` o directamente en el repositorio de GitHub.
