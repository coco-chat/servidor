/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vanya
 */
public class Conexion {
    
    protected Connection con;
    
    public Connection getCon() {
        return con;
    }
    
    public Conexion() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            try {
                con = DriverManager.getConnection("jdbc:mysql://localhost:3306/chat",
                    "root", "");
            } catch(SQLException ex) {
                Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch(ClassNotFoundException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public ResultSet ComandoSelect(String consulta){
        try {
            PreparedStatement sql = con.prepareStatement(consulta);
            return sql.executeQuery();
        } catch(SQLException ex) {
            return null;
        }
    }
    
    public int ComandoInsertUpdateDelete(String consulta){
        try {
            PreparedStatement sql = con.prepareStatement(consulta);
            return sql.executeUpdate();
        } catch(SQLException ex){
            return -1;
        }
    }
   
}
