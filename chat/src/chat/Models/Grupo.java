/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.Models;

import java.io.Serializable;

/**
 *
 * @author juanc
 */
public class Grupo implements Serializable {
    
    private int id;
    private int admin;
    private String nombre;
    
    public Grupo() {
        id = -1;
        admin = -1;
        nombre = " ";
    }
    
    /**
     * Asigna un id
     * @param id el id a asignar
     */
    public void setId(int id) {
        this.id = id;
    }
    
    /**
     * Obtiene el id
     * @return id
     */
    public int getId() {
        return this.id;
    }
    
    /**
     * Asigna el id del asministrador
     * @param admin id del administrador
     */
    public void setAdmin(int admin) {
        this.admin = admin;
    }
    
    /**
     * Obtiene el id del administrador
     * @return el id del administrdor
     */
    public int getAdmin() {
        return this.admin;
    }
    
    /**
     * Asigna el nombre del grupo
     * @param nombre nombre del grupo
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    /**
     * Obtiene el nombre del grupo
     * @return el nombre del grupo
     */
    public String getNombre() {
        return this.nombre;
    }
    
}
