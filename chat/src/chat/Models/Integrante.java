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
public class Integrante implements Serializable {
    
    private int id;
    private int grupo;
    private int usuario;
    
    public Integrante() {
        id = -1;
        grupo = -1;
        usuario = -1;
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
     * @return el id
     */
    public int getId() {
        return this.id;
    }
    
    /**
     * Asigna un grupo
     * @param grupo el grupo a asignar
     */
    public void setGrupo(int grupo) {
        this.grupo = grupo;
    }
    
    /**
     * Obtiene el grupo
     * @return el grupo
     */
    public int getGrupo() {
        return this.grupo;
    }
    
    /**
     * Asigna un usuario
     * @param usuario el usuario a asignar
     */
    public void setUsuario(int usuario) {
        this.usuario = usuario;
    }
    
    /**
     * Obtiene el id del usuario
     * @return el id del usuario
     */
    public int getUsuario() {
        return this.usuario;
    }
    
}
