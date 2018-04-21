/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat;

import chat.Controladores.Controlador_usuarios;
import chat.Modelos.Modelo_usuarios;
import chat.Controladores.Controlador_cuentas;
import chat.Hashing.Hash;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 *
 * @author vanya
 */
public class Chat {
    
    public static void main(String[] args) {
        // TODO code application logic here
       
        /*Pruebas
        try {
            
            Controlador_usuarios x = new Controlador_usuarios();
            Controlador_cuentas y = new Controlador_cuentas();
            Modelo_usuarios usuario = new Modelo_usuarios();
            
            usuario.setUsername("Ma");
            usuario.setPassword("pe");
            
            List<Modelo_usuarios> listaIntegrantes = x.Select();
            System.out.println(listaIntegrantes.get(0).getId());
            System.out.println(listaIntegrantes.get(0).getUsername()); 
            System.out.println(listaIntegrantes.get(0).getPassword());
            
            int res = y.Register(usuario.getUsername(), usuario.getPassword());
            System.out.println(res);
          
        } catch (SQLException ex) {
            Logger.getLogger(Conexion.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        
    }
    
}
