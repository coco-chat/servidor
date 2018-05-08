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
    
    /**
     * Devuelve la variable de la conexión
     * @return variable de conexión
     */
    public Connection getCon() {
        return con;
    }
    
    /**
     * Crea la conexión con la base de datos
     */
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
    
    /**
     * Ejecuta una consulta de select
     * @param consulta la cadena de consulta
     * @return el resultado de la consulta
     */
    public ResultSet ComandoSelect(String consulta){
        try {
            PreparedStatement sql = con.prepareStatement(consulta);
            return sql.executeQuery();
        } catch(SQLException ex) {
            return null;
        }
    }
    
    /**
     * Ejecuta un comando de inset, update o delete
     * @param consulta la cadena de consulta
     * @return el numero de filas afectadas. En caso de error retorna -1
     */
    public int ComandoInsertUpdateDelete(String consulta){
        try {
            PreparedStatement sql = con.prepareStatement(consulta);
            return sql.executeUpdate();
        } catch(SQLException ex){
            return -1;
        }
    }
   
}
