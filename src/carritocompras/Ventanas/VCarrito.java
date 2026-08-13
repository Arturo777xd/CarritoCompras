/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package carritocompras.Ventanas;

import java.sql.Connection;
import javax.swing.JFrame;

/**
 *
 * @author Arturo
 */
public class VCarrito extends javax.swing.JFrame {
    private java.sql.Connection conexionActiva;
    private int idUsuario;
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(VCarrito.class.getName());

    /**
     * Creates new form VCarrito
     */
    public VCarrito() {
        initComponents();
        configurarAcciones();
        TemaAplicacion.aplicarVentana(this);
        setTitle("Carrito Compras - Mi carrito");
    }
    public VCarrito(java.sql.Connection conexion, int idUsuario) {
        initComponents();
        configurarAcciones();
        TemaAplicacion.aplicarVentana(this);
        setTitle("Carrito Compras - Mi carrito");
        this.conexionActiva = conexion;
        this.idUsuario = idUsuario;
        // Carga los productos guardados en menu a la tabla
        cargarTablaCarrito();
        this.setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);
    }
    
    public void cargarTablaCarrito() {
    // 1. Definir columnas para la tabla
    String[] columnas = {"Producto", "Precio Unitario", "Cantidad", "Subtotal", "Id producto"};
    javax.swing.table.DefaultTableModel modelo = new javax.swing.table.DefaultTableModel(columnas, 0);

    double totalGeneral = 0.0;

    // 2. Consulta SQL a PostgreSQL
    String sql = "SELECT p.id_productos, p.nombre, p.precio, c.cantidad " +
                 "FROM carrito c " +
                 "JOIN productos p ON c.id_producto = p.id_productos " +
                 "WHERE c.id_usuario = ?";

    try {
        java.sql.PreparedStatement ps = this.conexionActiva.prepareStatement(sql);
        ps.setInt(1, this.idUsuario); // Lee los productos del usuario activo
        java.sql.ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            String nombre = rs.getString("nombre");
            double precio = rs.getDouble("precio");
            int cantidad = rs.getInt("cantidad");
            int idProducto = rs.getInt("id_productos");
            double subtotal = precio * cantidad;

            totalGeneral += subtotal;

            // Solo agregamos los datos del producto
            modelo.addRow(new Object[]{nombre, "$" + precio, cantidad, "$" + subtotal, idProducto});
        }

        tblCarrito.setModel(modelo);
        javax.swing.table.TableColumn columnaId = tblCarrito.getColumnModel().getColumn(4);
        columnaId.setMinWidth(0);
        columnaId.setMaxWidth(0);
        columnaId.setPreferredWidth(0);
        TemaAplicacion.aplicarComponente(tblCarrito);
        lblTotal.setText("Total: $" + String.format("%.2f", totalGeneral));

    } catch (java.sql.SQLException e) {
        System.err.println("Error al cargar carrito: " + e.getMessage());
    }
}

    private void configurarAcciones() {
        btnFinalizarCompra.addActionListener(this::btnFinalizarCompraActionPerformed);
        tblCarrito.setToolTipText("Selecciona un producto y usa clic derecho o la tecla Supr para eliminarlo.");

        javax.swing.JPopupMenu menuCarrito = new javax.swing.JPopupMenu();
        javax.swing.JMenuItem eliminar = new javax.swing.JMenuItem("Eliminar producto seleccionado");
        eliminar.addActionListener(e -> eliminarProductoSeleccionado());
        menuCarrito.add(eliminar);
        tblCarrito.setComponentPopupMenu(menuCarrito);

        tblCarrito.getInputMap(javax.swing.JComponent.WHEN_FOCUSED).put(
                javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_DELETE, 0), "eliminarProducto");
        tblCarrito.getActionMap().put("eliminarProducto", new javax.swing.AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                eliminarProductoSeleccionado();
            }
        });
    }

    private void btnFinalizarCompraActionPerformed(java.awt.event.ActionEvent evt) {
        if (tblCarrito.getRowCount() == 0) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "No hay productos en el carrito para generar una factura.",
                    "Carrito vacío", javax.swing.JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        if (this.conexionActiva == null) {
            javax.swing.JOptionPane.showMessageDialog(this, "No hay conexión activa con la base de datos.",
                    "Compra no disponible", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }

        VentanaEnvio envio = new VentanaEnvio(this, this.conexionActiva, this.idUsuario);
        envio.setVisible(true);
        if (!envio.isDatosGuardados()) {
            return;
        }

        int confirmarPago = javax.swing.JOptionPane.showConfirmDialog(this,
                "¿Confirmas que el pago fue realizado?\nSe registrará la compra.",
                "Confirmar pago", javax.swing.JOptionPane.YES_NO_OPTION,
                javax.swing.JOptionPane.QUESTION_MESSAGE);
        if (confirmarPago != javax.swing.JOptionPane.YES_OPTION) {
            return;
        }
        try {
            registrarCompra();
            java.io.File factura = FacturaPDF.crearFacturaTemporal(
                    tblCarrito, lblTotal.getText(), envio.getDireccionFactura());
            cargarTablaCarrito();
            abrirFactura(factura);
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Compra registrada. La factura se abrió automáticamente.",
                    "Factura generada", javax.swing.JOptionPane.INFORMATION_MESSAGE);
        } catch (java.sql.SQLException e) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "No se pudo registrar la compra: " + e.getMessage(),
                    "Error al finalizar compra", javax.swing.JOptionPane.ERROR_MESSAGE);
        } catch (java.io.IOException e) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "La compra fue registrada, pero no se pudo generar o abrir la factura: " + e.getMessage(),
                    "Factura no disponible", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirFactura(java.io.File factura) throws java.io.IOException {
        if (!java.awt.Desktop.isDesktopSupported()) {
            throw new java.io.IOException("No se puede abrir el visor PDF en este equipo.");
        }
        java.awt.Desktop.getDesktop().open(factura);
    }

    //Registra encabezado, detalle, stock y limpieza del carrito en una sola transacción
    private void registrarCompra() throws java.sql.SQLException {
        boolean autoCommitOriginal = this.conexionActiva.getAutoCommit();
        try {
            this.conexionActiva.setAutoCommit(false);
            String sqlCompra = "INSERT INTO compras (id_comprador, total) VALUES (?, ?)";
            java.sql.PreparedStatement psCompra = this.conexionActiva.prepareStatement(
                    sqlCompra, java.sql.Statement.RETURN_GENERATED_KEYS);
            psCompra.setInt(1, this.idUsuario);
            psCompra.setBigDecimal(2, obtenerTotalNumerico());
            psCompra.executeUpdate();

            java.sql.ResultSet claves = psCompra.getGeneratedKeys();
            if (!claves.next()) {
                throw new java.sql.SQLException("No se pudo obtener el identificador de la compra.");
            }
            int idCompra = claves.getInt(1);

            String sqlStock = "UPDATE productos SET stock = stock - ? WHERE id_productos = ? AND stock >= ?";
            String sqlDetalle = "INSERT INTO detalle_compras (id_compra, id_producto, cantidad, precio_unitario) VALUES (?, ?, ?, ?)";
            java.sql.PreparedStatement psStock = this.conexionActiva.prepareStatement(sqlStock);
            java.sql.PreparedStatement psDetalle = this.conexionActiva.prepareStatement(sqlDetalle);

            for (int fila = 0; fila < tblCarrito.getRowCount(); fila++) {
                int cantidad = ((Number) tblCarrito.getModel().getValueAt(fila, 2)).intValue();
                int idProducto = ((Number) tblCarrito.getModel().getValueAt(fila, 4)).intValue();
                java.math.BigDecimal precio = obtenerPrecio(fila);

                psStock.setInt(1, cantidad);
                psStock.setInt(2, idProducto);
                psStock.setInt(3, cantidad);
                if (psStock.executeUpdate() == 0) {
                    throw new java.sql.SQLException("Stock insuficiente para: " + tblCarrito.getValueAt(fila, 0));
                }

                psDetalle.setInt(1, idCompra);
                psDetalle.setInt(2, idProducto);
                psDetalle.setInt(3, cantidad);
                psDetalle.setBigDecimal(4, precio);
                psDetalle.executeUpdate();
            }

            java.sql.PreparedStatement psCarrito = this.conexionActiva.prepareStatement(
                    "DELETE FROM carrito WHERE id_usuario = ?");
            psCarrito.setInt(1, this.idUsuario);
            psCarrito.executeUpdate();
            this.conexionActiva.commit();
        } catch (java.sql.SQLException e) {
            this.conexionActiva.rollback();
            throw e;
        } finally {
            this.conexionActiva.setAutoCommit(autoCommitOriginal);
        }
    }

    private java.math.BigDecimal obtenerTotalNumerico() {
        return new java.math.BigDecimal(lblTotal.getText().replace("Total: $", "").trim());
    }

    private java.math.BigDecimal obtenerPrecio(int fila) {
        return new java.math.BigDecimal(String.valueOf(tblCarrito.getModel().getValueAt(fila, 1))
                .replace("$", "").trim());
    }

    private void eliminarProductoSeleccionado() {
        int fila = tblCarrito.getSelectedRow();
        if (fila < 0) {
            javax.swing.JOptionPane.showMessageDialog(this, "Selecciona un producto del carrito para eliminarlo.",
                    "Producto no seleccionado", javax.swing.JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        if (this.conexionActiva == null) {
            return;
        }

        String producto = String.valueOf(tblCarrito.getValueAt(fila, 0));
        int confirmar = javax.swing.JOptionPane.showConfirmDialog(this,
                "¿Eliminar '" + producto + "' del carrito?", "Eliminar producto",
                javax.swing.JOptionPane.YES_NO_OPTION, javax.swing.JOptionPane.WARNING_MESSAGE);
        if (confirmar != javax.swing.JOptionPane.YES_OPTION) {
            return;
        }

        int idProducto = ((Number) tblCarrito.getModel().getValueAt(fila, 4)).intValue();
        String sql = "DELETE FROM carrito WHERE id_usuario = ? AND id_producto = ?";
        try {
            java.sql.PreparedStatement ps = this.conexionActiva.prepareStatement(sql);
            ps.setInt(1, this.idUsuario);
            ps.setInt(2, idProducto);
            ps.executeUpdate();
            cargarTablaCarrito();
        } catch (java.sql.SQLException e) {
            javax.swing.JOptionPane.showMessageDialog(this, "No se pudo eliminar el producto: " + e.getMessage(),
                    "Error al eliminar", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        tblCarrito = new javax.swing.JTable();
        lblTotal = new javax.swing.JLabel();
        btnFinalizarCompra = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        tblCarrito.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(tblCarrito);

        lblTotal.setText("Total: $0.00");

        btnFinalizarCompra.setText("Finalizar compra");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 375, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnFinalizarCompra)
                    .addComponent(lblTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(98, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTotal)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnFinalizarCompra)
                .addContainerGap(19, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnFinalizarCompra;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JTable tblCarrito;
    // End of variables declaration//GEN-END:variables
}
