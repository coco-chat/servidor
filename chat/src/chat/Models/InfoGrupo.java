/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chat.Models;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author vanya
 */
public class InfoGrupo implements Serializable{
    private Grupo grupo;
    private List<Usuario> miembros;
    private List<Usuario> noMiembros;
    private Usuario admin;

    public InfoGrupo() {
        this.grupo = null;
        this.miembros = null;
        this.admin = null;
        this.noMiembros = null;
    }
    
    /**
     * Obtiene el grupo
     * @return el grupo
     */
    public Grupo getGrupo() {
        return grupo;
    }

    /**
     * Asigna un grupo
     * @param grupo el grupo a asignar
     */
    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    /**
     * Obtiene los miembros del grupo
     * @return lista de usuarios que están en el grupo
     */
    public List<Usuario> getMiembros() {
        return miembros;
    }

    /**
     * Asigna usuarios que están el grupo
     * @param miembros lista de usuarios a asignar
     */
    public void setMiembros(List<Usuario> miembros) {
        this.miembros = miembros;
    }

    /**
     * Obtiene el administrador del grupo
     * @return el usuario administrador del grupo
     */
    public Usuario getAdmin() {
        return admin;
    }

    /**
     * Asigna un usuario como adminstrador
     * @param admin el administrador a asignar
     */
    public void setAdmin(Usuario admin) {
        this.admin = admin;
    }

    /**
     * Obtiene la lista de posibles usuarios del grupo
     * @return la lista de usuarios
     */
    public List<Usuario> getNoMiembros() {
        return noMiembros;
    }

    /**
     * Asigna una lista de miembros potenciales del grupo
     * @param noMiembros la lista a signar
     */
    public void setNoMiembros(List<Usuario> noMiembros) {
        this.noMiembros = noMiembros;
    }
        
}
