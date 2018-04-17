/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.Modelos;

/**
 *
 * @author Kevin Alan Martinez Virgen 14300260 8B1
 */
public class Modelo_Comunicacion {
    public static enum MTypes{
        RQ_LOGIN,RQ_REG, RQ_LOGOUT,
        RQ_ACTUALIZAR,RQ_MENSAJE,RQ_NAMIGO,
        RQ_DAMIGO,RQ_APODO, RQ_GRUPO,
        RQ_NMIEMBRO, RQ_DMIEMBRO,ACK,
        ACK_LOGIN, SEND_MENSAJE, SEND_GRUPO,
        SEND_CONECTADOS
    };

    private MTypes tipo;
    private Object contenido;
    
    public Modelo_Comunicacion() {
        tipo = MTypes.ACK;
        contenido = null;
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
