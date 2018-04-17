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
public class Modelo_pet_amigos implements Serializable {
    
    private String id;
    private int solicitante;
    private int solicitado;
    
    public Modelo_pet_amigos() {
        
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(int solicitante) {
        this.solicitante = solicitante;
    }

    public int getSolicitado() {
        return solicitado;
    }

    public void setSolicitado(int solicitado) {
        this.solicitado = solicitado;
    }
    
}
