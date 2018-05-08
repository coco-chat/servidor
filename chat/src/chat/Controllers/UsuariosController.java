/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.Controllers;

import chat.Conexion;
import chat.Models.Usuario;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author vanya
 */
public class UsuariosController {
    
    private Conexion db;
    
    public UsuariosController() {
        this.db = new Conexion();
    }
    
    /**
     * Obtiene la lista de usuarios de la base de datos
     * @return la lista de usuarios
     */
    public List<Usuario> Select() {
        try{
            Usuario usuario;
            List<Usuario> usuarios = new ArrayList<>();
            ResultSet x = db.ComandoSelect("SELECT * FROM usuarios");
            while (x.next()) {
                usuario = new Usuario();
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
    
    /**
     * Inserta un nuevo usuario
     * @param usuario el usuario a insertar
     * @return el estado de la inserción. En caso de error -1
     */
    public int Insert(Usuario usuario) {
        try {
            int id = this.NextId();
            String username = usuario.getUsername();
            String password = usuario.getPassword();
            String consulta = "INSERT INTO usuarios VALUES (" + id + ", '" + 
                    username + "', sha('" + password + "'))";
            db.getCon().setAutoCommit(false);
            if (db.ComandoInsertUpdateDelete(consulta) == 1) {
                db.getCon().commit();
                return id;
            }
            return -1;
        } catch(Exception ex) {
            try {
                db.getCon().rollback();
            } catch(Exception e) {
            }
            return -1;
        }
    }
    
    /**
     * Actualiza un usuario
     * @param usuario el usuario a actualizar
     * @return el estado de la actualización. En caso de error -1
     */
    public int Update(Usuario usuario) {
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
    
    /**
     * Elimina un usuario
     * @param usuario el usuario a eliminar
     * @return el estado de la eliminación. En caso de error -1
     */
    public int Delete(Usuario usuario) {
        try {
            int id = usuario.getId();
            String consulta = "DELETE FROM usuarios WHERE id = " + id;
            return db.ComandoInsertUpdateDelete(consulta);
        } catch(Exception ex) {
            return -1;
        }
    }
    
    /**
    * Obtiene el siguiente id de la lista de amigos
    * @return el siguiente id
    */
    public int NextId() {
        List<Usuario> lista = this.Select();
        if(lista == null)
            return -1;
        if (lista.isEmpty())
            return 1;
        Usuario ultimo = (Usuario)lista.get(lista.size() - 1);
        return ultimo.getId() + 1;
    }
    
    /**
     * Obtiene un usuario mediante id
     * @param id la id a buscar
     * @return el usuario que tiene la id correspondiente. En caso de error null
     */
    public Usuario getUsuario(int id) {
        Usuario x = new Usuario();
        try {
            String consulta = "SELECT * FROM usuarios WHERE id = " + id;
            ResultSet rs = db.ComandoSelect(consulta);
            while(rs.next()) {
                x.setId(id);
                x.setUsername(rs.getString(2));
                x.setPassword(rs.getString(3));
            }
            return x;
        } catch(SQLException ex) {
            return null;
        }
    }
    
}
