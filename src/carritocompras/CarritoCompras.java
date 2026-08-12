/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package carritocompras;
import carritocompras.Ventanas.Menú;
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
public class CarritoCompras {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Connection objConnection =  BS();
        if (objConnection != null){
            System.out.println("Se conectó");
            Menú VentanaMenu = new Menú(objConnection, null, 0);
            VentanaMenu.setVisible(true);
            VentanaMenu.setLocationRelativeTo(null);
        }else{
              System.err.println("no se pudo iniciar la aplicación.");
        }
        
    }
    
    public static Connection BS(){
        String url="jdbc:postgresql://localhost:5432/ProyectoCarrito";
        String usuario="postgres";
        String password="Arturo777";
        Connection objConnection=null;
        try{
           objConnection=DriverManager.getConnection(url, usuario, password);
           if(objConnection != null){
               System.out.println("si se pudo conectar");
               return objConnection;
           }
        }catch (SQLException e){
            System.err.println("no se pudo");
            System.err.println(e.toString());
        }
        return objConnection;
    }
    
    
}
