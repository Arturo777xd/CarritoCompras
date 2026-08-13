package carritocompras.Ventanas;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * Prueba unitaria ejecutable sin conexión a la base de datos.
 * Verifica que la factura incluya sus datos principales y tenga formato PDF.
 */
public class FacturaPDFTest {

    public static void main(String[] args) throws Exception {
        DefaultTableModel modelo = new DefaultTableModel(
                new Object[]{"Producto", "Precio unitario", "Cantidad", "Subtotal"}, 0);
        modelo.addRow(new Object[]{"Mouse Gamer", "$500.00", "1", "$500.00"});
        JTable tabla = new JTable(modelo);

        File archivo = File.createTempFile("factura-prueba-", ".pdf");
        try {
            FacturaPDF.generarFactura(archivo, tabla, "Total: $500.00", "Puebla, Puebla");
            byte[] contenido = Files.readAllBytes(archivo.toPath());
            String texto = new String(contenido, StandardCharsets.ISO_8859_1);

            verificar(archivo.length() > 0, "La factura debe generar un archivo.");
            verificar(texto.startsWith("%PDF-1.4"), "El archivo debe tener encabezado PDF.");
            verificar(texto.contains("FACTURA DE COMPRA"), "La factura debe contener su título.");
            verificar(texto.contains("Mouse Gamer"), "La factura debe contener el producto.");
            verificar(texto.contains("Total: $500.00"), "La factura debe contener el total.");
            verificar(texto.contains("Puebla, Puebla"), "La factura debe contener el envío.");

            System.out.println("FacturaPDFTest: OK");
        } finally {
            Files.deleteIfExists(archivo.toPath());
        }
    }

    private static void verificar(boolean condicion, String mensaje) {
        if (!condicion) {
            throw new AssertionError(mensaje);
        }
    }
}
