/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.Modelos;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGrupo() {
        return grupo;
    }

    public void setGrupo(int grupo) {
        this.grupo = grupo;
    }

    public int getUsuario() {
        return usuario;
    }

    public void setUsuario(int usuario) {
        this.usuario = usuario;
    }
    
}
