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
public class Comunicacion implements Serializable{
    
    public static enum MTypes{
        RQCUENTA_LOGIN,RQCUENTA_REG, RQ_LOGOUT,
        RQ_ACTUALIZAR,RQMENSAJES_SENDPERSONAL, RQMENSAJES_SENDGRUPO,
        RQAMIGOS_INVITE, RQAMIGOS_DELETE, RQAMIGOS_ADD,
        RQAMIGOS_UPDATE, RQGRUPOS_UPDATE, RQGRUPOS_CREATE,RQGRUPOS_ADD,
        RQGRUPOS_INVITE, RQGRUPOS_DELETE,
        RQUSUARIOS_CONECTADOS, RQUSUARIOS_DESCONECTADOS,
        RQAMIGOS_GETALL, RQAMIGOS_GETUSUARIOS,
        RQAMIGOS_CONECTADOS, RQAMIGOS_DESCONECTADOS, RQGRUPOS_GETALL, RQGRUPOS_GETINFO,
        RQ_MENSAJES_GRUPO_NO_RECIBIDOS, RQUSUARIOS_GETALL,
        RQAMIGOS_GETPET, RQGRUPOS_GETPET,
        ACK, ACK_LOGIN,
        SEND_MENSAJE, SEND_GRUPO, SEND_CONECTADOS, SEND_DESCONECTADOS,
        SEND_AMIGOSCON, SEND_AMIGOSDES, SEND_GRUPOS, SEND_INFOGRUPO, SEND_MENSAJE_GRUPO,
        SEND_LUSUARIOS, SEND_AMIGOS, SEND_USUARIOS, SEND_PETAMIGOS, SEND_PETGRUPOS,
        SENDUSUARIO_CONECTADO, SENDUSUARIO_DESCONECTADO,
        SENDAMIGO_CONECTADO, SENDAMIGO_DESCONECTADO
    };

    private MTypes tipo;
    private Object contenido;
    
    public Comunicacion() {
        tipo = MTypes.ACK;
        contenido = " ";
    }

    /**
     *
     * @return
     */
    public MTypes getTipo() {
        return tipo;
    }

    public void setTipo(MTypes type) {
        this.tipo = type;
    }

    public Object getContenido() {
        return contenido;
    }

    public void setContenido(Object contenido) {
        this.contenido = contenido;
    }
    
    
    
    
}
