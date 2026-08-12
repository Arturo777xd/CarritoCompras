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
public class Compras {
   int id_compras;
   int id_comprador;
   String fecha_compra;
   float total;
   
   public void BuscarProducto(java.sql.Connection objConnection){
        try{
            Statement objStatement=objConnection.createStatement();
            ResultSet objRS = objStatement.executeQuery("select * from compras");
            while(objRS.next()){
                String total = objRS.getString("total");
                System.out.println("Compra Total: " + total);
                
            }
            
            
            
            
            
        }catch(SQLException e){
            
        }
    
}
   
   
}
