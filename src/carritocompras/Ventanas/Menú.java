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


public class Menú extends javax.swing.JFrame {
    private java.sql.Connection conexionActiva;
    private boolean usuario = false;
    private boolean MenuDespegable = false;
    private int idUsuario;

    //lista global de menu para guardar productos agregados
public static java.util.List<String[]> listaCarrito = new java.util.ArrayList<>();    
    
    public Menú(java.sql.Connection conexion, String nUsuario, int idUsuario) {
        initComponents();
        this.conexionActiva = conexion;
        this.idUsuario = idUsuario;
        this.setLocationRelativeTo(null);
        
        PanelBarra.setVisible(false);
        if (nUsuario != null && !nUsuario.isEmpty()){
            this.usuario = true;
            BInicioSesion.setVisible(false);
            LBienvenida.setText("Bienvenido "+nUsuario  );
            LBienvenida.setVisible(true);
            
            
        }else{
            this.usuario = false;
            BInicioSesion.setVisible(true);
            LBienvenida.setVisible(false);
        }
        BCerrarSesion.setVisible(this.usuario);
        
        cargarProductos("Todas");
        TemaAplicacion.aplicarVentana(this);
        setTitle("Carrito Compras");
        this.setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);
    
    }
    public void cargarProductos(String categorias){
        //Limpia tarjetas anteriores
        pnlProductos.removeAll();
        
        String sql;
        if(categorias.equals("Todas")){
            sql = "SELECT * FROM productos";
        }else{
            sql = "SELECT * FROM productos WHERE carcateristica ILIKE ?"; //ILIKE se usa por si hay problemas con los nombres
        }
        try{
            java.sql.PreparedStatement ps = this.conexionActiva.prepareStatement(sql);
            if(!categorias.equals("Todas")){
                ps.setString(1, categorias);
            }
            java.sql.ResultSet rs = ps.executeQuery();
            
            //revisamos productos en la BS
            while(rs.next()){
            String nombre = rs.getString("nombre");
            double precio = rs.getDouble("precio");
            String marca = rs.getString("marca");
            int stock = rs.getInt("stock");
            int idProd = rs.getInt("id_productos");
            
            //se crean tarjetas visuales para cada producto
            javax.swing.JPanel tarjeta = new javax.swing.JPanel();
            tarjeta.setBackground(TemaAplicacion.BLANCO);
            tarjeta.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                    javax.swing.BorderFactory.createLineBorder(TemaAplicacion.BORDE),
                    javax.swing.BorderFactory.createEmptyBorder(6, 6, 6, 6)));
            tarjeta.setLayout(new java.awt.GridLayout(5, 1)); // 4 líneas verticalmente
            tarjeta.setPreferredSize(new java.awt.Dimension(180, 165));
            
            //componentes de la tarjeta
            javax.swing.JLabel lblNombre = new javax.swing.JLabel(nombre, javax.swing.SwingConstants.CENTER);
            lblNombre.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
            
            javax.swing.JLabel lblMarca = new javax.swing.JLabel("Marca: " + marca, javax.swing.SwingConstants.CENTER);
            javax.swing.JLabel lblPrecio = new javax.swing.JLabel("$" + precio, javax.swing.SwingConstants.CENTER);
            javax.swing.JLabel lblStock = new javax.swing.JLabel("Disponibles: " + stock, javax.swing.SwingConstants.CENTER);
            
            javax.swing.JButton btnAgregar = new javax.swing.JButton("Agregar al Carrito");
            
        btnAgregar.addActionListener(e -> {
        // Validar si hay usuario logueado
        if (!this.usuario) {
            javax.swing.JOptionPane.showMessageDialog(this, "Debes iniciar sesión para agregar productos al carrito.");
            return;
        }
        String sqlInsert = "INSERT INTO carrito (id_usuario, id_producto, cantidad) VALUES (?, ?, 1) " +
                       "ON CONFLICT (id_usuario, id_producto) " +
                       "DO UPDATE SET cantidad = carrito.cantidad + 1";
        try{
        java.sql.PreparedStatement psCarrito = this.conexionActiva.prepareStatement(sqlInsert);
        psCarrito.setInt(1, this.idUsuario);
        psCarrito.setInt(2, idProd);
        
        psCarrito.executeUpdate();
        javax.swing.JOptionPane.showMessageDialog(this, "¡" + nombre + " guardado en tu carrito!");
        
        }catch (java.sql.SQLException ex){
            System.err.println("Error al guardar en carrito: " + ex.getMessage());
        javax.swing.JOptionPane.showMessageDialog(this, "Error al guardar en el carrito.");
            
        }

        
    });
            
            //elementos de las tarjetas
            tarjeta.add(lblNombre);
            tarjeta.add(lblMarca);
            tarjeta.add(lblPrecio);
            tarjeta.add(lblStock);
            tarjeta.add(btnAgregar);
            TemaAplicacion.aplicarComponente(tarjeta);
            // Metemos la tarjeta al contenedor principal
            pnlProductos.add(tarjeta);
            
            }
            //Refrescamos la interfaz para pintar los cambios
            pnlProductos.revalidate();
            pnlProductos.repaint();
        }catch (java.sql.SQLException e) {
        System.err.println("Error al cargar productos: " + e.getMessage());
    }
        
    }

    /**
     * Creates new form Menú
     */
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        BInicioSesion = new javax.swing.JButton();
        LBienvenida = new javax.swing.JLabel();
        PanelBarra = new javax.swing.JPanel();
        BVenta = new javax.swing.JButton();
        cbxCategorias = new javax.swing.JComboBox<>();
        BCarrito = new javax.swing.JButton();
        BHistorial = new javax.swing.JButton();
        BCerrarSesion = new javax.swing.JButton();
        BHamburguesa = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        pnlProductos = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        BInicioSesion.setText("Iniciar Sesión");
        BInicioSesion.addActionListener(this::BInicioSesionActionPerformed);

        LBienvenida.setText("jLabel2");

        PanelBarra.setBackground(new java.awt.Color(153, 153, 153));

        BVenta.setBackground(new java.awt.Color(153, 153, 153));
        BVenta.setText("Vender");
        BVenta.addActionListener(this::BVentaActionPerformed);

        cbxCategorias.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Almacenamiento", "Bocinas", "Fuentes de poder", "Gabinetes", "Gráficas", "Microfonos", "Monitores", "Mouse", "RAM", "Procesadores", "Sillas", "Tarjetas madres", "Teclados", "Ventiladores (Gabinetes)" }));
        cbxCategorias.addActionListener(this::cbxCategoriasActionPerformed);

        BCarrito.setText("Carrito 🛒");
        BCarrito.addActionListener(this::BCarritoActionPerformed);

        BHistorial.setText("Historial");
        BHistorial.addActionListener(this::BHistorialActionPerformed);

        BCerrarSesion.setText("Cerrar sesión");
        BCerrarSesion.addActionListener(this::BCerrarSesionActionPerformed);

        javax.swing.GroupLayout PanelBarraLayout = new javax.swing.GroupLayout(PanelBarra);
        PanelBarra.setLayout(PanelBarraLayout);
        PanelBarraLayout.setHorizontalGroup(
            PanelBarraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PanelBarraLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(PanelBarraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbxCategorias, 0, 0, Short.MAX_VALUE)
                    .addGroup(PanelBarraLayout.createSequentialGroup()
                        .addGroup(PanelBarraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(BCarrito, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(BHistorial, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(BVenta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(BCerrarSesion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 80, Short.MAX_VALUE)))
                .addContainerGap())
        );
        PanelBarraLayout.setVerticalGroup(
            PanelBarraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, PanelBarraLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(cbxCategorias, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 228, Short.MAX_VALUE)
                .addComponent(BCarrito)
                .addGap(18, 18, 18)
                .addComponent(BHistorial)
                .addGap(18, 18, 18)
                .addComponent(BVenta)
                .addGap(18, 18, 18)
                .addComponent(BCerrarSesion)
                .addGap(73, 73, 73))
        );

        BHamburguesa.setText("☰");
        BHamburguesa.addActionListener(this::BHamburguesaActionPerformed);

        jScrollPane1.setViewportView(pnlProductos);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(BHamburguesa, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(BInicioSesion, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addComponent(LBienvenida, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(PanelBarra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 579, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(BInicioSesion)
                    .addComponent(LBienvenida, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(17, 17, 17)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(BHamburguesa)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(PanelBarra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BInicioSesionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BInicioSesionActionPerformed
        // TODO add your handling code here:
        //Instancia de ventana login con conexion
        Login VLogin = new Login(this.conexionActiva, this);
        //Abrimos ventana
        VLogin.setVisible(true);
        //codigo para centrar ventana en medio
        VLogin.setLocationRelativeTo(null);
        
        
        
    }//GEN-LAST:event_BInicioSesionActionPerformed

    private void BHamburguesaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BHamburguesaActionPerformed
        // TODO add your handling code here:
        if (MenuDespegable){
            PanelBarra.setVisible(false);
            MenuDespegable = false;
        }else{
            PanelBarra.setVisible(true);
            MenuDespegable = true;
        }
        // Al mostrar la barra, la ventana se ajusta para desplazar el contenido
        // y conservar visible el catálogo completo.
        pack();
        setLocationRelativeTo(null);
    }//GEN-LAST:event_BHamburguesaActionPerformed

    private void BVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BVentaActionPerformed
        // TODO add your handling code here:
        if (this.usuario){
            VPublicar ventanap = new VPublicar(this.conexionActiva, this.idUsuario);
                ventanap.setVisible(true);
                ventanap.setLocationRelativeTo(null); 
        }else{
            javax.swing.JOptionPane.showMessageDialog(this, "Debes iniciar sesión para publicar y vender productos.");
            
            Login ventLogin = new Login (this.conexionActiva);
            ventLogin.setVisible(true);
            ventLogin.setLocationRelativeTo(null);
            
        }
        
        cargarProductos("Todas");
        
    }//GEN-LAST:event_BVentaActionPerformed

    private void cbxCategoriasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxCategoriasActionPerformed
        // TODO add your handling code here:
        //Obtenemos el nombre de la categoria que seleccione el usuario
        String categoriaSeleccionada = cbxCategorias.getSelectedItem().toString();
        
        //llamamos a cargarProductos para filtrar la categoria
        cargarProductos(categoriaSeleccionada);
    }//GEN-LAST:event_cbxCategoriasActionPerformed

    private void BCarritoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BCarritoActionPerformed
        // TODO add your handling code here:
        if (!this.usuario) {
        javax.swing.JOptionPane.showMessageDialog(this, 
            "Debes iniciar sesión para ver tu carrito.", 
            "Sesión requerida", 
            javax.swing.JOptionPane.WARNING_MESSAGE);
        return;
    }
        VCarrito ventCarrito = new VCarrito(this.conexionActiva, this.idUsuario);
    ventCarrito.setVisible(true);
    ventCarrito.setLocationRelativeTo(null);
    }//GEN-LAST:event_BCarritoActionPerformed

    private void BHistorialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BHistorialActionPerformed
        if (!this.usuario) {
            javax.swing.JOptionPane.showMessageDialog(this,
                    "Debes iniciar sesión para consultar tu historial.",
                    "Sesión requerida", javax.swing.JOptionPane.WARNING_MESSAGE);
            return;
        }
        VCompras historial = new VCompras(this.conexionActiva, this.idUsuario);
        historial.setVisible(true);
        historial.setLocationRelativeTo(null);
    }//GEN-LAST:event_BHistorialActionPerformed

    private void BCerrarSesionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BCerrarSesionActionPerformed
        int confirmar = javax.swing.JOptionPane.showConfirmDialog(this,
                "¿Deseas cerrar la sesión actual?", "Cerrar sesión",
                javax.swing.JOptionPane.YES_NO_OPTION, javax.swing.JOptionPane.QUESTION_MESSAGE);
        if (confirmar == javax.swing.JOptionPane.YES_OPTION) {
            Menú menuInvitado = new Menú(this.conexionActiva, null, 0);
            menuInvitado.setVisible(true);
            menuInvitado.setLocationRelativeTo(null);
            this.dispose();
        }
    }//GEN-LAST:event_BCerrarSesionActionPerformed

    /**
     * @param args the command line arguments
     */
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BCarrito;
    private javax.swing.JButton BCerrarSesion;
    private javax.swing.JButton BHistorial;
    private javax.swing.JButton BHamburguesa;
    private javax.swing.JButton BInicioSesion;
    private javax.swing.JButton BVenta;
    private javax.swing.JLabel LBienvenida;
    private javax.swing.JPanel PanelBarra;
    private javax.swing.JComboBox<String> cbxCategorias;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel pnlProductos;
    // End of variables declaration//GEN-END:variables
}
