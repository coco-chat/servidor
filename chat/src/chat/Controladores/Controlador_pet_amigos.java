/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.Controladores;


import chat.Conexion;
import chat.Modelos.Modelo_pet_amigos;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
/**
 *
 * @author vanya
 */
public class Controlador_pet_amigos {
    
    Conexion db = new Conexion();
    
    public Controlador_pet_amigos() {
        
    }
    
    public List<Modelo_pet_amigos> Select() throws SQLException{
        try{
            Modelo_pet_amigos pet_amigo;
            List<Modelo_pet_amigos> pet_amigos = new ArrayList<>();
           ResultSet x;
            x = db.Select("Select * from pet_amigos");
           
            while (x.next()) {
                
              pet_amigo = new Modelo_pet_amigos();
              pet_amigo.setId(x.getString(1));
              pet_amigo.setSolicitante(x.getInt(2));
              pet_amigo.setSolicitado(x.getInt(3));
                
              pet_amigos.add(pet_amigo);
            }
            
        return  pet_amigos;
        }catch(SQLException ex){
            System.out.println("error");
            return null;
        }
         
    }
    public void Insert(Modelo_pet_amigos pet_amigo){
        try{
          int solicitante = pet_amigo.getSolicitante();
          int solicitado = pet_amigo.getSolicitado();
          String id =  Integer.toString(solicitante)+Integer.toString(solicitado);
          JOptionPane.showMessageDialog(null, id);
          String consulta = "Insert into pet_amigos values('"+id+"',"+solicitante+","+solicitado+")";
          db.Comando(consulta);
        
        
        } catch(Exception ex){
            System.out.println("error insert");
            
        }
    }
    public void Update(Modelo_pet_amigos pet_amigo){
        try{
          String id = pet_amigo.getId();
          int solicitante = pet_amigo.getSolicitante();
          int solicitado = pet_amigo.getSolicitado();
          String consulta = "UPDATE pet_amigos SET solicitante = "+solicitante+", solicitado = "+solicitado+" where id = "+id;
          db.Comando(consulta);
        
        
        } catch(Exception ex){
            System.out.println("error");
            
        }
    }
    public void Delete(Modelo_pet_amigos pet_amigo){
        try{
          String id = pet_amigo.getId();
          String consulta = "Delete from pet_amigos where id="+id;
          db.Comando(consulta);
        
        
        } catch(Exception ex){
            System.out.println("error");
            
        }
    }
    
}
