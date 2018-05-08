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
public class Usuario implements Serializable {
    
    private int id;
    private String username;
    private String password;
    
    public Usuario() {
        this.id = -1;
        this.username = " ";
        this.password = " ";
    }

    /**
     * Obtiene el id
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * Asigna un id
     * @param id id a asignar
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre de usuairo
     * @return nombre de usuario
     */
    public String getUsername() {
        return username;
    }
    
    /**
     * Asigna un nombre de usuario
     * @param username nombre a asignar
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Obtiene el password del usuario
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Asigna el password del usuario
     * @param password password a asignar
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
