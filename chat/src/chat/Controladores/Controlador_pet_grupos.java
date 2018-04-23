/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.Controladores;

import chat.Conexion;
import chat.Modelos.Modelo_pet_grupos;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author vanya
 */
public class Controlador_pet_grupos {
    
    private Conexion db;
    
    public Controlador_pet_grupos() {
        this.db = new Conexion();
    }
    
    public List<Modelo_pet_grupos> Select() {
        try {
            Modelo_pet_grupos pet_grupo;
            List<Modelo_pet_grupos> pet_grupos = new ArrayList<>();
            ResultSet x = db.ComandoSelect("SELECT * FROM pet_grupos");
            while (x.next()) { 
                pet_grupo = new Modelo_pet_grupos();
                pet_grupo.setId(x.getInt(1));
                pet_grupo.setGrupo(x.getInt(2));
                pet_grupo.setUsuario(x.getInt(3));
                pet_grupos.add(pet_grupo);
            }
            return  pet_grupos;
        } catch(SQLException ex) {
            return null;
        } 
    }
    
    public int Insert(Modelo_pet_grupos pet_grupo) {
        try {
            int id = this.NextId();
            int grupo = pet_grupo.getGrupo();
            int usuario = pet_grupo.getUsuario();
            String consulta = "INSERT INTO pet_grupos VALUES (" + id + ", " 
                    + grupo + ", " + usuario + ")";
            return db.ComandoInsertUpdateDelete(consulta);
        } catch(Exception ex) {
            return -1;  
        }
    }
    
    public int Update(Modelo_pet_grupos pet_grupo) {
        try {
            int id = pet_grupo.getId();
            int grupo = pet_grupo.getGrupo();
            int usuario = pet_grupo.getUsuario();
            String consulta = "UPDATE pet_grupos SET grupo = " + grupo + 
                    ", usuario = " + usuario + " WHERE id = " + id;
            return db.ComandoInsertUpdateDelete(consulta);
        } catch(Exception ex) {
            return -1;
        }
    }
    
    public int Delete(Modelo_pet_grupos pet_grupo) {
        try {
            int id = pet_grupo.getId();
            String consulta = "DELETE FROM pet_grupos WHERE id = " + id;
            return db.ComandoInsertUpdateDelete(consulta);
        } catch(Exception ex) {
            return -1;
        }
    }
    
    // 0 -> No hay peticion, 1 -> si hay
    public int verificarPeticion(int grupo, int solicitado) {
        try {
            String consulta = "SELECT * FROM pet_grupos WHERE grupo = " 
                    + grupo + " AND usuario = " + solicitado;
            ResultSet x = db.ComandoSelect(consulta);
            if (x.next() == false)
                return 0;
            else
                return 1;
        } catch(Exception ex) {
            return -1;
        }
    }
    
    public int NextId() {
        List<Modelo_pet_grupos> lista = this.Select();
        if(lista == null)
            return -1;
        if (lista.isEmpty())
            return 1;
        Modelo_pet_grupos ultimo = (Modelo_pet_grupos)lista.get(lista.size() - 1);
        return ultimo.getId() + 1;
    }
    
}
