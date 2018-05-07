/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.Controllers;

import chat.Conexion;
import chat.Models.Integrante;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author juanc
 */
public class IntegrantesController {
    
    private Conexion db;
    
    public IntegrantesController() {
        this.db = new Conexion();
    }
    
    public List<Integrante> Select(){
        try {
            Integrante integrante;
            List<Integrante> listaIntegrantes = new ArrayList<>();
            ResultSet x = db.ComandoSelect("SELECT * FROM integrantes");
            while (x.next()) {
                integrante = new Integrante();
                integrante.setId(x.getInt(1));
                integrante.setGrupo(x.getInt(2));
                integrante.setUsuario(x.getInt(3));
                listaIntegrantes.add(integrante);
            }
            return listaIntegrantes;
        } catch(SQLException ex) {
            return null;
        }
    }
    
    public int Insert(Integrante integrante) {
        try {
            int id = this.NextId();
            int grupo = integrante.getGrupo();
            int usuario = integrante.getUsuario();
            String consulta = "INSERT INTO integrantes VALUES "
                    + "(" + id + ", " + grupo + ", " + usuario + ")";
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
    
    public int Update(Integrante integrante) {
        try {
            int id = integrante.getId();
            int grupo = integrante.getGrupo();
            int usuario = integrante.getUsuario();
            String consulta = "UPDATE integrantes SET "
                    + "grupo = " + grupo + ", "
                    + "usuario = " + usuario
                    + " WHERE id = " + id;
            return db.ComandoInsertUpdateDelete(consulta);
        } catch(Exception ex) {
            return -1;
        }
    }
    
    public int Delete(Integrante integrante){
        try {
            int id = integrante.getId();
            String consulta = "DELETE FROM integrantes WHERE id = " + id;
            return db.ComandoInsertUpdateDelete(consulta);
        } catch(Exception ex) {
            return -1;
        }
    }
    
    public int NextId() {
        List<Integrante> lista = this.Select();
        if(lista == null)
            return -1;
        if (lista.isEmpty())
            return 1;
        Integrante ultimo = (Integrante)lista.get(lista.size() - 1);
        return ultimo.getId() + 1;
    }
    
    public List<Integer> getListOfGrupos(int usuario) {
        List<Integer> lista = new ArrayList<>();
        Integer x;
        String consulta = "SELECT grupo FROM integrantes WHERE usuario = " + usuario;
        try {
            ResultSet rs = db.ComandoSelect(consulta);
            while(rs.next()) {
                x = rs.getInt(1);
                lista.add(x);
            }
            return lista;
        } catch(SQLException ex) {
            return null;
        }
    }
    
}
