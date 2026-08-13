/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package carritocompras.Ventanas;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.sql.Connection;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Arturo
 */
public class VCompras extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(VCompras.class.getName());
    private Connection conexionActiva;
    private int idUsuario;
    private JTable tablaCompras;
    private JTable tablaVentas;

    /**
     * Creates new form VCompras
     */
    public VCompras() {
        initComponents();
        configurarHistorial();
        TemaAplicacion.aplicarVentana(this);
        setTitle("Carrito Compras - Historial");
    }

    public VCompras(Connection conexion, int idUsuario) {
        initComponents();
        this.conexionActiva = conexion;
        this.idUsuario = idUsuario;
        configurarHistorial();
        cargarHistorial();
        TemaAplicacion.aplicarVentana(this);
        setTitle("Carrito Compras - Historial");
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
    }

    /** Construye una vista sencilla para las compras y ventas del usuario. */
    private void configurarHistorial() {
        tablaCompras = new JTable();
        tablaVentas = new JTable();

        JPanel contenido = new JPanel(new BorderLayout(0, 12));
        contenido.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        contenido.setPreferredSize(new Dimension(720, 410));

        JLabel titulo = new JLabel("Historial de actividad");
        titulo.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 20));
        javax.swing.JButton btnActualizar = new javax.swing.JButton("Actualizar");
        btnActualizar.setEnabled(conexionActiva != null);
        btnActualizar.addActionListener(e -> cargarHistorial());
        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.add(titulo, BorderLayout.WEST);
        encabezado.add(btnActualizar, BorderLayout.EAST);
        contenido.add(encabezado, BorderLayout.NORTH);

        JTabbedPane pestañas = new JTabbedPane();
        pestañas.addTab("Compras realizadas", new JScrollPane(tablaCompras));
        pestañas.addTab("Productos vendidos", new JScrollPane(tablaVentas));
        contenido.add(pestañas, BorderLayout.CENTER);

        setContentPane(contenido);
    }

    private void cargarHistorial() {
        cargarCompras();
        cargarVentas();
    }

    private void cargarCompras() {
        DefaultTableModel modelo = new DefaultTableModel(
                new String[]{"Compra", "Producto", "Cantidad", "Precio unitario", "Fecha", "Total"}, 0) {
            @Override public boolean isCellEditable(int fila, int columna) { return false; }
        };
        String sql = "SELECT c.id_compra, p.nombre, d.cantidad, d.precio_unitario, c.fecha_compra, c.total "
                + "FROM compras c JOIN detalle_compras d ON c.id_compra = d.id_compra "
                + "JOIN productos p ON d.id_producto = p.id_productos "
                + "WHERE c.id_comprador = ? ORDER BY c.fecha_compra DESC, c.id_compra DESC";
        try {
            java.sql.PreparedStatement ps = conexionActiva.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            java.sql.ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                modelo.addRow(new Object[]{rs.getInt("id_compra"), rs.getString("nombre"),
                    rs.getInt("cantidad"), "$" + rs.getBigDecimal("precio_unitario"),
                    rs.getTimestamp("fecha_compra"), "$" + rs.getBigDecimal("total")});
            }
        } catch (java.sql.SQLException e) {
            mostrarError("No se pudieron cargar las compras", e);
        }
        tablaCompras.setModel(modelo);
        TemaAplicacion.aplicarComponente(tablaCompras);
    }

    private void cargarVentas() {
        DefaultTableModel modelo = new DefaultTableModel(
                new String[]{"Compra", "Producto", "Cantidad", "Precio unitario", "Fecha", "Comprador"}, 0) {
            @Override public boolean isCellEditable(int fila, int columna) { return false; }
        };
        String sql = "SELECT c.id_compra, p.nombre, d.cantidad, d.precio_unitario, c.fecha_compra, u.nombre_usuario "
                + "FROM detalle_compras d JOIN compras c ON d.id_compra = c.id_compra "
                + "JOIN productos p ON d.id_producto = p.id_productos "
                + "JOIN usuarios u ON c.id_comprador = u.id_usuario "
                + "WHERE p.id_usuario_vendedor = ? ORDER BY c.fecha_compra DESC, c.id_compra DESC";
        try {
            java.sql.PreparedStatement ps = conexionActiva.prepareStatement(sql);
            ps.setInt(1, idUsuario);
            java.sql.ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                modelo.addRow(new Object[]{rs.getInt("id_compra"), rs.getString("nombre"),
                    rs.getInt("cantidad"), "$" + rs.getBigDecimal("precio_unitario"),
                    rs.getTimestamp("fecha_compra"), rs.getString("nombre_usuario")});
            }
        } catch (java.sql.SQLException e) {
            mostrarError("No se pudieron cargar las ventas", e);
        }
        tablaVentas.setModel(modelo);
        TemaAplicacion.aplicarComponente(tablaVentas);
    }

    private void mostrarError(String mensaje, java.sql.SQLException e) {
        javax.swing.JOptionPane.showMessageDialog(this, mensaje + ": " + e.getMessage(),
                "Historial", javax.swing.JOptionPane.ERROR_MESSAGE);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Mis compras");

        jLabel2.setText("Productos");

        jLabel3.setText("Fecha");

        jLabel4.setText("Total");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 143, Short.MAX_VALUE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(76, 76, 76))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addContainerGap(218, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new VCompras().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    // End of variables declaration//GEN-END:variables
}
