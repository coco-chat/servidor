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
 * @author Kevin Alan Martinez Virgen 14300260 8B1
 */
public class NuevoGrupo implements Serializable{
    private Grupo grupo;
    private List<PetGrupo> integrantes;

    
    public NuevoGrupo() {
        grupo = new Grupo();
        integrantes = new ArrayList<>();
    }

    /**
     * Obtiene el grupo
     * @return grupo
     */
    public Grupo getGrupo() {
        return grupo;
    }

    /**
     * Asinga un grupo
     * @param grupo grupo a asignar
     */
    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    /**
     * Obtiene la lista de peticiones
     * @return lista de peticiones
     */
    public List<PetGrupo> getIntegrantes() {
        return integrantes;
    }

    /**
     * Asigna una lista de peticiones de grupo
     * @param integrantes 
     */
    public void setIntegrantes(List<PetGrupo> integrantes) {
        this.integrantes = integrantes;
    }
    
    
}
