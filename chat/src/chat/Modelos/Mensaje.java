/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.Modelos;

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
    }

    public Usuario getOrigen() {
        return origen;
    }

    public void setOrigen(Usuario origen) {
        this.origen = origen;
    }

    public Usuario getDestino() {
        return destino;
    }

    public void setDestino(Usuario destino) {
        this.destino = destino;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }
    
    
}
