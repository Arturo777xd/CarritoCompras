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
public class Usuarios {
    int id_usuarios;
    String nombre_usuario;
    String correo;
    String contra_usuario;
    
    public void BuscarProducto(java.sql.Connection objConnection){
        try{
            Statement objStatement=objConnection.createStatement();
            ResultSet objRS = objStatement.executeQuery("select * from usuarios");
            while(objRS.next()){
                if(objRS.getString("nombre_user").equals("Messi")){
                    String nombre_user=objRS.getString("nombre_user");
                    System.out.println("Usuario: " + nombre_user);
                }
            }
            
            
            
            
            
        }catch(SQLException e){
            
        }
    
}
    
    
    
    
}
