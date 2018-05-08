/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.Models;

import java.io.Serializable;

/**
 *
 * @author vanya
 */
public class PetGrupo implements Serializable {
    
    private int id;
    private int grupo;
    private int usuario;
    
    public PetGrupo() {
        id = -1;
        grupo = -1;
        usuario = -1;
    }

    /**
     * Obtiene el id
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Asigna el id
     * @param id 
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene el grupo
     * @return 
     */
    public int getGrupo() {
        return grupo;
    }

    /**
     * Asigna el grupo
     * @param grupo grupo a asignar
     */
    public void setGrupo(int grupo) {
        this.grupo = grupo;
    }

    /**
     * Obtiene el usuario
     * @return usuario
     */
    public int getUsuario() {
        return usuario;
    }

    /**
     * Asigna un usuario
     * @param usuario usuario a asginar
     */
    public void setUsuario(int usuario) {
        this.usuario = usuario;
    }
    
}
