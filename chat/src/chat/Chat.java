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
import chat.Controladores.Controlador_pet_amigos;
import chat.Controladores.Controlador_pet_grupos;
import chat.Controladores.Controlador_usuarios;
import chat.Modelos.Modelo_pet_amigos;
import chat.Modelos.Modelo_pet_grupos;
import chat.Modelos.Modelo_usuarios;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
public class Chat {
    
    public static void main(String[] args) {
        // TODO code application logic here
       
         /*Pruebas
         try {
           
             Controlador_pet_grupos x = new Controlador_pet_grupos();
            Modelo_pet_grupos pet_grupo = new Modelo_pet_grupos();
            List<Modelo_pet_grupos> grupos = new ArrayList<Modelo_pet_grupos>();
            grupos = x.Select();
            
             System.out.println(grupos.get(0).getId()); 
             System.out.println(grupos.get(0).getGrupo()); 
             System.out.println(grupos.get(0).getUsuario()); 
             
              
            pet_grupo.setId(1);
              pet_grupo.setGrupo(1);
              pet_grupo.setUsuario(1);
              x.Insert(pet_grupo);
           
          
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    }
    
}
