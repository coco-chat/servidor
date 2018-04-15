/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.Controladores;

import chat.Conexion;
import chat.Modelos.Modelo_integrantes;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author juanc
 */
public class Controlador_integrantes {
    
    private Conexion db = new Conexion();
    
    public List<Modelo_integrantes> Select() throws SQLException {
        try {
            Modelo_integrantes integrante;
            List<Modelo_integrantes> listaIntegrantes = new ArrayList<>();
            ResultSet x = db.Select("SELECT * FROM integrantes");
            while (x.next()) {
                integrante = new Modelo_integrantes();
                integrante.setId(x.getInt(1));
                integrante.setGrupo(x.getInt(2));
                integrante.setUsuario(x.getInt(3));
                listaIntegrantes.add(integrante);
            }
            return listaIntegrantes;
        } catch(SQLException ex) {
            System.out.println("error select");
            return null;
        }
    }
    
    public void Insert(Modelo_integrantes integrante) {
        try {
            int grupo = integrante.getGrupo();
            int usuario = integrante.getUsuario();
            String consulta = "INSERT INTO integrantes (grupo, usuario) VALUES "
                    + "(" + grupo + ", " + usuario + ")";
            db.Comando(consulta);
        } catch(Exception ex) {
            System.out.println("error insert");
        }
    }
    
    public void Update(Modelo_integrantes integrante) {
        try {
            int id = integrante.getId();
            int grupo = integrante.getGrupo();
            int usuario = integrante.getUsuario();
            String consulta = "UPDATE integrantes SET "
                    + "grupo = " + grupo + ", "
                    + "usuario = " + usuario
                    + " WHERE id = " + id;
            db.Comando(consulta);
        } catch(Exception ex) {
            System.out.println("error update");
        }
    }
    
    public void Delete(Modelo_integrantes integrante){
        try {
            int id = integrante.getId();
            String consulta = "DELETE FROM integrantes WHERE id = " + id;
            db.Comando(consulta);
        } catch(Exception ex) {
            System.out.println("error");
        }
    }
    
}
