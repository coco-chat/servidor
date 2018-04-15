/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.Controladores;

import chat.Conexion;
import chat.Modelos.Modelo_amigos;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author juanc
 */
public class Controlador_amigos {
    
    private Conexion db = new Conexion();
    
    public List<Modelo_amigos> Select() throws SQLException {
        try {
            Modelo_amigos amigo;
            List<Modelo_amigos> listaAmigos = new ArrayList<>();
            ResultSet x = db.Select("SELECT * FROM amigos");
            while (x.next()) {
                amigo = new Modelo_amigos();
                amigo.setId(x.getInt(1));
                amigo.setAmigo1(x.getInt(2));
                amigo.setAmigo2(x.getInt(3));
                amigo.setApodo1(x.getString(4));
                amigo.setApodo2(x.getString(5));
                listaAmigos.add(amigo);
            }
            return listaAmigos;
        } catch(SQLException ex) {
            System.out.println("error select");
            return null;
        }
    }
    
    public void Insert(Modelo_amigos amigo) {
        try {
            int amigo1 = amigo.getAmigo1();
            int amigo2 = amigo.getAmigo2();
            String apodo1 = amigo.getApodo1();
            String apodo2 = amigo.getApodo2();
            String consulta = "INSERT INTO amigos (amigo1, amigo2, apodo1, apodo2)"
                    + " VALUES (" + amigo1 + ", " + amigo2 + ", '" + apodo1 +
                    "', '" + apodo2 + "')";
            db.Comando(consulta);
        } catch(Exception ex) {
            System.out.println("error insert");
        }
    }
    
    public void Update(Modelo_amigos amigo) {
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
            db.Comando(consulta);
        } catch(Exception ex) {
            System.out.println("error update");
        }
    }
    
    public void Delete(Modelo_amigos amigo){
        try {
            int id = amigo.getId();
            String consulta = "DELETE FROM amigos WHERE id = " + id;
            db.Comando(consulta);
        } catch(Exception ex) {
            System.out.println("error");
        }
    }
    
}
