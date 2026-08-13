package carritocompras.Ventanas;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JTable;

/** Genera una factura PDF sencilla usando únicamente las clases estándar de Java. */
public final class FacturaPDF {

    private FacturaPDF() {
    }

    /** Genera la factura en archivos temporales para abrirla de inmediato. */
    public static File crearFacturaTemporal(JTable tabla, String total) throws IOException {
        return crearFacturaTemporal(tabla, total, "");
    }

    public static File crearFacturaTemporal(JTable tabla, String total, String direccionEnvio) throws IOException {
        String fechaArchivo = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File carpetaFacturas = new File(System.getProperty("java.io.tmpdir"), "CarritoCompras");
        if (!carpetaFacturas.exists() && !carpetaFacturas.mkdirs()) {
            throw new IOException("No se pudo crear la carpeta temporal de facturas.");
        }
        File archivo = new File(carpetaFacturas, "factura_" + fechaArchivo + ".pdf");
        generarFactura(archivo, tabla, total, direccionEnvio);
        return archivo;
    }

    /** Crea el comprobante en la ubicación indicada; se usa al finalizar la compra. */
    public static void generarFactura(File archivo, JTable tabla, String total) throws IOException {
        generarFactura(archivo, tabla, total, "");
    }

    public static void generarFactura(File archivo, JTable tabla, String total, String direccionEnvio) throws IOException {
        List<String> productos = new ArrayList<>();
        for (int fila = 0; fila < tabla.getRowCount(); fila++) {
            productos.add(formatoProducto(tabla, fila));
        }

        List<List<String>> paginas = dividirPaginas(productos, 24);
        Date ahora = new Date();
        String fecha = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(ahora);
        String referencia = "PAGO-" + new SimpleDateFormat("yyyyMMddHHmmss").format(ahora);
        List<String> objetos = new ArrayList<>();
        objetos.add("<< /Type /Catalog /Pages 2 0 R >>");

        StringBuilder referenciasPaginas = new StringBuilder();
        for (int i = 0; i < paginas.size(); i++) {
            referenciasPaginas.append(4 + (i * 2)).append(" 0 R ");
        }
        objetos.add("<< /Type /Pages /Kids [" + referenciasPaginas + "] /Count " + paginas.size() + " >>");
        objetos.add("<< /Type /Font /Subtype /Type1 /BaseFont /Helvetica /Encoding /WinAnsiEncoding >>");

        for (int pagina = 0; pagina < paginas.size(); pagina++) {
            int contenido = 5 + (pagina * 2);
            objetos.add("<< /Type /Page /Parent 2 0 R /MediaBox [0 0 595 842] "
                    + "/Resources << /Font << /F1 3 0 R >> >> /Contents " + contenido + " 0 R >>");
            String textoPagina = contenidoPagina(paginas.get(pagina), pagina + 1, paginas.size(),
                    total, fecha, referencia, direccionEnvio);
            byte[] datos = textoPagina.getBytes(StandardCharsets.ISO_8859_1);
            objetos.add("<< /Length " + datos.length + " >>\nstream\n" + textoPagina + "\nendstream");
        }

        escribirPdf(archivo, objetos);
    }

    private static List<List<String>> dividirPaginas(List<String> productos, int maximo) {
        List<List<String>> paginas = new ArrayList<>();
        for (int inicio = 0; inicio < productos.size(); inicio += maximo) {
            paginas.add(new ArrayList<>(productos.subList(inicio, Math.min(inicio + maximo, productos.size()))));
        }
        if (paginas.isEmpty()) {
            paginas.add(new ArrayList<String>());
        }
        return paginas;
    }

    private static String formatoProducto(JTable tabla, int fila) {
        return recortar(String.valueOf(tabla.getValueAt(fila, 0)), 30)
                + " | " + recortar(String.valueOf(tabla.getValueAt(fila, 1)), 13)
                + " | " + recortar(String.valueOf(tabla.getValueAt(fila, 2)), 8)
                + " | " + recortar(String.valueOf(tabla.getValueAt(fila, 3)), 13);
    }

