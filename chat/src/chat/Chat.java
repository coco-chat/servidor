/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat;

/**
 *
 * @author vanya
 */
import chat.Controladores.ControladorUsuario;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
public class Chat {
    
    public static void main(String[] args) {
        // TODO code application logic here
       
        
         try {
            ControladorUsuario x = new ControladorUsuario();
            x.Select();
            
            /* Se establece el nombre de la base de datos que contiene 
             la información que se quiere consultar
             */
           
            
             /* Se establece la consulta que se desea hacer.
            Select se encargará de seleccionar todos los datos
            (representado por *) desde (FROM) la base de datos
            llamada "Registro"
             */
          
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
