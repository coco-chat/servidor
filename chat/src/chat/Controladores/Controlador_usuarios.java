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
    
    private Conexion db;
    
    public Controlador_usuarios() {
        this.db = new Conexion();
    }
    
    public List<Modelo_usuarios> Select() {
        try{
            Modelo_usuarios usuario;
            List<Modelo_usuarios> usuarios = new ArrayList<>();
            ResultSet x = db.ComandoSelect("SELECT * FROM usuarios");
            while (x.next()) {
                usuario = new Modelo_usuarios();
                usuario.setId(x.getInt(1));
                usuario.setUsername(x.getString(2));
                usuario.setPassword(x.getString(3));
                usuarios.add(usuario);
            }
            return  usuarios;
        } catch(SQLException ex) {
            return null;
        }
    }
    
    public int Insert(Modelo_usuarios usuario) {
        try {
            String username = usuario.getUsername();
            String password = usuario.getPassword();
            String consulta = "Insert into usuarios values(null,'" + username + 
                    "',sha('" + password + "'))";
            return db.ComandoInsertUpdateDelete(consulta);
        } catch(Exception ex) {
            return -1;
        }
    }
    
    public int Update(Modelo_usuarios usuario) {
        try {
            int id = usuario.getId();
            String username = usuario.getUsername();
            String password = usuario.getPassword();
            String consulta = "UPDATE usuarios SET username = '" + username + 
                    "', password = sha('" + password + "') WHERE id = " + id;
            return db.ComandoInsertUpdateDelete(consulta);
        } catch(Exception ex) {
            return -1;            
        }
    }
    
    public int Delete(Modelo_usuarios usuario) {
        try {
            int id = usuario.getId();
            String consulta = "DELETE FROM usuarios WHERE id = " + id;
            return db.ComandoInsertUpdateDelete(consulta);
        } catch(Exception ex) {
            return -1;
        }
    }
    
}
