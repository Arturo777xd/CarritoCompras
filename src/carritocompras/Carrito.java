/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package carritocompras;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Arturo
 */
public class Carrito {
    private int id_producto;
    private String nombre;
    private double precioUnitario;
    private int cantidad;
    
    public void BuscarProducto(java.sql.Connection objConnection){
        try{
            Statement objStatement=objConnection.createStatement();
            ResultSet objRS = objStatement.executeQuery("select * from carrito");
            while(objRS.next()){
                String idCarrito = objRS.getString("id_carrito");
                System.out.println("Carrito: " + idCarrito);
            }
            
            
            
            
            
        }catch(SQLException e){
            
        }
    
}
    
}
