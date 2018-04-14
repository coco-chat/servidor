/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.Controladores;


import chat.Conexion;
import chat.Modelos.Modelo_usuarios;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author vanya
 */
public class Controlador_usuarios {
    Conexion db = new Conexion();
    int id;
    String username,password;
    
    public List<Modelo_usuarios> Select() throws SQLException{
        try{
            Modelo_usuarios usuario;
            List<Modelo_usuarios> usuarios = new ArrayList<Modelo_usuarios>();
           ResultSet x;
            x = db.Select("Select * from usuarios");
           
            while (x.next()) {
                
              usuario = new Modelo_usuarios();
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
    public void Insert(Modelo_usuarios usuario){
        try{
          username = usuario.getUsername();
          password = usuario.getPassword();
          String consulta = "Insert into usuarios values(null,'"+username+"',sha('"+password+"'))";
          db.Comando(consulta);
        
        
        } catch(Exception ex){
            System.out.println("error insert");
            
        }
    }
    public void Update(Modelo_usuarios usuario){
        try{
          id = usuario.getId();
          username = usuario.getUsername();
          password = usuario.getPassword();
          String consulta = "UPDATE usuarios SET username = '"+username+"', password = sha('"+password+"') where id = "+id;
          db.Comando(consulta);
        
        
        } catch(Exception ex){
            System.out.println("error");
            
        }
    }
    public void Delete(Modelo_usuarios usuario){
        try{
          id = usuario.getId();
          String consulta = "Delete from usuarios where id="+id;
          db.Comando(consulta);
        
        
        } catch(Exception ex){
            System.out.println("error");
            
        }
    }
    
}
