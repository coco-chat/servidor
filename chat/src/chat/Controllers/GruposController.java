/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.Controllers;

import chat.Conexion;
import chat.Models.Grupo;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author juanc
 */
public class GruposController {
    
    private Conexion db;
    
    public GruposController() {
        this.db = new Conexion();
    }
    
    public List<Grupo> Select(){
        try {
            Grupo grupo;
            List<Grupo> listaGrupos = new ArrayList<>();
            ResultSet x = db.ComandoSelect("SELECT * FROM grupos");
            while (x.next()) {
                grupo = new Grupo();
                grupo.setId(x.getInt(1));
                grupo.setAdmin(x.getInt(2));
                grupo.setNombre(x.getString(3));
                listaGrupos.add(grupo);
            }
            return listaGrupos;
        } catch(SQLException ex) {
            return null;
        }
    }
    
    public int Insert(Grupo grupo) {
        try {
            int id = this.NextId();
            int admin = grupo.getAdmin();
            String nombre = grupo.getNombre();
            String consulta = "INSERT INTO grupos VALUES "
                    + "(" + id + ", " + admin + ", '" + nombre + "')";
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
    
    public int Update(Grupo grupo) {
        try {
            int id = grupo.getId();
            int admin = grupo.getAdmin();
            String nombre = grupo.getNombre();
            String consulta = "UPDATE grupos SET "
                    + "admin = " + admin + ", "
                    + "nombre = '" + nombre + "' "
                    + "WHERE id = " + id;
            return db.ComandoInsertUpdateDelete(consulta);
        } catch(Exception ex) {
            return -1;
        }
    }
    
    public int Delete(Grupo grupo){
        try {
            int id = grupo.getId();
            String consulta = "DELETE FROM grupos WHERE id = " + id;
            return db.ComandoInsertUpdateDelete(consulta);
        } catch(Exception ex) {
            return -1;
        }
    }
    
    public int NextId() {
        List<Grupo> lista = this.Select();
        if(lista == null)
            return -1;
        if (lista.isEmpty())
            return 1;
        Grupo ultimo = (Grupo)lista.get(lista.size() - 1);
        return ultimo.getId() + 1;
    }
    
    public Grupo getGrupo(int id) {
        Grupo x = new Grupo();
        try {
            String consulta = "SELECT * FROM grupos WHERE id = " + id;
            ResultSet rs = db.ComandoSelect(consulta);
            while(rs.next()) {
                x.setId(id);
                x.setAdmin(rs.getInt("admin"));
                x.setNombre(rs.getString("nombre"));
            }
            return x;
        } catch(SQLException ex) {
            return null;
        }
    }
}