    private static String contenidoPagina(List<String> productos, int actual, int totalPaginas, String total,
            String fecha, String referencia, String direccionEnvio) {
        StringBuilder contenido = new StringBuilder();
        int y = 790;
        agregarTexto(contenido, 50, y, 18, "FACTURA DE COMPRA");
        y -= 24;
        agregarTexto(contenido, 50, y, 10, "Carrito Compras");
        y -= 16;
        agregarTexto(contenido, 50, y, 10, "Fecha: " + fecha);
        y -= 16;
        agregarTexto(contenido, 50, y, 10, "Codigo de referencia: " + referencia);
        y -= 16;
        if (!direccionEnvio.isEmpty()) {
            agregarTexto(contenido, 50, y, 9, "Envio: " + recortar(direccionEnvio, 90));
            y -= 16;
        }
        agregarCodigoBarras(contenido, 50, y - 52, referencia);
        y -= 74;
        contenido.append("50 ").append(y).append(" m 545 ").append(y).append(" l S\n");
        y -= 16;
        agregarTexto(contenido, 52, y, 9, "Producto                         | Precio unitario | Cantidad | Subtotal");
        y -= 12;
        contenido.append("50 ").append(y).append(" m 545 ").append(y).append(" l S\n");
        y -= 16;
        for (String producto : productos) {
            agregarTexto(contenido, 52, y, 9, producto);
            y -= 20;
        }
        if (actual == totalPaginas) {
            contenido.append("50 ").append(y).append(" m 545 ").append(y).append(" l S\n");
            y -= 22;
            agregarTexto(contenido, 405, y, 12, total);
        }
        agregarTexto(contenido, 50, 42, 8, "Pagina " + actual + " de " + totalPaginas);
        return contenido.toString();
    }

    /** Dibuja un código de barras asociado al folio de la compra. */
    private static void agregarCodigoBarras(StringBuilder contenido, int x, int y, String referencia) {
        int posicion = x;
        for (char caracter : referencia.toCharArray()) {
            int patron = caracter * 31;
            for (int bit = 0; bit < 7; bit++) {
                int ancho = ((patron >> bit) & 1) == 1 ? 2 : 1;
                if (((patron >> bit) & 1) == 1) {
                    contenido.append(posicion).append(" ").append(y).append(" ")
                            .append(ancho).append(" 48 re f\n");
                }
                posicion += ancho + 1;
            }
            posicion += 1;
        }
    }

    private static void agregarTexto(StringBuilder contenido, int x, int y, int tamano, String texto) {
        contenido.append("BT /F1 ").append(tamano).append(" Tf ")
                .append(x).append(" ").append(y).append(" Td (")
                .append(escapar(texto)).append(") Tj ET\n");
    }

    private static String escapar(String texto) {
        StringBuilder resultado = new StringBuilder();
        for (char caracter : texto.toCharArray()) {
            if (caracter == '(' || caracter == ')' || caracter == '\\') {
                resultado.append('\\');
            }
            resultado.append(caracter <= 255 ? caracter : '?');
        }
        return resultado.toString();
    }

    private static String recortar(String valor, int limite) {
        return valor.length() > limite ? valor.substring(0, limite - 3) + "..." : valor;
    }

    private static void escribirPdf(File archivo, List<String> objetos) throws IOException {
        ByteArrayOutputStream salida = new ByteArrayOutputStream();
        List<Integer> posiciones = new ArrayList<>();
        escribir(salida, "%PDF-1.4\n%âãÏÓ\n");
        for (int i = 0; i < objetos.size(); i++) {
            posiciones.add(salida.size());
            escribir(salida, (i + 1) + " 0 obj\n" + objetos.get(i) + "\nendobj\n");
        }
        int inicioXref = salida.size();
        escribir(salida, "xref\n0 " + (objetos.size() + 1) + "\n0000000000 65535 f \n");
        for (Integer posicion : posiciones) {
            escribir(salida, String.format("%010d 00000 n \n", posicion));
        }
        escribir(salida, "trailer\n<< /Size " + (objetos.size() + 1) + " /Root 1 0 R >>\nstartxref\n"
                + inicioXref + "\n%%EOF");
        try (FileOutputStream destino = new FileOutputStream(archivo)) {
            salida.writeTo(destino);
        }
    }

    private static void escribir(ByteArrayOutputStream salida, String texto) throws IOException {
        salida.write(texto.getBytes(StandardCharsets.ISO_8859_1));
    }
}
