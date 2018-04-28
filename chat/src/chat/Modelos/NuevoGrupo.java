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
 * @author Kevin Alan Martinez Virgen 14300260 8B1
 */
public class NuevoGrupo implements Serializable{
    private Grupo grupo;
    private List<PetGrupo> integrantes;

    public NuevoGrupo() {
        this.grupo = null;
        this.integrantes = null;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    public List<PetGrupo> getIntegrantes() {
        return integrantes;
    }

    public void setIntegrantes(List<PetGrupo> integrantes) {
        this.integrantes = integrantes;
    }
    
    
}
