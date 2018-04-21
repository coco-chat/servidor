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
public class Modelo_Mensaje implements Serializable{
    private Modelo_usuarios origen;
    private Modelo_usuarios destino;
    private String contenido;

    public Modelo_Mensaje() {
    }

    public Modelo_usuarios getOrigen() {
        return origen;
    }

    public void setOrigen(Modelo_usuarios origen) {
        this.origen = origen;
    }

    public Modelo_usuarios getDestino() {
        return destino;
    }

    public void setDestino(Modelo_usuarios destino) {
        this.destino = destino;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }
    
    
}
