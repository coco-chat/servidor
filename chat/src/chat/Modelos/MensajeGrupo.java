/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.Modelos;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author Emiliano
 */
public class MensajeGrupo implements Serializable {
    private Grupo grupo;
    private List <Integer> integrantes;
    private Mensaje mensaje;
    
    public MensajeGrupo () {}

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    public List<Integer> getIntegrantes() {
        return integrantes;
    }

    public void setIntegrantes(List<Integer> integrantes) {
        this.integrantes = integrantes;
    }

    public Mensaje getMensaje() {
        return mensaje;
    }

    public void setMensaje(Mensaje mensaje) {
        this.mensaje = mensaje;
    }
    
    
}
