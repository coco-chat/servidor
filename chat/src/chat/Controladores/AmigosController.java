/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.Controladores;

import chat.Conexion;
import chat.Modelos.Amigo;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author juanc
 */
public class AmigosController {
    
    private Conexion db;
    
    public AmigosController() {
        this.db = new Conexion();
    }
    
    public List<Amigo> Select(){
        try {
            Amigo amigo;
            List<Amigo> listaAmigos = new ArrayList<>();
            ResultSet x = db.ComandoSelect("SELECT * FROM amigos");
            while (x.next()) {
                amigo = new Amigo();
                amigo.setId(x.getInt(1));
                amigo.setAmigo1(x.getInt(2));
                amigo.setAmigo2(x.getInt(3));
                amigo.setApodo1(x.getString(4));
                amigo.setApodo2(x.getString(5));
                listaAmigos.add(amigo);
            }
            return listaAmigos;
        } catch(SQLException ex) {
            return null;
        }
    }
    
    public int Insert(Amigo amigo) {
        try {
            int id = this.NextId();
            int amigo1 = amigo.getAmigo1();
            int amigo2 = amigo.getAmigo2();
            String apodo1 = amigo.getApodo1();
            String apodo2 = amigo.getApodo2();
            String consulta = "INSERT INTO amigos VALUES " + "(" + id + ", " + 
                    amigo1 + ", " + amigo2 + ", '" + apodo1 + "', '" + apodo2 + "')";
            db.getCon().setAutoCommit(false);
            if (db.ComandoInsertUpdateDelete(consulta) == 1) {
                db.getCon().commit();
                return id;
            }
            return -1;
        } catch(Exception ex) {
            try {
                db.getCon().rollback();
            } catch(SQLException e) {
            }
            return -1;
        }
    }
    
    public int Update(Amigo amigo) {
        try {
            int id = amigo.getId();
            int amigo1 = amigo.getAmigo1();
            int amigo2 = amigo.getAmigo2();
            String apodo1 = amigo.getApodo1();
            String apodo2 = amigo.getApodo2();
            String consulta = "UPDATE amigos SET "
                    + "amigo1 = " + amigo1 + ", "
                    + "amigo2 = " + amigo2 + ", "
                    + "apodo1 = '" + apodo1 + "', "
                    + "apodo2 = '" + apodo2 + "' "
                    + "WHERE id = " + id;
            return db.ComandoInsertUpdateDelete(consulta);
        } catch(Exception ex) {
            return -1;
        }
    }
    
    public int Delete(Amigo amigo){
        try {
            int id = amigo.getId();
            String consulta = "DELETE FROM amigos WHERE id = " + id;
            return db.ComandoInsertUpdateDelete(consulta);
        } catch(Exception ex) {
            return -1;
        }
    }
    
    public int NextId() {
        List<Amigo> lista = this.Select();
        if(lista == null)
            return -1;
        if (lista.isEmpty())
            return 1;
        Amigo ultimo = (Amigo)lista.get(lista.size() - 1);
        return ultimo.getId() + 1;
    }
    
}