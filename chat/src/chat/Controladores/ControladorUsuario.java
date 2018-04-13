/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.Controladores;


import chat.Conexion;
import chat.Modelos.ModeloUsuario;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author vanya
 */
public class ControladorUsuario {
    Conexion db = new Conexion();
    String tablaDB = "usuarios";
    int id;
    String username,password;
    
    public List<ModeloUsuario> Select() throws SQLException{
        try{
            ModeloUsuario usuario;
            List<ModeloUsuario> usuarios = new ArrayList<ModeloUsuario>();
           ResultSet x;
            x = db.Select("Select * from usuarios");
           
            while (x.next()) {
                
              usuario = new ModeloUsuario();
              usuario.setId(x.getInt(1));
              usuario.setUsername(x.getString(2));
              usuario.setPassword(x.getString(3));
              usuarios.add(usuario);
            }
            
        return  usuarios;
        }catch(SQLException ex){
            System.out.println("error");
            return null;
        }
         
    }
    public void Insert(ModeloUsuario usuario){
        try{
          id = usuario.getId();
          username = usuario.getUsername();
          password = usuario.getPassword();
          String consulta = "Insert into usuarios values(null,"+username+",sha("+password+"))";
          db.Comando(consulta);
        
        
        } catch(Exception ex){
            System.out.println("error");
            
        }
    }
    public void Update(ModeloUsuario usuario){
        try{
          id = usuario.getId();
          username = usuario.getUsername();
          password = usuario.getPassword();
          String consulta = "Update usuarios values(null,"+username+",sha("+password+")) where id="+id+")";
          db.Comando(consulta);
        
        
        } catch(Exception ex){
            System.out.println("error");
            
        }
    }
    public void Delete(ModeloUsuario usuario){
        try{
          id = usuario.getId();
          String consulta = "Delete from usuario where id="+id+")";
          db.Comando(consulta);
        
        
        } catch(Exception ex){
            System.out.println("error");
            
        }
    }
    
}
