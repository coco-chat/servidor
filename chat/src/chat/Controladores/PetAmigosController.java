/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.Controladores;

import chat.Conexion;
import chat.Modelos.PetAmigo;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author vanya
 */
public class PetAmigosController {
    
    private Conexion db;
    
    public PetAmigosController() {
        this.db = new Conexion();
    }
    
    public List<PetAmigo> Select() {
        try {
            PetAmigo pet_amigo;
            List<PetAmigo> pet_amigos = new ArrayList<>();
            ResultSet x = db.ComandoSelect("SELECT * FROM pet_amigos");
            while (x.next()) {
                pet_amigo = new PetAmigo();
                pet_amigo.setId(x.getString(1));
                pet_amigo.setSolicitante(x.getInt(2));
                pet_amigo.setSolicitado(x.getInt(3));
                pet_amigos.add(pet_amigo);
            }
            return  pet_amigos;
        } catch(SQLException ex) {
            return null;
        }
    }
    
    public String Insert(PetAmigo pet_amigo) {
        try {
            int solicitante = pet_amigo.getSolicitante();
            int solicitado = pet_amigo.getSolicitado();
            String id = Integer.toString(solicitante) + "-" + 
                        Integer.toString(solicitado);
            String consulta = "INSERT INTO pet_amigos VALUES('" + id + "'," + 
                    solicitante + "," + solicitado + ")";
            db.getCon().setAutoCommit(false);
            if (db.ComandoInsertUpdateDelete(consulta) == 1) {
                db.getCon().commit();
                return id;
            }
            return "";
        } catch(Exception ex) {
            try {
                db.getCon().rollback();
            } catch(Exception e) {
            }
            return "";
        }
    }
    
    public int Update(PetAmigo pet_amigo) {
        try {
            String id = pet_amigo.getId();
            int solicitante = pet_amigo.getSolicitante();
            int solicitado = pet_amigo.getSolicitado();
            String consulta = "UPDATE pet_amigos SET solicitante = " + solicitante + 
                    ", solicitado = " + solicitado + " WHERE id = " + id;
            return db.ComandoInsertUpdateDelete(consulta);
        } catch(Exception ex) {
            return -1;
        }
    }
    
    public int Delete(PetAmigo pet_amigo) {
        try {
            String id = pet_amigo.getId();
            String consulta = "DELETE FROM pet_amigos WHERE id="+id;
            return db.ComandoInsertUpdateDelete(consulta);
        } catch(Exception ex) {
            return -1;
        }
    }
    
    // 0 -> No hay peticion, 1 -> si hay
    public int verificarPeticion(int solicitante, int solicitado) {
        try {
            String consulta = "SELECT * FROM pet_amigos WHERE solicitante = " 
                    + solicitante + " AND solicitado = " + solicitado;
            ResultSet x = db.ComandoSelect(consulta);
            if (x.next() == false)
                return 0;
            else
                return 1;
        } catch(Exception ex) {
            return -1;
        }
    }
    
}