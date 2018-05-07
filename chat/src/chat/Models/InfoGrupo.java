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

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    public List<Usuario> getMiembros() {
        return miembros;
    }

    public void setMiembros(List<Usuario> miembros) {
        this.miembros = miembros;
    }

    public Usuario getAdmin() {
        return admin;
    }

    public void setAdmin(Usuario admin) {
        this.admin = admin;
    }

    public List<Usuario> getNoMiembros() {
        return noMiembros;
    }

    public void setNoMiembros(List<Usuario> noMiembros) {
        this.noMiembros = noMiembros;
    }
        
}
