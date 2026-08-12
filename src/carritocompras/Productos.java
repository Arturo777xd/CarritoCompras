/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package carritocompras;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;

/**
 *
 * @author Arturo
 */
public class Productos {
    private int id_productos;
    private String nombre;
    private double precio;
    private String marca;
    private String carcateristica; 
    private int stock;
    private Integer id_usuario_vendedor; 
    private Integer id_marca;
    
    public void BuscarProducto(java.sql.Connection objConnection){
        try{
            Statement objStatement=objConnection.createStatement();
            ResultSet objRS = objStatement.executeQuery("select * from productos");
            while(objRS.next()){
                if(objRS.getString("nombre").equals("Procesador")){
                    String nombre=objRS.getString("nombre");
                    System.out.println("Producto: " + nombre);
                }
            }
            
            
            
            
            
        }catch(SQLException e){
            
        }
    
}
    
}
