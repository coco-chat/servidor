/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.Modelos;

import java.io.Serializable;

/**
 *
 * @author juanc
 */
public class Modelo_integrantes implements Serializable {
    
    private int id;
    private int grupo;
    private int usuario;
    
    public Modelo_integrantes() {
        
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getId() {
        return this.id;
    }
    
    public void setGrupo(int grupo) {
        this.grupo = grupo;
    }
    
    public int getGrupo() {
        return this.grupo;
    }
    
    public void setUsuario(int usuario) {
        this.usuario = usuario;
    }
    
    public int getUsuario() {
        return this.usuario;
    }
    
}
