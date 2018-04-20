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
    
    private Conexion db = new Conexion();
    
    public List<Modelo_grupos> Select(){
        try {
            Modelo_grupos grupo;
            List<Modelo_grupos> listaGrupos = new ArrayList<>();
            ResultSet x = db.Select("SELECT * FROM grupos");
            while (x.next()) {
                grupo = new Modelo_grupos();
                grupo.setId(x.getInt(1));
                grupo.setAdmin(x.getInt(2));
                grupo.setNombre(x.getString(3));
                listaGrupos.add(grupo);
            }
            return listaGrupos;
        } catch(SQLException ex) {
            System.out.println("error select");
            return null;
        }
    }
    
    public void Insert(Modelo_grupos grupo) {
        try {
            int admin = grupo.getAdmin();
            String nombre = grupo.getNombre();
            String consulta = "INSERT INTO grupos (admin, nombre) VALUES "
                    + "(" + admin + ", '" + nombre + "')";
            db.Comando(consulta);
        } catch(Exception ex) {
            System.out.println("error insert");
        }
    }
    
    public void Update(Modelo_grupos grupo) {
        try {
            int id = grupo.getId();
            int admin = grupo.getAdmin();
            String nombre = grupo.getNombre();
            String consulta = "UPDATE grupos SET "
                    + "admin = " + admin + ", "
                    + "nombre = '" + nombre + "' "
                    + "WHERE id = " + id;
            db.Comando(consulta);
        } catch(Exception ex) {
            System.out.println("error update");
        }
    }
    
    public void Delete(Modelo_grupos grupo){
        try {
            int id = grupo.getId();
            String consulta = "DELETE FROM grupos WHERE id = " + id;
            db.Comando(consulta);
        } catch(Exception ex) {
            System.out.println("error");
        }
    }
    
}
