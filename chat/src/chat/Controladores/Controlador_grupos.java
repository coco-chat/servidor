/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.Controladores;

import chat.Conexion;
import chat.Modelos.Modelo_grupos;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author juanc
 */
public class Controlador_grupos {
    
    private Conexion db;
    
    public Controlador_grupos() {
        this.db = new Conexion();
    }
    
    public List<Modelo_grupos> Select(){
        try {
            Modelo_grupos grupo;
            List<Modelo_grupos> listaGrupos = new ArrayList<>();
            ResultSet x = db.ComandoSelect("SELECT * FROM grupos");
            while (x.next()) {
                grupo = new Modelo_grupos();
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
    
    public int Insert(Modelo_grupos grupo) {
        try {
            int id = this.NextId();
            int admin = grupo.getAdmin();
            String nombre = grupo.getNombre();
            String consulta = "INSERT INTO grupos VALUES "
                    + "(" + id + ", " + admin + ", '" + nombre + "')";
            return db.ComandoInsertUpdateDelete(consulta);
        } catch(Exception ex) {
            return -1;
        }
    }
    
    public int Update(Modelo_grupos grupo) {
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
    
    public int Delete(Modelo_grupos grupo){
        try {
            int id = grupo.getId();
            String consulta = "DELETE FROM grupos WHERE id = " + id;
            return db.ComandoInsertUpdateDelete(consulta);
        } catch(Exception ex) {
            return -1;
        }
    }
    
    public int NextId() {
        List<Modelo_grupos> lista = this.Select();
        if(lista == null)
            return -1;
        if (lista.isEmpty())
            return 1;
        Modelo_grupos ultimo = (Modelo_grupos)lista.get(lista.size() - 1);
        return ultimo.getId() + 1;
    }
    
}
