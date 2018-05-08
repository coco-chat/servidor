/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.Models;

import java.io.Serializable;

/**
 *
 * @author Kevin Alan Martinez Virgen 14300260 8B1
 */
public class Mensaje implements Serializable{
    private Usuario origen;
    private Usuario destino;
    private String contenido;

    public Mensaje() {
        origen = new Usuario();
        destino = new Usuario();
        contenido = " ";
    }

    /**
     * Obtiene el usuario que envió el mensaje
     * @return usuario de origen
     */
    public Usuario getOrigen() {
        return origen;
    }

    /**
     * Asigna un usuario como remitente
     * @param origen remitente
     */
    public void setOrigen(Usuario origen) {
        this.origen = origen;
    }

    /**
     * Obtiene el usuario destinatario
     * @return usuario destinatario
     */
    public Usuario getDestino() {
        return destino;
    }

    /**
     * Asigna el usuario de destino
     * @param destino usuario destino
     */
    public void setDestino(Usuario destino) {
        this.destino = destino;
    }

    /**
     * Obtiene el contenido del mensaje
     * @return contenido del mensaje
     */
    public String getContenido() {
        return contenido;
    }

    /**
     * Asigna el contenido del grupo
     * @param contenido 
     */
    public void setContenido(String contenido) {
        this.contenido = contenido;
    }
    
    
}
