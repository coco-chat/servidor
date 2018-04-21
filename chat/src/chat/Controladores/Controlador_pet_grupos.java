/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.Controladores;


import chat.Conexion;
import chat.Modelos.Modelo_pet_grupos;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author vanya
 */
public class Controlador_pet_grupos {
    
    Conexion db = new Conexion();
    
    public Controlador_pet_grupos() {
        
    }
    
    public List<Modelo_pet_grupos> Select(){
        try{
            Modelo_pet_grupos pet_grupo;
            List<Modelo_pet_grupos> pet_grupos = new ArrayList<>();
           ResultSet x;
            x = db.Select("Select * from pet_grupos");
           
            while (x.next()) {
                
              pet_grupo = new Modelo_pet_grupos();
              pet_grupo.setId(x.getInt(1));
              pet_grupo.setGrupo(x.getInt(2));
              pet_grupo.setUsuario(x.getInt(3));
                
              pet_grupos.add(pet_grupo);
            }
            
        return  pet_grupos;
        }catch(SQLException ex){
            System.out.println("error");
            return null;
        }
         
    }
    public void Insert(Modelo_pet_grupos pet_grupo){
        try{
          int grupo = pet_grupo.getGrupo();
          int usuario = pet_grupo.getUsuario();
          String consulta = "Insert into pet_grupos values(null,"+grupo+","+usuario+")";
          db.Comando(consulta);
        
        
        } catch(Exception ex){
            System.out.println("error insert");
            
        }
    }
    public void Update(Modelo_pet_grupos pet_grupo){
        try{
          int id = pet_grupo.getId();
          int grupo = pet_grupo.getGrupo();
          int usuario = pet_grupo.getUsuario();
          String consulta = "UPDATE pet_grupos SET grupo = "+grupo+", usuario = "+usuario+" where id = "+id;
          db.Comando(consulta);
        
        
        } catch(Exception ex){
            System.out.println("error");
            
        }
    }
    public void Delete(Modelo_pet_grupos pet_grupo){
        try{
          int id = pet_grupo.getId();
          String consulta = "Delete from pet_grupos where id="+id;
          db.Comando(consulta);
        
        
        } catch(Exception ex){
            System.out.println("error");
            
        }
    }
    
}
