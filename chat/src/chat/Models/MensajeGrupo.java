/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Emiliano
 */
public class MensajeGrupo implements Serializable {
    private Grupo grupo;
    private List <Usuario> integrantes;
    private String mensaje;
    private Usuario remitente;
    
    public MensajeGrupo () {
        this.grupo = new Grupo();
        this.integrantes = new ArrayList<>();
        this.mensaje = "";
        this.remitente = new Usuario();
    }

    /**
     * Obtiene el grupo
     * @return el grupo
     */
    public Grupo getGrupo() {
        return grupo;
    }

    /**
     * Asigna el grupo
     * @param grupo el grupo a asignar
     */
    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    /**
     * Obtiene los integrantes
     * @return lista de integrantes
     */
    public List<Usuario> getIntegrantes() {
        return integrantes;
    }

    /**
     * Asigna un arreglo de usuarios como integrantes del grupo
     * @param integrantes la lista de usuario
     */
    public void setIntegrantes(List<Usuario> integrantes) {
        this.integrantes = integrantes;
    }

    /**
     * Obtiene el contenido del mensaje
     * @return contenido de mensaje
     */
    public String getMensaje() {
        return mensaje;
    }

    /**
     * Asigna un contenido de mensaje
     * @param mensaje contenido a asignar
     */
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    /**
     * Obtiene el usuario remitente
     * @return usuario remitente
     */
    public Usuario getRemitente() {
        return remitente;
    }

    /**
     * Asigna el remitente del mensaje
     * @param remitente usuario remitentes
     */
    public void setRemitente(Usuario remitente) {
        this.remitente = remitente;
    }
       
}
