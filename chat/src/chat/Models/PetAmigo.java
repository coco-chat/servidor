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
public class PetAmigo implements Serializable {
    
    private String id;
    private int solicitante;
    private int solicitado;
    
    public PetAmigo() {
        id = " ";
        solicitado = -1;
        solicitante = -1;
    }

    /**
     * Obtiene el id
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * Asignael id
     * @param id id aasignar
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Obtiene el id del solicitante
     * @return solicitante
     */
    public int getSolicitante() {
        return solicitante;
    }

    /**
     * Asigna el id del solicitante
     * @param solicitante id del solicitante
     */
    public void setSolicitante(int solicitante) {
        this.solicitante = solicitante;
    }

    /**
     * Obtiene el id del solicitado
     * @return id del solicitado
     */
    public int getSolicitado() {
        return solicitado;
    }

    /**
     * Asigna el id del solicitado
     * @param solicitado id del solicitado
     */
    public void setSolicitado(int solicitado) {
        this.solicitado = solicitado;
    }
    
}
